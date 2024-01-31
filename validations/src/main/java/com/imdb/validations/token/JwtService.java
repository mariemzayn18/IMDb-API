package com.imdb.validations.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secretKey}")
    private String secretKey;

    @Value("${security.jwt.expirationMillis}")
    private long expirationMillis;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ) {
        Date now = new Date();
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
        if (userDetails == null) {
            return !isTokenExpired(token);
        }
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

    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getBody();
    }
}
