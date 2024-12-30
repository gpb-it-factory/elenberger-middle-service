package com.gpbitfactory.middle.serviceTest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.gpbitfactory.middle.config.MiddleConfig;
import com.gpbitfactory.middle.model.TransferDTO;
import com.gpbitfactory.middle.service.TransferService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(classes = {MiddleConfig.class})
@WireMockTest
public class TransferServiceTest {
    private static WireMockServer wireMockServer;
    static MiddleConfig middleConfig = new MiddleConfig();
    String postUrl = "/v2/transfers";
    String getUrl = "/v2/users/";
    TransferService transferService = middleConfig.transferService(wireMockServer.baseUrl());
    TransferDTO transferDTO = new TransferDTO("OMEMA", "ABOBA", "200.50");

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
    public void makeTransferTestOk() {
        wireMockServer.stubFor(get(urlEqualTo(getUrl + transferDTO.from()))
                .willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(get(urlEqualTo(getUrl + transferDTO.to()))
                .willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(post(urlEqualTo(postUrl))
                .willReturn(aResponse().withStatus(200).withBody("Денежку перевел!")));

        ResponseEntity<?> response = transferService.makeTransfer(transferDTO);
        int responseCode = response.getStatusCode().value();

        Assertions.assertEquals(200, responseCode);
    }

    @Test
    public void makeTransferIncorrectSender() {
        wireMockServer.stubFor(get(urlEqualTo(getUrl + transferDTO.from()))
                .willReturn(aResponse().withStatus(404).withBody("У вас нет счета!")));
        ResponseEntity<String> responseExpected = new ResponseEntity<>("У вас нет счета!",
                HttpStatusCode.valueOf(404));

        ResponseEntity<?> response = transferService.makeTransfer(transferDTO);

        Assertions.assertEquals(responseExpected, response);
    }

    @Test
    public void makeTransferIncorrectDestination() {
        wireMockServer.stubFor(get(urlEqualTo(getUrl + transferDTO.from()))
                .willReturn(aResponse().withStatus(200)));
        wireMockServer.stubFor(get(urlEqualTo(getUrl + transferDTO.to()))
                .willReturn(aResponse().withStatus(404).withBody("У адресата нет счета!")));
        ResponseEntity<String> responseExpected = new ResponseEntity<>("У адресата нет счета!",
                HttpStatusCode.valueOf(404));

        ResponseEntity<?> response = transferService.makeTransfer(transferDTO);

        Assertions.assertEquals(responseExpected, response);
    }
}
