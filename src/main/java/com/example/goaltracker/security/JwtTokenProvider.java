package com.example.goaltracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;

    private Key key;

    @PostConstruct
    protected void init() {
        // Convertir la clé secrète en tableau d'octets et l'utiliser pour générer la clé HS512
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired while trying to extract username: " + e.getMessage());
            return e.getClaims().getSubject(); // Retourne le sujet même si le token est expiré
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid or malformed token: " + e.getMessage());
        }
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.err.println("Token expired" + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            System.err.println("Unsupported JWT token" + e.getMessage());
            throw new JwtException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            System.err.println("Malformed JWT token" + e.getMessage());
            throw new JwtException("Malformed JWT token");
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty" + e.getMessage());
            throw new JwtException("JWT claims string is empty");
        } catch (JwtException e) {
            System.err.println("JWT token is invalid" + e.getMessage());
            throw new JwtException("JWT token is invalid");
        }
    }

    public boolean canTokenBeRefreshed(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Expired or invalid JWT token");
            return false;
        }
    }
}
