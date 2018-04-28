package Word;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.nio

public class FileLineDataHandler implements DataProcessHandler {
    private String encode = "utf-8";
    private Collection<String> articles = new ArrayList<String>();
    private int flag = 0;
//    private NLPIR nlpir;

//    public void registerNLPIR(NLPIR nlpir) throws UnsupportedEncodingException {
//        this.nlpir = nlpir;
//        this.nlpir.init();
//    }
//
//    public void destoryNLPIR(){
//        this.nlpir.unInit();
//    }
    @Override
    public void process(byte[] data,NLPIR nlpir) {

//        Main demo = new Main();
        try {
            String tempString = new String(data,encode);
//            System.out.println(tempString);
            String result = "";
            String[] strs = tempString.split(" ");
            StringBuilder text = new StringBuilder();
            for (String tmp : strs){
                if (!isContainNumber(tmp)){
                    text.append(tmp);
                }
            }
            result= text.toString();
//            System.out.println(result);
            result = Main.getNLPIRresult(nlpir,result,Main.getStopWords());

            System.out.println("articles size : "+(++flag));
            articles.add(result);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public Collection<String> getresult(){
        return articles;
    }

    private static boolean isContainNumber(String company) {

        Pattern p = Pattern.compile("[0-9]|[a-zA-Z]");
        Matcher m = p.matcher(company);
        return m.find();

    }
}

