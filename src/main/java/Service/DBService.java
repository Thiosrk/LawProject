package Service;

//import DB.DAO;
import DB.DAO;
import DB.DBUtil;
import Model.Kuan;
import Model.Law;
import Model.Tiao;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

/**
 * Created by 69401 on 2018/3/19.
 */
public class DBService {

    DBUtil dbUtil = new DBUtil();
    MongoClient mongoClient = dbUtil.getMongoClient();
    MongoDatabase mongoDatabase = dbUtil.getMongoDataBase(mongoClient);

    public void insertLaw(Law law){

        DAO dao = new DAO();

        Document tiaosroot = new Document();
        List<Tiao> tiaos = law.getTiao();
        if (tiaos==null){

        }else {
            for (Tiao tiao : tiaos){
                List<Kuan> kuans = tiao.getKuan();
                Document kuanroot = new Document();
                for (int i=0;i<kuans.size();++i){
                    Kuan kuan = kuans.get(i);
                    Document kuancontent = new Document("内容",kuan.getContent());
                    List<String> xiang = kuan.getXiang();
                    if (xiang!=null){
                        Document xiangcontent = new Document();

                        for (int j = 0;j<xiang.size();++j){
                            xiangcontent.append(j+1+"",xiang.get(j));
                        }
                        kuancontent.append("项",xiangcontent);
                    }
                    kuanroot.append("款"+(i+1),kuancontent);

                }
                tiaosroot.append(tiao.getContent(),kuanroot);

            }
        }


        Document lawroot = new Document("法律名称",law.getName())
                .append("时效性",law.getTimelimit())
                .append("效力级别",law.getLevel())
                .append("发布日期",law.getPublishtime())
                .append("实施日期",law.getStarttime())
                .append("法律内容",tiaosroot);

        String result = dao.insert(mongoDatabase,"Law3",lawroot);

        System.out.println(result);
    }


}
