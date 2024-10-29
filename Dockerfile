# Use an Ubuntu base image
FROM ubuntu:20.04

# Install dependencies
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    openjdk-17-jdk \
    && apt-get clean

# Set the Android SDK version
ENV ANDROID_SDK_VERSION=commandlinetools-linux-8512546_latest.zip

# Download and install the Android command line tools
RUN wget https://dl.google.com/android/repository/$ANDROID_SDK_VERSION -O android-sdk.zip && \
    mkdir -p /android-sdk/cmdline-tools && \
    unzip android-sdk.zip -d /android-sdk/cmdline-tools && \
    rm android-sdk.zip

# Set the environment variables for Android SDK
ENV ANDROID_SDK_ROOT=/android-sdk
ENV PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools/bin

# Accept the Android SDK licenses
RUN yes | sdkmanager --licenses

# Install required SDK packages (adjust based on your project's needs)
RUN sdkmanager "platform-tools" "platforms;android-33" "build-tools;33.0.0"

# Copy your Android project files into the Docker image
COPY . /app
WORKDIR /app

# Build the debug version of your Android application
RUN ./gradlew assembleDebug --stacktrace --info


# Optionally, you can expose a directory to access the built APKs
VOLUME /app/app/build/outputs/apk/debug
