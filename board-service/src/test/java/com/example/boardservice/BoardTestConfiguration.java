package com.example.boardservice;

import com.example.common.H2TruncateUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.example.boardservice", "com.example.shboardcommon"}, basePackageClasses = {H2TruncateUtils.class})
public class BoardTestConfiguration {
}
