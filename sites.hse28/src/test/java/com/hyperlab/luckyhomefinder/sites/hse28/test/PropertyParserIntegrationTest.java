package com.hyperlab.luckyhomefinder.sites.hse28.test;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.repository.PropertyRepository;
import com.hyperlab.luckyhomefinder.repository.spring.RepositoryConfiguration;
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
        fetcher = new H28sePropertyFetcher();
        ApplicationContext context = new AnnotationConfigApplicationContext(
                RepositoryConfiguration.class);
        propertyRepository = context.getBean(PropertyRepository.class);
    }

    @Test
    public void propertyIT() throws Exception {

        String link = null;
        Property property = null;
        while (true) {

            link = nextLink();
            System.out.println("processing Link: " + link);
            property = fetcher.fetchProperty(link);
            if (property != null) {
                propertyRepository.save(property);
            }
            // Sleep
            if (propertyCounter <= 5000) {
                break;
            }
            Thread.sleep((new Random(System.currentTimeMillis()).nextInt(3) * 1000) + 1500);
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
