package com.hyperlap.luckyhomefinder.sites.gohome.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.junit.Test;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TestGoHomeLinksFetcher {

	@Test
	public void testConnect() throws LinksFetcherException {
		GoHomeLinksFetcher linksFetcher = new GoHomeLinksFetcher();
		Response response = linksFetcher.connect(1);
		assertNotNull(response);
		assertNotNull(response.body());
	}

	@Test
	public void testGetPropertyLinks() throws LinksFetcherException {
		GoHomeLinksFetcher linksFetcher = new GoHomeLinksFetcher();
		Response response = linksFetcher.connect(1);
		List<String> links = linksFetcher.getPropertyLinks(response);
		assertNotNull(links);
		assertTrue(!links.isEmpty());
	}

	@Test
	public void testExtractLinksMiddle() {
		GoHomeLinksFetcher linksFetcher = new GoHomeLinksFetcher();
		// Initializing Mock Links
		List<String> mockLinks = new ArrayList<>();
		mockLinks
				.add("http://property.gohome.com.hk/Wanchai/J-Residence/ad-1152974/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Wanchai/star-street-Standalone-Building/ad-1181878/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Hung-Hom/Harbour-Place/ad-1169714/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Whampao/Whampoa-Estate/ad-1135203/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Mid-Levels-Central/Royal-Court-1671/ad-738219/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Tai-Hang/WarrenWoods/ad-1181875/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Wanchai/ONE-WANCHAI/ad-1151158/en/");
		// End of Mock links
		final String lastPropertyLink = "http://property.gohome.com.hk/Mid-Levels-Central/Royal-Court-1671/ad-738219/en/";
		List<String> links = linksFetcher.extractLinks(lastPropertyLink,
				mockLinks);

		assertNotNull(links);
		assertTrue(!links.isEmpty());
		assertEquals(4, links.size());

		for (String link : links)
			System.out.println(link);
	}

	@Test
	public void testExtractLinksFirst() {
		GoHomeLinksFetcher linksFetcher = new GoHomeLinksFetcher();
		// Initializing Mock Links
		List<String> mockLinks = new ArrayList<>();
		mockLinks
				.add("http://property.gohome.com.hk/Wanchai/J-Residence/ad-1152974/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Wanchai/star-street-Standalone-Building/ad-1181878/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Hung-Hom/Harbour-Place/ad-1169714/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Whampao/Whampoa-Estate/ad-1135203/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Mid-Levels-Central/Royal-Court-1671/ad-738219/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Tai-Hang/WarrenWoods/ad-1181875/en/");
		mockLinks
				.add("http://property.gohome.com.hk/Wanchai/ONE-WANCHAI/ad-1151158/en/");
		// End of Mock links
		final String lastPropertyLink = "http://property.gohome.com.hk/Wanchai/J-Residence/ad-1152974/en/";
		List<String> links = linksFetcher.extractLinks(lastPropertyLink,
				mockLinks);

		assertNotNull(links);
		assertTrue(links.isEmpty());
		assertEquals(0, links.size());

		for (String link : links)
			System.out.println(link);
	}

	@Test
	public void testLinksFetcher() throws LinksFetcherException {
		final Property mockProperty = new Property();
		mockProperty
				.setLink("http://property.gohome.com.hk/Olympic-Station/Park-Avenue/ad-1163710/en/");
		GoHomeLinksFetcher linksFetcher = new GoHomeLinksFetcher();
		List<String> validLinks = linksFetcher.fetchLinks(mockProperty);
		assertNotNull(validLinks);
		assertTrue(!validLinks.isEmpty());
		assertTrue(validLinks.size() >= 40);
		int i = 0;
		for (String link : validLinks) {
			System.out.println(link);
			i++;
			if (i % 20 == 0) {
				System.out.println("======================");
			}
		}
	}
}
