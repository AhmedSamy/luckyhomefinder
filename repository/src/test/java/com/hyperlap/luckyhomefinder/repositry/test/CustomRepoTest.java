package com.hyperlap.luckyhomefinder.repositry.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.repository.CustomRepository;
import com.hyperlab.luckyhomefinder.repository.spring.RepositoryConfiguration;
import static org.junit.Assert.*;

public class CustomRepoTest {
	CustomRepository customeRepos;

	@Before
	public void setup() {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				RepositoryConfiguration.class);
		customeRepos = context.getBean(CustomRepository.class);
	}

	@Test
	public void testCustomeFind() {
		Query query = new Query();
		query.with(new Sort(Sort.Direction.DESC, "postDate"));
		Property result = customeRepos.findLastProperty();
		assertNotNull(result);
		assertNotNull(customeRepos);
		
	}

	
}
