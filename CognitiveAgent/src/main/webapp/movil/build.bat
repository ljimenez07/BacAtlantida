@echo off

@echo var serverDomain = "%1"; > ./www/js/globals.js
call handlebars ./www/plantillas/chats.handlebars -f ./www/js/chats.handlebars.js
call handlebars ./www/plantillas/layout.handlebars -f ./www/js/layout.handlebars.js
call handlebars ./www/plantillas/ofertas.handlebars -f ./www/js/ofertas.handlebars.js
call handlebars ./www/plantillas/listaDeOfertas.handlebars -f ./www/js/listaDeOfertas.handlebars.js
call handlebars ./www/plantillas/oferta.handlebars -f ./www/js/oferta.handlebars.js
call handlebars ./www/plantillas/popupconocerte.handlebars -f ./www/js/popupconocerte.handlebars.js
call handlebars ./www/plantillas/login.handlebars -f ./www/js/login.handlebars.js

IF [%2] == [] GOTO :SALIR

rd /s /q platforms
cordova platform add %2
cordova plugin add cordova-plugin-queries-schemes
cordova plugin add cordova-plugin-x-socialsharing
cordova plugin add cordova-plugin-whitelist
cordova plugin add cordova-plugin-splashscreen

IF %2 == android GOTO :COPIAR_ARCHIVOS

:BUILD
cordova build %2 --release
GOTO :SALIR

:COPIAR_ARCHIVOS
for /f "delims=" %%a in ('dir /b/ad ".\res\drawable-*" ') do xcopy ".\res\%%a\*" ".\platforms\android\res\%%a\" /y
for /f "delims=" %%a in ('dir /b/ad ".\res\mipmap-*" ') do xcopy ".\res\%%a\*" ".\platforms\android\res\%%a\" /y
GOTO :BUILD

:SALIR
