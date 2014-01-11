package com.hyperlab.luckyhomefinder.service.Exceptions;

import org.springframework.stereotype.Component;

/**
 * Exceptions that will be thrown in case of error occurred while links fetcher
 * is performing.
 * 
 * @author Kareem ElSHahawe
 * */
@Component
public class LinksFetcherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LinksFetcherException() {
		super();

	}

	public LinksFetcherException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LinksFetcherException(String message, Throwable cause) {
		super(message, cause);
	}

	public LinksFetcherException(String message) {
		super(message);
	}

	public LinksFetcherException(Throwable cause) {
		super(cause);
	}

}
