package com.hyperlap.luckyhomefinder.repositry.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.common.domain.PropertyType;
import com.hyperlab.luckyhomefinder.repository.PropertyRepository;
import com.hyperlab.luckyhomefinder.repository.spring.RepositoryConfiguration;

import static org.junit.Assert.assertNotNull;

public class propertyRepositoryTest {

    PropertyRepository propertyRepository;

    @Before
    public void setup() {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                RepositoryConfiguration.class);
        propertyRepository = context.getBean(PropertyRepository.class);
    }

    @Test
    public void testRepositoryOperations() throws Exception {
        propertyRepository.deleteAll();
        List<Property> properties = initProperties();
        List<Property> savedProperties = propertyRepository.save(properties);
        assertNotNull(properties);
        assertNotNull(savedProperties);
    }

    private List<Property> initProperties() throws Exception {
        List<Property> properties = new ArrayList<Property>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd aa.hh:mm");

        properties.add(new Property(UUID.randomUUID(), "1002435", "SHATIN",
                dateFormat.parse("1925.05.10 PM.12:33"), 1235, 50000000l, 200,
                PropertyType.APARTMENT, true, false,
                "http://eng.28hse.com/utf8/property106671.html", true, true));
        properties.add(new Property(UUID.randomUUID(), "10021335", "SHATIN",
                dateFormat.parse("1925.05.10 PM.12:33"), 12535, 520000000l,
                200, PropertyType.APARTMENT, true, false,
                "http://eng.28hse.com/utf8/property10226671.html", true, true));
        properties.add(new Property(UUID.randomUUID(), "1002435", "SHATIN",
                dateFormat.parse("1925.05.10 PM.12:33"), 1235, 50000000l, 200,
                PropertyType.APARTMENT, true, false,
                "http://eng.28hse.com/utf8/property106671.html", true, true));
        properties.add(new Property(UUID.randomUUID(), "1002435", "SHATIN",
                dateFormat.parse("1925.05.10 PM.12:33"), 1235, 50000000l, 200,
                PropertyType.APARTMENT, true, false,
                "http://eng.28hse.com/utf8/property106671.html", true, true));
        properties.add(new Property(UUID.randomUUID(), "1002435", "SHATIN",
                dateFormat.parse("1925.05.10 PM.12:33"), 1235, 50000000l, 200,
                PropertyType.APARTMENT, true, false,
                "http://eng.28hse.com/utf8/property106671.html", true, true));
        properties.add(new Property(UUID.randomUUID(), "1002435", "SHATIN",
                dateFormat.parse("1925.05.10 PM.12:33"), 1235, 50000000l, 200,
                PropertyType.APARTMENT, true, false,
                "http://eng.28hse.com/utf8/property106671.html", true, true));
        return properties;
    }
}
