[![Build][build-img]][build-url]

# vplan-android
> Android vplan app

## Build

### Prerequsits

- Android SDK / Studio
  - NDK
  - CMake
  - LLDB

- Rust toolchain + targets:
  - `arm-linux-androideabi`
  - `aarch64-linux-android`
  - `i686-linux-androi`
  - `x86_64-linux-android`

- corresponding NDK toolchains:
  - `arm` (min. API level 18)
  - `arm64` (min. API level 23)
  - `x86` (min. API level 18)
  - `x86_64` (min. API level 23)

### Command Line

Use the `gradlew` command line tool:

#### UNIX

```shell
./gradlew
```

#### Windows

```shell
gradlew.bat
```

Debug:
```shell
gradlew assembleDebug
```

Release:
```shell
gradlew assembleRelease
```

#### Linting

Rust (requires [`clippy`](clippy) and [`rustfmt`](rustfmt)):
```shell
cargo clippy -- --deny clippy
cargo fmt --all -- --check
```

Android:
```shell
gradlew lint
gradlew ktlint
gradlew detektCheck
```

[build-img]: https://travis-ci.com/fschillerg/vplan-android.svg?branch=master
[build-url]: https://travis-ci.com/fschillerg/vplan-android
[clippy]: https://github.com/rust-lang-nursery/rust-clippy
[rustfmt]: https://github.com/rust-lang-nursery/rustfmt
