package DB;

import Model.Law;
import Model.LawModel;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
//import nju.software.wsjd.model.lawModel.Law;
import org.bson.Document;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Created by 69401 on 2018/3/19.
 */

/**
 * Dao 层接口
 *
 */
//@ComponentScan("DB")
//@Component
public interface LawDBInterface {

        public LawModel findByname(String name);

        public void save(List<LawModel> lawList);

        public void delete();

        public List<LawModel> findLawAndUpdate();



}
