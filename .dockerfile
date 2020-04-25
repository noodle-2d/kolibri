FROM openjdk:8

WORKDIR /usr/app

COPY gradle ./gradle
COPY gradlew ./

RUN ./gradlew

COPY start.sh ./
COPY *.gradle.kts ./
COPY kolibri-common ./kolibri-common
COPY kolibri-commandline-utility ./kolibri-commandline-utility
COPY kolibri-telegram-bot ./kolibri-telegram-bot

RUN ./gradlew clean build
