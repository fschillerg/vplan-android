#!/bin/bash

./gradlew lint

cat app/build/reports/lint-results.xml | grep '</issue>' > /dev/null

if [ $? -eq 0 ]; then
    echo "[LINT] Issues found!" >&2
    cat app/build/reports/lint-results.xml
    exit 1
fi

echo "[LINT] No issues found!"
