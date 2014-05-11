package com.hyperlab.luckyhomefinder.repositry.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.repository.spring.ReposConf;
import com.hyperlab.luckyhomefinder.repository.custom.CustomRepository;
import com.hyperlab.luckyhomefinder.repository.custom.CustomRepositoryImpl;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ReposConf.class)
public class CustomRepoTest {

	@Autowired
	private CustomRepository customeRepos;

	@Before
	public void setup() {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				ReposConf.class);
	}

	@Test
	public void testCustomeFind() {
		Property result = customeRepos.findLastProperty("28hse");
		assertNotNull(result);
		assertNotNull(customeRepos);

	}

}
