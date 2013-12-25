package com.hyperlap.luckyhomefinder.crawlers.service;

import com.hyperlap.luckhomefinder.crawlers.Exceptions.PropertyFetcherException;
import com.hyperlap.luckhomefinder.crawlers.domain.Property;

/**
 * propertyParser provide functionality needed to parse property information
 * from the property page.
 * 
 * */
public interface PropertyParser {

	/**
	 * Parse property information from the property Link
	 * 
	 * @param property
	 *            Link
	 * @return property object containing property data.
	 * @throws PropertyFetcherException
	 *             If Error occurred while performing.
	 * */
	public Property fetchProperty(String link) throws PropertyFetcherException;
}
