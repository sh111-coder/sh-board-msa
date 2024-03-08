package com.example.common;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public abstract class ServiceTest extends OpenFeignClientTest {

    @Autowired
    private H2TruncateUtils truncateUtils;

    @BeforeEach
    void setUp() {
        truncateUtils.truncateAll();
    }
}
