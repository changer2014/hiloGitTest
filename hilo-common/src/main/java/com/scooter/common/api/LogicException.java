package com.scooter.common.api;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Api exception.
 *
 * @version 1.0
 */
@Setter
@Getter
public class LogicException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6709225918809612781L;

	private String code;

	private Object[] args;

	/**
	 * Instantiates a new Api exception.
	 */
	public LogicException() {
		this(null);
	}

	/**
	 * Instantiates a new Logic exception.
	 *
	 * @param code the code
	 */
	public LogicException(String code) {
		this(code, null);
	}

	/**
	 * Instantiates a new Logic exception.
	 *
	 * @param code the code
	 * @param e    the e
	 */
	public LogicException(String code, Throwable e) {
		this(code, null, e);
	}

	/**
	 * Instantiates a new Api exception.
	 *
	 * @param code the code
	 * @param args the args
	 * @param e    the e
	 */
	public LogicException(String code, Object[] args, Throwable e) {
		super(e);
		this.code = code;
		this.args = args;
	}
}
