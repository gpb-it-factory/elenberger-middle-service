package com.gpbitfactory.middle.controller;

import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.MiddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MiddleController {

    private final MiddleService middleService;

    @Autowired
    public MiddleController(MiddleService middleService) {
        this.middleService = middleService;
    }

    @PostMapping("/id")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO requestDTO) {
        if (middleService.isRegistered(requestDTO.userID())) {
            return new ResponseEntity<String>("Пользователь уже зарегистрирован!", HttpStatus.OK);
        }
        if (middleService.userRegistration(requestDTO.userID())) {
            return new ResponseEntity<String>("Пользователь " + requestDTO.userName() + " зарегистрирован", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Произошла непредвиденная ошибка!", HttpStatus.I_AM_A_TEAPOT);
        }

    }
}
