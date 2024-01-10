#!/bin/bash

echo "Running lint check..."

# Checkstyle을 사용하여 lint 체크 수행
checkstyle -c /path/to/checkstyle.xml /path/to/source/code

echo "Lint check completed."