package za.co.tangent.service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;

import za.co.tangent.domain.Helper;
import za.co.tangent.domain.Helper.Counter;

@Service
public class MongoDBCounterService {

	 @Bean
	 public MongoDbFactory mongoDbFactory() throws UnknownHostException{
		 return new SimpleMongoDbFactory(new MongoClient("localhost", 27017), "test");
	 }

	@Bean
	 public MongoOperations mongoOperations() throws UnknownHostException{
		return new MongoTemplate(mongoDbFactory());
	 }
	   
	public int getNextSequence(String collectionName) {
		MongoOperations mongoOperations = null;
		//Query query = new Query();
		//query.addCriteria(Criteria.where("_id").is(collectionName));
		//List<Counter> result = mongoOperations.find(query, Counter.class);
		//List<Counter> result = mongoOperations.find(Query.query(Criteria.where("_id").is("file")), Counter.class);
		/*if (result.isEmpty()){
			mongoOperations.insert("{ '_id' : collectionName, 'seq' : 0 }");
		}*/
		try {
			mongoOperations = mongoOperations();
			Query q = query(where("_id").is(collectionName));
			Update u = new Update().inc("seq", 1);
			FindAndModifyOptions o = options().returnNew(true);
			Counter counter = mongoOperations.findAndModify(q, u, o, Counter.class);
			return counter.getSeq();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	 }
}
