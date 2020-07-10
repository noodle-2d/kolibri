docker-env:
	./scripts/init-docker-env.sh

format:
	./gradlew ktlintFormat

gradle-build/telegram-bot:
	./gradlew clean kolibri-telegram-bot:build

gradle-build/commandline-utility:
	./gradlew clean kolibri-commandline-utility:build

build/telegram-bot: gradle-build/telegram-bot
	docker-compose build kolibri-telegram-bot

build/commandline-utility: gradle-build/commandline-utility
	docker-compose build kolibri-commandline-utility

run/proxy:
	docker-compose up -d proxy

run/database:
	docker-compose up -d kolibri-database

run/telegram-bot:
	docker-compose up -d kolibri-telegram-bot

run/commandline-utility/import-old-sheets:
	docker-compose run -e ACTION=import-old-sheets kolibri-commandline-utility

stop/proxy:
	docker-compose stop proxy

stop/database:
	docker-compose stop kolibri-database

stop/telegram-bot:
	docker-compose stop kolibri-telegram-bot