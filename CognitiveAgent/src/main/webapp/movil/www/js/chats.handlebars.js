(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['chats'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div style=\"height: 69px\" />\r\n<div class=\"container contenedor-campo-de-entrada\">\r\n	<div id=\"chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\" class=\"row\">\r\n		<div class=\"col-xs-10 col-sm-11\">\r\n			<input placeholder=\"Escribe aquí\" type=\"text\" id=\"campoDeEntrada\" class=\"form-control\">\r\n		</div>\r\n		<div class=\"col-xs-2 col-sm-1 contenedor-centrado\">\r\n			<div>\r\n				<button id=\"enviar\" class=\"btn btn-default\"></button>\r\n			</div>\r\n		</div>\r\n	</div>\r\n</div>\r\n<style>\r\n	.panel-body\r\n	{\r\n		height: 100%;\r\n		overflow: scroll;\r\n	}\r\n</style>\r\n\r\n<script>\r\n\r\n$(function(){\r\n	function initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "()\r\n	{\r\n		var chat = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " .chat\");\r\n		var conversation = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #conversation\");\r\n		var campoDeEntrada = $(\"#chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #campoDeEntrada\");\r\n		var mensajeDeAgente = $('<li class=\"other\"><div class=\"msg\"><p></p><time>20:17</time></div></li>');\r\n		var mensajeDeUsuario = $('<li class=\"self\"><div class=\"msg\"><p></p><time>20:17</time></div><div class=\"after\" /></li>');\r\n		var botonDeEnviarMensaje = $(\"#chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #enviar\");\r\n		var ultimoMensajeEnviado=\"\";\r\n		var debeReproducirElAudio = false;\r\n		var guardarCola = true;\r\n		var colaDeReproduccion = new Array();\r\n		\r\n		$(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\").prepend('<div style=\"height: 57px\" />');\r\n		if(deviceType != \"iPad\" && deviceType != \"iPhone\")\r\n		{\r\n			$(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\").prepend('<div class=\"row contenedor-boton-escuchar-mensajes\"><div class=\"col-xs-6\"><div class=\"boton-escuchar-mensajes desactivado\" /></div></div>');\r\n		}\r\n		\r\n		var botonDeAudio = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " .boton-escuchar-mensajes\");\r\n		\r\n		$(botonDeAudio).click(function()\r\n		{\r\n			$(this).toggleClass(\"activado\").toggleClass(\"desactivado\");\r\n			debeReproducirElAudio = !debeReproducirElAudio;\r\n			botonesDeAudio."
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " = debeReproducirElAudio;\r\n			guardarCola = false;\r\n			if(colaDeReproduccion.length > 0)\r\n			{\r\n				$.each(colaDeReproduccion, function(index, value)\r\n				{\r\n					reproducirMensaje(value);\r\n				});\r\n			}\r\n			if(!debeReproducirElAudio)\r\n			{\r\n				colaDeReproduccion = new Array();\r\n				audios = new Array();\r\n				audioEnReproduccion.pause();\r\n				estaReproduciendo = false;\r\n				aunDebeEjecutarCallback = true;\r\n			}\r\n		});\r\n		\r\n		if(vistaActual == \""
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\" && !botonesDeAudio."
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + ")\r\n		{\r\n			botonDeAudio.click();\r\n		}\r\n		\r\n		function reproducirMensaje(pathDelAudio, callback)\r\n		{\r\n			if(debeReproducirElAudio)\r\n			{\r\n				//agregarCarga();\r\n				agregarAColaDeReproduccion(pathDelAudio, callback);\r\n			}\r\n			else if(guardarCola)\r\n			{\r\n				colaDeReproduccion.push(pathDelAudio);\r\n			}\r\n		}\r\n		\r\n		document.getElementById(\"campoDeEntrada\").addEventListener(\"input\", detenerAudios);\r\n		\r\n		function detenerAudios(){\r\n			\r\n			colaDeReproduccion = new Array();\r\n			audios = new Array();\r\n			audioEnReproduccion.pause();\r\n			estaReproduciendo = false;\r\n			aunDebeEjecutarCallback = true;\r\n			\r\n		}\r\n		botonDeEnviarMensaje.click(function()\r\n		{\r\n			enviarAlServer({ textoAEnviar:campoDeEntrada.val() });\r\n		});\r\n		\r\n		resizeDiv();\r\n		\r\n		window.onresize = function(event) {\r\n			resizeDiv();\r\n		}\r\n		\r\n		function resizeDiv(muestraTeclado, callback)\r\n		{\r\n			if(callback != undefined)\r\n			{\r\n				callback();\r\n			}\r\n		}\r\n		\r\n		$('input').bind('focus', function()\r\n		{\r\n			resizeDiv(true, function(){\r\n				moverScrollParaAbajoEnFuncionDelUltimoMensaje(function()\r\n					{\r\n						var tamanioEnPxDelScroll = chat.parent().parent()[0].scrollHeight;\r\n						if(tamanioEnPxDelScroll > 0) // evita que el scroll retorne arriba por culpa del callback.\r\n						{\r\n							$(\"html, body\").animate({scrollTop: tamanioEnPxDelScroll}, \"fast\");\r\n						}\r\n					\r\n					});\r\n			});\r\n		});\r\n		\r\n		$('input').bind('blur', function()\r\n		{\r\n			setTimeout(resizeDiv, 100);\r\n		});\r\n		\r\n		campoDeEntrada.keypress(function(e)\r\n		{\r\n			if(e.which == 13) \r\n			{\r\n				enviarAlServer({ textoAEnviar:campoDeEntrada.val()});\r\n			}\r\n		});\r\n		\r\n		enviarAlServer({sePuedenEnviarVaciosAlServer:true, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:\"Hola\"});\r\n		\r\n		function enviarAlServer(param)\r\n		{\r\n			var sePuedenEnviarVaciosAlServer = ( param.sePuedenEnviarVaciosAlServer == undefined ) ? false : true;\r\n			var imprimirElMensajeQueElUsuarioEscribioEnLaConversacion = ( param.imprimirElMensajeQueElUsuarioEscribioEnLaConversacion == undefined ) ? true : false;\r\n			var textoAEnviar = param.textoAEnviar.trim();\r\n					\r\n			if(!sePuedenEnviarVaciosAlServer && textoAEnviar == \"\") return	\r\n			if(imprimirElMensajeQueElUsuarioEscribioEnLaConversacion)\r\n			{\r\n				var fecha = new Date();\r\n				var hora = dosDigitos(fecha.getHours()) + \":\" + dosDigitos(fecha.getMinutes());\r\n				var mensaje = mensajeDeUsuario.clone();\r\n				mensaje.find(\".msg p\").html(textoAEnviar);\r\n				mensaje.find(\"time\").html(hora);\r\n				campoDeEntrada.val(\"\");\r\n				conversation.append(mensaje);\r\n				\r\n				moverScrollParaAbajoEnFuncionDelUltimoMensaje();\r\n			}\r\n			\r\n			ajax({\r\n				type: \"POST\",\r\n				contentType :\"text/plain; charset=UTF-8\",\r\n				url : serverDomain + \"/conversacion/"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "/\",\r\n				data : textoAEnviar,\r\n				done: function( data ) \r\n				{\r\n					var callback, callbackOnPlay;\r\n					var textos = data.textos;\r\n					if(textos != undefined)\r\n					{\r\n						minutosEnSesion = 0;\r\n						var indiceDelUltimoMensajeConAudio;\r\n						if(data.seTerminoElChat)\r\n						{\r\n							debeEjecutarCallbackEnElCambioDeVista = true;\r\n							ajax({\r\n								url: serverDomain + \"/conversacion/borrarUnaConversacion\"\r\n							});\r\n							\r\n							campoDeEntrada.attr(\"disabled\", true);\r\n							if(estaReproduciendo)\r\n							{\r\n								colaDeReproduccion = new Array();\r\n								audios = new Array();\r\n								audioEnReproduccion.pause();\r\n								estaReproduciendo = false;\r\n							}\r\n							\r\n							callback = function(seEstaEjecutandoEnElCambioDeVista)\r\n							{\r\n								$(\".app #"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\").html(Handlebars.templates.layout(pantallas[\""
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\"]));\r\n								bindClickeable();\r\n								\r\n								//reproducirCola();\r\n								if(seEstaEjecutandoEnElCambioDeVista != true)\r\n								{\r\n									debeEjecutarCallbackEnElCambioDeVista = false;\r\n									cambiarVista(\"ofertas\", true);\r\n								}\r\n							};\r\n							\r\n							aux1 = callback;\r\n							\r\n							callbackOnPlay = function()\r\n							{\r\n								aunDebeEjecutarCallback = false;\r\n							};\r\n						\r\n							for(var i = 0; i < textos.length; i++)\r\n							{\r\n								if(textos[i].audio != \"\")\r\n								{\r\n									indiceDelUltimoMensajeConAudio = i;\r\n								}\r\n							}\r\n							\r\n							if(!debeReproducirElAudio || indiceDelUltimoMensajeConAudio == undefined)\r\n							{\r\n								setTimeout(function()\r\n								{\r\n									if(aunDebeEjecutarCallback)\r\n									{\r\n										callback();\r\n									}\r\n								}, 15000);\r\n							}\r\n						}\r\n						else\r\n						{\r\n							if(estaReproduciendo)\r\n							{\r\n								colaDeReproduccion = new Array();\r\n								audios = new Array();\r\n								audioEnReproduccion.pause();\r\n								estaReproduciendo = false;\r\n							}\r\n							callback = function(){};\r\n							\r\n							callbackOnPlay = function(){};\r\n						}\r\n						\r\n						var fecha = new Date();\r\n						var hora = dosDigitos(fecha.getHours()) + \":\" + dosDigitos(fecha.getMinutes());\r\n						for(var i = 0; i < textos.length; i++)\r\n						{\r\n							var htmlAudio = \"\";\r\n							if(textos[i].audio != \"\")\r\n							{\r\n								if(deviceType == \"iPad\" || deviceType == \"iPhone\")\r\n								{\r\n									aunDebeEjecutarCallback = true;\r\n									aux1 = function(){\r\n										aunDebeEjecutarCallback = true;\r\n										setTimeout(function()\r\n										{\r\n											if(aunDebeEjecutarCallback)\r\n											{\r\n												callback();\r\n											}\r\n										}, 3000);\r\n									};\r\n									aux2 = callbackOnPlay;\r\n									htmlAudio = '<br /><br /><audio controls preload=\"auto\" onended=\"aux1()\" onplay=\"aux2()\"><source src=\"' + serverDomain + textos[i].audio + '\"></audio>';\r\n								}\r\n								else\r\n								{\r\n									if(i == indiceDelUltimoMensajeConAudio)\r\n									{\r\n										reproducirMensaje(textos[i].audio, callback);\r\n									}\r\n									else\r\n									{\r\n										reproducirMensaje(textos[i].audio);\r\n									}\r\n								}\r\n							}\r\n							var respuesta = mensajeDeAgente.clone();\r\n							respuesta.find(\".msg p\").html(textos[i].texto + htmlAudio);\r\n							respuesta.find(\"time\").html(hora);\r\n							respuesta.find(\".msg\").append('<div class=\"after\" />');\r\n							conversation.append(respuesta);\r\n						}\r\n					}\r\n					moverScrollParaAbajoEnFuncionDelUltimoMensaje();\r\n				},\r\n				fail: function(XMLHttpRequest, textStatus, textStatus) \r\n				{\r\n					console.log(JSON.stringify(XMLHttpRequest));\r\n					console.log(XMLHttpRequest.responseJSON.exception);\r\n					if( XMLHttpRequest.responseJSON.exception.includes(\"NoSessionException\") )\r\n					{\r\n						cambiarVista( \"login\" , true);\r\n					}\r\n				}\r\n			});\r\n			\r\n			ultimoMensajeEnviado = textoAEnviar;\r\n		}\r\n		\r\n		function moverScrollParaAbajoEnFuncionDelUltimoMensaje(callback)\r\n		{\r\n			window.scrollTo(0,chat.parent().parent()[0].scrollHeight);\r\n			if(callback != undefined)\r\n			{\r\n				callback();\r\n			}\r\n		}\r\n		\r\n		this.reenviarUltimoMensajeSinImprimirlo = function()\r\n		{\r\n			enviarAlServer({sePuedenEnviarVaciosAlServer:false, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:ultimoMensajeEnviado});\r\n		}\r\n		\r\n		function dosDigitos(numero)\r\n		{\r\n			return numero > 9 ? numero : \"0\" + numero\r\n		}\r\n	}\r\n	var initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " = initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "();\r\n});\r\n</script>";
},"useData":true});
})();