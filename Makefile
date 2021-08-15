docker-env:
	./scripts/init-docker-env.sh

format:
	./gradlew ktlintFormat

gradle-build/api:
	./gradlew clean kolibri-api:build

gradle-build/scheduler:
	./gradlew clean kolibri-scheduler:build

gradle-build/consumer:
	./gradlew clean kolibri-consumer:build

list-docker-services:
	docker-compose ps

build/api: gradle-build/api
	docker-compose build kolibri-api

build/scheduler: gradle-build/scheduler
	docker-compose build kolibri-scheduler

build/consumer: gradle-build/consumer
	docker-compose build kolibri-consumer

run/zookeeper:
	docker-compose up -d kolibri-zookeeper

run/kafka:
	docker-compose up -d kolibri-kafka

run/database:
	docker-compose up -d kolibri-database

run/api:
	docker-compose up -d kolibri-api

run/scheduler:
	docker-compose up -d kolibri-scheduler

run/consumer:
	docker-compose up -d kolibri-consumer

stop/zookeeper:
	docker-compose stop kolibri-zookeeper

stop/kafka:
	docker-compose stop kolibri-kafka

stop/database:
	docker-compose stop kolibri-database

stop/api:
	docker-compose stop kolibri-api

stop/scheduler:
	docker-compose stop kolibri-scheduler

stop/consumer:
	docker-compose stop kolibri-consumer
