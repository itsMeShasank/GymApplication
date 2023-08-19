package com.epam.authenticationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private static final String METHOD_INVOKED = "method {} invoked in AuthService class";
    private final JwtService jwtService;

    public String generateToken(String username) {
        log.info(METHOD_INVOKED,"generateToken");
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        log.info(METHOD_INVOKED,"validateToken");
        jwtService.validateToken(token);
    }

}
