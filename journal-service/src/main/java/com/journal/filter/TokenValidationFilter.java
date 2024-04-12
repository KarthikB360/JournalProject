package com.journal.filter;

import com.journal.service.TokenValidationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenValidationService tokenValidationService;


    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
        // Extract token and role from request
        String token = extractToken(request);
        String role = "ADMIN";

        // Validate token using token validation service
        if (StringUtils.hasText(token) && tokenValidationService.validateToken(token, role)) {
            // Token is valid, continue with the filter chain
            log.info("User authenticated");
            filterChain.doFilter(request, response);
        } else {
            // Token is invalid or missing role, return unauthorized response
            log.error("User not authenticated");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }
}

