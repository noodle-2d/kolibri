FROM openjdk:8

WORKDIR /usr/app

COPY kolibri-commandline-utility/build/libs/kolibri-commandline-utility-shadow-1.0.jar ./app.jar
