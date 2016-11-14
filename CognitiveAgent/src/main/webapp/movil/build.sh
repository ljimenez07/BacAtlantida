#!/bin/bash

echo "var serverDomain = \"$1\";" > $2/www/js/globals.js
handlebars $2/www/plantillas/chats.handlebars -f $2/www/js/chats.handlebars.js 
handlebars $2/www/plantillas/layout.handlebars -f $2/www/js/layout.handlebars.js 
handlebars $2/www/plantillas/ofertas.handlebars -f $2/www/js/ofertas.handlebars.js 
handlebars $2/www/plantillas/listaDeOfertas.handlebars -f $2/www/js/listaDeOfertas.handlebars.js 
handlebars $2/www/plantillas/oferta.handlebars -f $2/www/js/oferta.handlebars.js 
# cordova run browser
