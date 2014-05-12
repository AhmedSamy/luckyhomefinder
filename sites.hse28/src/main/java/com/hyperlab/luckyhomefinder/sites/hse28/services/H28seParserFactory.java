package com.hyperlab.luckyhomefinder.sites.hse28.services;

import java.util.concurrent.CountDownLatch;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParser;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParserFactory;

/**
 * Implementation of {@link PropertyParserFactory} interface to create H28SE
 * property parsers with scope of prototype.
 * 
 * @author Kareem El-Shahawe
 * */
public class H28seParserFactory implements PropertyParserFactory {

	/**
	 * {@inheritDoc}
	 * */
	public final PropertyParser dispatchParser(final Property property,
			final CountDownLatch countDownLatch, final String link) {
		return new H28sePropertyFetcher(property, countDownLatch, link);
	}

}
