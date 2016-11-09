@echo off
fOR %%f IN (./www/plantillas/*.handlebars) DO @echo handlebars ./www/plantillas/%%f -f ./www/plantillas/%%f.js
cordova run browser
