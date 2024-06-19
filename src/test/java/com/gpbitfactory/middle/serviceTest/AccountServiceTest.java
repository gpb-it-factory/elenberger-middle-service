package com.gpbitfactory.middle.serviceTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.gpbitfactory.middle.config.MiddleConfig;
import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.AccountService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(classes = {MiddleConfig.class})
@WireMockTest
public class AccountServiceTest {
    private static WireMockServer wireMockServer;
    static MiddleConfig middleConfig = new MiddleConfig();
    String url = "/v2/users/100/accounts";
    AccountService accountService = middleConfig.accountService(wireMockServer.baseUrl());
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
    public void createAccountOKTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(202)));
        System.out.println(wireMockServer.baseUrl());
        Assertions.assertEquals(204, accountService.createAccount(requestDTO));

    }

    @Test
    public void createAccountConflictTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(409)));
        System.out.println(wireMockServer.baseUrl());
        Assertions.assertEquals(409, accountService.createAccount(requestDTO));

    }
}
