docker-env:
	./scripts/init-docker-env.sh

format:
	./gradlew ktlintFormat

gradle-build/telegram-bot:
	./gradlew clean kolibri-telegram-bot:build

gradle-build/commandline-utility:
	./gradlew clean kolibri-commandline-utility:build

gradle-build/scheduler:
	./gradlew clean kolibri-scheduler:build

build/telegram-bot: gradle-build/telegram-bot
	docker-compose build kolibri-telegram-bot

build/commandline-utility: gradle-build/commandline-utility
	docker-compose build kolibri-commandline-utility

build/scheduler: gradle-build/scheduler
	docker-compose build kolibri-scheduler

run/proxy:
	docker-compose up -d proxy

run/database:
	docker-compose up -d kolibri-database

run/telegram-bot:
	docker-compose up -d kolibri-telegram-bot

run/commandline-utility/import-old-sheets:
	docker-compose run -e ACTION=import-old-sheets kolibri-commandline-utility

run/scheduler:
	docker-compose up -d kolibri-scheduler

stop/proxy:
	docker-compose stop proxy

stop/database:
	docker-compose stop kolibri-database

stop/telegram-bot:
	docker-compose stop kolibri-telegram-bot

stop/scheduler:
	docker-compose stop kolibri-scheduler
