package com.hyperlab.luckyhomefinder.service.Exceptions;

/**
 * Exception thrown when error occurred while dispatching property fetchers.
 * */
public class DispatcherException extends ManagerException {
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 1L;

	public DispatcherException() {
		super();

	}

	public DispatcherException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DispatcherException(String message, Throwable cause) {
		super(message, cause);
	}

	public DispatcherException(String message) {
		super(message);
	}

	public DispatcherException(Throwable cause) {
		super(cause);
	}
}
