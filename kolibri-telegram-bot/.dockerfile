FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-telegram-bot/build/libs/kolibri-telegram-bot-shadow-1.0.jar ./app.jar
