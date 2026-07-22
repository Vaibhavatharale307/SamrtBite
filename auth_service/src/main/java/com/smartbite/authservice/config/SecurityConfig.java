package com.smartbite.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.smartbite.authservice.security.CustomerUserDetailsService;
import com.smartbite.authservice.security.JwtFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	
	private final JwtFilter jwtfilter;
	private final CustomerUserDetailsService userDetailsService;
	
	
	//PasswordEncoder
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	//Authentication Provider
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

	    DaoAuthenticationProvider authProvider =new DaoAuthenticationProvider(userDetailsService);

	    authProvider.setPasswordEncoder(passwordEncoder());

	    return authProvider;
	}
	
	
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		    .csrf(csrf->csrf.disable())
		    .authenticationProvider(authenticationProvider())
		    .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
		    .authorizeHttpRequests(auth->auth
		    		.requestMatchers("/auth/register", "/auth/login").permitAll().anyRequest().authenticated());
		
		return http.build();
	}

}
