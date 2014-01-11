package com.hyperlab.luckyhomefinder.service.Exceptions;

import org.springframework.stereotype.Component;

/**
 * Exceptions that will be thrown in case of error occurred while property
 * fetcher is performing.
 * @author Kareem ElSHahawe
 * */
@Component
public class PropertyFetcherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertyFetcherException() {
		super();

	}

	public PropertyFetcherException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PropertyFetcherException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyFetcherException(String message) {
		super(message);
	}

	public PropertyFetcherException(Throwable cause) {
		super(cause);
	}

}
