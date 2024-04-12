package com.journal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class TokenValidationService {

    @Autowired
    private RestTemplate restTemplate;

    public boolean validateToken(String token, String role) {
        String requestBody = "{\"token\": \"" + token + "\", \"role\": \"" + role + "\"}";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request entity with body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request to the validation endpoint
        String validateTokenUrl = "http://localhost:8181/auth/validateToken";
        URI uri = URI.create(validateTokenUrl);
        ResponseEntity<Boolean> response = restTemplate.postForEntity(uri, requestEntity, Boolean.class);

        // Check response status code and body
        return response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody();
    }

}
