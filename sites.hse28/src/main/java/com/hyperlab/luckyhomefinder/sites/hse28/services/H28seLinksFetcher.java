package com.hyperlab.luckyhomefinder.sites.hse28.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hyperlab.luckyhomefinder.common.domain.ConnectionConfigs;
import com.hyperlab.luckyhomefinder.service.Exceptions.LinksFetcherException;
import com.hyperlab.luckyhomefinder.service.parsers.LinksFetcher;
import com.hyperlab.luckyhomefinder.sites.hse28.domain.H28seConstants;

/**
 * Implementations of links fetcher service to handle fetching links from H28Se
 * site.Implementations will be as follows: 1-Fetch last added property ID from
 * site.
 * 2-build possible property links.
 * 
 * @author Kareem ElShahawe
 * */
public class H28seLinksFetcher implements LinksFetcher {
	/**
	 * {@inheritDoc}
	 * */
	public final List<String> fetchLinks(final String lastKnownPropertyId)
			throws LinksFetcherException {
		Connection connection = initConnection();
		Response response = null;
		String lastPropertyIdOnSite = null;
		try {
			response = connection.execute();
			// Getting last property Id on website
			lastPropertyIdOnSite = parseLinks(response);
		} catch (final IOException e) {
			throw new LinksFetcherException(e);
		}

		// Building properties Links
		List<String> possiblePropertiesLinks = buildPropertyLinks(
				lastKnownPropertyId, lastPropertyIdOnSite);
		return possiblePropertiesLinks;
	}

	/**
	 * Initialize connection object used to connect to the website.
	 * 
	 * @return Connection object used to perform connection to the website.
	 * */
	protected final Connection initConnection() {
		Connection connection = Jsoup
				.connect(H28seConstants.MAINSEARCH.value());
		connection.data(H28seConstants.ACTIONKEY.value(),
				H28seConstants.ACTIONVALUE.value());
		connection.data(H28seConstants.ALLDATAKEY.value(),
				H28seConstants.ALLDATAVALUE.value());
		connection.maxBodySize(0);
		connection.timeout(0);
		connection.method(Method.POST);
		connection.referrer(H28seConstants.MAINSEARCH.value());
		connection.userAgent(ConnectionConfigs.USERAGENT.value());
		return connection;

	}

	/**
	 * ParseLinks return last property siteId, this will be used to know the
	 * last site Id that has been added.
	 * 
	 * @param response
	 *            connection {@link Response} after performing search by date.
	 * @return return last property ID that has been added
	 * @throws LinksFetcherException
	 *             in case of error while parsing connection response.
	 * */
	protected final String parseLinks(final Response response)
			throws LinksFetcherException {
		Document document;
		try {
			document = response.parse();
		} catch (final IOException e) {
			throw new LinksFetcherException(
					"error while trying to parse response for H28se.", e);
		}
		Elements elements = document.getElementsByTag("a");
		String regx = "property\\d+";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher;
		Element element = elements.first();
		String linkHtml = element.attr("href");
		matcher = pattern.matcher(linkHtml);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
	}

	/**
	 * Builds possible property links between startId and last added property.
	 * 
	 * @param startId
	 *            last known property Id fetched from remote site.
	 * @param lastId
	 *            last added property id on the remote website.
	 * @return {@link List} of possible property links.
	 * */
	protected final List<String> buildPropertyLinks(final String startId,
			final String lastId) {
		long start = 1 + Long.parseLong(startId);
		long end = getPropertyId(lastId);
		List<String> possiblePropertyLinks = new LinkedList<String>();
		String link = null;
		for (long i = start; i <= end; i++) {
			// Build the property link,baseLink+propertyId+.html
			link = H28seConstants.PROPERTYBASELINK.value() + i + ".html";
			possiblePropertyLinks.add(link);
		}
		return possiblePropertyLinks;
	}

	/**
	 * Return property ID.
	 * 
	 * @param property
	 *            text representation of the property.
	 * @return property ID.
	 * 
	 * */
	protected final long getPropertyId(final String property) {
		final String regx = "\\d+";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(property);
		long id = 0;
		if (matcher.find()) {
			id = Long.parseLong(matcher.group());
		}
		return id;
	}
}
