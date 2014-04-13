package com.hyperlap.luckyhomefinder.sites.gohome.service;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javassist.tools.framedump;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.format.datetime.DateFormatter;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.common.domain.PropertyType;
import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;
import com.hyperlab.luckyhomefinder.service.Exceptions.PropertyFetcherException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TestGoHomeProeprtyFetcher {

	@Test
	public void testConnect() throws PropertyFetcherException {
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		Response response = propertyFetcher
				.connect("http://property.gohome.com.hk/Mid-Levels-North-Point-Braemar-Hill/Wilshire-Towers/ad-1181794/en/");
		assertNotNull(response);
		System.out.println(response.body());
	}

	@Test
	public void testGetDistrict() throws Exception {
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		Response response = propertyFetcher
				.connect("http://property.gohome.com.hk/Mid-Levels-North-Point-Braemar-Hill/Wilshire-Towers/ad-1181794/en/");
		String districString = propertyFetcher.getDistrict(response.parse());
		assertNotNull(districString);
		assertTrue(!districString.isEmpty());
		assertTrue(districString.contains("North Point"));
		System.out.println(districString);
	}

	@Test
	public void StressTestGetDistrict() throws Exception {
		Property mockProperty = new Property();
		mockProperty
				.setLink("http://property.gohome.com.hk/Mid-Levels-West/Robinson-Place/ad-741554/en/");
		GoHomeLinksFetcher linksFethcer = new GoHomeLinksFetcher();
		List<String> links = linksFethcer.fetchLinks(mockProperty);

		// Testing getProperty
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		Response response = null;
		String district = "";
		for (String link : links) {
			response = propertyFetcher.connect(link);
			district = propertyFetcher.getDistrict(response.parse());
			if (district.contains("/"))
				System.out.println(district + " =>> " + link);
			assertNotNull(district);
			assertTrue(!district.isEmpty());
			Thread.sleep(1500);
		}
	}

	@Test
	public void testGetPropertyId() {
		final String link = "http://property.gohome.com.hk/Fortress-Hill/Southern-Building-8872/ad-1183869/en/";
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		String propertyId = propertyFetcher.getPrpertySiteId(link);
		assertNotNull(propertyId);
		System.out.print(propertyId);
		assertTrue(!propertyId.isEmpty());
		assertEquals("1183869", propertyId);
	}

	@Test
	public void testGetPriceRent() throws Exception {
		final String link = "http://property.gohome.com.hk/Tsuen-Wan/Allway-Gardens/ad-1150804/en/";
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		Response response = propertyFetcher.connect(link);
		final String[] details = propertyFetcher.getPriceRentFeetStr(response
				.parse());
		System.out.println(Arrays.toString(details));
	}

	@Test
	public void testGetPrice() throws Exception {
		final String price_1 = "99.8M";
		final String price_2 = "100034412";
		final String price_3 = "150.89M";
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		assertEquals(99800000, propertyFetcher.getPrice(price_1));
		assertEquals(100034412, propertyFetcher.getPrice(price_2));
		assertEquals(150890000, propertyFetcher.getPrice(price_3));
	}

	@Test
	public void testInitPriceRentFeet() throws Exception {
		GoHomeLinksFetcher linkFetcher = new GoHomeLinksFetcher();
		Property property = new Property();
		property.setLink("http://property.gohome.com.hk/Ma-On-Shan/Oceanaire/ad-1184052/en/");
		List<String> links = linkFetcher.fetchLinks(property);

		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();

		for (String link : links) {
			Response response = propertyFetcher.connect(link);
			propertyFetcher.initPriceRentFeet(property, response.parse());
			assertTrue((property.getRent() > 0 || property.getSell() > 0 ? true
					: false));
			assertTrue((property.isForSale() || property.isForRent() ? true
					: false));
			assertTrue(property.getFeets() > 0);
			// Print property information
			System.out.println(link);
			System.out
					.println(property.isForRent() + "  " + property.getRent());
			System.out
					.println(property.isForSale() + "  " + property.getSell());
			System.out.println(property.getFeets());
			System.out
					.println("===========================================================");
			Thread.sleep(1500);
		}

	}

	@Test
	public void testGetPostDate() throws Exception {
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		final Response response = propertyFetcher
				.connect("http://property.gohome.com.hk/Tung-Chung/Coastal-Skyline/ad-1186769/en/");
		Date postDate = propertyFetcher.getPostDate(response.parse());
		assertNotNull(postDate);
		assertEquals("Wed Apr 09 00:00:00 HKT 2014", postDate.toString());
	}

	@Test
	public void testGetPropertyType() throws Exception {
		// Getting links
		GoHomeLinksFetcher linkFetcher = new GoHomeLinksFetcher();
		Property property = new Property();
		property.setLink("http://property.gohome.com.hk/Causeway-Bay/Well-Found-Building/ad-1178185/en/");
		List<String> links = linkFetcher.fetchLinks(property);
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		for (String link : links) {
			final Response response = propertyFetcher.connect(link);
			PropertyType type = propertyFetcher.getPropertyType(response
					.parse());
			if (type == null) {
				System.out.println("ERROR!!! =>>>> " + link);
				break;
			}
			System.out.println(type.toString() + "  =>>> " + link);
			Thread.sleep(3000);
		}
	}

	@Test
	public void testCheckLink() throws Exception {
		final String falseLink = "http://property.gohome.com.hk/Tai-Po/Lam-Tsuen/ad-11333267924/en/";
		GoHomePropertyFetcher propertyFetcher = new GoHomePropertyFetcher();
		Response response = propertyFetcher.connect(falseLink);
		Document document = response.parse();
		Elements elements = document.getElementsByClass("title");
		System.out.println(elements.text());
	}

	@Test
	public void testPropertyFetcher() throws Exception {

		GoHomeLinksFetcher linksFetcher = new GoHomeLinksFetcher();
		Property lastProperty = new Property();
		lastProperty
				.setLink("http://property.gohome.com.hk/Sai-Ying-Pun/Imperial-Terrace/ad-1189133/en/");
		List<String> links = linksFetcher.fetchLinks(lastProperty);

		Property property;
		List<Property> homes = new ArrayList<Property>();
		List<String> currentLinks;

		for (int i = 0; i < links.size(); i++) {
			CountDownLatch counter = new CountDownLatch(4);
			currentLinks = links.subList(i, i += 4);
			for (String link : currentLinks) {
				property = new Property();
				homes.add(property);
				new GoHomePropertyFetcher(property, counter, link);
			}
			counter.await();

			// Print information.
			for (Property home : homes) {
				System.out.println(home.getLink() + "\n" + home.getDistrict()
						+ "\n" + home.getFeets() + "\n" + home.getRent() + "\n"
						+ home.getSell() + "\n" + home.getPostDate() + "\n"
						+ home.getPropertyType() + "\n");
				System.out
						.println("======================================================");
			}

			homes = new ArrayList<Property>();
		}

	}

	@Test
	public void testPropertyFetcherSingle() throws Exception {
		final String link = "http://property.gohome.com.hk/Mid-Levels-North-Point-Braemar-Hill/42-60-Tin-Hau-Temple-Road/ad-1173596/en/?source=7";
		CountDownLatch counter = new CountDownLatch(1);
		Property property = new Property();

		new GoHomePropertyFetcher(property, counter, link);
		counter.await();
		System.out.println(property.getDistrict() + "\n" + property.getLink()
				+ "\n" + property.getFeets() + "\n"
				+ property.getPropertyType() + "\n" + property.getRent() + "\n"
				+ property.getSell() + "\n");

	}
}
