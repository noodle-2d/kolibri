#!/bin/bash

case $1 in
  "telegram-bot")
    java -jar kolibri-telegram-bot/build/libs/kolibri-telegram-bot-shadow-1.0.jar ;;
  "commandline-utility")
    java -jar kolibri-commandline-utility/build/libs/kolibri-commandline-utility-shadow-1.0.jar action=${ACTION} ;;
esac
