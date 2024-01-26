# 베이스 이미지 선택: Java 애플리케이션을 빌드하기 위해 JDK를 포함한 이미지 사용
FROM adoptopenjdk:11-jdk-hotspot AS builder

# ARG를 사용하여 환경 변수 SPRING_PROFILES_ACTIVE 설정, ENV를 사용하여 ARG로부터 환경 변수 설정
ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENV JASYPT_ENCRYPTOR_PASSWORD=암호화키

# 이미지를 생성한 사람을 명시하는 라벨 추가
LABEL authors="alisonjung"

# 빌드 작업 디렉토리 설정
WORKDIR /build

# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함.
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 애플리케이션을 빌드하기 위해 필요한 파일들을 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# 현재 디렉토리의 모든 파일을 컨테이너의 /app 경로로 복사
COPY . /app

# 빌더 이미지에서 애플리케이션 빌드
COPY . /build

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 필요한 패키지를 설치하고 이미지를 정리
RUN apt-get update && apt-get install -y \
#    package1 \
#    package2 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# gradlew를 사용하여 실행 가능한 JAR 파일 생성
RUN ./gradlew bootJar

# 린트 체크를 수행하는 스크립트를 추가
COPY lint_check.sh /app/lint_check.sh
RUN chmod +x /app/lint_check.sh
RUN /app/lint_check.sh

# 테스트를 수행하는 스크립트를 추가
COPY test_check.sh /app/test_check.sh
RUN chmod +x /app/test_check.sh
RUN /app/test_check.sh

# 코드 커버리지 리포트를 생성하는 스크립트를 추가
COPY coverage_report.sh /app/coverage_report.sh
RUN chmod +x /app/coverage_report.sh
RUN /app/coverage_report.sh

# 새로운 단계에서 adoptopenjdk:11-jdk-hotspot 베이스 이미지를 사용
FROM adoptopenjdk:11-jdk-hotspot

# 작업 디렉토리 설정
WORKDIR /app

# 호스트 머신의 포트와 연결될 포트 설정
EXPOSE 3000

# builder stage에서 생성된 JAR 파일을 복사하여 이미지 내부로 가져옴
COPY --from=builder /build/build/libs/health-check-0.0.1-SNAPSHOT.jar /app/health-check-0.0.1-SNAPSHOT.jar

# 실행 명령어 설정 (환경 변수에 따라 Spring 프로파일을 설정하고 Java 애플리케이션 실행)
CMD ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-local}", "health-check-0.0.1-SNAPSHOT.jar"]