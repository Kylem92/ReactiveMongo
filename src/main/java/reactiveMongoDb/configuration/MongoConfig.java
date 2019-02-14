//package reactiveMongoDb.configuration;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
//import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
//import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
//
//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//
//@Configuration
//@EnableReactiveMongoRepositories("reactiveMongoDb.repositories")
//public class MongoConfig extends AbstractReactiveMongoConfiguration{
//
//	@Value("${spring.data.mongodb.uri}")
//	private String mongoHost;
//
//	@Bean
//	public ReactiveMongoTemplate reactiveMongoTemplate() {
//	    return new ReactiveMongoTemplate(reactiveMongoDatabaseFactory());
//	}
//
//	@Bean
//	public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory() {
//	    return new SimpleReactiveMongoDatabaseFactory(reactiveMongoClient(), "react");
//	}
//
//	@Override
//	public MongoClient reactiveMongoClient() {
//		return MongoClients.create(mongoHost);
//	}
//
//	@Override
//	protected String getDatabaseName() {
//		return "react";
//	}
//}
