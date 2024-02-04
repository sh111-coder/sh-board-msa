package com.example.boardservice;

import com.example.common.H2TruncateUtils;
import com.example.memberservice.member.domain.Member;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.example.boardservice", "com.example.memberservice", "com.example.shboardcommon"}, basePackageClasses = {H2TruncateUtils.class, Member.class})
public class BoardTestConfiguration {
}
