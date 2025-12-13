package org.example.service;

// Import Claims interface to work with JWT payload data
import io.jsonwebtoken.Claims;
// Import main JJWT class for building and parsing tokens
import io.jsonwebtoken.Jwts;
// Import enum that represents the signing algorithm we will use (HS256)
import io.jsonwebtoken.SignatureAlgorithm;
// Import utility for Base64 decoding
import io.jsonwebtoken.io.Decoders;
// Import utility for creating cryptographic keys
import io.jsonwebtoken.security.Keys;

// Import Spring annotation to mark this as a service bean
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Import Java Key interface used by the signing algorithm
import java.security.Key;
// Import Java Date class for issued-at and expiration times
import java.util.Date;
// Import Java Function interface for generic claim extraction
import java.util.function.Function;

// Mark this class as a Spring-managed service component
@Service
public class JwtService {

    // Inject a Base64-encoded secret key string from application.properties (app.jwt.secret)
    @Value("${app.jwt.secret}")
    private String secretKey;

    // Inject expiration time in milliseconds, with a default of 24 hours if not set
    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    // ---------------------------------------------------------------------
    // generateToken - creates a signed JWT for the given username
    // ---------------------------------------------------------------------
    public String generateToken(String username) {
        // Create a Date object representing the current time (issue time)
        Date now = new Date();
        // Create a Date object for the expiration time (now + configured duration)
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        // Build and return a compact JWT string using the JJWT builder
        return Jwts.builder()
                // Set the subject (sub) claim to the username (who the token belongs to)
                .setSubject(username)
                // Set the issued-at (iat) claim to the current time
                .setIssuedAt(now)
                // Set the expiration (exp) claim to the calculated expiry date
                .setExpiration(expiry)
                // Sign the token using our secret key and HS256 algorithm
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                // Convert the token object into its compact String representation
                .compact();
    }

    // ---------------------------------------------------------------------
    // extractUsername - reads the username (subject) out of a JWT
    // ---------------------------------------------------------------------
    public String extractUsername(String token) {
        // Delegate to the generic extractClaim method using Claims::getSubject as resolver
        return extractClaim(token, Claims::getSubject);
    }

    // ---------------------------------------------------------------------
    // isTokenValid - checks that the token belongs to the given username and is not expired
    // ---------------------------------------------------------------------
    public boolean isTokenValid(String token, String username) {
        // Extract the username from the token
        String extractedUser = extractUsername(token);
        // Return true only if usernames match and token is not expired
        return extractedUser.equals(username) && !isTokenExpired(token);
    }

    // ---------------------------------------------------------------------
    // isTokenExpired - checks if the token's expiration time is in the past
    // ---------------------------------------------------------------------
    private boolean isTokenExpired(String token) {
        // Extract the expiration date from the token
        Date expiration = extractClaim(token, Claims::getExpiration);
        // Compare expiration date with the current time and return true if expired
        return expiration.before(new Date());
    }

    // ---------------------------------------------------------------------
    // extractClaim - generic helper to pull any claim from the token
    // ---------------------------------------------------------------------
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        // Parse the token and get all claims (payload)
        Claims claims = extractAllClaims(token);
        // Apply the given resolver function to the claims and return the result
        return resolver.apply(claims);
    }

    // ---------------------------------------------------------------------
    // extractAllClaims - parses the JWT and returns all its claims
    // ---------------------------------------------------------------------
    private Claims extractAllClaims(String token) {
        // Build a JWT parser configured with our signing key
        return Jwts.parserBuilder()
                // Set the signing key to verify the token's signature
                .setSigningKey(getSignKey())
                // Build the parser instance
                .build()
                // Parse the signed JWT string and get the JWS body (claims)
                .parseClaimsJws(token)
                .getBody();
    }

    // ---------------------------------------------------------------------
    // getSignKey - converts the Base64 secret string into a cryptographic key
    // ---------------------------------------------------------------------
    private Key getSignKey() {
        // Decode the Base64-encoded secretKey string into a raw byte array
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Create an HMAC-SHA key (for HS256) from the raw secret bytes
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
