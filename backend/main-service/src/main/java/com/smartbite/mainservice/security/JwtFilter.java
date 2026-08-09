package com.smartbite.mainservice.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.repository.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomerUserDetailsService userdetservice;
    private final UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No token → continue as anonymous
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // Extract email — throws JwtException if token is tampered/expired/wrong-key
            String email = jwtService.extractUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userdetservice.loadUserByUsername(email);

                User user = userRepo.findByEmail(email).orElse(null);

                if (user != null && jwtService.isTokenValid(token, user)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (JwtException ex) {
            /*
             * Catches:
             *   - SignatureException   (wrong secret / tampered token)
             *   - ExpiredJwtException  (token past expiry)
             *   - MalformedJwtException (garbage token)
             *   - UnsupportedJwtException
             *
             * We simply skip authentication → Spring Security treats request
             * as anonymous → protected endpoints return 401/403 normally.
             *
             * We do NOT throw here — that caused the 500 errors you saw.
             */
            log.warn("Invalid JWT token [{}]: {}", request.getRequestURI(), ex.getMessage());

        } catch (Exception ex) {
            // Catch-all for any other unexpected error (e.g. user not found in DB)
            log.warn("JWT filter error [{}]: {}", request.getRequestURI(), ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
