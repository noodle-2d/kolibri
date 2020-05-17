#!/bin/bash

env_file_name=".env"
telegram_bot_env_file="kolibri-telegram-bot/${env_file_name}"
commandline_utility_env_file="kolibri-commandline-utility/${env_file_name}"

recreate_file() {
  filename=$1
  if [[ -f "$filename" ]]; then
    echo "File $filename exists, delete it? (yes/no)"
    read answer
    if [[ ${answer} == "yes" ]]; then
      rm ${filename}
    else
      echo "Stopped initializing environment"
      exit
    fi
  fi
  touch ${filename}
}

write_telegram_properties() {
  filename=$1
  echo "TELEGRAM_BOT_API_URL=https://api.telegram.org" >> ${filename}
  echo "TELEGRAM_BOT_TOKEN=" >> ${filename}
  echo "TELEGRAM_BOT_URL=" >> ${filename}
  echo "TELEGRAM_BOT_OWNER_ID=" >> ${filename}
}

init_telegram_bot_env() {
  recreate_file ${telegram_bot_env_file}
  echo "PORT=8080" >> ${telegram_bot_env_file}
  write_telegram_properties ${telegram_bot_env_file}
  echo "Initialized telegram bot environment: ${telegram_bot_env_file}"
}

init_commandline_utility_env() {
  recreate_file ${commandline_utility_env_file}
  write_telegram_properties ${commandline_utility_env_file}
  echo "Initialized command line utility environment: ${commandline_utility_env_file}"
}

init_telegram_bot_env
init_commandline_utility_env
