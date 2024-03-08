package com.example.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "member.find.feign-endpoint=http://localhost:${wiremock.server.port}"
})
public abstract class OpenFeignClientTest {

    @Autowired
    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        wireMockServer.stop();
        wireMockServer.start();
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }
}
