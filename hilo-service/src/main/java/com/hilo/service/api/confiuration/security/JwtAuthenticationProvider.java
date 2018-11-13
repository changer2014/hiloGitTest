package com.hilo.service.api.confiuration.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.hilo.service.api.model.UserInfo;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private JwtTokenProvider jwtTokenProvider;

  public JwtAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();
    try {
      UserInfo user = this.jwtTokenProvider.parseAccessToken(token);
      return new JwtAuthenticationToken(user, Collections.emptyList());
    } catch (InvalidJwtTokenException e) {
      throw new BadCredentialsException(e.getMessage());
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
