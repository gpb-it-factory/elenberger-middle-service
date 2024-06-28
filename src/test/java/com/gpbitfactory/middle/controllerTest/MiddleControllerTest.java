package com.gpbitfactory.middle.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpbitfactory.middle.config.MiddleConfig;
import com.gpbitfactory.middle.controller.MiddleController;
import com.gpbitfactory.middle.model.AccountRegisterDTO;
import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.AccountService;
import com.gpbitfactory.middle.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MiddleController.class)
@Import(MiddleConfig.class)
public class MiddleControllerTest {
    @MockBean
    UserService userService;
    @MockBean
    AccountService accountService;
    @Autowired
    private MockMvc mockMvc;
    RegisterRequestDTO requestDTO = new RegisterRequestDTO(10L, "ABOBA");
    AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO(10L, "name");

    @Test
    void registerUserOKTest() throws Exception {
        when(userService.registerUser(requestDTO)).thenReturn(204);
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("Пользователь " + requestDTO.userName() +
                        " зарегистрирован")));
    }

    @Test
    void registerUserConflictTest() throws Exception {
        when(userService.registerUser(requestDTO)).thenReturn(409);
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Пользователь уже зарегистрирован!")));
    }

    @Test
    void registerUserErrorTest() throws Exception {
        when(userService.registerUser(requestDTO)).thenReturn(500);
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Произошла непредвиденная ошибка!")));
    }

    @Test
    void createAccountOKTest() throws Exception {
        when(accountService.createAccount(accountRegisterDTO)).thenReturn(204);
        mockMvc.perform(post("/api/v1/users/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountRegisterDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Cчёт для пользователя создан")));
    }

    @Test
    void createAccountConflictTest() throws Exception {
        when(accountService.createAccount(accountRegisterDTO)).thenReturn(409);
        mockMvc.perform(post("/api/v1/users/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountRegisterDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Пользователь уже имеет счет в мини-банке!")));
    }

    @Test
    void createAccountErrorTest() throws Exception {
        when(accountService.createAccount(accountRegisterDTO)).thenReturn(500);
        mockMvc.perform(post("/api/v1/users/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(accountRegisterDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Произошла непредвиденная ошибка!")));
    }

    @Test
    void getBalanceOk() throws Exception {
        when(accountService.getBalance(requestDTO.userID()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        mockMvc.perform(get("/api/v1/users/10/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getBalanceHttpError() throws Exception {
        when(accountService.getBalance(requestDTO.userID()))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/api/v1/users/10/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isNotFound());
    }


    @Test
    void getBalanceError() throws Exception {
        when(accountService.getBalance(requestDTO.userID()))
                .thenThrow(new JsonParseException());
        mockMvc.perform(get("/api/v1/users/10/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isNoContent());
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
