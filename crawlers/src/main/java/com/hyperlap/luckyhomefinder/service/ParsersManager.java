package com.hyperlap.luckyhomefinder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.repository.PropertyRepository;
import com.hyperlab.luckyhomefinder.service.Exceptions.ManagerException;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParserFactory;

/**
 * Manages property fetcher threads and save the return data to the DB.
 * 
 * */
public class ParsersManager {

	/**
	 * Logger.
	 * */
	private static final Logger LOG = Logger.getLogger(ParsersManager.class);
	/** number of property fetchers to be dispatched. */
	private static final int INCREMENT = 4;

	/** Provide access to properties records in the data base. */
	@Autowired
	private PropertyRepository propertyRepository;

	/** Property parser factory used in dispatching property parser threads. */
	private PropertyParserFactory parserFactory;

	/** Private empty constructor to prevent empty initialization of manager. */
	@SuppressWarnings("unused")
	private ParsersManager() {
	}

	/**
	 * Constructor.
	 * 
	 * @param parserFacotry
	 *            parser factory that will be used to dispatch property parsers.
	 * */
	public ParsersManager(final PropertyParserFactory parserFacotry) {
		this.parserFactory = parserFacotry;
	}

	/**
	 * Process the property links by dispatching property fetchers, and then
	 * save the return into the database.
	 * 
	 * @param links
	 *            links to {@link Property}s to be fetched.
	 */
	public final void processLinks(final List<String> links) {
		List<String> subLinks = null;
		List<Property> properties = null;
		for (int i = 0; i < links.size(); i += INCREMENT) {
			subLinks = links.subList(i, i + INCREMENT);
			try {
				properties = dispatchPropertyFetchers(subLinks);
				persistProperties(properties);
			} catch (ManagerException e) {
				continue;
			}

		}
	}

	/**
	 * Dispatch property fetchers, fixed number will be 4 property fetchers.
	 * 
	 * @param links
	 *            properties links.
	 * @return {@link List} of {@link Property}s .
	 * @throws ManagerException
	 *             If {@link CountDownLatch} got interrupted while waiting for
	 *             fetchers to finish.
	 * */
	protected final List<Property> dispatchPropertyFetchers(
			final List<String> links) throws ManagerException {
		CountDownLatch countDownLatch = new CountDownLatch(links.size());
		List<Property> properties = new ArrayList<>();
		Property currentProperty = null;
		for (String link : links) {
			currentProperty = new Property();
			properties.add(currentProperty);
			parserFactory.dispatchParser(currentProperty, countDownLatch, link);
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			LOG.error(e);
			throw new ManagerException(e);
		}
		return properties;
	}

	/**
	 * Persist list of properties into the database.
	 * 
	 * @param properties
	 *            list of properties to be persisted to the DB.
	 * */
	protected final void persistProperties(final List<Property> properties) {
		List<Property> validData = new ArrayList<Property>();
		if (properties != null && !properties.isEmpty()) {
			for (Property property : properties) {
				if (property != null && property.getId() != null) {
					validData.add(property);
				}
			}
			
			propertyRepository.save(validData);
		}
	}

}
