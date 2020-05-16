#!/bin/bash

# todo: this script should be removed from the project

mkdir certificates

openssl genrsa -out certificates/rootCA.key 2048
openssl req -x509 -new -key certificates/rootCA.key -days 10000 -out certificates/rootCA.crt

openssl genrsa -out certificates/telegram-bot.key 2048
openssl req -new -key certificates/telegram-bot.key -out certificates/telegram-bot.csr
openssl x509 -req -in certificates/telegram-bot.csr -CA certificates/rootCA.crt \
    -CAkey certificates/rootCA.key -CAcreateserial -out certificates/telegram-bot.crt -days 5000

cp certificates/telegram-bot.key proxy/ssl.key
cp certificates/telegram-bot.crt proxy/ssl.crt
