package com.hilo.service.api.confiuration.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

import com.hilo.service.api.model.UserInfo;
import com.scooter.common.api.JsonResponse;
import com.scooter.common.util.GsonUtils;

import lombok.Cleanup;

public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter
		implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	private JwtTokenProvider jwtTokenProvider;

	/**
	 * Instantiates a new Authentication filter.
	 */
	public UsernamePasswordAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
		super("/login");
		setAuthenticationSuccessHandler(this);
		setAuthenticationFailureHandler(this);
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {
		if (HttpMethod.valueOf(request.getMethod()) != HttpMethod.POST) {
			throw new AuthenticationServiceException("Authentication method " + request.getMethod() + " not supported");
		}
		String json = getRequestBody(request);
		String phone = null;
		Object password = null;
		if (!StringUtils.isEmpty(json)) {
			UserInfo user = GsonUtils.fromJson(json, UserInfo.class);
			phone = user.getPhone().trim();
			password = user.getPassword();
		}
		if (phone == null) {
			phone = "";
		}
		if (password == null) {
			password = "";
		}
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(phone, password);
		return this.getAuthenticationManager().authenticate(token);
	}

	private String getRequestBody(HttpServletRequest request) throws IOException {
		@Cleanup
		InputStream inputStream = request.getInputStream();
		@Cleanup
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		StringBuilder holder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			holder.append(line);
		}
		return holder.toString();
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		writeResponse(response, JsonResponse.failed(exception.getMessage()));
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		UserInfo riderInfo = (UserInfo) authentication.getPrincipal();
		String accessToken = jwtTokenProvider.getAccessToken(riderInfo);
		String refreshToken = jwtTokenProvider.getRefreshToken(riderInfo.getPhone());
		Map<String, Object> body = new HashMap<>();
		body.put("accessToken", JwtAuthenticationFilter.BEARER + accessToken);
		body.put("refreshToken", refreshToken);

		writeResponse(response, JsonResponse.success(body));
	}

	private void writeResponse(HttpServletResponse response, JsonResponse<Map<String, Object>> jsonResponse)
			throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(jsonResponse.toJson());
		response.getWriter().flush();
	}
}
