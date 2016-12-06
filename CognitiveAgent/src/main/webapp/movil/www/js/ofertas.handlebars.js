(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ofertas'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div class=\"row\">\r\n	<div class=\"col-xs-12 contenedor-de-mensaje-bienvenida\">\r\n		<p><strong>¡Hola!</strong></p><br>\r\n		<p>¿Sab&iacute;as que si inicias sesi&oacute;n puedes tener acceso a <strong>m&uacute;ltiples beneficios</strong> que tenemos disponibles especialmente para ti?</p>\r\n		<br>\r\n	</div>\r\n</div>\r\n\r\n<div class=\"contenedor-de-ofertas\">\r\n</div>\r\n<div id=\"contenedor-mostrar-mas\" style=\"display: none\">\r\n	<br>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 text-center\">\r\n			<span id=\"mostrar-mas\">Mostrar más</span>\r\n		</div>\r\n	</div>\r\n</div>\r\n\r\n<script type=\"text/javascript\">\r\nvar pagina = 1;\r\n\r\nfunction evaluarVerMas()\r\n{\r\n	ajax({\r\n		url: serverDomain + \"/ofertas/cantidad\",\r\n		done: function(data)\r\n		{\r\n			cargarOfertas();\r\n			var cantidadDeOfertasMostradas = pagina * 10;\r\n			if(data.cantidad > cantidadDeOfertasMostradas)\r\n			{\r\n				$(\"#contenedor-mostrar-mas\").show();\r\n			}\r\n			else\r\n			{\r\n				$(\"#contenedor-mostrar-mas\").remove();\r\n			}\r\n		}\r\n	});\r\n}\r\n\r\nevaluarVerMas();\r\n\r\n$(\"#mostrar-mas\").on(\"click\", function()\r\n{\r\n	evaluarVerMas();\r\n	pagina++;\r\n});\r\n\r\nfunction bindAcciones()\r\n{\r\n	$(\"#ofertas .datos-de-oferta\").off(\"click\").on(\"click\", function()\r\n	{\r\n		$(\"#oferta .panel-body\").html(\"\");\r\n		cambiarVista(\"oferta\");\r\n		var id = $(this).attr(\"id\");\r\n		ajax({\r\n			url: serverDomain + \"/ofertas/\" + id,\r\n			done: function(data)\r\n			{\r\n				$(\"#oferta .panel-body\").html(Handlebars.templates.oferta(data));\r\n	\r\n				$(\"#oferta .boton-escuchar-oferta\").off(\"click\").on(\"click\", function()\r\n				{\r\n					agregarAColaDeReproduccion($(this).data(\"audio\"));\r\n				});\r\n				$(\"#oferta .imagen-de-oferta\").each(function()\r\n				{\r\n					var path = serverDomain + $(this).attr(\"src\");\r\n					$(this).attr(\"src\", path);\r\n				});\r\n	 		}\r\n		});\r\n	});\r\n	\r\n	$(\"#ofertas .boton-like, #ofertas .boton-dislike\").off(\"click\").on(\"click\", function()\r\n	{\r\n		var parent = $(this).parent().parent();\r\n		var idDeLaOferta = $(this).data(\"id\");\r\n		var activo = parent.find(\".estado-1\");\r\n		if(activo.attr('class') != $(this).attr('class')) {\r\n			activo.addClass(\"estado-0\").removeClass(\"estado-1\");\r\n		}\r\n		$(this).toggleClass(\"estado-1\").toggleClass(\"estado-0\");\r\n		var esLike = parent.find(\".boton-like\").hasClass(\"estado-1\");\r\n		var esDislike = parent.find(\".boton-dislike\").hasClass(\"estado-1\");\r\n		var reaccion = null;\r\n		if(esLike)\r\n		{\r\n			reaccion = true;\r\n		}\r\n		else if(esDislike)\r\n		{\r\n			reaccion = false;\r\n		}\r\n		ajax({\r\n			type: \"POST\",\r\n			url: serverDomain + \"/reaccion/oferta\",\r\n			data:\r\n			{\r\n				idOferta: idDeLaOferta,\r\n				reaccion: reaccion\r\n			}\r\n		});\r\n	});\r\n	\r\n	$(\"#ofertas .boton-escuchar\").off(\"click\").on(\"click\", function()\r\n	{\r\n		agregarAColaDeReproduccion($(this).data(\"audio\"));\r\n	});\r\n	\r\n	$(\"#ofertas .boton-compartir\").off(\"click\").on(\"click\", function()\r\n	{\r\n		agregarAColaDeReproduccion(\"/archivos/beep.wav\");\r\n	});\r\n}\r\n\r\nfunction agregarDominioAImagenes()\r\n{\r\n	$(\"#ofertas .imagen-de-oferta\").each(function()\r\n	{\r\n		var source = $(this).attr(\"src\");\r\n		var noTieneDominio = source.indexOf(serverDomain) < 0;\r\n		if(noTieneDominio)\r\n		{\r\n			var path = serverDomain + source;\r\n			$(this).attr(\"src\", path);\r\n		}\r\n	});\r\n}\r\n\r\nfunction cargarOfertas()\r\n{\r\n	ajax({\r\n		url: serverDomain + \"/ofertas\",\r\n		data:\r\n		{\r\n			pagina: pagina\r\n		},\r\n		done: function(data)\r\n		{\r\n			$(\"#ofertas .contenedor-de-ofertas\").append(Handlebars.templates.listaDeOfertas({ofertas: data}));\r\n			bindAcciones();\r\n			bindClickeable();\r\n			agregarDominioAImagenes();\r\n		}\r\n	});\r\n}\r\n</script>\r\n";
},"useData":true});
})();