#!/bin/bash

echo "var serverDomain = \"$1\";" > $2/www/js/globals.js
handlebars $2/www/plantillas/chats.handlebars -f $2/www/js/chats.handlebars.js 
handlebars $2/www/plantillas/layout.handlebars -f $2/www/js/layout.handlebars.js 
handlebars $2/www/plantillas/ofertas.handlebars -f $2/www/js/ofertas.handlebars.js 
handlebars $2/www/plantillas/listaDeOfertas.handlebars -f $2/www/js/listaDeOfertas.handlebars.js 
handlebars $2/www/plantillas/oferta.handlebars -f $2/www/js/oferta.handlebars.js 
handlebars $2/www/plantillas/popupconocerte.handlebars -f $2/www/js/popupconocerte.handlebars.js 
handlebars $2/www/plantillas/login.handlebars -f $2/www/js/login.handlebars.js 

if [ ${#3} -gt 0 ]; then
	rm -rf platforms
	cordova platform add ios
	cordova platform add android
	cordova plugin add cordova-plugin-statusbar
	cordova build $3
fi
echo "fin"