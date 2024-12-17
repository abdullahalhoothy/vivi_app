# Use the latest Ubuntu base image
FROM ubuntu:22.04

# Install required packages
RUN apt-get update && \
    apt-get install -y build-essential devscripts openjdk-17-jdk curl unzip git

# Set Android SDK environment variables
ENV ANDROID_HOME=/root/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Create the Android SDK directory structure
RUN mkdir -p $ANDROID_HOME/cmdline-tools

# Download and install Android SDK command-line tools
RUN curl -L https://dl.google.com/android/repository/commandlinetools-linux-9123335_latest.zip -o sdktools.zip && \
    unzip sdktools.zip -d $ANDROID_HOME/cmdline-tools && \
    rm sdktools.zip

# Move the cmdline-tools to `latest` if necessary
RUN mv $ANDROID_HOME/cmdline-tools/cmdline-tools $ANDROID_HOME/cmdline-tools/latest

# Accept licenses and install required SDK components
# Use the absolute path to sdkmanager to avoid PATH issues
RUN mkdir -p ~/.android && touch ~/.android/repositories.cfg && \
    yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --sdk_root=$ANDROID_HOME "platform-tools" "build-tools;33.0.0" "platforms;android-33"


# Set addit`ional environment variables
ENV CONTAINER=true \
    ANDROID_SDK_PATH=$ANDROID_HOME \
    ANDROID_SDK_ROOT=$ANDROID_HOME 

# Set the working directory
WORKDIR /root/build

# Clone the specific branch from GitHub
#RUN git clone -b main https://github.com/rashid1428/vivi_app.git /root/build
RUN git clone -b main https://github.com/abdullahalhoothy/vivi_app.git /root/build

# Set permissions for the Gradle wrapper to make it executable
RUN chmod +x /root/build/gradlew


# Additional setup commands, if any
RUN /root/build/gradlew assembleDebug --stacktrace


# Optionally, you can expose a directory to access the built APKs
VOLUME /root/build/app/build/outputs/apk/debug
