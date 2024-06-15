package com.gpbitfactory.middle.controller;

import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.UserService;
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

    private final UserService userService;

    @Autowired
    public MiddleController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO requestDTO) {
        if (userService.isRegistered(requestDTO.userID())) {
            return new ResponseEntity<String>("Пользователь уже зарегистрирован!", HttpStatus.CONFLICT);
        }
        if (userService.postUserToBack(requestDTO)) {
            return new ResponseEntity<String>("Пользователь " + requestDTO.userName() + " зарегистрирован",
                    HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<String>("Произошла непредвиденная ошибка!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
