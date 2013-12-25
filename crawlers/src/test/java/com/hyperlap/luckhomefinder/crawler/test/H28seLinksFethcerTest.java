package com.hyperlap.luckhomefinder.crawler.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hyperlap.luckhomefinder.crawlers.Exceptions.LinksFetcherException;
import com.hyperlap.luckyhomefinder.crawler.webconstants.H28seConstants;
import come.hyperlap.luckyhomefinder.crawlers.h28se.H28seLinksFetcher;

public class H28seLinksFethcerTest {

	H28seLinksFetcher h28LinksFethcer;
	
	@Before
	public void setup(){
		h28LinksFethcer = new H28seLinksFetcher();
	}
	
	@After
	public void tearDown(){
		h28LinksFethcer = null;
	}
	
	@Test
	public void testFetchLinks() throws LinksFetcherException{
	String targetLink=	H28seConstants.MAINSEARCH.value();
	h28LinksFethcer.fetchLinks(targetLink);	
	}
}
