package com.hilo.service.api.confiuration.security;

public class InvalidJwtTokenException extends Exception {

	private static final long serialVersionUID = 4057431748317644065L;

	/**
	 * Instantiates a new Invalid jwt token exception.
	 *
	 * @param message the message
	 */
	public InvalidJwtTokenException(String message) {
		super(message);
	}
}
