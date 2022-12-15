package com.mjt.condo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.mjt.condo.providers.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Autowired
	private CustomAuthenticationProvider authProvider;

	private static final String[] WHITE_LIST = {
			"/apartments",
			"/apartments/{^[\\d]$}",
			"/apartments/dto",
			"/apartments/dto/{^[\\d]$}",
	};

	private static final String[] BLACK_LIST = {
			"/tenants",
			"/tenants/**",
			"/users",
			"/users/**",
			"/apartments/show/{^[\\d]$}",
			"/apartments/count/{^[\\d]$}",
	};

	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_USER = "USER";

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception  {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authProvider);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		HttpSecurity http = httpSecurity
				.csrf(csrf -> csrf.disable())
				.authorizeRequests(auth -> {
					auth
					.antMatchers("/h2-console/**").permitAll()

					.antMatchers(HttpMethod.GET, WHITE_LIST).hasAnyRole(ROLE_USER,ROLE_ADMIN)
					.antMatchers(HttpMethod.POST, WHITE_LIST).hasRole(ROLE_ADMIN)
					.antMatchers(HttpMethod.PUT, WHITE_LIST).hasRole(ROLE_ADMIN)
					.antMatchers(HttpMethod.DELETE, WHITE_LIST).hasRole(ROLE_ADMIN)
					
					.antMatchers(HttpMethod.GET, BLACK_LIST).hasRole(ROLE_ADMIN)
					.antMatchers(HttpMethod.POST, BLACK_LIST).hasRole(ROLE_ADMIN)
					.antMatchers(HttpMethod.PUT, BLACK_LIST).hasRole(ROLE_ADMIN)
					.antMatchers(HttpMethod.DELETE, BLACK_LIST).hasRole(ROLE_ADMIN)
							.anyRequest().authenticated();
				})
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(Customizer.withDefaults());
		http.headers().frameOptions().disable();
		return http.build();
	}

}