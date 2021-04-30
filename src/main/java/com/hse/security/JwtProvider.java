package com.hse.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.Key;

@Component
public class JwtProvider {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public Key getKey() {
        return key;
    }

    public String generateToken(@NotNull String login) {
        return "Bearer " + Jwts.builder()
                .setSubject(login)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            token = token.replace("Bearer ", "");
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException ignored) {
            return false;
        }
    }
}
