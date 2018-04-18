package DB;

import Model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by 69401 on 2018/3/28.
 */
public interface PersonRepository extends MongoRepository<Person,String>{

    public Person findByName(String firstname);

}
