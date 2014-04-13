package com.hyperlab.luckyhomefinder.common.domain;

/**
 * Holds the property type, wither this property is a car park, commercial
 * building...etc
 * */
public enum PropertyType {

	APARTMENT("apartment", 1), INDUSTRIAL("industrial", 2), COMMERCIAL(
			"commercial", 3), SHOPS("shop", 4), BUSINESS("business", 5), PARK(
			"park", 6), SHARE("share", 7), STUDIO("studio", 8), VILLAGEHOUSE(
			"village house", 9), SHORT("short", 10), FARM("farm", 11), HOUSE(
			"house",12);
	/** Hold the string representation of the property type */
	private String type;
	/** Hold the integer representation of the property type */
	private int code;

	/** Property type constructor */
	private PropertyType(String type, int code) {
		this.type = type;
		this.code = code;
	}

	/**
	 * Return String value of the type.
	 * 
	 * @return String representation of the property type.
	 */
	public final String value() {
		return this.type;
	}

	/**
	 * Return property type code.
	 * 
	 * @return integer representation of the property type.
	 */
	public final int code() {
		return this.code;
	}
}
