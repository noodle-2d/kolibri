FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-monolite/build/libs/app.jar ./app.jar
