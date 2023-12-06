#!/bin/bash

if [[ $GITHUB_EVENT_NAME == 'push' ]]; then
    if [[ $GITHUB_REF == 'refs/tags/*stage-health-check' ]]; then
        SPRING_PROFILES_ACTIVE=stage
    elif [[ $GITHUB_REF == 'refs/tags/*main-health-check' ]]; then
        SPRING_PROFILES_ACTIVE=prod
    else
        SPRING_PROFILES_ACTIVE=dev
    fi
fi

# 만약 해당 환경 변수가 이미 설정되어 있다면 건드리지 않음
if [ -z "$SPRING_PROFILES_ACTIVE" ]; then
    SPRING_PROFILES_ACTIVE=dev
fi

# Spring Boot 애플리케이션 실행
java -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE health-check-0.0.1-SNAPSHOT.jar
