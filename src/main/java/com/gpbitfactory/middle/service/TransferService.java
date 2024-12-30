package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.TransferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
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
            log.error("У пользователя " + transferDTO.from() + " нет зарегистрированного счета!");
            return new ResponseEntity<>("У вас нет счета!", HttpStatus.NOT_FOUND);
        }
        try {
            restClient.get().uri("/v2/users/" + transferDTO.to())
                    .retrieve().toEntity(String.class);
        } catch (HttpClientErrorException f) {
            log.error("У пользователя " + transferDTO.to() + " нет зарегистрированного счета!");
            return new ResponseEntity<>("У адресата нет счета!", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<?> response = restClient.post()
                .uri("/v2/transfers")
                .body(transferDTO)
                .retrieve().toEntity(String.class);
        log.info("Перевод на сумму " + transferDTO.amount()
                + ", от пользователя " + transferDTO.from()
                + ", для пользователя " + transferDTO.to()
                + "выполнен успешно!");
        return response;
    }

}
