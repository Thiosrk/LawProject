package Word;

import lombok.AllArgsConstructor;
import lombok.NonNull;
//import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.deeplearning4j.iterator.provider.LabelAwareConverter;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.text.documentiterator.LabelAwareDocumentIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.interoperability.DocumentIteratorConverter;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.interoperability.SentenceIteratorConverter;
import org.deeplearning4j.text.sentenceiterator.labelaware.LabelAwareSentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.primitives.Pair;

import java.util.*;

/**
 * Created by 69401 on 2018/4/10.
 */
public class CnnSentenceDataSetIterator implements DataSetIterator {

    public enum UnknownWordHandling{
        RemoveWord,UseUnknownVector
    }

    public static final String UNKNOWN_WORD_SENTINEL = "UNKNOWN_WORD_SENTINEL";

    private LabeledSentenceProvider sentenceProvider = null;

    private Word2Vec wordVectors;

    private  boolean useNormalizedWordVectors;

    private TokenizerFactory tokenizerFactory;

    private WeightLookupTable weightLookupTable;

    private VocabCache vocabCache;

    private UnknownWordHandling unknownWordHandling;

    private int minibatchSize;

    private int maxSentenceLength;

    private boolean sentencesAlongHeight;

    private DataSetPreProcessor dataSetPreProcessor;

    private int wordVectorSize;

    private int numClasses;

    private Map<String,Integer> labelClassMap;

    private INDArray unknown;

    private int cursor = 0;



    private CnnSentenceDataSetIterator(Builder builder){
        this.sentenceProvider = builder.sentenceProvider;
        this.wordVectors = builder.wordVectors;
        this.tokenizerFactory = builder.tokenizerFactory;
//        this.weightLookupTable = builder.weightLookupTable;
//        this.vocabCache = builder.vocabCache;
        this.unknownWordHandling = builder.unknownWordHandling;
        this.useNormalizedWordVectors = builder.useNormalizedWordVectors;
        this.minibatchSize = builder.minibatchSize;
        this.maxSentenceLength = builder.maxSentenceLength;
        this.sentencesAlongHeight = builder.sentencesAlongHeight;
        this.dataSetPreProcessor = builder.dataSetPreProcessor;
        this.numClasses = this.sentenceProvider.numLabelClasses();
        this.labelClassMap = new HashMap<>();
        int count = 0;

        //First: sort the labels to ensure the same label assignment order (say train vs. test)
        List<String> sortedLabels = new ArrayList<>(this.sentenceProvider.allLabels());
        Collections.sort(sortedLabels);

        for(String str : sortedLabels){
            this.labelClassMap.put(str,count++);
        }
        if (unknownWordHandling==UnknownWordHandling.UseUnknownVector){
            wordVectors.getWordVectorMatrixNormalized(wordVectors.getUNK());
        }else {
            wordVectors.getWordVectorMatrix(wordVectors.getUNK());
        }

        this.wordVectorSize = wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length;
        this.weightLookupTable = wordVectors.lookupTable();
        this.vocabCache = weightLookupTable.getVocabCache();
        System.out.println("----------------wordVectorSize is : "+this.wordVectorSize+"----------------------");

    }

    public INDArray loadSingleSentence(String sentence){
        List<String> tokens = tokenizeSentence(sentence);

        int[] featureShape = new int[]{1,1,0,0};
        if (sentencesAlongHeight){
            featureShape[2] = maxSentenceLength;
            featureShape[3] = wordVectorSize;
        }else {
            featureShape[2] = wordVectorSize;
            featureShape[3] = maxSentenceLength;
        }
        INDArray features = Nd4j.create(featureShape);
        for (int i=0;i<maxSentenceLength;++i){
            INDArray vector;
            if (i<tokens.size()){
                vector = getVector(tokens.get(i));
            }else {
                vector = Nd4j.create(wordVectorSize);
            }
            INDArrayIndex[] indices = new INDArrayIndex[4];
            indices[0] = NDArrayIndex.point(0);
            indices[1] = NDArrayIndex.point(0);
            if (sentencesAlongHeight){
                indices[2] = NDArrayIndex.point(i);
                indices[3] = NDArrayIndex.all();
            }else {
                indices[2] = NDArrayIndex.all();
                indices[3] = NDArrayIndex.point(i);
            }
            features.put(indices,vector);
        }
        return features;

    }

    private List<String> tokenizeSentence(String sentence) {
//        WeightLookupTable weightLookupTable = wordVectors.lookupTable();
//        VocabCache vocabCache = weightLookupTable.getVocabCache();
//        System.out.println(vocabCache.hasToken("婚姻"));
        String[] t = sentence.split(" ");
        List<String> tokens = new ArrayList<>();
        for (String token : t){
            if (!vocabCache.hasToken(token)){
                switch (unknownWordHandling) {
                    case RemoveWord:
                        continue;
                    case UseUnknownVector:
                        token = UNKNOWN_WORD_SENTINEL;
                }
            }
            tokens.add(token);
        }
        return tokens;
    }

    public Map<String,Integer> getLabelClassMap(){
        return new HashMap<>(labelClassMap);
    }

    private INDArray getVector(String word){
        INDArray vector;
        //Yes, this *should* be using == for the sentinel String here
        if (unknownWordHandling ==UnknownWordHandling.UseUnknownVector && word ==UNKNOWN_WORD_SENTINEL){
            vector = unknown;
        }else {
            if (useNormalizedWordVectors){
                vector = wordVectors.getWordVectorMatrixNormalized(word);
            }else {
                vector = wordVectors.getWordVectorMatrix(word);
            }
        }
        return vector;
    }
//    private

    @Override
    public DataSet next(int num) {
        if (sentenceProvider == null) {
            throw new UnsupportedOperationException("Cannot do next/hasNext without a sentence provider");
        }
        List<Pair<List<String>,String>> tokenizedSentences = new ArrayList<>(num);
        int maxLength = maxSentenceLength;
        int minLength = Integer.MAX_VALUE; //Track to we know if we can skip mask creation for "all same length" case
        for (int i=0;i<num && sentenceProvider.hasNext();++i){
            Pair<String,String> p = sentenceProvider.nextSentence();
            List<String> tokens = tokenizeSentence(p.getFirst());
//            maxLength = Math.max(maxLength,tokens.size());
            tokenizedSentences.add(new Pair<>(tokens,p.getSecond()));
        }
//        if (maxSentenceLength>0 && maxLength>maxSentenceLength){
//            maxLength = maxSentenceLength;
//        }
        int currMinibatchSize = tokenizedSentences.size();
        INDArray labels = Nd4j.create(currMinibatchSize,numClasses);
        for (int i=0;i<currMinibatchSize;++i){
            String labelStr = tokenizedSentences.get(i).getSecond();
            if (!labelClassMap.containsKey(labelStr)){
                throw new IllegalStateException("Got label \"" + labelStr
                        + "\" that is not present in list of LabeledSentenceProvider labels");

            }
            int labelIdx = labelClassMap.get(labelStr);

            labels.putScalar(i,labelIdx,1.0);
        }
        int[] featuresShape = new int[4];
        featuresShape[0] = currMinibatchSize;
        featuresShape[1] = 1;
        if (sentencesAlongHeight){
            featuresShape[2] = maxLength;
            featuresShape[3] = wordVectorSize;
        }else
        {
            featuresShape[2] = wordVectorSize;
            featuresShape[3] = maxLength;
        }
        INDArray features = Nd4j.create(featuresShape);
        for (int i=0;i<currMinibatchSize;++i){
            List<String> currSentence = tokenizedSentences.get(i).getFirst();
            for (int j=0;j<maxLength;++j){
                INDArray vector;
                if (j<currSentence.size()){
                    vector = getVector(currSentence.get(j));
                }else {
                    vector = Nd4j.create(wordVectorSize);
                }
                INDArrayIndex[] indices = new INDArrayIndex[4];

                indices[0] = NDArrayIndex.point(i);
                indices[1] = NDArrayIndex.point(0);
                if (sentencesAlongHeight){
                    indices[2] = NDArrayIndex.point(j);
                    indices[3] = NDArrayIndex.all();
                }else {
                    indices[2] = NDArrayIndex.all();
                    indices[3] = NDArrayIndex.point(j);
                }
                features.put(indices,vector);



            }
        }
        INDArray featuresMask = null;
        if (minLength != maxLength){
            featuresMask = Nd4j.create(currMinibatchSize,maxLength);
            for (int i=0;i<currMinibatchSize;++i){
                int sentenceLength = tokenizedSentences.get(i).getFirst().size();
                if (sentenceLength >= maxLength){
                    featuresMask.getRow(i).assign(1.0);
                }else {
                    featuresMask.get(NDArrayIndex.point(i),NDArrayIndex.interval(0,sentenceLength)).assign(1.0);
                }
            }
        }
        DataSet dataSet = new DataSet(features,labels,featuresMask,null);
        if (dataSetPreProcessor !=null){
            dataSetPreProcessor.preProcess(dataSet);
        }
        cursor += dataSet.numExamples();
        return dataSet;

    }

    @Override
    public int totalExamples() {
        return sentenceProvider.totalNumSentences();
    }

    @Override
    public int inputColumns() {
        return wordVectorSize;
    }

    @Override
    public int totalOutcomes() {
        return numClasses;
    }

    @Override
    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public void reset() {
        cursor = 0;
        sentenceProvider.reset();
    }

    @Override
    public int batch() {
        return minibatchSize;
    }

    @Override
    public int cursor() {
        return cursor;
    }

    @Override
    public int numExamples() {
        return totalExamples();
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor dataSetPreProcessor) {
        this.dataSetPreProcessor = dataSetPreProcessor;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public DataSetPreProcessor getPreProcessor() {
        return dataSetPreProcessor;
    }

    @Override
    public List<String> getLabels() {
        //We don't want to just return the list from the LabelledSentenceProvider, as we sorted them earlier to do the
        // String -> Integer mapping
        String[] str  = new String[labelClassMap.size()];
        for (Map.Entry<String,Integer> e : labelClassMap.entrySet()){
            str[e.getValue()] = e.getKey();
        }
        return Arrays.asList(str);
    }

    @Override
    public boolean hasNext() {
        if (sentenceProvider ==null){
            throw new UnsupportedOperationException("Cannot do next/hasNext without a sentence provider");
        }
        return sentenceProvider.hasNext();
    }

    @Override
    public DataSet next() {
        return next(minibatchSize);
    }

    public static class Builder{

        private LabeledSentenceProvider sentenceProvider = null;
        private Word2Vec wordVectors;
        private TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        private UnknownWordHandling unknownWordHandling = UnknownWordHandling.RemoveWord;
        private boolean useNormalizedWordVectors = true;
        private int maxSentenceLength = -1;
        private int minibatchSize = 32;
        private boolean sentencesAlongHeight = true;
        private DataSetPreProcessor dataSetPreProcessor;

        /**
         * Specify how the (labelled) sentences / documents should be provided
         */
        public Builder sentenceProvider(LabeledSentenceProvider labeledSentenceProvider){
            this.sentenceProvider = labeledSentenceProvider;
            return this;
        }
        /**
         * Specify how the (labelled) sentences / documents should be provided
         */
        public Builder sentenceProvider(LabelAwareIterator iterator,@NonNull List<String> labels){
            LabelAwareConverter converter = new LabelAwareConverter(iterator,labels);
            return sentenceProvider(converter);
        }
        /**
         * Specify how the (labelled) sentences / documents should be provided
         */
        public Builder sentenceProvider(LabelAwareDocumentIterator iterator,@NonNull List<String> labels){
            DocumentIteratorConverter converter = new DocumentIteratorConverter(iterator);
            return sentenceProvider(converter,labels);
        }
        /**
         * Specify how the (labelled) sentences / documents should be provided
         */
        public Builder sentenceProvider(LabelAwareSentenceIterator iterator,@NonNull List<String> labels){
            SentenceIteratorConverter converter = new SentenceIteratorConverter(iterator);
            return sentenceProvider(converter,labels);
        }
        /**
         * Provide the WordVectors instance that should be used for training
         */
        public Builder wordVectors(Word2Vec wordVectors) {
            this.wordVectors = wordVectors;
            return this;
        }
        /**
         * The {@link TokenizerFactory} that should be used. Defaults to {@link DefaultTokenizerFactory}
         */
        public Builder tokenizerFactory(TokenizerFactory tokenizerFactory) {
            this.tokenizerFactory = tokenizerFactory;
            return this;
        }
        /**
         * Specify how unknown words (those that don't have a word vector in the provided WordVectors instance) should be
         * handled. Default: remove/ignore unknown words.
         */
        public Builder unknownWordHandling(UnknownWordHandling unknownWordHandling){
            this.unknownWordHandling = unknownWordHandling;
            return this;
        }
        /**
         * Minibatch size to use for the DataSetIterator
         */
        public  Builder minibatchSize(int minibatchSize){
            this.minibatchSize = minibatchSize;
            return this;
        }
        /**
         * Whether normalized word vectors should be used. Default: true
         */
        public Builder useNormalizedWordVectors(boolean useNormalizedWordVectors){
            this.useNormalizedWordVectors = useNormalizedWordVectors;
            return  this;
        }
        /**
         * Maximum sentence/document length. If sentences exceed this, they will be truncated to this length by
         * taking the first 'maxSentenceLength' known words.
         */
        public Builder maxSentenceLength(int maxSentenceLength){
            this.maxSentenceLength = maxSentenceLength;
            return this;
        }
        /**
         * If true (default): output features data with shape [minibatchSize, 1, maxSentenceLength, wordVectorSize]<br>
         * If false: output features with shape [minibatchSize, 1, wordVectorSize, maxSentenceLength]
         */
        public Builder sentencesAlongHeight(boolean sentencesAlongHeight){
            this.sentencesAlongHeight = sentencesAlongHeight;
            return this;
        }
        /**
         * Optional DataSetPreProcessor
         */
        public Builder dataSetPreProcessor(DataSetPreProcessor dataSetPreProcessor){
            this.dataSetPreProcessor = dataSetPreProcessor;
            return this;
        }

        public CnnSentenceDataSetIterator build(){
            if (wordVectors==null){
                throw new IllegalStateException(
                        "Cannot build CnnSentenceDataSetIterator without a WordVectors instance"
                );
            }
            return new CnnSentenceDataSetIterator(this);
        }



    }
}
