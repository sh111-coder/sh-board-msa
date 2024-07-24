# 📘 Monolithic to MSA 전환 프로젝트

---

## 🎯 소개
기존 Monolithic 구조의 개인 프로젝트를 MSA 구조로 전환한 프로젝트입니다.
<br> 
프로젝트 자체는 도메인이 Member, Board 2개인 간단한 개인 프로젝트로,
<br>
MSA로 전환하는 경험에 초점을 맞추고 진행했습니다.
<br>
MSA로 전환하면서 전반적인 Spring Cloud 환경을 구성하여 Spring Cloud 기술을 이해하게 되었습니다.

서비스 비즈니스 기능들은 기존 모놀리식 구조에서 변경된 점이 별로 없습니다.
<br>
기존 모놀리식 구조의 애플리케이션 코드가 궁금하신 분들은 아래의 Github Repository를 참고하시면 될 것 같습니다.
<br>

[🚀 기존 모놀리식 구조의 Application Github Repository](https://github.com/sh111-coder/sh-board-monolitic)



<br>

## 🖥️ 전체 MSA Flow
<img width="1309" alt="스크린샷 2024-07-24 오전 11 50 59" src="https://github.com/user-attachments/assets/e80ab1c1-5e49-4c3c-9a88-38f2f2caf81c">

<br>
<br>


## 🛠️사용한 기술 스택
```
* JAVA & Spring
1. Spring Boot 3.2.2
2. JAVA 17
3. Spring Data JPA
4. Querydsl

* Bulid
1. Gradle

* DB
1. H2
2. MySQL
3. Redis

*Message Queue
1. Kafka

* MSA 환경
1. Spring Cloud Nexflix Eureka
2. Spring Cloud Gateway
3. Spring Cloud OpenFeign
4. Spring Cloud Config
```



<br>


## 🌈️ 상세 코드 설명 - 블로그 참고
기존의 모놀리식 구조의 애플리케이션을 MSA로 전환하는 과정들을 챕터별로 블로그에 기록했습니다.
<br>
Spring Cloud 환경의 MSA 구조가 궁금하신 분들이나 구현 코드가 궁금하신 분들은 
<br>
아래의 블로그 링크를 참고해주시면 좋을 것 같습니다 😃
<br>

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (1) MSA란?](https://ksh-coding.tistory.com/135)


[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (2) 멀티 모듈 구성하기](https://ksh-coding.tistory.com/136)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (3) Service Discovery 패턴 적용하기(feat. Spring Cloud Eureka)](https://ksh-coding.tistory.com/137)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (4) API Gateway 구현(feat. Spring Cloud Gateway)](https://ksh-coding.tistory.com/138)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (5) 서비스 간 통신하기(feat.Spring Cloud OpenFeign)](https://ksh-coding.tistory.com/139)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (6) 각 서비스의 설정 파일 관리하기(feat. Spring Cloud Config)](https://ksh-coding.tistory.com/141)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (7) 서비스 장애 대응 Circuit Breaker 구현(feat. Resilience4J)](https://ksh-coding.tistory.com/142)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (8) 분산 트랜잭션 환경에서 트랜잭션 처리하기(feat. 2PC, Saga 패턴, Choreographed Saga)](https://ksh-coding.tistory.com/143)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (9) OpenFeign 테스트 하기 (feat. Spring Cloud Contract WireMock Test)](https://ksh-coding.tistory.com/144)

[[MSA] 개인 프로젝트 Monolithic to MSA 전환기 - (10) MSA 전환 후 비교 및 회고 + 마무리](https://ksh-coding.tistory.com/145)
