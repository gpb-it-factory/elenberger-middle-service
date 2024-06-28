package com.gpbitfactory.middle.service;

import com.gpbitfactory.middle.model.RegisterRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class UserService {

    private final RestClient restClient;

    @Autowired
    public UserService(RestClient restClient) {
        this.restClient = restClient;
    }

    public int registerUser(RegisterRequestDTO requestDTO) {
        try {
            restClient.post()
                    .uri("/v2/users")
                    .body(requestDTO)
                    .retrieve().toEntity(ResponseEntity.class);
            log.info("Регистрация пользователя " + requestDTO.userName() + " прошла успешно!");
            return 204;
        } catch (HttpClientErrorException e) {
            log.error("Произошла ошибка регистрации пользователя " + requestDTO.userName() + ". Код ошибки: " + e.getStatusCode().value());
            return e.getStatusCode().value();
        }
    }
}
