package com.hyperlab.luckyhomefinder.sites.gohome.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hyperlab.luckyhomefinder.common.domain.ConnectionConfigs;
import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;
import com.hyperlab.luckyhomefinder.service.parsers.LinksFetcher;
import com.hyperlab.luckyhomefinder.sites.gohome.domain.Links;
import com.hyperlab.luckyhomefinder.sites.gohome.domain.ParsersConstants;

/**
 * Provide implementation of {@link LinksFetcher} for GoHome website.
 * 
 * There is a major issue with the Gohome way of sorting it's links, first it
 * doesn't add the date by the which the ads were added to the site. second
 * properties Ids are not unique , they are only unique per territory but not
 * globally, which means that you can have two properties with the same
 * ID,however. since they are both in different territories no conflict will
 * Occur.
 * 
 * @author jason
 * */
public class GoHomeLinksFetcher implements LinksFetcher {
	/** Logger. */
	private static final Logger LOGGER = Logger
			.getLogger(GoHomeLinksFetcher.class);

	/**
	 * {@inheritDoc}
	 * */
	public final List<String> fetchLinks(final Property lastKnownProperty)
			throws LinksFetcherException {
		// Time to sleep before moving on to the next page.
		final int sleepTime = 1000;
		// String holding last known property Link.
		final String lastPropertyLink = lastKnownProperty.getLink();
		// valid Links will holds the valid property links after parsing
		// property links fetched from response page.
		List<String> validLinks = new ArrayList<String>();
		// Property links is links found in search page.
		List<String> propertyLinks = new ArrayList<>();
		// Boolean indicating that we have reached last known property.
		boolean found = false;
		// Connection response.
		Response response = null;
		List<String> links = new ArrayList<String>();
		for (int page = 1; !found; page++) {
			response = connect(page);
			// Fetching property links in response page.
			propertyLinks = getPropertyLinks(response);
			// If fetched links contain last property link then
			// 1- Extract links before last property link.
			// 2- change found to true.
			if (propertyLinks.contains(lastPropertyLink)) {
				// Extracting links.
				links = extractLinks(lastPropertyLink, propertyLinks);
				found = true;
			} else {
				links = propertyLinks;
			}
			addToLinks(validLinks, links);
			// If there are still more links to parse then sleep before
			// connecting to next page.
			if (!found) {
				try {

					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					LOGGER.error(
							"Sleeping thread has been interrupted while parsing pages ",
							e);
					throw new LinksFetcherException(
							"Sleeping thread has been interrupted while parsing pages ",
							e);

				}
			}
		}
		/*
		 * After getting the links, we need to reverse it and this is needed so
		 * that we can parse properties from last added to newly added. this is
		 * a special case to resolve issue with how goHome website save the
		 * data.
		 */
		Collections.reverse(validLinks);
		return validLinks;
	}

	/**
	 * Add the new links to the property links that will be fetched, this method
	 * was implemented to make solve following. -Eliminate duplicate links, and
	 * maintain the links order.
	 * 
	 * @param validLinks
	 *            valid links that will be parsed later by property parser.
	 * @param newLinks
	 *            new links that was fetched from the current page.
	 * */
	protected final void addToLinks(final List<String> validLinks,
			final List<String> newLinks) {
		newLinks.removeAll(validLinks);
		validLinks.addAll(newLinks);
	}

	/**
	 * Connect to the desired search page result.
	 * 
	 * @param page
	 *            page number to connect to [as in paging].
	 * @return connection response.
	 * @throws LinksFetcherException
	 *             In case of connection error while connecting to result pages.
	 * */
	protected final Response connect(final int page)
			throws LinksFetcherException {
		Connection connection = initConnection(page);
		try {
			return connection.execute();
		} catch (final IOException e) {
			LOGGER.error("Error while trying to connect to page " + page, e);
			throw new LinksFetcherException(e);
		}
	}

	/**
	 * Initialize connection object.
	 * 
	 * @param page
	 *            page number to connect to.
	 * @return connection object used to connect to the target website.
	 */
	protected final Connection initConnection(final int page) {
		Connection connection = Jsoup.connect(Links.PagingBase.value() + page);
		connection.maxBodySize(0);
		connection.timeout(0);
		connection.method(Method.GET);
		connection.userAgent(ConnectionConfigs.USERAGENT.value());
		return connection;
	}

	/**
	 * Extract property links from response page.
	 * 
	 * @param response
	 *            response object resulting from connecting to the search page.
	 * @return List of property links in result search page.
	 * @throws LinksFetcherException
	 *             If error occurred while parsing search result page.
	 */
	protected final List<String> getPropertyLinks(final Response response)
			throws LinksFetcherException {
		// List that will hold extracted property links.
		List<String> propertyLinks = new ArrayList<>();
		try {
			// Property Divs found in the search result page.
			final Elements propertysDiv = response.parse().getElementsByClass(
					ParsersConstants.TITLEDIV.value());
			String link = "";
			for (Element propertyDiv : propertysDiv) {
				link = propertyDiv.select(ParsersConstants.CSSQUERY.value())
						.attr(ParsersConstants.HREF.value());
				// This line to add only links related to properties.
				if (link.contains(ParsersConstants.PROPERTYBASELINK.value())) {
					propertyLinks.add(link);
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Error while parsing search result page ", e);
			throw new LinksFetcherException(e);
		}
		return propertyLinks;
	}

	/**
	 * ExtractLinks extract links till it hit last property link.
	 * 
	 * @param lastPropertyLink
	 *            last known property fetched and persisted in the db.
	 * @param propertyLinks
	 *            property links fetched from search result page.
	 * @return all links up to last property link.
	 * */
	protected final List<String> extractLinks(final String lastPropertyLink,
			final List<String> propertyLinks) {
		final int lastPropertyIndex = propertyLinks.indexOf(lastPropertyLink);
		// If the index of the last property link is the first property then
		// return empty list.
		if (lastPropertyIndex == 0) {
			return new ArrayList<String>();
		} else {
			return propertyLinks.subList(0, lastPropertyIndex);
		}
	}
}
