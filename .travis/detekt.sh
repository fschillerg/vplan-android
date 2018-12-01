#!/bin/bash

./gradlew detekt

cat app/build/reports/detekt/detekt.xml | grep 'error' > /dev/null

if [ $? -eq 0 ]; then
    echo "[DETEKT] Issues found!" >&2
    cat app/build/reports/detekt/detekt.xml
    exit 1
fi

echo "[DETEKT] No issues found!"
