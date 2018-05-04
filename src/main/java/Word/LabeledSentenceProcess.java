package Word;

import javafx.util.Pair;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabeledSentenceProcess implements Serializable {

    int totalcount = 0;

    Map<String,Pair<String,INDArray>> filesBylabel = new HashMap<>();//数据格式Map<label,Pair<sentence,doc2vec>>

    List<String> stringList = new ArrayList<>();

    List<String> labelList = new ArrayList<>();

    Word2Vec word2Vec;

    int word2VecSize;

    public LabeledSentenceProcess(String datapath, Word2Vec word2Vec){
        this.word2Vec = word2Vec;
        this.word2VecSize = word2Vec.getWordVector(word2Vec.vocab().wordAtIndex(0)).length;

        BufferedReader buffered = null;

        try {
            buffered = new BufferedReader(new InputStreamReader(new FileInputStream(datapath)));
            String line = buffered.readLine();
            while(line != null){
                String[] lines = line.split("\t");
                String label = lines[0];
                String content = lines[1];
                if ("".equals(label)||" ".equals(label)){
                    line = buffered.readLine();
                    continue;
                }
                labelList.add(label);
                stringList.add(content);
                totalcount++;
                line = buffered.readLine();
//                filesBylabel.put(label,content);

            }
            buffered.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("TotalCount is : "+totalcount);

        for (int i=0;i<totalcount;++i){
            Map<String,Pair<Double,INDArray>> tokens = tokenizeSentence(stringList.get(i));
            filesBylabel.put(labelList.get(i),new Pair<>(stringList.get(i),Doc2vec(false,tokens)));
        }

    }

    private INDArray Doc2vec(boolean isNew,Map<String,Pair<Double,INDArray>> tokens){
        INDArray doc2vec = Nd4j.create(word2Vec.getWordVector(word2Vec.vocab().wordAtIndex(0)).length);
        for (Map.Entry<String,Pair<Double,INDArray>> set : tokens.entrySet()){
            String word = set.getKey();
            int times = 0;
            int total = totalcount;
            if (isNew) {
                times = 1;
                ++total;
            }
            for (int j =0 ;j<totalcount;++j){
                String tmp = stringList.get(j);
                if (tmp.contains(word)){
                    times++;
                }
            }
            double dou = total/(times+1);
            Double idf = Math.log(dou)/Math.log(10);
            Pair<Double,INDArray> value = set.getValue();
            INDArray vec = value.getValue().mul(value.getKey()*idf);
            doc2vec.addi(vec);
        }
        doc2vec.divi(tokens.size());
        return doc2vec;
    }

    public List<Pair<String,Double>> calculateSimilarity(String sentence){


        List<Pair<String,Double>> pairList = new ArrayList<>();
        Map<String,Pair<Double,INDArray>> tokens = tokenizeSentence(sentence);

        INDArray doc2vec = Doc2vec(true,tokens);
        for (Map.Entry<String,Pair<String,INDArray>> doc : filesBylabel.entrySet()){

            String label = doc.getKey();
            Pair<String,INDArray> content = doc.getValue();
            INDArray contentvec = content.getValue();
            Double result = calculateCos(doc2vec,contentvec);
            pairList.add(new Pair<>(label,result));
        }

        return  pairList;

    }

    private double calculateCos(INDArray doc2vec,INDArray contentvec){
        double k1 = 0;
        double k2 = 0;
        double k3 = 0;
        for (int i=0;i<word2VecSize;++i){
            double x = doc2vec.getDouble(0,i);
            double y = contentvec.getDouble(0,i);
            k1 += x*y;
            k2 += x*x;
            k3 += y*y;
        }
        k2 = Math.sqrt(k2);
        k3 = Math.sqrt(k3);
        return  0.5+0.5*k1/(k2*k3);
    }

    private  Map<String,Pair<Double,INDArray>> tokenizeSentence(String sentence) {
//        WeightLookupTable weightLookupTable = wordVectors.lookupTable();
//        VocabCache vocabCache = weightLookupTable.getVocabCache();
//        System.out.println(vocabCache.hasToken("婚姻"));
        WeightLookupTable weightLookupTable = word2Vec.lookupTable();
        VocabCache vocabCache = weightLookupTable.getVocabCache();
//        Pair<String,Integer> word = new Pair<>();
//        List<String> tokens = new ArrayList<>();
        String[] t = sentence.split(" ");
        Map<String,Pair<Double,INDArray>> tokens = new HashMap<>();
        for (int i=0;i<t.length;++i){
            String word = t[i];
            if (!vocabCache.hasToken(word)){
                continue;
            }
            Double tf1 = 1.0/t.length;
            if (tokens.containsKey(word)){
                Pair old = tokens.remove(word);
                Double num = (Double) old.getKey()+tf1;
                tokens.put(word,new Pair<>(num,(INDArray) old.getValue()));
            }else {
                INDArray vector = word2Vec.getWordVectorMatrixNormalized(word);
                tokens.put(word,new Pair<>(tf1,vector));
            }

        }
        return tokens;
    }

    public int getTotalcount(){
        return this.totalcount;
    }

    public int getWord2VecSize() {
        return word2VecSize;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public List<String> getStringList() {
        return stringList;
    }
}
