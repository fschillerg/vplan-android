#!/bin/bash

echo y | sdkmanager 'ndk-bundle'
echo y | sdkmanager 'cmake;3.6.4111459'
echo y | sdkmanager 'lldb;3.1'

curl -Lo rustup https://sh.rustup.rs
sh rustup -y --default-toolchain $RUST
source $HOME/.cargo/env

rustup target add arm-linux-androideabi
rustup target add aarch64-linux-android
rustup target add i686-linux-android
rustup target add x86_64-linux-android

mkdir ${HOME}/.ndk
${ANDROID_HOME}/ndk-bundle/build/tools/make_standalone_toolchain.py --api 18 --arch arm --install-dir ${HOME}/.ndk/arm
${ANDROID_HOME}/ndk-bundle/build/tools/make_standalone_toolchain.py --api 23 --arch arm64 --install-dir ${HOME}/.ndk/arm64
${ANDROID_HOME}/ndk-bundle/build/tools/make_standalone_toolchain.py --api 18 --arch x86 --install-dir ${HOME}/.ndk/x86
${ANDROID_HOME}/ndk-bundle/build/tools/make_standalone_toolchain.py --api 23 --arch x86_64 --install-dir ${HOME}/.ndk/x86_64

rm -f ${HOME}/.cargo/config && mv .travis/cargo.toml ${HOME}/.cargo/config
