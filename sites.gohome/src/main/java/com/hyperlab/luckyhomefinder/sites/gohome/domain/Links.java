package com.hyperlab.luckyhomefinder.sites.gohome.domain;

/**
 * Holds main links used to parse the site.
 * */
public enum Links {
	/** Search link value.where sort by 6 is to sort data by date. */
	SEARCH("http://search.gohome.com.hk/en/?sortby=6"),
	/** Main site link. */
	Site("http://www.gohome.com.hk/en/"),
	/**Paging Link.*/
	PagingBase("http://search.gohome.com.hk/en/?sortby=6&Page=");
	/** Links value. */
	private String value;

	/**
	 * Set links String value.
	 * 
	 * @param link
	 *            link string value.
	 */
	private Links(final String link) {
		this.value = link;
	}

	/**
	 * Return link string value.
	 * 
	 * @return string value of link.
	 */
	public final String value() {
		return value;
	}

}
