package com.imdb.authenticationAPI.config.service;

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

    private SecretKey secretKey;

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
            Date now = new Date();
            long expirationMillis = 1000 * 60 * 60 * 2; // 2 hours
            Date expiryDate = new Date(now.getTime() + expirationMillis);
            SecretKey key= getSecretKey();
            return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(key)
                    .compact();
    }

    // Validate whether token is expired or not
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
            final Date expiration = extractExpiration(token);
            return expiration.before(new Date());

    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
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

    private SecretKey getSecretKey(){
        if(secretKey==null){
            try {
                secretKey= generateSecretKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return secretKey;
    }
    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getBody();
    }
}
