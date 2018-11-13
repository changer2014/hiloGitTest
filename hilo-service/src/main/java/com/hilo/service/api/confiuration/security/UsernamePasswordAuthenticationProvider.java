package com.hilo.service.api.confiuration.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.hilo.service.api.model.UserInfo;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {


    private PasswordEncoder passwordEncoder;

    public UsernamePasswordAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        UserInfo riderInfo = null;//this.userService.findByPhone(phone);
        if (riderInfo == null) {
            throw new UsernameNotFoundException("User " + phone + " Not Found");
        }
        if (!this.passwordEncoder.matches(password, riderInfo.getPassword())) {
            throw new BadCredentialsException("Bad Credentials");
        }
        return new UsernamePasswordAuthenticationToken(riderInfo, null, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
