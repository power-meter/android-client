#!/usr/bin/env bash

echo "============================================================================== 
Task         : Keys file 
Description  : Create keys file from ENV variables for android gradle scripts 
Version      : 1.0.0 
Author       : Zahin 
=============================================================================="
echo "HOCKEY_APP_SECRET=$hockey_app_seceret" >../keys.properties

echo "============================================================================== 
Task         : Firebase JSON
Description  : Create Dev and Prod JSON files from ENV variables
Version      : 1.0.0 
Author       : Zahin 
=============================================================================="

echo $dev_json|sed 's/\\//g'>./src/google-services.json
cat ./src/dev/google-services.json
#echo $prod_json|sed 's/\\//g'>./src/release/google-services.json
#cat ./google-services.json
