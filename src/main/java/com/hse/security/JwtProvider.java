package com.hse.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final Key key;

    @Autowired
    public JwtProvider() {
        this.key = Keys.hmacShaKeyFor("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c".getBytes());
    }

    public Key getKey() {
        return key;
    }

    public String generateToken(@NotNull String login) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        return Jwts.builder()
                .setSubject(login)
                .signWith(key)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .compact();
    }

    public boolean validateToken(String token) {
        if (token == null) {
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (JwtException exception) {
            return false;
        }
    }
}
