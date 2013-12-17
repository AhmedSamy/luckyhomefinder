package com.hyperlap.luckyhomefinder.crawlers.service;

import java.util.List;
/**
 * LinksFetcher provide functionality needed to retrieve property links in the target website search result page.
 * @author Kareem elshahawe
 * */
public interface LinksFetcher {
	/**
	 * Retrieves all property links based on the website constant class.
	 * @param link search result page link.
	 * @return list of property links.
	 * */
List<String> fetchLinks(String link);
}
