#!/usr/bin/env python3
import sys

app_center_key = sys.argv[1]
firebase_dev_json  = sys.argv[2]
firebase_prod_json  = sys.argv[3]

with open("./keys2.properties", "w") as text_file:
    text_file.write("HOCKEY_APP_SECRET=\"{}\"".format(app_center_key))

# TODO: Prod and Dev flavors
with open("./app/google-services.json", "w") as text_file:
    text_file.write(firebase_prod_json)

