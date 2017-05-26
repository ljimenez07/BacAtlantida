(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['login'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<form id=\"formLogin\">\r\n	<div class=\"row mensaje-bienvenida-login\"><div class=\"col-xs-12 text-center\">Nuestra Asesora Bancaria Interactiva te da la bienvenida, ingresa con tus credenciales de Atl&aacute;ntida Online.</div></div>\r\n	<div class=\"row\" style=\"height: 25px;\"><div class=\"col-xs-12\"></div></div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<input placeholder=\"Usuario\" type=\"text\" id=\"name\" name=\"name\" class=\"form-control\" autofocus>\r\n		</div>\r\n	</div>\r\n	<div class=\"row\" style=\"height: 40px;\"><div class=\"col-xs-12\"></div></div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<input placeholder=\"Contraseña\" type=\"password\" id=\"password\" name=\"password\" class=\"form-control\" >\r\n		</div>\r\n	</div>\r\n	<div class=\"row contenedor-centrado\" style=\"height: 60px;\"><div class=\"col-xs-12 text-center\"><span id=\"contenedor-de-mensajes-login\"></span></div></div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group text-center\">\r\n			<button class=\"btn form-control\">Continuar</button>\r\n		</div>\r\n	</div>\r\n	<div class=\"alert alert-danger\" style =\"display:none;\" role=\"alert\"></div>\r\n	<div class=\"row\" id=\"fondo-del-login\" style=\"height: 300px;\"></div>\r\n</form>\r\n\r\n<script>\r\n\r\n$(function(){\r\n	function initLogin()\r\n	{\r\n		$(\"#login .botonDeIngresar\").remove();\r\n		\r\n		actualizarDimensiones();\r\n		\r\n		$(\"#formLogin\").submit(function(e) \r\n		{\r\n			$(\"body\").append(\"<div id='overlay' style='position:absolute;top:0;left:0;height:100%;width:100%;z-index:999'></div>\");\r\n			\r\n			$(\"#contenedor-de-mensajes-login\").html(\"\");\r\n			\r\n			ajax({\r\n				type: \"POST\",\r\n				url: serverDomain + \"/movil/login\",\r\n				data: $(this).serialize(),\r\n				done: function(data) \r\n				{\r\n					estaLogueado = true;\r\n					\r\n					cargarInformacionDelUsuario();\r\n					$(\"#overlay\").remove();\r\n					cambiarVista(vistaActual, true);\r\n					validateSession = setInterval(session_checking, 60000);					\r\n				},\r\n				fail: function(XMLHttpRequest, textStatus, errorThrown) \r\n				{\r\n					$(\"#overlay\").remove();\r\n					$(\"#contenedor-de-mensajes-login\").html(XMLHttpRequest.responseText);\r\n				}\r\n			});\r\n			e.preventDefault();\r\n		});\r\n\r\n	}\r\n	initLogin();\r\n\r\n});\r\n</script>";
},"useData":true});
})();