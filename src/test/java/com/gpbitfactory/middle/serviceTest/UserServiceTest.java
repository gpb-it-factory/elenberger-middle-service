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
    public void isRegisteredTrueTest() {
        wireMockServer.stubFor(get(urlEqualTo(url + "/100")).willReturn(aResponse().withStatus(204)));
        Assertions.assertTrue(userService.isRegistered(100L));
    }

    @Test
    public void isRegisteredFalseTest() {
        wireMockServer.stubFor(get(urlEqualTo(url + "/100")).willReturn(aResponse().withStatus(404)));
        Assertions.assertFalse(userService.isRegistered(100L));
    }

    @Test
    public void postUserToBankTrueTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(204).withBody("AMOGUS")));
        Assertions.assertTrue(userService.postUserToBack(new RegisterRequestDTO(100L, "ABOBA")));
    }

    @Test
    public void postUserToBankFalseTest() {
        wireMockServer.stubFor(post(urlEqualTo(url)).willReturn(aResponse().withStatus(404).withBody("AMOGUS")));
        Assertions.assertFalse(userService.postUserToBack(new RegisterRequestDTO(100L, "ABOBA")));
    }
}
