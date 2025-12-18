package com.edvalld;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class JwtUtils {

    // JWT expiration time (1 hour)
    private final int jwtExpirationMs = (int) TimeUnit.HOURS.toMillis (1);

    static JwtUtils jwtUtils;

    public static JwtUtils getInstance() {
        if(jwtUtils == null) {
            jwtUtils = new JwtUtils();
        }
        return jwtUtils;
    }

    private SecretKey generateKey(String keyValue)
    {
        byte[] keyBytes = Base64.getDecoder ().decode(keyValue);
        return Keys.hmacShaKeyFor(keyBytes );
    }

    public String generateJwtToken (List<String> roles, String username, String keyValue ) {
        /*List<String> roles = customUser.getRoles().stream().map(
                userRole -> userRole.getRoleName()
        ).toList();*/
        return Jwts.builder()
                .subject(username)
                .claim("authorities", roles)
                .issuedAt (new Date())
                .expiration (new Date(System.currentTimeMillis () + jwtExpirationMs ))
                .signWith(generateKey(keyValue))
                .compact();
    }

    public String getUsernameFromJwtToken (String token, String keyValue) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith (generateKey(keyValue))
                    .build()
                    .parseSignedClaims (token)
                    .getPayload ();
            return claims.getSubject ();
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getRolesFromJwtToken (String token, String keyValue) {

        Claims claims = Jwts.parser()
                .verifyWith (generateKey(keyValue))
                .build()
                .parseSignedClaims (token)
                .getPayload();

        Object rolesClaim = claims.get("roles");

        if (rolesClaim == null) {
            return List.of();
        }

        if (rolesClaim instanceof List<?> rolesList) {
            return rolesList.stream()
                    .map(Object::toString)
                    .toList();
        }

        throw new IllegalStateException("Invalid roles claim in JWT");
    }

    public boolean validateJwtToken (String authToken, String keyValue) {
        try {
            Jwts.parser()
                    .verifyWith (generateKey(keyValue))
                    .build()
                    .parseSignedClaims (authToken );
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }
}
