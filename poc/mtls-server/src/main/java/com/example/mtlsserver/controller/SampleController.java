package com.example.mtlsserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
public class SampleController {

    @GetMapping("/v1/sample")
    public ResponseEntity<String> sampleGet(Principal principal){
        UserDetails currentUser
                = (UserDetails) ((Authentication) principal).getPrincipal();
        log.info("User is : " + currentUser);
        return ResponseEntity.ok(currentUser.getUsername());
    }
}
