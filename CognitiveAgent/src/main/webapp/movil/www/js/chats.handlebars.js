(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['chats'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div id=\"chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "\" class=\"row\">\r\n	<div class=\"col-xs-10\">\r\n		<input placeholder=\"Escribe aquí\" type=\"text\" id=\"campoDeEntrada\" class=\"form-control\" >\r\n	</div>\r\n	<div class=\"col-xs-2\">\r\n		<button id=\"enviar\" class=\"btn btn-default\"></button>\r\n	</div>\r\n</div>\r\n<style>\r\n	.panel-body\r\n	{\r\n		height: 350px;\r\n		overflow: scroll\r\n	}\r\n</style>\r\n\r\n<script>\r\n\r\n$(function(){\r\n	function initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "()\r\n	{\r\n		var chat = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " .chat\");\r\n		var conversation = $(\"#chat-id-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #conversation\");\r\n		var campoDeEntrada = $(\"#chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #campoDeEntrada\");\r\n		var mensajeDeAgente = $('<li class=\"other\"><div class=\"msg\"><p></p><time>20:17</time></div></li>');\r\n		var mensajeDeUsuario = $('<li class=\"self\"><div class=\"msg\"><p></p><time>20:17</time></div></li>');\r\n		var botonDeEnviarMensaje = $(\"#chat-id-container-"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " #enviar\");\r\n		var ultimoMensajeEnviado=\"\";\r\n		\r\n		botonDeEnviarMensaje.click(function(){\r\n		\r\n			enviarAlServer({ textoAEnviar:campoDeEntrada.val() });\r\n			\r\n		});\r\n		\r\n		campoDeEntrada.keypress(function(e) {\r\n			if(e.which == 13) \r\n			{\r\n				enviarAlServer({ textoAEnviar:campoDeEntrada.val()});\r\n			}\r\n		});\r\n		\r\n		enviarAlServer({sePuedenEnviarVaciosAlServer:true, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:\"Hola\"});\r\n		\r\n		function enviarAlServer(param)\r\n		{\r\n			var sePuedenEnviarVaciosAlServer = ( param.sePuedenEnviarVaciosAlServer == undefined ) ? false : true;\r\n			var imprimirElMensajeQueElUsuarioEscribioEnLaConversacion = ( param.imprimirElMensajeQueElUsuarioEscribioEnLaConversacion == undefined ) ? true : false;\r\n			var textoAEnviar = param.textoAEnviar.trim();\r\n					\r\n			if( ! sePuedenEnviarVaciosAlServer && textoAEnviar == \"\") return	\r\n			if( imprimirElMensajeQueElUsuarioEscribioEnLaConversacion )\r\n			{\r\n				var mensaje = mensajeDeUsuario.clone();\r\n				mensaje.find(\".msg p\").html(textoAEnviar);\r\n				campoDeEntrada.val(\"\");\r\n				conversation.append(mensaje);\r\n				\r\n				moveScrollParaAbjoEnFuncionDelUltimoMEnsaje();\r\n			}\r\n			\r\n			$.ajax({\r\n				type: \"POST\",\r\n				contentType :\"text/plain; charset=UTF-8\",\r\n				dataType : \"json\",\r\n				url : serverDomain+\"/conversacion/"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "/\",\r\n				data : textoAEnviar\r\n			})\r\n			.done(function( data ) \r\n			{\r\n				var respuesta = mensajeDeAgente.clone();\r\n				respuesta.find(\".msg p\").html(data.texto);\r\n				conversation.append(respuesta);\r\n				\r\n				moveScrollParaAbjoEnFuncionDelUltimoMEnsaje();\r\n			})\r\n			.fail(function(XMLHttpRequest, textStatus, textStatus) \r\n			{\r\n				console.log(JSON.stringify(XMLHttpRequest));\r\n				console.log(XMLHttpRequest.responseJSON.exception);\r\n				if( XMLHttpRequest.responseJSON.exception.includes(\"NoSessionException\") )\r\n				{\r\n					cambiarVista( \"login\" );\r\n				}\r\n			});\r\n			\r\n			ultimoMensajeEnviado = textoAEnviar;\r\n			\r\n		}\r\n		function moveScrollParaAbjoEnFuncionDelUltimoMEnsaje()\r\n		{\r\n			chat.parent().parent().scrollTop(chat.parent().parent()[0].scrollHeight);\r\n		}\r\n		\r\n		this.reenviarUltimoMensajeSinImprimirlo = function()\r\n		{\r\n			enviarAlServer({sePuedenEnviarVaciosAlServer:false, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:ultimoMensajeEnviado});\r\n		}\r\n\r\n	}\r\n	var initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + " = initChats"
    + alias4(((helper = (helper = helpers.pathDelServidor || (depth0 != null ? depth0.pathDelServidor : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"pathDelServidor","hash":{},"data":data}) : helper)))
    + "();	\r\n});\r\n</script>";
},"useData":true});
})();