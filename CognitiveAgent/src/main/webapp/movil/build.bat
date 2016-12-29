@echo off

@echo var serverDomain = "%1"; > ./www/js/globals.js
call handlebars ./www/plantillas/chats.handlebars -f ./www/js/chats.handlebars.js 
call handlebars ./www/plantillas/layout.handlebars -f ./www/js/layout.handlebars.js 
call handlebars ./www/plantillas/ofertas.handlebars -f ./www/js/ofertas.handlebars.js 
call handlebars ./www/plantillas/listaDeOfertas.handlebars -f ./www/js/listaDeOfertas.handlebars.js 
call handlebars ./www/plantillas/oferta.handlebars -f ./www/js/oferta.handlebars.js 
call handlebars ./www/plantillas/popupconocerte.handlebars -f ./www/js/popupconocerte.handlebars.js 
call handlebars ./www/plantillas/login.handlebars -f ./www/js/login.handlebars.js 

IF [%2] == [] GOTO :EOF

rd /s /q platforms
cordova platform add %2
cordova plugin add cordova-plugin-queries-schemes
cordova plugin add cordova-plugin-x-socialsharing
cordova plugin add cordova-plugin-whitelist
cordova build %2
