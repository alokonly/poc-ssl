package com.example.mtlsclientsecond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/v1/test")
public class ClientTestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping
    ResponseEntity<String> callSecuredServer(){
        return restTemplate.getForEntity("https://localhost:8443/v1/sample", String.class, Collections.emptyMap());
    }
}
