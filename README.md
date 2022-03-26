# Kolibri

TBD: write about building, deploying and other stuff.

### How to run commands inside Kafka

1. Run `source .env` (fill `SERVER_HOST_NAME` environment variable).
2. Run `docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -e HOST_IP=$SERVER_HOST_NAME -i -t wurstmeister/kafka /bin/bash` (then Kafka shell is opened).
3. Run `cd $KAFKA_HOME/bin` (to get inside directory with Kafka scripts).
4. Run ``./kafka-topics.sh --bootstrap-server `broker-list.sh` --list`` (get the list of existing topics).
5. Run ``./kafka-topics.sh --bootstrap-server `broker-list.sh` --describe --topic test-events`` (describe existing topic).
6. Run ``./kafka-topics.sh --bootstrap-server `broker-list.sh` --create --topic new-events --partitions 4 --replication-factor 2`` (create a new topic).

TBD: write a script to do this in more automatic way.

Here is useful [link](https://medium.com/big-data-engineering/hello-kafka-world-the-complete-guide-to-kafka-with-docker-and-python-f788e2588cfc) to do more things.

### Technical todos

- Update dependency versions to newest ones in Gradle build script.
- Update to Java 16 compatibility
- Fix build scripts to use transitive dependencies
- Resolve build warnings after updating dependency versions

### List of things to do while moving to the new server

- [Create a separate user](https://www.pluralsight.com/guides/user-and-group-management-linux)
- [Create a swap file](https://www.digitalocean.com/community/tutorials/how-to-add-swap-space-on-ubuntu-20-04) (optional)
- Install make
- Install git
- [Install java](https://losst.ru/ustanovka-java-v-ubuntu-18-04)
- [Install docker](https://docs.docker.com/engine/install/ubuntu/)
- [Make docker to run with root rights](https://www.digitalocean.com/community/questions/how-to-fix-docker-got-permission-denied-while-trying-to-connect-to-the-docker-daemon-socket)
- Copy cron job files from previous server (optional)
- Clone kolibri git-repository
- Copy .env files from previous server
- Start up kolibri-database
- [Change database user default password](https://stackoverflow.com/questions/12720967/how-to-change-postgresql-user-password)
- [Update local postgresql](https://www.postgresql.org/download/linux/ubuntu/) (optional)
- [Update local pg_dump symlink](https://stackoverflow.com/questions/12836312/postgresql-9-2-pg-dump-version-mismatch) (optional)
- [Migrate database from previous server](https://postgrespro.ru/docs/postgresql/9.6/backup-dump) (use migrate-database.sh script)
- Start up kolibri-monolite
