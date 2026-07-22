package com.smartbite.authservice.security;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import com.smartbite.authservice.entity.User;

/*
 * jwtservice for generating and validating jwt tokens
 */
@Service
public class JwtService {
	
	// Secret key read from application.properties
	@Value("${jwt.secret}")
	private String secret;
	
	// Token validity duration in milliseconds
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	
	 /*
     * Creates a cryptographic signing key
     * from the configured secret string
     */
	private Key getSigninKey() {
		
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	
	/*
     * Generates a JWT token for the authenticated userr
     */
	public String generateToken(User user) {
		
		return Jwts.builder()
				
				// Stores user's email as token subject
	            .setSubject(user.getEmail())

	            // Sets token creation time
	            .setIssuedAt(new Date())

	            // Sets token expiry time
	            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))

	            // Digitally signs the token
	            .signWith(getSigninKey(), SignatureAlgorithm.HS256)

	            // Builds the final JWT string
	            .compact();
		
		
		
		
	}
	
	
	/*
	 * extract all claims from jwt token
	 */
	
	private Claims extractAllClaims(String token) {
		
		return Jwts.parserBuilder()
				.setSigningKey(getSigninKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
				
	}
	
	
	/*
	 * Extracts email stored inside JWT
	 */
	
	public String extractUsername(String token) {
		
		return extractAllClaims(token).getSubject();
	}
	
	
	/*
	 * to check whether token has expired
	 * or not
	 */ 

	private boolean isTokenExpired(String token) {
		
		return extractAllClaims(token)
				.getExpiration()
				.before(new Date());
	}
	
	
	/*
	 * Validates token against user details
	 */
	
	
	public boolean isTokenValid(String token, User user) {
		
		String username = extractUsername(token);
		
		return username.equals(user.getEmail())
				&& !isTokenExpired(token);
	}
	

}
