// Define the package for this security configuration class
package org.example.service;
// Import configuration annotations for Spring
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
// Import annotation to enable Spring Security for web
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// Import HttpSecurity builder class to configure security behavior
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// Import SecurityFilterChain which represents the built security configuration
import org.springframework.security.web.SecurityFilterChain;
// Import SessionCreationPolicy to set stateless session management for JWT
import org.springframework.security.config.http.SessionCreationPolicy;
// Import filter class to decide where JwtAuthFilter should be placed in chain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Mark this class as a Spring configuration class
@Configuration
// Enable web security features in this application
@EnableWebSecurity
public class SecurityConfig {

    // Field to hold reference to JwtAuthFilter
    private final JwtAuthFilter jwtAuthFilter;

    // Constructor injection of JwtAuthFilter into SecurityConfig
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        // Save injected JwtAuthFilter into field
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // Define a SecurityFilterChain bean that configures all HTTP security settings
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF protection because we use token-based (JWT) auth and not browser forms
        http.csrf(csrf -> csrf.disable());

        // Configure session management to be STATELESS for JWT usage
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // Configure authorization rules for different HTTP endpoints
        http.authorizeHttpRequests(auth -> auth
                // Allow unauthenticated access to login endpoint
                .requestMatchers("/api/users/login").permitAll()
                // Allow unauthenticated access to signup endpoint
                .requestMatchers("/api/users/signup").permitAll()
                // Allow unauthenticated access to a simple hello or public endpoint (if you have one)
                .requestMatchers("/api/users/hello").permitAll()
                // Allow unauthenticated access to Spring error endpoint
                .requestMatchers("/error").permitAll()
                // Require authentication for any other HTTP request
                .anyRequest().authenticated()
        );

        // Register JwtAuthFilter to run before the UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the SecurityFilterChain object
        return http.build();
    }
}
