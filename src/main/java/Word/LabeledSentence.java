package Word;

import org.datavec.api.util.RandomUtils;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.nd4j.linalg.collection.CompactHeapStringList;
import org.nd4j.linalg.primitives.Pair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by 69401 on 2018/4/10.
 */
public class LabeledSentence implements LabeledSentenceProvider {

    private int totalcount;

    private Map<String,String> filesBylabel;

    private List<String> stringList;

    private final List<String> sentencesList;

    private final int[] labelIndexes;

    private final Random random;

    private final int[] order;

    private final List<String> allLabels;

    private  int cursor = 0;

    public LabeledSentence(String path){
        this(path,new Random());
    }

    public LabeledSentence (String path,Random random){

        totalcount = 0;

        filesBylabel = new HashMap<String,String>();

        stringList = new ArrayList<String>();

        BufferedReader buffered = null;

        try {
            buffered = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line = buffered.readLine();
            while(line != null){
                String[] lines = line.split("\t");
                String label = lines[0];
                String content = lines[1];
                stringList.add(content);
                totalcount++;
                line = buffered.readLine();
                filesBylabel.put(label,content);
            }
            buffered.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("TotalCount is : "+totalcount);
//        filesBylabel.put()
        this.random = random;

        if (random ==null){
            order = null;
        }else {
            order = new int[totalcount];
            for (int i=0;i<totalcount;++i){
                order[i] = i;
            }
            RandomUtils.shuffleInPlace(order,random);
        }
        allLabels = new ArrayList<>(filesBylabel.keySet());
        Collections.sort(allLabels);
        Map<String,Integer> labelsToIdx = new HashMap<>();

        for (int i=0;i<allLabels.size();++i){
            labelsToIdx.put(allLabels.get(i),i);
        }
        sentencesList = new CompactHeapStringList();
        labelIndexes = new int[totalcount];
        int position = 0;
        for (Map.Entry<String,String> entry:filesBylabel.entrySet()){
            int labelIdx = labelsToIdx.get(entry.getKey());
            sentencesList.add(entry.getValue());
            labelIndexes[position] = labelIdx;
            position++;
        }
    }


    @Override
    public boolean hasNext() {
        return cursor < totalcount;
    }

    @Override
    public Pair<String, String> nextSentence() {
        int idx;
        if (random==null){
            idx = cursor++;
        }else {
            idx = order[cursor++];
        }
        String label = allLabels.get(labelIndexes[idx]);
        String sentence;
        sentence = stringList.get(idx);
        return new Pair<>(sentence,label);
    }

    @Override
    public void reset() {
        cursor = 0;
        if (random!=null){
            RandomUtils.shuffleInPlace(order,random);
        }
    }

    @Override
    public int totalNumSentences() {
        return totalcount;
    }

    @Override
    public List<String> allLabels() {
        return allLabels;
    }

    @Override
    public int numLabelClasses() {
        return allLabels.size();
    }
}
