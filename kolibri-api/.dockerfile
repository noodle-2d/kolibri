FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-api/build/libs/app.jar ./app.jar
