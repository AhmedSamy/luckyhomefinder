package com.hyperlap.luckyhomefinder.sites.gohome.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hyperlab.luckyhomefinder.common.domain.ConnectionConfigs;
import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.common.domain.PropertyType;
import com.hyperlab.luckyhomefinder.service.Exceptions.PropertyFetcherException;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParser;
import com.hyperlap.luckyhomefinder.sites.gohome.domain.ParsersConstants;

/**
 * Provide Implementation of {@link PropertyParser} for GoHome website.
 * */
public class GoHomePropertyFetcher extends Thread implements PropertyParser {
	/** Logger. */
	private static final Logger LOGGER = Logger
			.getLogger(GoHomePropertyFetcher.class);
	/**
	 * CountDown latch for property parsers.
	 * */
	private CountDownLatch counter;

	/** Property that will be fetched. */
	private Property finalProperty;

	/** Property page link. */
	private String link;

	public GoHomePropertyFetcher() {
	}

	public GoHomePropertyFetcher(final Property property,
			final CountDownLatch instanceCounter, final String link) {
		this.finalProperty = property;
		this.link = link;
		this.counter = instanceCounter;
		start();
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public final void run() {
		try {
			// Sleep random amount before start fetching
			hibernate();
			fetchProperty(this.link);
		} catch (PropertyFetcherException e) {
			this.finalProperty = null;
		} finally {
			counter.countDown();
		}
	}

	/**
	 * {@inheritDoc}
	 * */
	@Override
	public Property fetchProperty(final String link)
			throws PropertyFetcherException {
		LOGGER.info("Parsing " + link);
		// Connecting to target page.
		Response response = connect(link);
		initProperty(response);
		return this.finalProperty;
	}

	/**
	 * The main purpose of this method is to sleep random amount of secs in
	 * order not to hammer the target website.
	 * 
	 * @throws PropertyFetcherException
	 *             in case of thread interrupted.
	 * 
	 * */
	protected final void hibernate() throws PropertyFetcherException {
		final int minimumPeriod = ThreadLocalRandom.current().nextInt(1000,
				3000) + 3000;
		final int randomPeriod = ThreadLocalRandom.current().nextInt(2000);
		try {
			sleep(minimumPeriod + randomPeriod);
		} catch (InterruptedException e) {
			throw new PropertyFetcherException(e);
		}
	}

	/**
	 * Connect to target property page.
	 * 
	 * @param propertyLink
	 *            link to property page.
	 * @return response resulting from connecting to property page.
	 * @throws PropertyFetcherException
	 *             in case of error occurred while connecting to peroperty page.
	 * */
	protected final Response connect(final String propertyLink)
			throws PropertyFetcherException {
		Connection connection = initConnect(propertyLink);
		try {
			Response response = connection.execute();
			return response;
		} catch (IOException e) {
			LOGGER.error("Error while connecting to " + propertyLink, e);
			throw new PropertyFetcherException(e);
		}

	}

	/**
	 * Initialize connection configuration to connect to property page.
	 * 
	 * @param propertyLink
	 *            link to property page.
	 * @return {@link Connection} object that will be used in executing
	 *         connection to property page.
	 * */
	protected final Connection initConnect(final String propertyLink) {
		Connection connection = Jsoup.connect(propertyLink);
		connection.timeout(0);
		connection.maxBodySize(0);
		connection.userAgent(ConnectionConfigs.USERAGENT.value());
		return connection;
	}

	/**
	 * Initialize property and filling it with property information.
	 * 
	 * @param response
	 *            response page resulting from connection.
	 * @return initialized property
	 * 
	 * @throws PropertyFetcherException
	 *             in case of error while parsing property information.
	 * */
	protected final void initProperty(final Response response)
			throws PropertyFetcherException {
		Document document;
		try {
			document = response.parse();
		} catch (IOException e) {
			LOGGER.error("Error while parsing document for " + link, e);
			throw new PropertyFetcherException(e);
		}

		// First we check if ad is not expired.
		if (isExpired(document)) {
			this.finalProperty = null;
			return;
		}

		// Start initializing property.
		// UUID
		initID(this.finalProperty);
		// SiteID
		this.finalProperty.setSiteId(getPrpertySiteId(this.link));
		// District
		this.finalProperty.setDistrict(getDistrict(document));
		// price, rent, and feet.
		initPriceRentFeet(this.finalProperty, document);
		// Post date
		this.finalProperty.setPostDate(getPostDate(document));
		// property type
		this.finalProperty.setPropertyType(getPropertyType(document));
		// listed by
		listedBy(this.finalProperty);
		//Link
		this.finalProperty.setLink(this.link);
	}

	/**
	 * Extract location of the property from connection response.
	 * 
	 * @param document
	 *            contain information of the property page.
	 * @return property district information.
	 * */
	protected final String getDistrict(final Document document) {
		// Separator used by gohome to separate between district location data.
		final String seperator = "-";
		final int first = 0;
		Elements districtElement = document
				.getElementsByClass(ParsersConstants.PRODETAIL.value()).first()
				.getElementsByClass(ParsersConstants.TITLEDIV.value());
		String districtData = districtElement.text();
		// This is to solve the Mid-Level problem
		if (districtData.toLowerCase().contains("mid-levels")) {
			return "Mid Levels";
		} else {
			districtData = districtData.split(seperator)[first];
			// This is to solve the issue resulting from adding / to the
			// district names, EX: Central/Mid level.
			if (districtData.contains("/")) {
				return districtData.split("/")[first];
			} else {
				return districtData;
			}
		}

	}

	/**
	 * Extract the property ID from the property Link.
	 * 
	 * @param link
	 *            link to the property page.
	 * @return Id of the property on the site.
	 */
	protected final String getPrpertySiteId(final String link) {
		final String regex = "ad-\\d+";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(link);
		if (matcher.find()) {
			return matcher.group().replace("ad-", "");
		} else {
			return "";
		}
	}

	/**
	 * Initialize the {@link Property} object with all information related to
	 * price ,rent,and property size in feet.
	 * 
	 * @param property
	 *            property object to initialize.
	 * @param document
	 *            response object to extract data from.
	 * */
	protected final void initPriceRentFeet(final Property property,
			final Document document) {
		final String notApplicable = "-";
		final int price = 0;
		final int rent = 1;
		final int sa = 2; // Sellable area in feet
		final int gfa = 3; // Gross feet area
		final String[] data = getPriceRentFeetStr(document);

		// Checking price data
		long priceValue = getPrice(data[price]);
		boolean canIBuyProperty = priceValue > 0 ? true : false;
		property.setSellingStatus(canIBuyProperty);
		property.setSell(priceValue);

		// Checking rent data.
		long rentValue = getPrice(data[rent]);
		boolean canIRentProperty = rentValue > 0 ? true : false;
		property.setRentStatus(canIRentProperty);
		property.setRent(rentValue);

		if (!data[sa].contains(notApplicable)) {
			property.setFeets(Integer.parseInt(data[sa]));
		} else if (!data[gfa].contains(notApplicable)) {
			property.setFeets(Integer.parseInt(data[gfa]));
		} else {
			property.setFeets(0);
		}
	}

	/**
	 * Extract rent and price cost, and feet data from {@link Document}.
	 * 
	 * @param document
	 *            response object to extract rent and selling price from.
	 * @return return array of string holding rent,price details, and size in
	 *         feet of the property [Rent, Price,feet].
	 */
	protected final String[] getPriceRentFeetStr(final Document document) {
		String[] priceRentFeetData;
		Element elements;

		elements = document.getElementsByClass("TxtDiv").first()
				.getElementsByClass("cols2").first();

		/*
		 * In these lines we are removing all html code and other non relative
		 * characters, reason is that if we extracted only the text from
		 * elements they are not structured in a good way which will prevent us
		 * from extracting the values. Also after removing the new lines and
		 * replacing them with | delimiter I needed to replace it again with ","
		 * since split consider | as a regex not a character which will result
		 * in splitting array as characters.
		 */
		String data = elements.html().replace("\n", "|").replace("<br>", "")
				.replace("<br />", "").replace("( SA )", "")
				.replace("ft&sup2;", "").replace("( GFA )", "")
				.replace(" ", "").replace(",", "").replace("$", "")
				.replace("N/A", "-").replace("|", ",");
		priceRentFeetData = data.split(",");
		return priceRentFeetData;
	}

	/**
	 * Extract property price from price String, price can be a rent or selling
	 * price.
	 * 
	 * @param price
	 *            price string contained in the property page.
	 * @return property price.
	 * */
	protected final long getPrice(final String price) {
		final String millionStr = "m";
		final int million = 1000000;
		long priceLong = 0L;
		// If price contain - means that price is not for sale.
		if (price.contains("-")) {
			return priceLong;
		}
		// Extracting the String value of the price
		String priceValue = price.toLowerCase().replace(millionStr, "");
		// If price in Million
		if (price.toLowerCase().contains(millionStr)) {
			priceValue = String.valueOf((Math.round(Float
					.parseFloat(priceValue) * million)));
			// Extracting value in digits.
			return (Long.parseLong(priceValue));
		}
		// If price is not in million
		return Long.parseLong(priceValue);
	}

	/**
	 * Extract post date of the property ad.
	 * 
	 * @param document
	 *            document containing the property data.
	 * @return {@link Date} object containing date of the ad post.
	 * */
	protected final Date getPostDate(final Document document) {
		final String dateFormatStr = "dd MMM yyyy";
		Elements possibleElements = document
				.getElementsByClass(ParsersConstants.TXTDiV.value()).first()
				.getElementsByClass(ParsersConstants.ROW2.value()).first()
				.getElementsByClass(ParsersConstants.COLS3.value());
		DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		Date postDate = null;
		for (Element element : possibleElements) {
			try {
				postDate = dateFormat.parse(element.text());
				break;
			} catch (ParseException e) {
				continue;
				// Do nothing.
			}
		}
		return postDate;
	}

	/**
	 * Extract property type.
	 * 
	 * @param document
	 *            document containing the property data.
	 * @return property type.
	 * */
	protected final PropertyType getPropertyType(final Document document) {
		final Elements possibleElements = document
				.getElementsByClass(ParsersConstants.TABLEDATA.value()).first()
				.getElementsByClass(ParsersConstants.COLS2.value());
		final String typeStr = possibleElements.first().text().toLowerCase();

		if (typeStr.contains(PropertyType.HOUSE.value())
				|| typeStr.contains("villa")) {
			return PropertyType.HOUSE;
		} else if (typeStr.contains(PropertyType.VILLAGEHOUSE.value())) {
			return PropertyType.VILLAGEHOUSE;
		} else if (typeStr.contains("stand-alone building")
				|| typeStr.contains("public / hos flat")) {
			return PropertyType.STUDIO;
		} else if (typeStr.contains("tenement")) {
			return PropertyType.SHARE;
		} else {
			return PropertyType.APARTMENT;
		}

	}

	/**
	 * Initialize {@link Property} object to define wither property was listed
	 * by agent or owner. Note that all properties in gohom is listed by agents.
	 * 
	 * @param property
	 *            property to initialize.
	 * */
	protected final void listedBy(final Property property) {
		property.setOwner(false);
		property.setAgent(true);
	}

	/**
	 * Initialize property with {@link UUID}.
	 * 
	 * @param property
	 *            property to initialize it's ID.
	 * */
	protected final void initID(final Property property) {
		property.setId(UUID.randomUUID());
	}

	/**
	 * Check if the ad page is expired or not.
	 * 
	 * @param document
	 *            property page.
	 * @return boolean if not expired false, if expired true.
	 */
	protected final boolean isExpired(final Document document) {
		final Elements elements = document.getElementsByClass("title");
		final String expiredMessage = "This listing has already expired";
		return elements.text().contains(expiredMessage);
	}
}
