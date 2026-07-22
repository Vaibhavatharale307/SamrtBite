package com.smartbite.authservice.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartbite.authservice.entity.User;
import com.smartbite.authservice.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	
	private final JwtService jwtService;
	
	private final CustomerUserDetailsService userdetservice;
	
	private final UserRepository userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("==JWT FILTER EXCUTED==");
		
		//reading authorization header
		
		String authHeader = request.getHeader("Authorization");
		
		//continue request if header is missing
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			
			return;
		}
		
		
		//remove "Bearer" prefix
		String token = authHeader.substring(7);
		
		// Extract email from JWT
		String email = jwtService.extractUsername(token);
		
		// Authenticate only if not already authenticated
		if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			UserDetails userdetails = userdetservice.loadUserByUsername(email);
			
			User user = userRepo.findByEmail(email)
					.orElseThrow();
			
			//validate jwt
			
			if(jwtService.isTokenValid(token, user)) {

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userdetails,
						null,
						userdetails.getAuthorities()
						);
				

				
     			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
     			
     			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
     			
 			
				
			}
			
		}
		
		///////////////////////////
		
		filterChain.doFilter(request, response);
		
	}
	
	

}
