package DB;

import Model.Law;
import Model.LawModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 69401 on 2018/3/27.
 */
@RunWith(SpringRunner.class)
//@SpringBootConfiguration
@SpringBootTest(classes = LawDBInterface.class)
@EnableMongoRepositories(basePackages = "DB")
public class DBTest {

    @Autowired
    LawDBInterface lawDBInterface;

    @Test
    public void findLawBy法律名称(){
        LawModel law= lawDBInterface.findByname("中华人民共和国慈善法");
        System.out.println("user is "+law);
    }

}
