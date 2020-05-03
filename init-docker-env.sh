#!/bin/bash

telegram_bot="telegram-bot"
commandline_utility="commandline-utility"
all="all"
possible_envs=(${telegram_bot} ${commandline_utility} ${all})

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

init_telegram_bot_env() {
  recreate_file ${telegram_bot_env_file}
  echo "PORT=8080" >> ${telegram_bot_env_file}
  echo "TELEGRAM_API_URL=https://api.telegram.org" >> ${telegram_bot_env_file}
  echo "TELEGRAM_API_TOKEN=" >> ${telegram_bot_env_file}
  echo "TELEGRAM_BOT_URL=" >> ${telegram_bot_env_file}
  echo "TELEGRAM_BOT_OWNER_ID=" >> ${telegram_bot_env_file}
  echo "Initialized telegram bot environment: ${telegram_bot_env_file}"
}

init_commandline_utility_env() {
  recreate_file ${commandline_utility_env_file}
  echo "Initialized command line utility environment: ${commandline_utility_env_file}"
}

print_help() {
  echo "Usage: ./init-docker-env.sh <env1> <env2>"
  echo "Possible env arguments: ${possible_envs[*]}"
  exit
}

if [[ $# -eq 0 ]]; then
  print_help
fi

params=( "$@" )
params_count=$#

for param in ${params[@]}; do
  if [[ ! ${possible_envs[@]} =~ ${param} ]]; then
    print_help
  fi
done

envs_to_init=()

for possible_env in ${possible_envs[@]}; do
  if [[ ${possible_env} != ${all} && ( ${params[@]} =~ ${all} || ${params[@]} =~ ${possible_env} ) ]]; then
    envs_to_init+=(${possible_env})
  fi
done

for env_to_init in ${envs_to_init[@]}; do
  case ${env_to_init} in
    ${telegram_bot})
      init_telegram_bot_env ;;
    ${commandline_utility})
      init_commandline_utility_env ;;
  esac
done
