package DB;

import Model.Law;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 69401 on 2018/3/19.
 */
public class DAO implements DAOInterface{


    public Map<String, Integer> queryByID(MongoDatabase db, String table, Object Id) throws Exception {
        return null;
    }

    public String insert(MongoDatabase db, String table, Document doc) {

        MongoCollection<Document> collection = db.getCollection(table);
        collection.insertOne(doc);
        long count = collection.count(doc);

        System.out.println("count: "+count);
        if(count == 1){
            return "文档插入成功";
        }else{
            return "文档插入失败";
        }
    }

    public boolean delete(MongoDatabase db, String table, BasicDBObject doc) {
        return false;
    }

    public boolean update(MongoDatabase db, String table, BasicDBObject oldDoc, BasicDBObject newDoc) {
        return false;
    }
}
