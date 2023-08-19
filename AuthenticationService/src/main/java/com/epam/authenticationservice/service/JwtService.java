package com.epam.authenticationservice.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    private static final String SERVICE_INVOKED = "method {} invoked in JwtService class";
    private static final String SERVICE_EXITED = "method {} exited in JwtService class";

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public void validateToken(final String token) {
        log.info(SERVICE_INVOKED,"validateToken");
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        log.info(SERVICE_EXITED,"validateToken");
    }

    public String generateToken(String username) {
        log.info(SERVICE_INVOKED,"generateToken");
        Map<String,Object> claims = new HashMap<>();
        log.info(SERVICE_EXITED,"generateToken");
        return createToken(claims,username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        log.info(SERVICE_INVOKED,"createToken");
        String token =  Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30 * 60))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        log.info(SERVICE_EXITED,"createToken");
        return token;
    }

    private Key getSignKey() {
        log.info(SERVICE_INVOKED,"getSignKey");
        byte[] keys = Decoders.BASE64.decode(SECRET);
        log.info(SERVICE_EXITED,"getSignKey");
        return Keys.hmacShaKeyFor(keys);
    }
}
