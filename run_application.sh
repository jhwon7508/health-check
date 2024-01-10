#!/bin/bash

if [[ $GITHUB_EVENT_NAME == 'pull_request' ]]; then
    if [[ $GITHUB_REF == 'refs/tags/*.dev-health-check' ]]; then
          SPRING_PROFILES_ACTIVE=dev
    elif [[ $GITHUB_REF == 'refs/tags/*.stg-health-check' ]]; then
        SPRING_PROFILES_ACTIVE=stage
    elif [[ $GITHUB_REF == 'refs/tags/*.prod-health-check' ]]; then
        SPRING_PROFILES_ACTIVE=prod
    else
        SPRING_PROFILES_ACTIVE=local
    fi
fi

# 만약 해당 환경 변수가 이미 설정되어 있다면 건드리지 않음
if [ -z "$SPRING_PROFILES_ACTIVE" ]; then
    SPRING_PROFILES_ACTIVE=local
fi

# Spring Boot 애플리케이션 실행
java -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE health-check-0.0.1-SNAPSHOT.jar