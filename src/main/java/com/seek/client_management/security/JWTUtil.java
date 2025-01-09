package com.seek.client_management.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }
    public String generateToken(UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public boolean isValid(String jwt) {
        Algorithm algorithm = getAlgorithm();
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUser(String jwt) {
        Algorithm algorithm = getAlgorithm();
        return JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getSubject();
    }
}