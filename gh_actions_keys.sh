#!/usr/bin/env bash
# Generate's key files from env variables
echo "Provided $# arguments"
echo "==============================================================================
Task         : Keys file
Description  : Create keys file from ENV variables for android gradle scripts
Version      : 1.0.0
Author       : Zahin
=============================================================================="
echo "HOCKEY_APP_SECRET=$0" >./keys.properties

echo "==============================================================================
Task         : Firebase JSON
Description  : Create Dev and Prod JSON files from ENV variables
Version      : 1.0.0
Author       : Zahin
=============================================================================="
echo "TODO: Update paths once dev and prod flavours are made"
# echo $1|sed 's/\\//g'>./app/src/dev/google-services.json
# cat ./src/dev/google-services.json
echo "$2"|sed 's/\\//g'>./app/google-services.json
cat ./app/google-services.json
