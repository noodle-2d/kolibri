#!/bin/bash

DB_OLD_HOST=""
DB_NEW_HOST=""
DB_PORT="5432"
DB_USER="kolibri"
DB_NAME="kolibri"
DB_DUMP_FILE="kolibri-database-dump.sql"

pg_dump -h ${DB_OLD_HOST} -p ${DB_PORT} -U ${DB_USER} ${DB_NAME} > ${DB_DUMP_FILE}
psql -h ${DB_NEW_HOST} -p ${DB_PORT} -U ${DB_USER} ${DB_NAME} < ${DB_DUMP_FILE}
