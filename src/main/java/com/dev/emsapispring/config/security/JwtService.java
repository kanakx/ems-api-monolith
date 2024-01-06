package com.dev.emsapispring.config.security;

import com.dev.emsapispring.exceptions.CustomApiException;
import com.dev.emsapispring.exceptions.ExceptionMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.List;

@Service
public class JwtService {

    private final Key key;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message(ExceptionMessage.expiredToken())
                    .build();
        } catch (JwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message(ExceptionMessage.invalidToken())
                    .build();
        }
    }

    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message(ExceptionMessage.tokenParsingError())
                    .build();
        }
    }

    public List<String> extractUserRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String rolesString = claims.get("roles", String.class);
            if (rolesString != null && !rolesString.isEmpty()) {
                return Arrays.stream(rolesString.split(","))
                        .map(String::trim)
                        .toList();
            } else {
                throw new JwtException("Roles not found in token");
            }
        } catch (JwtException e) {
            throw CustomApiException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message(ExceptionMessage.missingTokenClaims("Roles"))
                    .build();
        }
    }

}
