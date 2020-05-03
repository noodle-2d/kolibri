#!/bin/bash

case $1 in
  "telegram-bot")
    docker-compose stop kolibri-telegram-bot ;;
  "commandline-utility")
    docker-compose stop kolibri-commandline-utility ;;
esac
