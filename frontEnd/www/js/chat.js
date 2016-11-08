$(function(){
	var conversation = $("#conversation");
	var campoDeEntrada = $("#campoDeEntrada");
	var mensajeDeAgente = $('<li style="text-align:right;"><div class="avatar"><img src="img/favicon.ico" draggable="false"/></div><div class="msg"><p></p><time>20:17</time></div></li>');
	var mensajeDeUsuario = $('<li><div class="avatar"><img src="img/favicon.ico" draggable="false"/></div><div class="msg"><p></p><time>20:17</time></div></li>');
	$("#enviar").click(function(){
	
		var mensaje = mensajeDeUsuario.clone();
		mensaje.find(".msg p").html(campoDeEntrada.val());
		campoDeEntrada.val("");
		conversation.prepend(mensaje);
		
		$.ajax({
			type: "POST",
			contentType: "application/text; charset=utf-8",
			dataType   : "text",
			url : "http://localhost:8080/convesacion/mensaje",
			data : mensaje
		})
		.done(function( data ) 
		{
			var respuesta = mensajeDeAgente.clone();
			respuesta.find(".msg p").html(data);
			conversation.prepend(respuesta);
		});
		
	});
});