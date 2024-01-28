package com.example.IMDbAPI.config.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    // to generate a token without any extra claims
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(),  userDetails);
    }

    private String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ){
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // 1 day
            SecretKey key= generateSecretKey();
            return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(key)
                    .compact();
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    // Validate whether token is expired or not
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
            final Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration.before(new Date());

    }

    // for retrieving any information from token
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        // Generate a secret key using AES algorithm with a key size of 256 bits
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();

        // Encode the SecretKey to Base64
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getEncoded());

        // Encode the AES key to Base64 and use it to generate an HMAC key
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException {
        return Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getBody();
    }
}
