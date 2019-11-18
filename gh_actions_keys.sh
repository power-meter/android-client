#!/usr/bin/env bash
# Generate's key files from env variables
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
# echo $1|sed 's/\\//g'>./app/src/dev/google-services.json
# cat ./src/dev/google-services.json
echo $2|sed 's/\\//g'>./app/src/prod/google-services.json
cat ./src/prod/google-services.json
