#!/bin/bash

case $1 in
  "telegram-bot")
    ./gradlew clean kolibri-telegram-bot:build
    docker-compose build kolibri-telegram-bot ;;
  "commandline-utility")
    ./gradlew clean kolibri-commandline-utility:build
    docker-compose build kolibri-commandline-utility ;;
esac
