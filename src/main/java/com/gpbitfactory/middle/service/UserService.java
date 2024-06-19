package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int registerUser(RegisterRequestDTO requestDTO) {
        try {
            postToBack(requestDTO);
            return 204;
        } catch (HttpClientErrorException e) {
            return e.getStatusCode().value();
        }
    }

    private void postToBack(RegisterRequestDTO requestDTO) {
        restTemplate.postForEntity("/v2/users", requestDTO, ResponseEntity.class);
    }
}
