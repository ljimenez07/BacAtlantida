#!/bin/bash

echo "var serverDomain = \"$1\";" > ./www/js/globals.js
handlebars ./www/plantillas/chats.handlebars -f ./www/js/chats.handlebars.js 
handlebars ./www/plantillas/layout.handlebars -f ./www/js/layout.handlebars.js 
handlebars ./www/plantillas/ofertas.handlebars -f ./www/js/ofertas.handlebars.js 
# cordova run browser
