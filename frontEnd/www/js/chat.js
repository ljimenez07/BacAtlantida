$(function(){
	var conversation = $("#conversation");
	var campoDeEntrada = $("#campoDeEntrada");
	var mensajeDeAgente = $('<li style="text-align:right;"><div class="msg"><p class="bck-red color-white"></p><time>20:17</time></div></li>');
	var mensajeDeUsuario = $('<li><div class="msg"><p class="bck-gray"></p><time>20:17</time></div></li>');
	var botonDeEnviarMensaje = $("#enviar");
	var contexto = "";
	
	botonDeEnviarMensaje.click(function(){
	
		enviarAlServer({ textoAEnviar:campoDeEntrada.val() });
		
	});
	
	campoDeEntrada.keypress(function(e) {
	  if(e.which == 13) 
	  {
		enviarAlServer({ textoAEnviar:campoDeEntrada.val()});
	  }
	});
	
	enviarAlServer({sePuedenEnviarVaciosAlServer:true, imprimirElMensajeQueElUsuarioEscribioEnLaConversacion:false, textoAEnviar:"Hola"});
	
	function enviarAlServer(param)
	{
		var sePuedenEnviarVaciosAlServer = ( param.sePuedenEnviarVaciosAlServer == undefined ) ? false : true;
		var imprimirElMensajeQueElUsuarioEscribioEnLaConversacion = ( param.imprimirElMensajeQueElUsuarioEscribioEnLaConversacion == undefined ) ? true : false;
		var textoAEnviar = param.textoAEnviar;
				
		if( ! sePuedenEnviarVaciosAlServer && textoAEnviar == "") return	
		if( imprimirElMensajeQueElUsuarioEscribioEnLaConversacion )
		{
			var mensaje = mensajeDeUsuario.clone();
			mensaje.find(".msg p").html(textoAEnviar);
			campoDeEntrada.val("");
			conversation.prepend(mensaje);
		}
		
		$.ajax({
			type: "POST",
			dataType   : "json",
			url : serverDomain+"conversacion/"+contexto,
			data : textoAEnviar
		})
		.done(function( data ) 
		{
			var respuesta = mensajeDeAgente.clone();
			respuesta.find(".msg p").html(data.texto);
			conversation.prepend(respuesta);
			
			contexto = data.contexto;
		});
		
	}


});