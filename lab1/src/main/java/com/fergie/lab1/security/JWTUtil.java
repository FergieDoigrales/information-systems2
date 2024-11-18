package com.fergie.lab1.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    @Value("FERGIEakssaksd") //из application.properties
    private String secret;
    public String generateToken(String username) {
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000); //час
        return JWT.create()
                .withSubject("user details")
                .withClaim("username", username) //пары, к-рые добавляем в пейлоад
                .withClaim("role", "USER")
                .withIssuedAt(new Date()) //когда выдан
                .withIssuer("fergie") //хто выдал
                .withExpiresAt(expirationDate) //когда закончится
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(secret))
                .withSubject("user details")
                .withIssuer("fergie")
                .build()
                .verify(token) //проверка подписи
                .getClaim("username")
                .asString();
    }

}
