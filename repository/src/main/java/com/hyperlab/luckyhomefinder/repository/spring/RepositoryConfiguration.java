package com.hyperlab.luckyhomefinder.repository.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.hyperlab.luckyhomefinder.repository.CustomRepository;
import com.hyperlab.luckyhomefinder.repository.CustomRepositoryImpl;
import com.mongodb.Mongo;

@Configuration
@PropertySource("classpath:mongodb.properties")
@ComponentScan(basePackages = { "com.hyperlab.luckyhomefinder.repository" })
@EnableMongoRepositories("com.hyperlab.luckyhomefinder.repository")
public class RepositoryConfiguration extends AbstractMongoConfiguration {

	@Value("${mongodb.username}")
	private String username;
	@Value("${mongodb.password}")
	private String password;
	@Value("${mongodb.database}")
	private String database;
	@Value("${mongodb.url}")
	private String url;

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		org.springframework.context.support.PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources = new ClassPathResource[] { new ClassPathResource(
				"mongodb.properties") };
		properties.setLocations(resources);
		properties.setIgnoreResourceNotFound(true);
		return properties;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDbFactory(), mappingMongoConverter());
	}


	@Bean
	public CustomRepository customeRepository() {
		return new CustomRepositoryImpl();
	}

	@Override
	protected String getDatabaseName() {
		return database;
	}

	@Override
	public Mongo mongo() throws Exception {
		return new Mongo(url);
	}

}
