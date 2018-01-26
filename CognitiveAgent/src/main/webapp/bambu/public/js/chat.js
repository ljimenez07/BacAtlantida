
function chat(){
	
	var idDeLaConversacion = "";
	
	this.obtenerElIdDeLaConversacion = function(){
		
		if(idDeLaConversacion === "" || idDeLaConversacion === null || idDeLaConversacion === undefined){
			return "";
		}else{
			return idDeLaConversacion;
		}
		
	}
	
	this.iniciar = function(args){
		
		if(isEmpty(args.urlDelServidor)){
			alert("Agrega la url del servidor donde está alojado el widget.");
			return "";
		}
		
		$.ajax(
		{
			type: "POST",
			contentType :"application/json",
			url: args.urlDelServidor +"/"+ args.idCliente +"/conversacion/iniciarChat",
			dataType : "json",
			async: false,
			data : JSON.stringify({
				texto: "",
				idDeLaConversacon: args.identificadorDelChat,
				datos: {
					email: args.email,
					numeroCelular: args.celular,
					nombreCliente: args.nombre,
					identificadorDelUsuario: args.identificadorDelUsuarioLogueado,
					tipoDeProceso: args.tipoDeProceso
				}
			}),
			success: function(data) 
			{
				idDeLaConversacion = data.idDeLaConversacion; 
			}
		});	
		
	}
	
	this.crearChat = function(args){
		
		var parametros = {};
		
		if(isEmpty(args.urlDelServidor)){
			alert("Agrega la url del servidor donde está alojado el widget.");
			return "";
		}
		parametros['urlDelServer'] = args.urlDelServidor;
		
		if(isEmpty(args.idCliente)){
			alert("Agrega un cliente al widget");
			return "";
		}
		parametros['idCliente'] = args.idCliente;
		
		if(isEmpty(args.identificadorDelUsuarioLogueado)){
			parametros['idUsuario'] = "";
		}else{
			parametros['idUsuario'] = args.identificadorDelUsuarioLogueado;
		}
		
		if(isEmpty(args.identificadorDelChat)){
			alert("Agrega el id del chat a cargar.");
			return "";
		}
		parametros['identificadorDelChat'] = args.identificadorDelChat;
		
		if(isEmpty(args.ancho)){
			parametros['ancho'] = "100%";
		}else{
			parametros['ancho'] = args.ancho;
		}
		
		if(isEmpty(args.altura)){
			parametros['ancho'] = "100%";
		}else{
			parametros['altura'] = args.altura;
		}
		
		if(isEmpty(args.autoPlay)){
			parametros['autoPlay'] = false;
		}else{
			parametros['autoPlay'] = args.autoPlay;
		}
		
		if(isEmpty(args.urlCustomCss)){
			parametros['urlCustomCss'] = "";
		}else{
			parametros['urlCustomCss'] = args.urlCustomCss;
		}
		
		if(isEmpty(args.email)){
			parametros['emailDeUsuario'] = "";
		}else{
			parametros['emailDeUsuario'] = args.email;
		}

		if(isEmpty(args.celular)){
			parametros['numeroCelularDeUsuario'] = "";
		}else{
			parametros['numeroCelularDeUsuario'] = args.celular;
		}
		
		if(isEmpty(args.nombre)){
			parametros['nombreClienteDeUsuario'] = "";
		}else{
			parametros['nombreClienteDeUsuario'] = args.nombre;
		}
		
		if(isEmpty(args.tipoDeProceso)){ // BandejaDeMensajes - Cotizacion - Caso - Tarea - Oportunidad
			parametros['tipoDeProceso'] = "BandejaDeMensajes";
		}else{
			parametros['tipoDeProceso'] = args.tipoDeProceso;
		}
		
		if(isEmpty(args.hayQueCargarElHistoricoDelUsuario)){
			parametros['hayQueCargarElHistoricoDelUsuario'] = false;
		}else{
			parametros['hayQueCargarElHistoricoDelUsuario'] = args.hayQueCargarElHistoricoDelUsuario;
		}
		
		if(isEmpty(args.idUsuarioACargarHistorico)){
			parametros['idUsuarioACargarHistorico'] = "";
		}else{
			parametros['idUsuarioACargarHistorico'] = args.idUsuarioACargarHistorico;
		}
		
		if(isEmpty(args.hayQueUsarElAgenteCognitivo)){
			parametros['hayQueUsarElAgenteCognitivo'] = true;
		}else{
			parametros['hayQueUsarElAgenteCognitivo'] = args.hayQueUsarElAgenteCognitivo;
		}
				
		$.ajax({
			
			type: "POST",
			contentType :"application/json",
			url: args.urlDelServidor + "/widget/chat",
			data : JSON.stringify(parametros),
			success: function(respuesta){
				var idDelWidget = args.selector.replace("#", "");
				var target = document.getElementById(idDelWidget);
				target.innerHTML = '<iframe frameBorder="0" style="overflow:hidden; overflow-x:hidden;" scrolling="auto" id="idChatCognitivo-'+idDelWidget+'" width="'+args.ancho+'" height="'+args.altura+'"></iframe>';
				
				var iframeElementContainer = document.getElementById('idChatCognitivo-'+idDelWidget).contentDocument;
				iframeElementContainer.open();
				iframeElementContainer.writeln(respuesta);
				iframeElementContainer.close();
				
				//$(args.selector).html(respuesta);
				//$(args.selector).css({"height": args.altura, "width": args.ancho, "position": "relative"});
			}
		}).fail(function(XMLHttpRequest, textStatus, errorThrown ) {
			alert("Error al cargar el widget: "+ XMLHttpRequest +" - "+ textStatus+" - "+errorThrown);
		});	
	}
	
	function isEmpty(str){
	    if (typeof str == 'undefined' || str.length === 0 || str === ""){
	        return true;
	    }
	    else{
	        return false;
	    }
	}
	
}

var miChat = new chat();