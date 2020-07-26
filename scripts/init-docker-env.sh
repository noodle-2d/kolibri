#!/bin/bash

env_file_name=".env"
docker_compose_env_file="${env_file_name}"
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

init_docker_compose_env() {
  recreate_file ${docker_compose_env_file}
  echo "TELEGRAM_BOT_INTERNAL_PORT=8080" >> ${docker_compose_env_file}
  echo "TELEGRAM_BOT_HTTP_PORT=80" >> ${docker_compose_env_file}
  echo "TELEGRAM_BOT_HTTPS_PORT=443" >> ${docker_compose_env_file}
  echo "TELEGRAM_PROXY_PORT=81" >> ${docker_compose_env_file}
  echo "POSTGRES_PORT=5432" >> ${docker_compose_env_file}
  echo "POSTGRES_DB=kolibri" >> ${docker_compose_env_file}
  echo "POSTGRES_USER=kolibri" >> ${docker_compose_env_file}
  echo "POSTGRES_PASSWORD=" >> ${docker_compose_env_file}
  echo "Initialized common environment: ${docker_compose_env_file}"
}

init_telegram_bot_env() {
  recreate_file ${telegram_bot_env_file}
  echo "LOG_FOLDER=/var/log" >> ${telegram_bot_env_file}
  echo "MODULE_NAME=kolibri-telegram-bot" >> ${telegram_bot_env_file}
  echo >> ${telegram_bot_env_file}
  echo "PORT=8080" >> ${telegram_bot_env_file}
  echo "TELEGRAM_BOT_API_URL=https://api.telegram.org" >> ${telegram_bot_env_file}
  echo "TELEGRAM_BOT_TOKEN=" >> ${telegram_bot_env_file}
  echo "TELEGRAM_BOT_URL=" >> ${telegram_bot_env_file}
  echo "TELEGRAM_BOT_OWNER_ID=" >> ${telegram_bot_env_file}
  echo "Initialized telegram bot environment: ${telegram_bot_env_file}"
}

init_commandline_utility_env() {
  recreate_file ${commandline_utility_env_file}
  echo "LOG_FOLDER=/var/log" >> ${commandline_utility_env_file}
  echo "MODULE_NAME=kolibri-commandline-utility" >> ${commandline_utility_env_file}
  echo >> ${commandline_utility_env_file}
  echo "ACCOUNTS_SPREADSHEET_ID=" >> ${commandline_utility_env_file}
  echo >> ${commandline_utility_env_file}
  echo "DATABASE_URL=jdbc:postgresql://kolibri-database:5432/kolibri" >> ${commandline_utility_env_file}
  echo "DATABASE_USER=" >> ${commandline_utility_env_file}
  echo "DATABASE_PASSWORD=" >> ${commandline_utility_env_file}
  echo >> ${commandline_utility_env_file}
  echo "TELEGRAM_BOT_CLIENT_URL=http://kolibri-telegram-bot:8080" >> ${commandline_utility_env_file}
  echo "Initialized command line utility environment: ${commandline_utility_env_file}"
}

init_docker_compose_env
init_telegram_bot_env
init_commandline_utility_env
