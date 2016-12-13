(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['login'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<form id=\"formLogin\">\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12\">\r\n			<div id=\"image-container\" class=\"text-center\">\r\n				<img alt=\"Logo Banco Atlantida\" src=\"img/logo-BA-rojo.png\" class=\"img-responsive imagen-centrada\"/>\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<div class=\"row\" style=\"height: 100px;\"><div class=\"col-xs-12\"></div></div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<input placeholder=\"Usuario\" type=\"text\" id=\"name\" name=\"name\" class=\"form-control\" autofocus>\r\n		</div>\r\n	</div>\r\n	<div class=\"row\" style=\"height: 40px;\"><div class=\"col-xs-12\"></div></div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<input placeholder=\"Contraseña\" type=\"password\" id=\"password\"name=\"password\" class=\"form-control\" >\r\n		</div>\r\n	</div>\r\n	<div class=\"row\" style=\"height: 60px;\"><div class=\"col-xs-12\"></div></div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group text-center\">\r\n			<button class=\"btn form-control\">Continuar</button>\r\n		</div>\r\n	</div>\r\n	<div class=\"alert alert-danger\" style =\"display:none;\" role=\"alert\"></div>\r\n	<div class=\"row\" style=\"height: 300px;\"><div class=\"col-xs-12\"></div></div>\r\n</form>\r\n<style>\r\n	\r\n</style>\r\n\r\n<script>\r\n\r\n$(function(){\r\n	function initLogin()\r\n	{\r\n		$(\"#formLogin\").submit(function(e) \r\n		{\r\n			ajax({\r\n				type: \"POST\",\r\n				url: serverDomain + \"/movil/login\",\r\n				data: $(this).serialize(),\r\n				done: function(data) \r\n				{\r\n					$(\".app #ofertas\").html(Handlebars.templates.layout(pantallas[\"ofertas\"]));\r\n					establecerBienvenida(data.usuarioEstaLogueado, data.usuarioNombre);\r\n					bindClickeable();\r\n					estaLogueado = true;\r\n					\r\n					idUsuario = data.idUsuario;\r\n					cargarInformacionDelUsuario();\r\n					cambiarVista(vistaActual, true);\r\n				},\r\n				fail: function(XMLHttpRequest, textStatus, textStatus) \r\n				{\r\n					if( XMLHttpRequest.responseJSON.exception.includes(\"CredencialesInvalidosException\") )\r\n					{\r\n						var alert= $(this).find(\".alert\");\r\n					}\r\n				}\r\n			});\r\n			e.preventDefault();\r\n		});\r\n\r\n	}/*CredencialesInvalidosException*/\r\n	initLogin();\r\n\r\n});\r\n</script>";
},"useData":true});
})();