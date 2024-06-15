package com.gpbitfactory.middle.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gpbitfactory.middle.config.MiddleConfig;
import com.gpbitfactory.middle.controller.MiddleController;
import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MiddleController.class)
@Import(MiddleConfig.class)
public class MiddleControllerTest {
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerUserTest() throws Exception {
        RegisterRequestDTO requestDTO = new RegisterRequestDTO(10L, "ABOBA");
        when(userService.isRegistered(requestDTO.userID())).thenReturn(false);
        when(userService.postUserToBack(requestDTO)).thenReturn(true);
        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("Пользователь " + requestDTO.userName() +
                        " зарегистрирован")));
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
