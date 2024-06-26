package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.AccountInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

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

    public ResponseEntity<?> getBalance(Long id) {
        ResponseEntity<List<AccountInfoDTO>> response = restClient.get()
                .uri("/v2/users/" + id + "/accounts")
                .retrieve().toEntity(new ParameterizedTypeReference<List<AccountInfoDTO>>() {
                });

        return response;
    }
}