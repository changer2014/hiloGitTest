package com.hilo.service.api.confiuration.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.scooter.common.api.JsonResponse;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String BEARER = "Bearer ";

	/**
	 * Instantiates a new Jwt authentication filter.
	 */
	public JwtAuthenticationFilter() {
		super(new SkipPathRequestMatcher(Arrays.asList(new AntPathRequestMatcher("/login", "POST"),
				new AntPathRequestMatcher("/token/refresh", "POST"), new AntPathRequestMatcher("/logout", "GET"),
				new AntPathRequestMatcher("/rider/register", "POST")), new AntPathRequestMatcher("/**")));
	}

	private void writeResponse(HttpServletResponse response, JsonResponse<?> jsonResponse) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(jsonResponse.toJson());
		response.getWriter().flush();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String header = request.getHeader("Authorization");
		return getAuthenticationManager()
				.authenticate(new JwtAuthenticationToken(header == null ? null : header.replace(BEARER, "")));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		writeResponse(response, JsonResponse.failed(failed.getMessage()));
	}
}
