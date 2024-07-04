package com.gpbitfactory.middle.controller;

import com.gpbitfactory.middle.model.AccountRegisterDTO;
import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.model.TransferDTO;
import com.gpbitfactory.middle.service.AccountService;
import com.gpbitfactory.middle.service.TransferService;
import com.gpbitfactory.middle.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class MiddleController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransferService transferService;

    @Autowired
    public MiddleController(UserService userService, AccountService accountService, TransferService transferService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transferService = transferService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO requestDTO) {
        log.info("Получен запрос регистрации пользователя: " + requestDTO.userName());
        return switch (userService.registerUser(requestDTO)) {
            case 204 -> new ResponseEntity<>("Пользователь " + requestDTO.userName() + " зарегистрирован",
                    HttpStatus.ACCEPTED);
            case 409 -> new ResponseEntity<>("Пользователь уже зарегистрирован!", HttpStatus.CONFLICT);
            default -> new ResponseEntity<>("Произошла непредвиденная ошибка!", HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @PostMapping("/users/accounts")
    public ResponseEntity<String> createAccount(@RequestBody AccountRegisterDTO accountRegisterDTO) {
        log.info("Получен запрос регистрации счета пользователя с Id: " + accountRegisterDTO.id());
        return switch (accountService.createAccount(accountRegisterDTO)) {
            case 204 -> new ResponseEntity<>("Cчёт для пользователя создан", HttpStatus.CREATED);
            case 409 -> new ResponseEntity<>("Пользователь уже имеет счет в мини-банке!", HttpStatus.CONFLICT);
            default -> new ResponseEntity<>("Произошла непредвиденная ошибка!", HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }

    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<?> getBalance(@RequestBody @PathVariable Long id) {
        log.info("Получен запрос просмотра списка счетов пользователя с id: " + id);
        try {
            return accountService.getBalance(id);
        } catch (HttpClientErrorException e) {
            log.error("Произошла ошибка! Счета не обнаружены!");
            return new ResponseEntity<>("Счет не найден!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Произошла непредвиденная ошибка!");
            return new ResponseEntity<>("Непредвиденная ошибка", HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping("/transfers")
    public ResponseEntity<?> makeTransfer(@RequestBody TransferDTO transferDTO) {
        log.info("Получен запрос перевода на сумму " + transferDTO.amount()
                + ", от пользователя " + transferDTO.from() + ", для пользователя " + transferDTO.to());
        try {
            return transferService.makeTransfer(transferDTO);
        } catch (Exception e) {
            log.error("Произошла непредвиденная ошибка!");
            return new ResponseEntity<>("Непредвиденная ошибка!", HttpStatus.BAD_REQUEST);
        }
    }

}
