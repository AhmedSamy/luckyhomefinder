package com.hyperlab.luckyhomefinder.common.domain;

/**
 * Enum holding generic connection configuration used to initiate connection to
 * any of the target website.
 * */
public enum ConnectionConfigs {
	USERAGENT(
			"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:26.0) Gecko/20100101 Firefox/26.0");

	private String param;

	private ConnectionConfigs(final String param) {
		this.param = param;
	}

	public final String value() {
		return this.param;
	}
}
