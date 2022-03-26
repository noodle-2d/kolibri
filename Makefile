format:
	./gradlew ktlintFormat

gradle-build/api:
	./gradlew kolibri-api:build

gradle-build/scheduler:
	./gradlew kolibri-scheduler:build

gradle-build/consumer:
	./gradlew kolibri-consumer:build

gradle-build/monolite:
	./gradlew kolibri-monolite:build

list-docker-services:
	docker-compose ps

build/api: gradle-build/api
	docker-compose build kolibri-api

build/scheduler: gradle-build/scheduler
	docker-compose build kolibri-scheduler

build/consumer: gradle-build/consumer
	docker-compose build kolibri-consumer

build/monolite: gradle-build/monolite
	docker-compose build kolibri-monolite

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

run/monolite:
	docker-compose up -d kolibri-monolite

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

stop/monolite:
	docker-compose stop kolibri-monolite
