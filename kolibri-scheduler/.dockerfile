FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-scheduler/build/libs/kolibri-scheduler-shadow-1.0.jar ./app.jar
COPY google/kolibri-tokens.json ./google/kolibri-tokens.json
