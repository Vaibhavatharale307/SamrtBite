package com.smartbite.walletservice.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class InternalApiKeyFilter extends OncePerRequestFilter {

	private static final String HEADER_NAME = "X-Internal-Api-Key";

	@Value("${internal.api.key}")
	private String internalApiKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!request.getRequestURI().startsWith("/wallet")) {
			filterChain.doFilter(request, response);
			return;
		}

		String apiKey = request.getHeader(HEADER_NAME);
		if (apiKey == null || !apiKey.equals(internalApiKey)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal API key");
			return;
		}

		filterChain.doFilter(request, response);
	}
}
