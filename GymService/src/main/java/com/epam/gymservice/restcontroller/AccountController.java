package com.epam.gymservice.restcontroller;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.ModifyCredentials;
import com.epam.gymservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gym-service")
public class AccountController {

    private static final String METHOD_ENTERED = "{} Api entered";
    private static final String METHOD_EXITED = "{} Api exited";

    private final AccountService accountServiceImpl;


    @PostMapping("/login")
    public ResponseEntity<HttpStatus> userLogin(@RequestBody @Valid CredentialsDetails details) {
        log.info(METHOD_ENTERED,"userLogin");
        accountServiceImpl.validateUser(details);
        log.info(METHOD_EXITED,"userLogin");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-password")
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody @Valid ModifyCredentials details) {
        log.info(METHOD_ENTERED,"updatePassword");
        accountServiceImpl.updateUser(details);
        log.info(METHOD_EXITED,"updatePassword");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
