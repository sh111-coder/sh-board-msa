package com.example.memberservice;

import com.example.common.H2TruncateUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"com.example.memberservice", "com.example.shboardcommon"}, scanBasePackageClasses = {H2TruncateUtils.class})
public class MemberServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }

}
