package com.hyperlab.luckyhomefinder.sites.hse28.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.repository.PropertyRepository;
import com.hyperlab.luckyhomefinder.repository.spring.ReposConf;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParser;
import com.hyperlab.luckyhomefinder.sites.hse28.services.H28sePropertyFetcher;

/**
 * This is an integration test for property fetcher.
 */

public class PropertyParserIntegrationTest {

	private static int propertyCounter = 107051;
	PropertyRepository propertyRepository;
	PropertyParser fetcher;

	@Before
	public void setup() {
		ApplicationContext context = new AnnotationConfigApplicationContext(
				ReposConf.class);
		propertyRepository = context.getBean(PropertyRepository.class);
	}

	@Test
	public void propertyIT() throws Exception {

		CountDownLatch counter = new CountDownLatch(4);
		List<String> links = new ArrayList<>();
		List<Property> properties = new ArrayList<>();
		Property property;
		for (int i = 0; i < 4; i++) {
			property = new Property();
			properties.add(property);
			new H28sePropertyFetcher(property, counter, nextLink());
		}
		counter.await();
printProperties(properties);
		// while (true) {
		//
		// link = nextLink();
		// System.out.println("processing Link: " + link);
		// property = fetcher.fetchProperty(link);
		// if (property != null) {
		// propertyRepository.save(property);
		// }
		// // Sleep
		// if (propertyCounter <= 5000) {
		// break;
		// }
		// Thread.sleep((new Random(System.currentTimeMillis()).nextInt(3) *
		// 1000) + 1500);
		// }
	}

	private final void printProperties(final List<Property> properties) {
		for (Property property : properties) {
			if (property != null) {
				System.out.println(property.getPropertyType());
				System.out.println(property.getSiteId());
				System.out.println(property.getDistrict());
			}
		}
	}

	/**
	 * This is a helper function to generate property links.
	 */
	private final String nextLink() {
		propertyCounter -= 1;
		String link = "http://eng.28hse.com/utf8/property" + propertyCounter
				+ ".html";
		return link;
	}
}
