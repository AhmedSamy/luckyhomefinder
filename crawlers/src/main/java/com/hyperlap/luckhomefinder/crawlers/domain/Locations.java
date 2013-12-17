package com.hyperlap.luckhomefinder.crawlers.domain;

/**
 * Holds the 4 main location categories exist in Hong Kong
 * 
 * @author Kareem ELshahawe
 * */
public enum Locations {
	ISLAND("island", 1), HKISLAND("hongkong island", 2), KOWLOON("kowloon", 3), NT(
			"new territories", 4);
	private String territory;
	private int code;

	/**
	 * Locations constructor
	 * */
	private Locations(String territory, int code) {
		this.code = code;
		this.territory = territory;
	}

	/**
	 * Return string representation of the territory.
	 * 
	 * @return string value of the territory.
	 * */
	public final String territory() {
		return territory;
	}

	/**
	 * Return territory code number.
	 * 
	 * @return territory code.
	 * */
	public final int code() {
		return code;
	}
}
