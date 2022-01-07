FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-scheduler/build/libs/app.jar ./app.jar
