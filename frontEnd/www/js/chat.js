$(function(){
	var conversation = $("#conversation");
	var campoDeEntrada = $("#campoDeEntrada");
	var mensajeDeAgente = $('<li style="text-align:right;"><div class="avatar"><img src="img/favicon.ico" draggable="false"/></div><div class="msg"><p></p><time>20:17</time></div></li>');
	var mensajeDeUsuario = $('<li><div class="avatar"><img src="img/favicon.ico" draggable="false"/></div><div class="msg"><p></p><time>20:17</time></div></li>');
	var contexto = "";
	$("#enviar").click(function(){
	
		var mensaje = mensajeDeUsuario.clone();
		mensaje.find(".msg p").html(campoDeEntrada.val());
		campoDeEntrada.val("");
		conversation.prepend(mensaje);
		
		$.ajax({
			type: "POST",
			dataType   : "json",
			url : serverDomain+"conversacion/"+contexto,
			data : mensaje
		})
		.done(function( data ) 
		{
			var respuesta = mensajeDeAgente.clone();
			respuesta.find(".msg p").html(data.texto);
			conversation.prepend(respuesta);
			
			contexto = data.contexto;
		});
		
	});
});