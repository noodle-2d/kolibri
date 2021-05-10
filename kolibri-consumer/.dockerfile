FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-consumer/build/libs/kolibri-consumer-shadow-1.0.jar ./app.jar
