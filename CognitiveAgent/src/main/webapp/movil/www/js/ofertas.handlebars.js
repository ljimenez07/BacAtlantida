(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ofertas'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div class=\"row\">\r\n	<div class=\"col-xs-12 contenedor-de-mensaje-bienvenida\">\r\n		<p><strong>¡Hola!</strong></p><br>\r\n		<p>¿Sab&iacute;as que si inicias sesi&oacute;n puedes tener acceso a <strong>m&uacute;ltiples beneficios</strong> que tenemos disponibles especialmente para ti?</p>\r\n		<br>\r\n	</div>\r\n</div>\r\n\r\n<div class=\"contenedor-de-ofertas\">\r\n</div>\r\n<div id=\"contenedor-mostrar-mas\" style=\"display: none\">\r\n	<br>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 text-center\">\r\n			<span id=\"mostrar-mas\">Mostrar más</span>\r\n		</div>\r\n	</div>\r\n</div>\r\n\r\n<script type=\"text/javascript\">\r\nvar pagina = 1;\r\nvar audioOfertas = new Audio();\r\nvar ultimoObjetoOferta;\r\n\r\nfunction evaluarVerMas()\r\n{\r\n	ajax({\r\n		url: serverDomain + \"/ofertas/cantidad\",\r\n		done: function(data)\r\n		{\r\n			cargarOfertas();\r\n			var cantidadDeOfertasMostradas = pagina * 10;\r\n			if(data.cantidad > cantidadDeOfertasMostradas)\r\n			{\r\n				$(\"#contenedor-mostrar-mas\").show();\r\n			}\r\n			else\r\n			{\r\n				$(\"#contenedor-mostrar-mas\").remove();\r\n			}\r\n		}\r\n	});\r\n}\r\n\r\nevaluarVerMas();\r\n\r\n$(\"#mostrar-mas\").on(\"click\", function()\r\n{\r\n	evaluarVerMas();\r\n	pagina++;\r\n});\r\n\r\nfunction bindAcciones()\r\n{\r\n	$(\"#ofertas .datos-de-oferta\").off(\"click\").on(\"click\", function()\r\n	{\r\n		$(\"#oferta .panel-body\").html(\"\");\r\n		cambiarVista(\"oferta\", true);\r\n		var id = $(this).attr(\"id\");\r\n		ajax({\r\n			url: serverDomain + \"/ofertas/\" + id,\r\n			done: function(data)\r\n			{\r\n				$(\"#oferta .panel-body\").html(Handlebars.templates.oferta(data));\r\n	\r\n				$(\"#oferta .boton-escuchar-oferta\").off(\"click\").on(\"click\", function()\r\n				{\r\n					//agregarCarga();\r\n					reproducirAudioDeLaOferta($(this).data(\"audio\"), $(\"#oferta .boton-escuchar-oferta\"));\r\n				});\r\n	\r\n				$(\"#oferta .boton-compartir\").off(\"click\").on(\"click\", function()\r\n				{\r\n					setTimeout(compartir($(this).data(\"id\"), $(this).data(\"path\"), $(this).data(\"titulo\")), 0);\r\n				});\r\n				\r\n				$(\"#oferta .imagen-de-oferta\").each(function()\r\n				{\r\n					var path = serverDomain + $(this).attr(\"src\");\r\n					$(this).attr(\"src\", path);\r\n				});\r\n	 		}\r\n		});\r\n	});\r\n	\r\n	$(\"#ofertas .boton-like, #ofertas .boton-dislike\").off(\"click\").on(\"click\", function()\r\n	{\r\n		var parent = $(this).parent().parent();\r\n		var idDeLaOferta = $(this).data(\"id\");\r\n		var activo = parent.find(\".estado-1\");\r\n		if(activo.attr('class') != $(this).attr('class')) {\r\n			activo.addClass(\"estado-0\").removeClass(\"estado-1\");\r\n		}\r\n		$(this).toggleClass(\"estado-1\").toggleClass(\"estado-0\");\r\n		var esLike = parent.find(\".boton-like\").hasClass(\"estado-1\");\r\n		var esDislike = parent.find(\".boton-dislike\").hasClass(\"estado-1\");\r\n		var reaccion = null;\r\n		if(esLike)\r\n		{\r\n			reaccion = true;\r\n		}\r\n		else if(esDislike)\r\n		{\r\n			reaccion = false;\r\n		}\r\n		ajax({\r\n			type: \"POST\",\r\n			url: serverDomain + \"/reaccion/oferta\",\r\n			data:\r\n			{\r\n				idOferta: idDeLaOferta,\r\n				reaccion: reaccion\r\n			}\r\n		});\r\n	});\r\n	\r\n	$(\"#ofertas .boton-escuchar\").off(\"click\").on(\"click\", function()\r\n	{\r\n		//agregarCarga();\r\n		if(deviceType != \"iPad\" && deviceType != \"iPhone\"){\r\n			reproducirAudioDeLaOferta($(this).data(\"audio\"), $(this));\r\n		}\r\n		else{\r\n			reproducirAudioDeLaOferta($(this).data(\"audio\"), $(this));\r\n		}\r\n	});\r\n	\r\n	$(\"#ofertas .boton-compartir\").off(\"click\").on(\"click\", function()\r\n	{\r\n		setTimeout(compartir($(this).data(\"id\"), $(this).data(\"path\"), $(this).data(\"titulo\")), 0);\r\n	});\r\n}\r\n\r\nfunction reproducirAudioDeLaOferta(urlDelAudio, objetoOferta){\r\n	\r\n	if( ! audioOfertas.paused){\r\n		audioOfertas.pause();\r\n		if(ultimoObjetoOferta != null){\r\n			if(urlDelAudio != ultimoObjetoOferta.data(\"audio\")){\r\n				terminoReproduccionOferta();\r\n				ultimoObjetoOferta = objetoOferta;\r\n				objetoOferta.addClass(\"pause\").removeClass(\"play\");\r\n				audioOfertas.src = serverDomain + urlDelAudio;\r\n				audioOfertas.addEventListener('ended', terminoReproduccionOferta);\r\n				audioOfertas.play();\r\n			}else{\r\n				terminoReproduccionOferta();\r\n			}\r\n		}else{\r\n			terminoReproduccionOferta();\r\n		}\r\n	}else{\r\n		if(ultimoObjetoOferta != null){\r\n			if(urlDelAudio != ultimoObjetoOferta.data(\"audio\")){\r\n				ultimoObjetoOferta = objetoOferta;\r\n				objetoOferta.addClass(\"pause\").removeClass(\"play\");\r\n				audioOfertas.src = serverDomain + urlDelAudio;\r\n				audioOfertas.addEventListener('ended', terminoReproduccionOferta);\r\n				audioOfertas.play();\r\n			}\r\n		}else{\r\n			ultimoObjetoOferta = objetoOferta;\r\n			objetoOferta.addClass(\"pause\").removeClass(\"play\");\r\n			audioOfertas.src = serverDomain + urlDelAudio;\r\n			audioOfertas.addEventListener('ended', terminoReproduccionOferta);\r\n			audioOfertas.play();\r\n		}\r\n	}\r\n}\r\n\r\nfunction terminoReproduccionOferta(){\r\n	if(ultimoObjetoOferta != null){\r\n		ultimoObjetoOferta.removeClass(\"pause\").addClass(\"play\");\r\n	}\r\n	ultimoObjetoOferta = null;\r\n}\r\n\r\nfunction compartir(id, pathImagen, titulo)\r\n{\r\n	//agregarCarga();\r\n	var options = {\r\n		message: titulo, // not supported on some apps (Facebook, Instagram)\r\n		subject: titulo, // fi. for email\r\n		files: [serverDomain + \"/archivossubidos/\" + pathImagen], // an array of filenames either locally or remotely\r\n		url: serverDomain + \"/ofertas/compartida/\" + id,\r\n		chooserTitle: 'Seleccione una aplicación' // Android only, you can override the default share sheet title\r\n	}\r\n	\r\n	var onSuccess = function(result)\r\n	{\r\n		//quitarCarga();\r\n		console.log(\"Share completed? \" + result.completed); // On Android apps mostly return false even while it's true\r\n		console.log(\"Shared to app: \" + result.app); // On Android result.app is currently empty. On iOS it's empty when sharing is cancelled (result.completed=false)\r\n	}\r\n	\r\n	var onError = function(msg)\r\n	{\r\n		//quitarCarga();\r\n		console.log(\"Sharing failed with message: \" + msg);\r\n	}\r\n	\r\n	try\r\n	{\r\n		window.plugins.socialsharing.shareWithOptions(options, onSuccess, onError);\r\n	}\r\n	catch(err)\r\n	{\r\n		//quitarCarga();\r\n		$(\"#compartirNoSoportado\").modal('show');\r\n	}\r\n}\r\n\r\nfunction agregarDominioAImagenes()\r\n{\r\n	$(\"#ofertas .imagen-de-oferta\").each(function()\r\n	{\r\n		var source = $(this).attr(\"src\");\r\n		var noTieneDominio = source.indexOf(serverDomain) < 0;\r\n		if(noTieneDominio)\r\n		{\r\n			var path = serverDomain + source;\r\n			$(this).attr(\"src\", path);\r\n		}\r\n	});\r\n}\r\n\r\nfunction cargarOfertas()\r\n{\r\n	ajax({\r\n		url: serverDomain + \"/ofertas\",\r\n		data:\r\n		{\r\n			pagina: pagina\r\n		},\r\n		done: function(data)\r\n		{\r\n			console.log(data.resultados.length);\r\n			$(\"#ofertas .contenedor-de-ofertas\").append(Handlebars.templates.listaDeOfertas({ofertas: data.resultados}));\r\n			bindAcciones();\r\n			bindClickeable();\r\n			agregarDominioAImagenes();\r\n			pagina = data.indice.pagina;\r\n		}\r\n	});\r\n}\r\n</script>\r\n";
},"useData":true});
})();