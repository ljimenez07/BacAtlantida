#!/bin/bash

echo "var serverDomain = \"$1\";" > $2/www/js/globals.js

if [ ${#3} -gt 0 ]; then
	cordova platform rm $3
	cordova platform add $3
	cordova plugin add cordova-plugin-queries-schemes
	cordova plugin add cordova-plugin-x-socialsharing
	cordova plugin add cordova-plugin-whitelist
	cordova plugin add cordova-plugin-splashscreen
	if [ $3 = "android" ]; then
		cp -r $2/res/drawable-* $2/platforms/android/res/
    cp -r $2/res/mipmap-* $2/platforms/android/res/
	fi
	cordova build $3
fi
echo "fin"
