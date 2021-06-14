package com.hse.security;

import com.hse.exceptions.ServiceException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.Key;

@Component
public class JwtProvider {

    private final Key key;

    @Autowired
    public JwtProvider(@Value("${jwt.key}") String key) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(@NotNull String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (JwtException exception) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Invalid token.");
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}