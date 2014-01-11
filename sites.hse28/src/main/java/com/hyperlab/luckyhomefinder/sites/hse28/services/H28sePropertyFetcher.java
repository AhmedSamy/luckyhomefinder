package com.hyperlab.luckyhomefinder.sites.hse28.services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hyperlab.luckyhomefinder.common.domain.ConnectionConfigs;
import com.hyperlab.luckyhomefinder.common.domain.Property;
import com.hyperlab.luckyhomefinder.common.domain.PropertyType;
import com.hyperlab.luckyhomefinder.service.Exceptions.PropertyFetcherException;
import com.hyperlab.luckyhomefinder.service.parsers.PropertyParser;

public class H28sePropertyFetcher implements PropertyParser {
	/** Object that will hold property information. */
	private Property property;
	/** Th tag string */
	final String TH = "th";
	/** Td tag string */
	final String TD = "td";
	/** rent keyword */
	final String RENT = "rent";
	/** Tr tag string */
	final String TR = "tr";
	/** Checker class name */
	final String CHECKER = "checker";

	/**
	 * {@inheritDoc}
	 * */
	public Property fetchProperty(String link) throws PropertyFetcherException {
		property = new Property();
		Connection connection = initConnection(link);
		Response response = null;
		try {
			response = connection.execute();
			Document document = response.parse();
			// Tr elemens contain the keywords and information
			Elements mainContainerElements = document
					.getElementsByClass(CHECKER);
			Elements trElements = mainContainerElements.first()
					.getElementsByTag(TR);
			// Clean elements of properties that we don't need
			Elements cleanedElements = cleanElements(trElements);
			// Start setting property information
			setRent(cleanedElements);
			setPrice(cleanedElements);
			setSiteId(link);
			setListedBy(cleanedElements);
			setPropertyType(cleanedElements);
			setFeets(cleanedElements);
			setPostDate(cleanedElements);
			setDistrict(document);
		} catch (IOException e) {
			throw new PropertyFetcherException(e);
		}
		property.setLink(link);
		return property;
	}

	/**
	 * findPropertyValue finds the value of the desired keyword, and if not
	 * found returns null.
	 * 
	 * @param elements
	 *            Html element tags exist in the property html page.
	 * @param keyword
	 *            Keyword we need to search far.
	 * @return value of the keyword, or null if keyword wasn't found.
	 * */
	protected final String findPropertyValue(final Elements elements,
			final String keyword) {
		String value = null;
		boolean keywordFound = false;
		for (Element element : elements) {
			// If text contains keyword string
			keywordFound = element.getElementsByTag(TH).first().text()
					.toLowerCase().contains(keyword);
			if (keywordFound) {
				value = element.getElementsByTag(TD).first().text();
				elements.remove(element);
				return value;
			}
		}
		return null;
	}

	/**
	 * Initialize the connection and configure it with the required settings.
	 * 
	 * @param link
	 *            target link to connect to.
	 * @return configured connection object.
	 * */
	protected final Connection initConnection(final String link) {
		Connection connection = Jsoup.connect(link);
		connection.timeout(0);
		connection.maxBodySize(0);
		connection.userAgent(ConnectionConfigs.USERAGENT.value());
		return connection;
	}

	/**
	 * Retrieve rent value from HTML page and assign it to the property, and
	 * assign the rent status.
	 * 
	 * @param elements
	 *            html tag elements exist in the property page.
	 * */
	protected final void setRent(final Elements elements) {
		property.setRentStatus(false);
		property.setRent(0L);
		String rentValue = findPropertyValue(elements, "rent");
		if (rentValue != null) {
			property.setRentStatus(true);
			// Removes any none digit, for example $ or ,.
			rentValue = rentValue.replaceAll("[^0-9]", "");
			property.setRent(Long.parseLong(rentValue));
		}
	}

	/**
	 * Retrieve rent value from HTML page and assign it to the property.
	 * 
	 * @param elements
	 *            html tag elements exist in the property page.
	 * */
	protected final void setPrice(final Elements elements) {
		// Init variable used to extract property price.
		property.setSellingStatus(false);
		property.setSellintPrice(0L);
		String regex = "\\d+(.?)\\d*(\\w?)";
		final int MILLION = 1000000;
		final String M = "m";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher;
		// Getting text containing property price
		String priceValue = findPropertyValue(elements, "price");
		if (priceValue != null) {
			property.setSellingStatus(true);
			// extract the selling price
			matcher = pattern.matcher(priceValue);
			if (matcher.find()) {
				priceValue = matcher.group();
				// If the price string contains M we need to convert that to
				// number.
				// I do that by removing the M first then parsing the string as
				// float and multiply it by 1000000.
				if (priceValue.toLowerCase().contains(M)) {
					priceValue = priceValue.toLowerCase().replace(M, "");
					priceValue = String.valueOf((Math.round(Float
							.parseFloat(priceValue) * MILLION)));
				}
				// If property price don't contain M means price in digits.
				property.setSellintPrice(Long.parseLong(priceValue));
			}
		}
	}

	/**
	 * Assign the corresponding property Id on the website.
	 * 
	 * @param link
	 *            web site link of the property.
	 * */
	protected final void setSiteId(final String link) {
		// Regex to extract the property Id from property link.
		final String regex = "\\d+.html";
		final String HTML = ".html";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(link);
		if (matcher.find())
			property.setSiteId(matcher.group().replace(HTML, ""));
	}

	/**
	 * Set the property district.
	 * 
	 * @param elements
	 *            Tr elements exist in the property page.
	 * */
	protected final void setDistrict(Document document) {
		final String LOCATIONCLASS = "de_box2";
		Element element = document.getElementsByClass(LOCATIONCLASS).first();
		String district = element.getElementsByTag("a").first().text();
		property.setDistrict(district);
	}

	/**
	 * Set wither ad was listed by the owner or agency.
	 * 
	 * @param elements
	 *            Tr elements exist in the property page.
	 * */
	protected final void setListedBy(Elements elements) {
		String listedBy = findPropertyValue(elements, "listing by");
		if (listedBy.toLowerCase().contains("owner")) {
			property.setOwner(true);
			property.setAgent(false);
		} else {
			property.setAgent(true);
			property.setOwner(false);
		}
	}

	/**
	 * Set the post date of the property ad.
	 * 
	 * @param elements
	 *            Tr elements exist in the property page.
	 * */
	protected final void setPostDate(Elements elements)
			throws PropertyFetcherException {

		String dateStr = findPropertyValue(elements, "start date");
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd aa.hh:mm");
		try {
			Date postDate = dateFormat.parse(dateStr);
			property.setPostDate(postDate);
		} catch (ParseException e) {
			throw new PropertyFetcherException(e);
		}

	}

	/**
	 * Set the property size in feets
	 * 
	 * @param elements
	 *            Tr elements exist in the property page.
	 * */
	protected final void setFeets(final Elements elements) {
		String feet = findPropertyValue(elements, "area");
		// If feet equals null, no feets are specified
		if (feet == null)
			property.setFeets(0);
		// Some cases feets might contain none digit, this to extract the
		// numbers
		else {
			if (feet.matches("\\D+")) {
				String regx = "\\d+";
				Pattern pattern = Pattern.compile(regx);
				Matcher matcher = pattern.matcher(feet);
				if (matcher.find()) {
					property.setFeets(Integer.parseInt(matcher.group().trim()));
				} else {
					property.setFeets(0);
				}

			} else {
				property.setFeets(Integer.parseInt(feet.trim()));
			}
		}
	}

	/**
	 * Set the property type, wither it is an apartment {studio, share rental},
	 * shop, or office.
	 * 
	 * @param elements
	 *            Tr elements exist in the property page.
	 * */
	protected final void setPropertyType(Elements elements) {
		String type = findPropertyValue(elements, "type").toLowerCase();
		// If propert is apartment then check if it is Sudio or share rental
		if (type.contains(PropertyType.APARTMENT.value())) {
			// If share rental
			if (type.contains(PropertyType.SHARE.value())) {
				property.setPropertyType(PropertyType.SHARE);
			} // If Studio
			else if (type.contains(PropertyType.STUDIO.value())) {
				property.setPropertyType(PropertyType.STUDIO);
			} else {
				property.setPropertyType(PropertyType.APARTMENT);
			}
		}// If commercial
		else if (type.contains(PropertyType.COMMERCIAL.value())) {
			// If type contains shops
			if (type.contains(PropertyType.SHOPS.value())) {
				property.setPropertyType(PropertyType.SHOPS);
			}// General category
			else {
				property.setPropertyType(PropertyType.INDUSTRIAL);
			}
		}// If car park
		else if (type.contains(PropertyType.PARK.value())) {
			property.setPropertyType(PropertyType.PARK);
		}
	}

	/**
	 * Cleans the elements of the price per feet elements.
	 * 
	 * @param elements
	 *            tr elements found in the property page
	 * 
	 * */
	protected final Elements cleanElements(Elements elements) {
		String deleteElements = "price per feet";
		Elements cleanElements = new Elements();
		boolean keywordFound = false;
		for (Element element : elements) {
			// If text contains keyword string
			keywordFound = element.getElementsByTag(TH).first().text()
					.toLowerCase().contains(deleteElements);
			if (!keywordFound) {
				cleanElements.add(element);
			}
		}
		return cleanElements;
	}

}
