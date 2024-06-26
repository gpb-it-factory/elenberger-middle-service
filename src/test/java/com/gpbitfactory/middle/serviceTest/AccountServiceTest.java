package com.gpbitfactory.middle.serviceTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.gpbitfactory.middle.config.MiddleConfig;
import com.gpbitfactory.middle.model.AccountInfoDTO;
import com.gpbitfactory.middle.model.RegisterRequestDTO;
import com.gpbitfactory.middle.service.AccountService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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

    String jsonResponse = """
            [
              {
                "accountId": "52d2ef91-0b62-4d43-bb56-e7ec542ba8f8",
                "accountName": "Деньги на шашлык",
                "amount": "3228"
              }
            ]""";


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

        int responseCode = accountService.createAccount(requestDTO.userID());

        Assertions.assertEquals(204, responseCode);
    }

    @Test
    public void createAccountConflictTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(409)));

        int responseCode = accountService.createAccount(requestDTO.userID());

        Assertions.assertEquals(409, responseCode);
    }

    @Test
    public void getBalanceTest() {
        AccountInfoDTO expectedBody = new AccountInfoDTO("52d2ef91-0b62-4d43-bb56-e7ec542ba8f8",
                "Деньги на шашлык", "3228");
        List<AccountInfoDTO> list = new ArrayList<>();
        list.add(expectedBody);
        wireMockServer.stubFor(get(urlEqualTo(url)).willReturn(aResponse()
                .withStatus(200)
                .withBody(jsonResponse)
                .withHeader("Content-Type", "application/json")));

        ResponseEntity<?> responseReceived = accountService.getBalance(requestDTO.userID());

        Assertions.assertEquals(list, responseReceived.getBody());
    }
}
