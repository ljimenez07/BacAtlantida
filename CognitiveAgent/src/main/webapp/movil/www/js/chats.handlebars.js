(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['chats'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div class=\"container contenedor-campo-de-entrada\">\n	<div id=\"chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\" class=\"row\">\n		<div class=\"col-xs-10\">\n			<input placeholder=\"Escribe aquí\" type=\"text\" id=\"campoDeEntrada\" class=\"form-control\" >\n		</div>\n		<div class=\"col-xs-2\">\n			<button id=\"enviar\" class=\"btn btn-default\"></button>\n		</div>\n	</div>\n</div>\n<style>\n	.panel-body\n	{\n		height: 100%;\n		overflow: scroll\n	}\n</style>\n\n<script>\n\n$(function(){\n	function initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "()\n	{\n		var chat = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " .chat\");\n		var conversation = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #conversation\");\n		var campoDeEntrada = $(\"#chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #campoDeEntrada\");\n		var mensajeDeAgente = $('<li class=\"other\"><div class=\"msg\"><p></p><time>20:17</time></div></li>');\n		var mensajeDeUsuario = $('<li class=\"self\"><div class=\"msg\"><p></p><time>20:17</time></div></li>');\n		var botonDeEnviarMensaje = $(\"#chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #enviar\");\n		var ultimoMensajeEnviado=\"\";\n		var debeReproducirElAudio = false;\n		var guardarCola = true;\n		var colaDeReproduccion = new Array();\n		\n		$(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\").prepend('<div class=\"row contenedor-boton-escuchar-mensajes\"><div class=\"col-xs-6\"><div class=\"boton-escuchar-mensajes desactivado\" /></div></div><div class=\"row\" style=\"height: 54px\" />');\n		$(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " .boton-escuchar-mensajes\").click(function()\n		{\n			$(this).toggleClass(\"activado\").toggleClass(\"desactivado\");\n			debeReproducirElAudio = !debeReproducirElAudio;\n			guardarCola = false;\n			if(colaDeReproduccion.length > 0)\n			{\n				$.each(colaDeReproduccion, function(index, value)\n				{\n					reproducirMensaje(value);\n				});\n			}\n		});\n		\n		function reproducirMensaje(pathDelAudio)\n		{\n			if(debeReproducirElAudio)\n			{\n				agregarAColaDeReproduccion(pathDelAudio);\n			}\n			else if(guardarCola)\n			{\n				colaDeReproduccion.push(pathDelAudio);\n			}\n		}\n		\n		botonDeEnviarMensaje.click(function()\n		{\n			enviarAlServer({ textoAEnviar:campoDeEntrada.val() });\n		});\n		\n		resizeDiv();\n		\n		window.onresize = function(event) {\n			resizeDiv();\n		}\n		\n		function resizeDiv(muestraTeclado, callback)\n		{\n			if(callback != undefined)\n			{\n				callback();\n			}\n		}\n		\n		$('input').bind('focus', function()\n		{\n			resizeDiv(true, function(){\n				moverScrollParaAbajoEnFuncionDelUltimoMensaje(function(){\n					$(\"html, body\").animate({scrollTop: 0}, \"fast\");\n				});\n			});\n		});\n		\n		$('input').bind('blur', function()\n		{\n			setTimeout(resizeDiv, 100);\n		});\n		\n		campoDeEntrada.keypress(function(e)\n		{\n			if(e.which == 13) \n			{\n				enviarAlServer({ textoAEnviar:campoDeEntrada.val()});\n			}\n		});\n		\n		enviarAlServer({sePuedenEnviarVaciosAlServer:true, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:\"Hola\"});\n		\n		function enviarAlServer(param)\n		{\n			var sePuedenEnviarVaciosAlServer = ( param.sePuedenEnviarVaciosAlServer == undefined ) ? false : true;\n			var imprimirElMensajeQueElUsuarioEscribioEnLaConversacion = ( param.imprimirElMensajeQueElUsuarioEscribioEnLaConversacion == undefined ) ? true : false;\n			var textoAEnviar = param.textoAEnviar.trim();\n					\n			if(!sePuedenEnviarVaciosAlServer && textoAEnviar == \"\") return	\n			if(imprimirElMensajeQueElUsuarioEscribioEnLaConversacion)\n			{\n				var fecha = new Date();\n				var hora = dosDigitos(fecha.getHours()) + \":\" + dosDigitos(fecha.getMinutes());\n				var mensaje = mensajeDeUsuario.clone();\n				mensaje.find(\".msg p\").html(textoAEnviar);\n				mensaje.find(\"time\").html(hora);\n				campoDeEntrada.val(\"\");\n				conversation.append(mensaje);\n				\n				moverScrollParaAbajoEnFuncionDelUltimoMensaje();\n			}\n			\n			ajax({\n				type: \"POST\",\n				contentType :\"text/plain; charset=UTF-8\",\n				url : serverDomain + \"/conversacion/"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "/\",\n				data : textoAEnviar,\n				done: function( data ) \n				{\n					var textos = data.textos;\n					if(textos != undefined)\n					{\n						for(var i = 0; i < textos.length; i++)\n						{\n							var fecha = new Date();\n							var hora = dosDigitos(fecha.getHours()) + \":\" + dosDigitos(fecha.getMinutes());\n							var respuesta = mensajeDeAgente.clone();\n							respuesta.find(\".msg p\").html(textos[i].texto);\n							respuesta.find(\"time\").html(hora);\n							conversation.append(respuesta);\n							if(textos[i].audio != \"\")\n							{\n								reproducirMensaje(textos[i].audio);\n							}\n						}\n					}\n					\n					moverScrollParaAbajoEnFuncionDelUltimoMensaje();\n				},\n				fail: function(XMLHttpRequest, textStatus, textStatus) \n				{\n					console.log(JSON.stringify(XMLHttpRequest));\n					console.log(XMLHttpRequest.responseJSON.exception);\n					if( XMLHttpRequest.responseJSON.exception.includes(\"NoSessionException\") )\n					{\n						cambiarVista( \"login\" );\n					}\n				}\n			});\n			\n			ultimoMensajeEnviado = textoAEnviar;\n		}\n		\n		function moverScrollParaAbajoEnFuncionDelUltimoMensaje(callback)\n		{\n			chat.parent().parent().scrollTop(chat.parent().parent()[0].scrollHeight);\n			if(callback != undefined)\n			{\n				callback();\n			}\n		}\n		\n		this.reenviarUltimoMensajeSinImprimirlo = function()\n		{\n			enviarAlServer({sePuedenEnviarVaciosAlServer:false, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:ultimoMensajeEnviado});\n		}\n		\n		function dosDigitos(numero)\n		{\n			return numero > 9 ? numero : \"0\" + numero\n		}\n	}\n	var initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " = initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "();	\n});\n</script>";
},"useData":true});
})();