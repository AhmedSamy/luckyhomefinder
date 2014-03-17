package com.hyperlab.luckyhomefinder.service.Exceptions;

/**
 * Thrown when there is a problem with the manager.
 * */
public class ManagerException extends Exception {
	/**
	 * Auto generated serial ID.
	 */
	private static final long serialVersionUID = 1L;

	public ManagerException() {
		super();

	}

	public ManagerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ManagerException(String message) {
		super(message);
	}

	public ManagerException(Throwable cause) {
		super(cause);
	}
}
