#!/bin/bash

for pid in `pidof mongod` ; do
  echo "kill -2 pid $pid"
  # see : https://docs.mongodb.com/manual/tutorial/manage-mongodb-processes/#use-kill
  kill -2 $pid
done
echo "Mongo stopped"