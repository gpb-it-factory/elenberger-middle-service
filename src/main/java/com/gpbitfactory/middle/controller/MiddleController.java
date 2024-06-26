package com.gpbitfactory.middle.controller;

import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.AccountService;
import com.gpbitfactory.middle.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/v1")
public class MiddleController {

    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public MiddleController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO requestDTO) {
        return switch (userService.registerUser(requestDTO)) {
            case 204 -> new ResponseEntity<>("Пользователь " + requestDTO.userName() + " зарегистрирован",
                    HttpStatus.ACCEPTED);
            case 409 -> new ResponseEntity<>("Пользователь уже зарегистрирован!", HttpStatus.CONFLICT);
            default -> new ResponseEntity<>("Произошла непредвиденная ошибка!", HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @PostMapping("/users/{id}/accounts")
    public ResponseEntity<String> createAccount(@RequestBody @PathVariable Long id) {
        return switch (accountService.createAccount(id)) {
            case 204 -> new ResponseEntity<>("Cчёт для пользователя создан", HttpStatus.CREATED);
            case 409 -> new ResponseEntity<>("Пользователь уже имеет счет в мини-банке!", HttpStatus.CONFLICT);
            default -> new ResponseEntity<>("Произошла непредвиденная ошибка!", HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<?> getBalance(@RequestBody @PathVariable Long id) {
        try {
            return accountService.getBalance(id);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>("Счет не найден!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Непредвиденная ошибка", HttpStatus.NO_CONTENT);
        }

    }
}
