package com.shboard.shboard.global.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

@Slf4j
@Profile("test")
@Configuration
public class EmbeddedRedisConfig {
    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;

        // 현재 시스템이 ARM 아키텍처인지 확인
        if (isArmArchitecture()) {
            // ARM 아키텍처가 맞다면, RedisServer 클래스를 사용하여 Redis 서버를 생성
            redisServer = new RedisServer(Objects.requireNonNull(getRedisServerExecutable()), port);
        } else {
            // ARM 아키텍처가 아니라면, RedisServer.builder()를 사용하여 Redis 서버를 생성
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory 128M")
                    .build();
        }

        redisServer.start();
    }

    // Arm 아키텍처가 맞는지 확인하는 메서드
    private boolean isArmArchitecture() {
        return System.getProperty("os.arch").contains("aarch64");
    }

    private File getRedisServerExecutable() {
        try {
            return new File("src/main/resources/binary/redis/redis-server-7.2.3-mac-arm64");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }



    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }


    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    public int findAvailablePort() throws IOException {

        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int port) throws IOException {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            log.info("OS is  " + OS + " " + port);
            String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
            String[] shell = {"cmd.exe", "/y", "/c", command};
            return Runtime.getRuntime().exec(shell);
        }
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return !StringUtils.isEmpty(pidInfo.toString());
    }
}
