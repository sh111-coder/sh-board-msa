package com.example.common;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.TestPropertySource;

// @AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "member.find.feign-endpoint=http://localhost:${wiremock.server.port}"
})
public abstract class OpenFeignClientTest {

    private static final int RANDOM_PORT = 0;

    /**
     * @Autowired
     * private WireMockServer wireMockServer;
     */
    private static WireMockServer wireMockServer = new WireMockServer(RANDOM_PORT);

    @BeforeEach
    void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }
}
