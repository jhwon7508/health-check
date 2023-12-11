#!/bin/bash

echo "Generating coverage report..."

# Jacoco를 사용하여 코드 커버리지 리포트 생성
./gradlew jacocoTestReport

echo "Coverage report generated."
