# 베이스 이미지 Amazon Corretto 17로 설정 (애플리케이션 Java Vendor)
FROM amazoncorretto:17

# 작업 디렉토리 설정 (원하는 경로로 수정 가능)
WORKDIR /shboard

# 변수 설정
ARG JAR_FILE=./build/libs/*.jar

# 빌드한 JAR 파일 원하는 경로에 복사
COPY ${JAR_FILE} /shboard/shboard.jar

# 어플리케이션 실행 명령어를 지정합니다.
ENTRYPOINT ["java", "-jar", "shboard.jar", "--spring.profiles.active=prod"]
