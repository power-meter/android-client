#!/usr/bin/env python3
import sys

firebase_prod_json  = sys.argv[1]
firebase_dev_json  = sys.argv[2]
app_center_key = sys.argv[3]

# # TODO: Prod and Dev flavors
with open("./app/google-services.json", "w") as text_file:
    text_file.write(firebase_prod_json)

with open("./keys.properties", "w") as text_file:
    text_file.write(app_center_key)