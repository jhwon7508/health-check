# health-check
CI/CD test
[STEP 01]웹 서버 생성
* 12/4(월) 제출 마감
* Project 생성 및 Health-Check router 구현
  - Spring Boot Actuator 사용(endpoint : /actuator/health)

- 서버 스택
    - [JVM]Java Spring Boot
- Phase
  - dev : 개발 (8080)
  - stg : QA (8282)
  - prod : 운영 (8888)
 
[STEP 02]빌드 환경 구축
* 12/6(수) 마감
* Docker 파일 작성 및 GitHub Actions 작성
- 설정 버전
  -Springboot version '2.7.18'
  -Spring dependency-management version '1.1.4'
  -Gradle version 8.5
  -java version 11\
- Docker
  - Expose Port : 1111
  - 환경 변수 SPRING_PROFILES_ACTIVE 설정
- Github Actions
  - runs-on: ubuntu-latest
  - trigger : tag push시 build
- run_application.sh 작성
  - tag명 "*.main-health-check"이면, SPRING_PROFILES_ACTIVE=prod
  - tag명 "*.stage-health-check"이면, SPRING_PROFILES_ACTIVE=stg
  - SPRING_PROFILES_ACTIVE이 비어있을 경우, SPRING_PROFILES_ACTIVE=dev(local)

[STEP 03]배포 환경 구축
* 12/8(금) 마감
