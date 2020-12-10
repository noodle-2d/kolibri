FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-api/build/libs/kolibri-api-shadow-1.0.jar ./app.jar
