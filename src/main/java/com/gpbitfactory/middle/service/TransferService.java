package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class TransferService {

    private final RestClient restClient;

    @Autowired
    public TransferService(RestClient restClient) {
        this.restClient = restClient;
    }

    public ResponseEntity<?> makeTransfer(TransferDTO transferDTO) {
        try {
            restClient.get()
                    .uri("/v2/users/" + transferDTO.from())
                    .retrieve().toEntity(String.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>("У вас нет счета!", HttpStatus.NOT_FOUND);
        }
        try {
            restClient.get().uri("/v2/users/" + transferDTO.to())
                    .retrieve().toEntity(String.class);
        } catch (HttpClientErrorException f) {
            return new ResponseEntity<>("У адресата нет счета!", HttpStatus.NOT_FOUND);
        }
        return restClient.post()
                .uri("/v2/transfers")
                .body(transferDTO)
                .retrieve().toEntity(String.class);
    }

}
