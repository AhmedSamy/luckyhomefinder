package com.hyperlab.luckyhomefinder.sites.hse28.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;
import com.hyperlab.luckyhomefinder.sites.hse28.domain.H28seConstants;
import com.hyperlab.luckyhomefinder.sites.hse28.services.H28seLinksFetcher;

public class H28seLinksFethcerTest {

	H28seLinksFetcher h28LinksFethcer;

	@Before
	public void setup() {
		h28LinksFethcer = new H28seLinksFetcher();
	}

	@After
	public void tearDown() {
		h28LinksFethcer = null;
	}

	@Test
	public void testFetchLinks() throws LinksFetcherException {
		String lastKnownId = "111792";
		List<String> links = h28LinksFethcer.fetchLinks(lastKnownId);
		for (String link : links) {
			System.out.println(link);
		}
	}

	@Test
	public void testFetchLinksErrorPage() throws Exception {
		h28LinksFethcer
				.fetchLinks("http://eng.28hse.com/utf8/property102382.html");
	}
}
