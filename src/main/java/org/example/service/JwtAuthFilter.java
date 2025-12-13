// Define the package for this JWT authentication filter class
package org.example.service;
// Import HttpServletRequest to access request data
import jakarta.servlet.http.HttpServletRequest;
// Import HttpServletResponse to access response object
import jakarta.servlet.http.HttpServletResponse;
// Import FilterChain to continue the filter processing chain
import jakarta.servlet.FilterChain;
// Import ServletException for filter method signature
import jakarta.servlet.ServletException;
// Import IOException for filter method signature
import java.io.IOException;

// Import Spring's @Component to register this filter as a Spring bean
import org.springframework.stereotype.Component;
// Import OncePerRequestFilter to ensure the filter runs once per request
import org.springframework.web.filter.OncePerRequestFilter;

// Import SecurityContextHolder to store authentication for current request
import org.springframework.security.core.context.SecurityContextHolder;
// Import UsernamePasswordAuthenticationToken to create Authentication object
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// Import WebAuthenticationDetailsSource to attach request details to Authentication
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// Import Collections to create empty authorities list
import java.util.Collections;

// Mark this class as a Spring component so it can be injected and used in SecurityConfig
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Field to hold reference to JwtService (for validating and parsing tokens)
    private final JwtService jwtService;

    // Constructor injection of JwtService into this filter
    public JwtAuthFilter(JwtService jwtService) {
        // Save the injected JwtService into the field
        this.jwtService = jwtService;
    }

    // Main filter method that runs once per HTTP request
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,       // current HTTP request
            HttpServletResponse response,     // current HTTP response
            FilterChain filterChain           // chain to continue with next filters
    ) throws ServletException, IOException {

        // Read the Authorization header from the incoming HTTP request
        String authHeader = request.getHeader("Authorization");

        // If Authorization header is missing or does not start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Skip JWT processing and continue the filter chain as normal
            filterChain.doFilter(request, response);
            // Return so we do not execute the rest of the method
            return;
        }

        // Remove the "Bearer " prefix and keep only the raw token string
        String token = authHeader.substring(7);

        // Initialize username variable to null before extraction
        String username = null;

        try {
            // Try to extract username (subject) from the token using JwtService
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            // If any exception occurs during parsing, just continue the filter chain
            filterChain.doFilter(request, response);
            // Return to avoid using an invalid token
            return;
        }

        // If we successfully extracted a username and there is no authentication yet
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Check if the token is valid for this username (not expired, matches subject)
            boolean valid = jwtService.isTokenValid(token, username);

            // If token is valid
            if (valid) {
                // Create an Authentication object with username and no authorities
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                username,                 // principal (username)
                                null,                     // credentials (not needed here)
                                Collections.emptyList()   // authorities (no roles for now)
                        );

                // Attach web authentication details (IP, session, etc.) from the request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Store the Authentication object in the SecurityContext for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain so the request can reach controllers
        filterChain.doFilter(request, response);
    }
}
