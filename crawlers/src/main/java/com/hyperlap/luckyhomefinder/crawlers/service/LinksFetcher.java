package com.hyperlap.luckyhomefinder.crawlers.service;

import java.util.List;

import com.hyperlap.luckhomefinder.crawlers.Exceptions.LinksFetcherException;
/**
 * LinksFetcher provide functionality needed to retrieve property links in the target website search result page.
 * @author Kareem elshahawe
 * */
public interface LinksFetcher {
	/**
	 * Retrieves all property links based on the website constant class.
	 * @param link search result page link.
	 * @return list of property links.
	 * @throws LinksFetcherException If error occurred while performing.
	 * */
List<String> fetchLinks(String link) throws LinksFetcherException;
}
