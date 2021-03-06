[![Build status](https://build.appcenter.ms/v0.1/apps/3e6849dd-3529-4f67-8962-568f0627840b/branches/master/badge)](https://appcenter.ms)
[![Maintainability](https://api.codeclimate.com/v1/badges/dfcf1399d64d42450923/maintainability)](https://codeclimate.com/github/power-meter/android-client/maintainability)
[![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![Known Vulnerabilities](https://snyk.io/test/github/power-meter/android-client/badge.svg?targetFile=app/build.gradle)](https://snyk.io/test/github/power-meter/android-client?targetFile=app/build.gradle)

# Development

## Keys

- place `keys.properties` file in `android-client/` 
- place `google-services.json` file under `android-client/app`

## Local DB Access
### Linux

Run the following in terminal

- `sudo ln -s ~/Android/Sdk/platform-tools/adb /bin/adb`
- `adb forward tcp:8080 tcp:8080` (will need to re-enter this command after restarts) 

Database  can be debugged in browser at `localhost:8080`
View docs for more details: `https://github.com/amitshekhariitbhu/Android-Debug-Database`

