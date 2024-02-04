package com.example.memberservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = MemberTestConfiguration.class)
class MemberServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
