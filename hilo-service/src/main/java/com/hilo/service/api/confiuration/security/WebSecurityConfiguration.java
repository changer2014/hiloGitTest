package com.hilo.service.api.confiuration.security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.scooter.common.api.JsonResponse;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider;

	private JwtAuthenticationProvider jwtAuthenticationProvider;

	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Autowired
	public void setUsernamePasswordAuthenticationProvider(
			UsernamePasswordAuthenticationProvider usernamePasswordAuthenticationProvider) {
		this.usernamePasswordAuthenticationProvider = usernamePasswordAuthenticationProvider;
	}

	@Autowired
	public void setJwtAuthenticationProvider(JwtAuthenticationProvider jwtAuthenticationProvider) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
	}

	@Bean
	public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
		UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter(jwtTokenProvider);
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(usernamePasswordAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}

	/**
	 * Password encoder password encoder.
	 *
	 * @return the password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling()
				.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(HttpServletResponse.SC_OK);
					writeResponse(response, JsonResponse.failed());
				}).accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					writeResponse(response, JsonResponse.failed());
				}).and().authorizeRequests().antMatchers(HttpMethod.POST, "/rider/register").permitAll()
				.antMatchers(HttpMethod.POST, "/token/refresh").permitAll().antMatchers(HttpMethod.POST, "/login")
				.permitAll().anyRequest().authenticated().and().logout().logoutUrl("/logout").permitAll()
				.logoutSuccessHandler((request, response, authentication) -> {
					response.setStatus(HttpServletResponse.SC_OK);
					writeResponse(response, JsonResponse.success());
				}).clearAuthentication(true).invalidateHttpSession(true).permitAll().and().sessionManagement()
				.maximumSessions(1);
		http.addFilterBefore(usernamePasswordAuthenticationFilter(),
				org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(jwtAuthenticationFilter(),
				org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
	}

	private void writeResponse(HttpServletResponse response, JsonResponse<?> jsonResponse) {
		try {
			response.getWriter().write(jsonResponse.toJson());
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
