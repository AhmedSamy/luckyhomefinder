package com.hyperlab.luckyhomefinder.sites.hse28.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.xml.sax.helpers.ParserFactory;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.repository.PropertyRepository;
import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;
import com.hyperlab.luckyhomefinder.service.Exceptions.ManagerException;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParserFactory;
import com.hyperlap.luckyhomefinder.service.ParsersManager;
import com.hyperlap.luckyhomefinder.sites.hse28.spring.H28seConfiguration;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ManagerTest {

	private ParsersManager manager;

	@Before
	public void setup() {

		ApplicationContext context = new AnnotationConfigApplicationContext(
				H28seConfiguration.class);
		manager = (ParsersManager) context.getBean("h28seManager");
	}

	@Test
	public void testDispatchPropertyFetchers() throws ManagerException {
		List<String> links = new ArrayList<>();
		links.add("http://www.28hse.com/en/utf8/property111774.html");
		links.add("http://www.28hse.com/en/utf8/property96947.html");
		links.add("http://www.28hse.com/en/utf8/property96949.html");
		links.add("http://www.28hse.com/en/utf8/property96951.html");
//		final List<Property> properties = manager
//				.dispatchPropertyFetchers(links);
		assertNotNull(properties);
		assertEquals(4, properties.size());
	}

	@Test
	public void testPersistProperties() {
		List<Property> properties = new ArrayList<>();
//		properties.add(null);
//		properties.add(new Property());
		Property property = new Property();
		property.setAgent(true);
		property.setDistrict("SHATIN");
		property.setFeets(300);
		property.setId(UUID.randomUUID());
		property.setOwner(true);
		property.setSiteId("112335");
		properties.add(property);
		manager.persistProperties(properties);
	}
	@Test
	public void testManager() throws LinksFetcherException{
		List<String> links = new H28seLinksFetcher().fetchLinks("111000");
		manager.processLinks(links);
	}
}
