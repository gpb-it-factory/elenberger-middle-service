package com.gpbitfactory.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Service
public class MiddleService {

    private final RestTemplate restTemplate;

    @Autowired
    public MiddleService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRegistered(@PathVariable Long id) {
        ResponseEntity<String> response = restTemplate.postForEntity("/users/{id}", id, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean userRegistration(Long id) {
        ResponseEntity<?> response = restTemplate.postForEntity("/users", id, ResponseEntity.class);
        return response.getStatusCode().is2xxSuccessful();
    }
}
