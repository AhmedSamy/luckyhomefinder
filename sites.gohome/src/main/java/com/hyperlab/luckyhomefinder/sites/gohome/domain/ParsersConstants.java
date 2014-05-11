package com.hyperlab.luckyhomefinder.sites.gohome.domain;

/**
 * Holds string keywords used in links fetcher and property parsers. keywords
 * can be for example tag names or div classes names.
 * */
public enum ParsersConstants {
	/** Property Div class name. */
	PROPERTYDIV("PropertyDiv"),
	/** CSS query to get properties Links html. */
	CSSQUERY("a[href"),
	/** Title class holding property title information. */
	TITLEDIV("title"),
	/** href attribute value. */
	HREF("href"),
	/**
	 * Property base link used to differentiate between property links and other
	 * links.
	 */
	PROPERTYBASELINK("property.gohome.com.hk"),
	/** Keyword to extract main profile details. */
	PRODETAIL("ProDetail1"),
	/** Row. */
	ROW2("row2"),
	/** TextDev class. */
	TXTDiV("TxtDiv"),
	/** Cols3 element. */
	COLS3("cols3"),
	/**Cols2 element.*/
	COLS2("cols2"),
	/**TableData element*/
	TABLEDATA("TableData");

	/** String value of the keyword. */
	private String value;

	/**
	 * Constructor.
	 * 
	 * @param str
	 *            value of the keyword.
	 */
	private ParsersConstants(final String str) {
		this.value = str;
	}

	/**
	 * Return string value of the designated keyword.
	 * 
	 * @return string value of the designated keyword.
	 */
	public final String value() {
		return this.value;
	}
}
