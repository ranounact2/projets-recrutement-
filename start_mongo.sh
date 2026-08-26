#!/bin/bash

#
#Create you own start_mongo_env.sh with INSTALL_DIR,  MONGO_ARCHIVE and MONGO_ARCHIVE_DIR vars 
. ./start_mongo_env.sh

INIT_MONGO=0
if [ ! -d "$INSTALL_DIR/mongo" ]; then
  echo "Mongo not found in local >> installing it in " $INSTALL_DIR
  cd /tmp
  curl https://fastdl.mongodb.org/linux/$MONGO_ARCHIVE --output /tmp/$MONGO_ARCHIVE
  mkdir -p $INSTALL_DIR
  tar -xvzf  /tmp/$MONGO_ARCHIVE
  mv /tmp/$MONGO_ARCHIVE_DIR "$INSTALL_DIR/mongo"
fi
export PATH=$INSTALL_DIR/mongo/bin:$PATH
echo "Start Mongo..."
MONGO_PRIMARY_PORT=27017

MONGO_PRIMARY_DATA=$INSTALL_DIR/mongo/data/mongodb1
if [ ! -d $MONGO_PRIMARY_DATA ]; then
  mkdir -p $MONGO_PRIMARY_DATA
  INIT_MONGO=1
fi
MONGO_SECONDARY_DATA=$INSTALL_DIR/mongo/data/mongodb2
if [ ! -d $MONGO_SECONDARY_DATA ]; then
  mkdir -p $MONGO_SECONDARY_DATA
  INIT_MONGO=1
fi

mongod --port $MONGO_PRIMARY_PORT --dbpath $MONGO_PRIMARY_DATA --replSet rs0 > /dev/null 2>&1 &
MONGO_SECONDARY_PORT=27018
mongod --port $MONGO_SECONDARY_PORT --dbpath $MONGO_SECONDARY_DATA --replSet rs0 > /dev/null 2>&1 &

echo 'db.runCommand("ping").ok' | mongo localhost:$MONGO_PRIMARY_PORT/test --quiet > /dev/null 2>&1
isPrimaryReady=`echo $?`
echo 'db.runCommand("ping").ok' | mongo localhost:$MONGO_SECONDARY_PORT/test --quiet > /dev/null 2>&1
isSecondaryReady=`echo $?`

while [[ $isPrimaryReady != 0 && $isSecondaryReady != 0 ]];
  do
    echo "Wait primary and secondary ready"
    sleep 0.3
    echo 'db.runCommand("ping").ok' | mongo localhost:$MONGO_PRIMARY_PORT/test --quiet > /dev/null 2>&1
    isPrimaryReady=`echo $?`
    echo 'db.runCommand("ping").ok' | mongo localhost:$MONGO_SECONDARY_PORT/test --quiet > /dev/null 2>&1
    isSecondaryReady=`echo $?`
  done

echo "### PRIMARY + SECONDARY are ready ###"

if [[ $INIT_MONGO -eq 1 ]]; then
  echo "### INIT MONGO ###"

  echo 'rs.initiate()' > primary-mongo.js
  echo 'rs.add("localhost:'$MONGO_SECONDARY_PORT'")' >> primary-mongo.js
  mongo localhost:$MONGO_PRIMARY_PORT < primary-mongo.js

  sleep 2
  echo 'use transactionlogdb' > primary-mongo.js
  echo 'db.createCollection("log")' >> primary-mongo.js
  mongo localhost:$MONGO_PRIMARY_PORT < primary-mongo.js
  rm primary-mongo.js
fi

echo "Mongo is ready on localhost:$MONGO_PRIMARY_PORT"