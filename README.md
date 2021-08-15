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
