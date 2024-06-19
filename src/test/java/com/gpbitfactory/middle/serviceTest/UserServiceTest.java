package com.gpbitfactory.middle.serviceTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.gpbitfactory.middle.config.MiddleConfig;
import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(classes = {MiddleConfig.class})
@WireMockTest
public class UserServiceTest {
    private static WireMockServer wireMockServer;
    static MiddleConfig middleConfig = new MiddleConfig();
    String url = "/v2/users";
    UserService userService = middleConfig.userService(wireMockServer.baseUrl());
    RegisterRequestDTO requestDTO = new RegisterRequestDTO(100L, "ABOBA");

    @BeforeAll
    public static void setUp() {
        WireMockConfiguration configuration = wireMockConfig().port(9999);
        wireMockServer = new WireMockServer(configuration);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void registerUserOKTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(202)));
        Assertions.assertEquals(204, userService.registerUser(requestDTO));

    }

    @Test
    public void registerUserConflictTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(409)));
        Assertions.assertEquals(409, userService.registerUser(requestDTO));

    }
}
