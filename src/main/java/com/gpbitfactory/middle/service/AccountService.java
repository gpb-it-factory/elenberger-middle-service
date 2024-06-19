package com.gpbitfactory.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountService {

    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public int createAccount(Long id) {
        try {
            postToBack(id);
            return 204;
        } catch (HttpClientErrorException e) {
            return e.getStatusCode().value();
        }
    }


    private void postToBack(Long id) {
        restTemplate.postForEntity("/v2/users/{id}/accounts", id, String.class, id);
    }
}
