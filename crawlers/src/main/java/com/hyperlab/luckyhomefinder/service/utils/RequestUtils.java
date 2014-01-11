package com.hyperlab.luckyhomefinder.service.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that provides generic functionality and helper functions.
 * @author Kareem ElShahawe
 * */
public class RequestUtils {

	/**
	 * Parse data query used when sending requests, takes query string in format
	 * of key=value&key2=value2.. and return a hash map of the keys and values.
	 * 
	 * @param query
	 *            String contain query data.
	 * @return hash map containing the key and values of the request data.
	 * */
	public static final Map<String, String> parseQuery(final String query) {
		Map<String, String> queryData = new HashMap<>();

		String[] keyValue = query.split("&");
		String[] element;
		for (String currentData : keyValue) {
			element = currentData.split("=");
			if (element.length == 2) {
				queryData.put(element[0], element[1]);
			} else {
				queryData.put(element[0], "");
			}

		}
		return queryData;
	}
}
