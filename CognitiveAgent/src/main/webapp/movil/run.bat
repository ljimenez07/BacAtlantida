@echo off

@echo var serverDomain = "%1"; > ./www/js/globals.js
call handlebars ./www/plantillas/chats.handlebars -f ./www/js/chats.handlebars.js 
call handlebars ./www/plantillas/layout.handlebars -f ./www/js/layout.handlebars.js 
call handlebars ./www/plantillas/ofertas.handlebars -f ./www/js/ofertas.handlebars.js 
call handlebars ./www/plantillas/oferta.handlebars -f ./www/js/oferta.handlebars.js 
cordova run android
