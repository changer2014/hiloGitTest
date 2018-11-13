package com.hilo.service.api.confiuration.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.hilo.service.api.model.UserInfo;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 7498571959374927812L;

	private UserInfo user;

	private String accessToken;

	/**
	 * Creates a token with the supplied array of authorities.
	 *
	 * @param accessToken the access token
	 */
	public JwtAuthenticationToken(String accessToken) {
		super(null);
		this.accessToken = accessToken;
		this.setAuthenticated(false);
	}

	/**
	 * Instantiates a new Jwt authentication token.
	 *
	 * @param user        the user
	 * @param authorities the authorities
	 */
	public JwtAuthenticationToken(UserInfo user, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.eraseCredentials();
		this.user = user;
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return accessToken;
	}

	@Override
	public Object getPrincipal() {
		return user;
	}
}
