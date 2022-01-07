FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-consumer/build/libs/app.jar ./app.jar
