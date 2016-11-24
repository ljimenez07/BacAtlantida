(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['login'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<form id=\"formLogin\">\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<label>Nombre</label>\r\n			<input placeholder=\"\" type=\"text\" id=\"name\" name=\"name\" class=\"form-control\" autofocus>\r\n		</div>\r\n	</div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<label>Password</label>\r\n			<input placeholder=\"\" type=\"password\" id=\"password\"name=\"password\" class=\"form-control\" >\r\n		</div>\r\n	</div>\r\n	<div class=\"row\">\r\n		<div class=\"col-xs-12 form-group\">\r\n			<button class=\"btn form-control\">Enviar</button>\r\n		</div>\r\n	</div>\r\n	<div class=\"alert alert-danger\" style =\"display:none;\" role=\"alert\"></div>\r\n</form>\r\n<style>\r\n	\r\n</style>\r\n\r\n<script>\r\n\r\n$(function(){\r\n	function initLogin()\r\n	{\r\n		$(\"#formLogin\").submit(function(e) \r\n		{\r\n			ajax({\r\n				type: \"POST\",\r\n				url: serverDomain + \"/movil/login\",\r\n				data: $(this).serialize(),\r\n				done: function( data ) \r\n				{\r\n					location.reload();\r\n				},\r\n				fail: function(XMLHttpRequest, textStatus, textStatus) \r\n				{\r\n					if( XMLHttpRequest.responseJSON.exception.includes(\"CredencialesInvalidosException\") )\r\n					{\r\n						var alert= $(this).find(\".alert\");\r\n					}\r\n				}\r\n			});\r\n			e.preventDefault();\r\n		});\r\n\r\n	}/*CredencialesInvalidosException*/\r\n	initLogin();\r\n\r\n});\r\n</script>";
},"useData":true});
})();