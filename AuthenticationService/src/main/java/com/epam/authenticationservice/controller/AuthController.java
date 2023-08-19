package com.epam.authenticationservice.controller;

import com.epam.authenticationservice.dto.CredentialsDetails;
import com.epam.authenticationservice.exception.UnAuthorizedException;
import com.epam.authenticationservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/authenticate")
public class AuthController {
    private static final String API_INVOKED = "method {} invoked in AuthController class";
    private static final String API_EXITED = "method {} exited in AuthController class";
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> generateToken(@RequestBody @Valid CredentialsDetails credentialsDetails) {
        log.info(API_INVOKED,"generateToken");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentialsDetails.getUserName(),credentialsDetails.getPassword()));
        if(!authentication.isAuthenticated()) {
            log.info("not authenticated wrong credentials");
            throw new UnAuthorizedException("Invalid Access");
        }
        String token = authService.generateToken(credentialsDetails.getUserName());
        log.info(API_EXITED,"generateToken");
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        log.info(API_INVOKED,"validateToken");
        authService.validateToken(token);
        log.info(API_EXITED,"validateToken");
        return new ResponseEntity<>("Valid Token",HttpStatus.OK);
    }
}
