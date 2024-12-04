package pe.bazan.luis.attendance.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {
    private String jwtSigningKey = "Z3BkQ2dDTXhydVFATnZycVZSNHBZUCRpWU0mZnF6enBDVTIydXZWalE2THUlcDljZkAyQDhUI2VFS1UlcUtoUjhtanNe";

    public String extractSubject(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            return "";
        }
    }

    public String generateToken(String username) {
        return generateToken(new HashMap<>(), username);
    }

    public boolean isValid(String token) {
        return !extractSubject(token).isEmpty();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .claims()
                .add(extraClaims)
                .subject(username)
                .and()
                .signWith(getSigningKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        SecretKey secretKey = new SecretKeySpec(getSigningKey().getEncoded(), getSigningKey().getAlgorithm());
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}