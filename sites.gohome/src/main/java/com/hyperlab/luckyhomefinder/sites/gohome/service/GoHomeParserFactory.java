package com.hyperlab.luckyhomefinder.sites.gohome.service;

import java.util.concurrent.CountDownLatch;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParser;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParserFactory;

/**
 * Implementation of {@link PropertyParserFactory} interface to create
 * {@link GoHomePropertyFetcher} with scope of prototype.
 * */
public class GoHomeParserFactory implements PropertyParserFactory {
	/**
	 * {@inheritDoc}
	 * */
	public final PropertyParser dispatchParser(final Property property,
			final CountDownLatch countDownLatch, final String link) {
		return new GoHomePropertyFetcher(property, countDownLatch, link);
	}

}
