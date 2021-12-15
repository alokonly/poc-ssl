package com.example.certclient.controller;

import com.example.certclient.config.SSLWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class TestService {

    private SSLWebClient sslWebClient;
    Environment env;
    RestTemplate restTemplate;

    @Autowired
    public TestService(SSLWebClient sslWebClient, Environment env, RestTemplate restTemplate) {
        this.sslWebClient = sslWebClient;
        this.env = env;
        this.restTemplate = restTemplate;
    }

    public String getData() {
         System.out.println("Got inside Client method");
        try {
            String msEndpoint = env.getProperty("endpoint.ssl-server");
            System.out.println("MS Endpoint name : [" + msEndpoint + "]");

            return restTemplate.getForObject(new URI(msEndpoint), String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Exception occurred.. so, returning default data";
    }
}
