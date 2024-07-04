package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.AccountInfoDTO;
import com.gpbitfactory.middle.model.AccountNameDTO;
import com.gpbitfactory.middle.model.AccountRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
public class AccountService {

    private final RestClient restClient;

    @Autowired
    public AccountService(RestClient restClient) {
        this.restClient = restClient;
    }

    public int createAccount(AccountRegisterDTO accountRegisterDTO) {

        AccountNameDTO accountNameDTO = new AccountNameDTO(accountRegisterDTO.accountName());
        try {
            restClient.post()
                    .uri("/v2/users/" + accountRegisterDTO.id() + "/accounts")
                    .body(accountNameDTO)
                    .retrieve().toEntity(String.class);
            log.info("Регистрация счета для пользователя с id: " + accountRegisterDTO.id() + " прошла успешно");
            return 204;
        } catch (HttpClientErrorException e) {
            log.error("Ошибка регистрации пользователя с id: " + accountRegisterDTO.id() + ". Код ошибки: " + e.getStatusCode().value());
            return e.getStatusCode().value();
        }
    }

    public ResponseEntity<?> getBalance(Long id) {

        ResponseEntity<?> response = restClient.get()
                .uri("/v2/users/" + id + "/accounts")
                .retrieve().toEntity(new ParameterizedTypeReference<List<AccountInfoDTO>>() {
                });
        log.info("Список счетов пользователя с id " + id + "получен");
        return response;
    }
}