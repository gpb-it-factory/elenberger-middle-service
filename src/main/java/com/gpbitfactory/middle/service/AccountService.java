package com.gpbitfactory.middle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class AccountService {

    private final RestClient restClient;

    @Autowired
    public AccountService(RestClient restClient) {
        this.restClient = restClient;
    }

    public int createAccount(Long id) {
        try {
            restClient.post()
                    .uri("/v2/users/" + id + "/accounts")
                    .body(id)
                    .retrieve().toEntity(String.class);
            return 204;
        } catch (HttpClientErrorException e) {
            return e.getStatusCode().value();
        }
    }
}
