docker-env:
	./scripts/init-docker-env.sh

format:
	./gradlew ktlintFormat

gradle-build/api:
	./gradlew clean kolibri-api:build

gradle-build/scheduler:
	./gradlew clean kolibri-scheduler:build

list-docker-services:
	docker-compose ps

build/api: gradle-build/api
	docker-compose build kolibri-api

build/scheduler: gradle-build/scheduler
	docker-compose build kolibri-scheduler

run/database:
	docker-compose up -d kolibri-database

run/api:
	docker-compose up -d kolibri-api

run/scheduler:
	docker-compose up -d kolibri-scheduler

stop/database:
	docker-compose stop kolibri-database

stop/api:
	docker-compose stop kolibri-api

stop/scheduler:
	docker-compose stop kolibri-scheduler
