package com.alberinando.imagelite.infrastructure.security;

import com.alberinando.imagelite.domain.entities.User;
import com.alberinando.imagelite.infrastructure.config.AccessToken;
import com.alberinando.imagelite.infrastructure.exceptions.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Jwt {

    private final SecretKeyGenerator secretKeyGenerator;

    private Date getExpiration() {
        LocalDateTime now = LocalDateTime.now().plusMinutes(60);
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Map<String, Object> generateTokenClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    public AccessToken getAccessToken(User user) {
        SecretKey secretKey = secretKeyGenerator.getSecretKey();
        Date expiration = getExpiration();
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expiration)
                .addClaims(generateTokenClaims(user))
                .signWith(secretKey)
                .compact();
        return new AccessToken(token);
    }

    public String getEmailFromToken(String token) {
        try{
            SecretKey secretKey = secretKeyGenerator.getSecretKey();

            var jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return jws.getBody().getSubject();
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}
