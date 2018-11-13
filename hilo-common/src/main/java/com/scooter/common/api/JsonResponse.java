package com.scooter.common.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * The type Json response.
 *
 * @param <T> the type parameter
 * @version 1.0
 */
@Getter
@Setter
public class JsonResponse<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2488889583493273806L;

	/**
	 * The constant STATE_OK.
	 */
	public static final Integer STATE_OK = 1;

	/**
	 * The constant STATE_FAILED.
	 */
	public static final Integer STATE_FAILED = 2;

	private Integer state;

	private T body;

	private String message;

	public JsonResponse() {
	}

	public JsonResponse(Integer state, T body, String message) {
		this.state = state;
		this.body = body;
		this.message = message;
	}

	/**
	 * Of json response.
	 *
	 * @param          <R> the type parameter
	 * @param state    the state
	 * @param body     the body
	 * @param messages the messages
	 * @return the json response
	 */
	public static <R> JsonResponse<R> of(Integer state, R body, String... messages) {
		return new JsonResponse<>(state, body, messages.length > 0 ? String.join(",", messages) : null);
	}

	/**
	 * Success json response.
	 *
	 * @param      <R> the type parameter
	 * @param body the body
	 * @return the json response
	 */
	public static <R> JsonResponse<R> success(R body) {
		return of(STATE_OK, body);
	}

	/**
	 * Success json response.
	 *
	 * @param <R> the type parameter
	 * @return the json response
	 */
	public static <R> JsonResponse<R> success() {
		return success(null);
	}

	/**
	 * Failed json response.
	 *
	 * @param          <R> the type parameter
	 * @param messages the messages
	 * @return the json response
	 */
	public static <R> JsonResponse<R> failed(String... messages) {
		return of(STATE_FAILED, null, messages);
	}

	/**
	 * To json string.
	 *
	 * @return the string
	 */
	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
