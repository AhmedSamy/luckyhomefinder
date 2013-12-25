package com.hyperlap.luckhomefinder.crawler.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.hyperlap.luckhomefinder.crawlers.domain.Property;
import com.hyperlap.luckhomefinder.crawlers.domain.PropertyType;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import come.hyperlap.luckyhomefinder.crawlers.h28se.H28sePropertyFetcher;

public class H28sePropertyFetcherTest {

	private H28sePropertyFetcher fetcher;

	@Before
	public void setup() {
		fetcher = new H28sePropertyFetcher();
	}

	@After
	public void tearDown() {
		fetcher = null;
	}

	@Test
	public void testFetchPropertyNoRent() throws Exception {
		final String LINK = "http://eng.28hse.com/utf8/property105184.html";
		Property property = fetcher.fetchProperty(LINK);
		assertNotNull(property);
		assertFalse(property.isForRent());
		assertEquals(0L, property.getRent());
	}
	@Test
	public void testFetchPropertyRent() throws Exception {
		final String LINK = "http://eng.28hse.com/utf8/property105242.html";
		Property property = fetcher.fetchProperty(LINK);
		assertNotNull(property);
		assertTrue(property.isForRent());
		assertTrue(property.getRent()>0);
	}
	
	@Test
	public void testFetchPropertySellingPrice() throws Exception {
		final String LINK = "http://eng.28hse.com/utf8/property39782.html";
		Property property = fetcher.fetchProperty(LINK);
		assertNotNull(property);
		assertTrue(property.isForRent());
		assertTrue(property.getRent()>0);
	}
	@Test
	public void TestFetchPropertyListedBy() throws Exception{
		String LINK = "http://eng.28hse.com/utf8/property39782.html";
		Property property = fetcher.fetchProperty(LINK);
		assertNotNull(property);
		assertTrue(!property.isForRent());
		assertTrue(property.isForSale());
		assertTrue(property.isOwner());
		assertTrue(!property.isAgent());
		//Testing agency
		LINK = "http://eng.28hse.com/utf8/property103109.html";
		property = fetcher.fetchProperty(LINK);
		assertNotNull(property);
		assertTrue(!property.isForRent());
		assertTrue(property.isForSale());
		assertTrue(!property.isOwner());
		assertTrue(property.isAgent());
	}
	@Test
	public void testFetchPropertyType() throws Exception{
		String LINK = "http://eng.28hse.com/utf8/property105184.html";
		Property property = fetcher.fetchProperty(LINK);
		assertEquals(PropertyType.INDUSTRIAL, property.getPropertyType());
		LINK = "http://eng.28hse.com/utf8/property105046.html";
		property = fetcher.fetchProperty(LINK);
		assertEquals(PropertyType.SHOPS, property.getPropertyType());
		LINK = "http://eng.28hse.com/utf8/property101275.html";
		property = fetcher.fetchProperty(LINK);
		assertEquals(PropertyType.SHOPS, property.getPropertyType());
	}
	
	@Test
	public void testFetchPropertyFeet() throws Exception{
		String Link ="http://eng.28hse.com/utf8/property105233.html";
		Property property = fetcher.fetchProperty(Link);
		assertEquals(600, property.getFeets());
		Link ="http://eng.28hse.com/utf8/property103075.html";
		property = fetcher.fetchProperty(Link);
		assertEquals(0, property.getFeets());
		Link ="http://eng.28hse.com/utf8/property69484.html";
		property = fetcher.fetchProperty(Link);
		assertEquals(100, property.getFeets());
	}
}
