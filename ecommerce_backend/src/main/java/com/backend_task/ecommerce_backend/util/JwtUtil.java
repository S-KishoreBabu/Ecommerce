package com.backend_task.ecommerce_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtil class handles the creation and validation of JWT tokens.
 * Uses io.jsonwebtoken (jjwt) library version 0.11.5.
 */
public class JwtUtil {

    // 🧠 Secret key used for signing the JWT — in a real app, store this safely (like in environment variables)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 🕐 Token validity (1 hour = 3600000 ms)
    private static final long EXPIRATION_TIME = 3600000;

    /**
     * Generate a JWT token for a given username.
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // user identity
                .setIssuedAt(new Date()) // current time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiry time
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256) // ✅ new method signature (Key, Algorithm)
                .compact(); // build the final token
    }

    /**
     * Extract the username from a given JWT token.
     */
    public static String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validate a token by checking:
     * - If username matches
     * - If token is not expired
     */
    public static boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Check if the token is expired.
     */
    private static boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * Parse and extract all claims (payload) from the token.
     */
    private static Claims extractAllClaims(String token) {
        // ✅ Uses new parserBuilder() method (requires jjwt-api + jjwt-impl + jjwt-jackson)
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
