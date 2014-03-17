package com.hyperlab.luckyhomefinder.service.parsers;

import java.util.concurrent.CountDownLatch;

import com.hyperlab.luckyhomefinder.common.domain.Property;

/**
 * PropertyParserFactory is to help dispatching the correct parser for the
 * Target website through applying stratgy pattern.
 * 
 * @author Kareem El-Shahawe
 * */
public interface PropertyParserFactory {

	/**
	 * Create a new thread of property parser.
	 * 
	 * @param property
	 *            property that will hold property information.
	 * @param countDownLatch
	 *            {@link CountDownLatch} that will manage the parser threads.
	 * @param link
	 *            property link
	 * @return {@link PropertyParser} created.
	 * */
	PropertyParser dispatchParser(Property property,
			CountDownLatch countDownLatch, String link);
}
