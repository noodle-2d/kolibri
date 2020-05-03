#!/bin/bash

case $1 in
  "telegram-bot")
    docker-compose up -d kolibri-telegram-bot ;;
  "commandline-utility")
    docker-compose run -e ACTION=$2 kolibri-commandline-utility ;;
esac
