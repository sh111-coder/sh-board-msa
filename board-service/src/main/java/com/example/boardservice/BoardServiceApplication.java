package com.example.boardservice;

import com.example.boardservice.board.domain.Board;
import com.example.boardservice.board.domain.BoardRepository;
import com.example.common.H2TruncateUtils;
import com.example.memberservice.member.domain.Member;
import com.example.memberservice.member.domain.MemberRepository;
import com.example.shboardcommon.global.BaseEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan(basePackageClasses = {Member.class, Board.class, BaseEntity.class})
@EnableJpaRepositories(basePackageClasses = {MemberRepository.class, BoardRepository.class})
public class BoardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardServiceApplication.class, args);
    }

}
