package DB;

import Model.*;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by 69401 on 2018/3/28.
 */
@Service
public class LawDBImpl implements LawDBInterface{

    @Autowired
    private MongoTemplate mongoTemplate;

    DBUtil dbUtil = new DBUtil();
    MongoClient mongoClient = dbUtil.getMongoClient();
    MongoDatabase mongoDatabase = dbUtil.getMongoDataBase(mongoClient);
    int num = 0;
//    Law law = null;
    List<LawModel> lawlist = new ArrayList<LawModel>();
    List<String> mullist = new ArrayList<String>();
    /**
     * 根据法律名称查询对象
     * @param Lawname
     * @return
     */
    @Override
    public LawModel findByname(String Lawname) {

        try{
            Query query=new Query(Criteria.where("lawname").is(Lawname));
//        Query query1 = new Query("")
            LawModel lawModel =  mongoTemplate.findOne(query , LawModel.class);
            return lawModel;
        }catch (Exception e){
            return null;
        }


    }

    @Override
    public void save(List<LawModel> lawList) {
        mongoTemplate.insert(lawList,"lawModel3");
        System.out.println("-------------finish insert----------------");
    }

    @Override
    public void delete() {
        mongoTemplate.dropCollection("lawModel");
        System.out.println("-------------finish drop----------------");
    }

    @Override
    public List<LawModel> findLawAndUpdate() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("Law3");
//        BasicDBObject query = new BasicDBObject();
//        query.put("法律名称",name);
        FindIterable<Document> iterable = collection.find();

        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
//                if (document.getString("法律名称").equals("中华人民共和国慈善法")){
                    System.out.println(document.getString("法律名称"));
                    System.out.println("----------Start DocToLaw-----------");
                    lawlist.add(DocToLaw(document));
//                }

            }
        });



        return lawlist;
    }

    //Document转Law类
    private LawModel DocToLaw(Document doc){
        LawModel law1 = new LawModel();
        law1.setName(doc.getString("法律名称"));
        if (doc.getString("效力级别")!=null){
            System.out.println(doc.getString("效力级别"));
            law1.setLevel(doc.getString("效力级别"));
        }
        if (doc.getString("时效性")!=null){
            System.out.println(doc.getString("时效性"));
            law1.setTimelimit(doc.getString("时效性"));
        }

        if (doc.getString("发布日期")!=null){
            System.out.println(doc.getString("发布日期"));
            law1.setPublishtime(doc.getString("发布日期"));
        }

        if (doc.getString("实施日期")!=null){
            System.out.println(doc.getString("实施日期"));
            law1.setStarttime(doc.getString("实施日期"));
        }
        Document tiao = (Document) doc.get("法律内容");
//        List<Tiao> tiaoList = new ArrayList<Tiao>();
        LinkedHashMap<String, TiaoModel> tiaoList = new LinkedHashMap<>();
        Set<String> keys = tiao.keySet();
        for (String tmp:keys){
//            System.out.println(tmp);
            TiaoModel tiaotmp  = new TiaoModel();
//            tiaotmp.setContent(tmp);
//            tiaotmp.setKuan();
            Document kuan = (Document) tiao.get(tmp);
            Set<String> kuankeys = kuan.keySet();
            LinkedHashMap<String,KuanModel> kuanList = new LinkedHashMap<String,KuanModel>();
            int k = 0;
            for (String a :kuankeys){
                ++k;
                KuanModel kuantmp = new KuanModel();
//                kuantmp.setContent(a);
                Document xiangroot = (Document)kuan.get(a);
                kuantmp.setContent(xiangroot.getString("内容"));
                Document xiang  = (Document) xiangroot.get("项");
                if (xiang!=null){
                    Set<String> xiangkeys = xiang.keySet();
                    List<String> xianglist = new ArrayList<String>();
//                    int t = 0;
                    for (String b:xiangkeys){
//                        ++t;
                        String xiangtmp = xiang.getString(b);
                        xianglist.add(xiangtmp);
                    }
                    kuantmp.setXiang(xianglist);
                }
                String k1 = toChinese(String.valueOf(k));

                kuanList.put("第"+k1+"款",kuantmp);
            }
            tiaotmp.setKuan(kuanList);
            tiaoList.put(tmp,tiaotmp);
        }
        law1.setTiao(tiaoList);





        return law1;
    }

    private String toChinese(String string) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

        String result = "";

        int n = string.length();
        for (int i = 0; i < n; i++) {

            int num = string.charAt(i) - '0';

            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
            System.out.println("  "+result);
        }

        System.out.println("----------------");
        System.out.println(result);
        return result;

    }



    /**
     * 查重
     */
//    if (lawlist.contains(document.getString("法律名称"))){
//        mullist.add(document.getString("法律名称"));
//    }else {
//        lawlist.add(document.getString("法律名称"));
//        System.out.println(document.toJson());
//        ++num;
//    }
//    for (String tmp :lawlist){
//        System.out.println(tmp);
//    }
//        System.out.println("------------------重复的法律名称----------------");
//        for (String tmp :mullist){
//        System.out.println(tmp);
//    }



}
