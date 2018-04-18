package DB;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Map;

/**
 * Created by 69401 on 2018/3/29.
 */
public interface DAOInterface {

    public Map<String, Integer> queryByID(MongoDatabase db, String table, Object Id) throws Exception;

    public String insert(MongoDatabase db, String table, Document doc);

    public boolean delete(MongoDatabase db, String table, BasicDBObject doc);

    public boolean update(MongoDatabase db, String table, BasicDBObject oldDoc, BasicDBObject newDoc);

}
