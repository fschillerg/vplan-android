#!/bin/bash

rm -rf dist
mkdir dist

cargo clean
./gradlew clean

./gradlew assembleRelease
zipalign 4 app/build/outputs/apk/release/app-release-unsigned.apk dist/vplan.apk
apksigner sign --ks-pass pass:$KEYSTORE_PASSWORD --key-pass pass:$KEYSTORE_PASSWORD --ks .travis/keystore.jks dist/vplan.apk
