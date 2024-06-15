package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRegistered(@PathVariable Long id) {
        ResponseEntity<?> response;
        try {
            response = restTemplate.getForEntity("/v2/users/{id}", String.class, id);
        } catch (HttpClientErrorException e) {
            return false;
        }
        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean postUserToBack(RegisterRequestDTO requestDTO) {
        ResponseEntity<?> response;
        try {
            response = restTemplate.postForEntity("/v2/users", requestDTO, ResponseEntity.class);
        } catch (HttpClientErrorException e) {
            return false;
        }
        return response.getStatusCode().is2xxSuccessful();
    }
}
