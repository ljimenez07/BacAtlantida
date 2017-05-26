(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['layout'] = template({"1":function(container,depth0,helpers,partials,data) {
    return "				<div class=\"col-xs-2 contenedor-centrado\">\r\n					<div><span data-accion=\"mostrar-menu\" class=\"header-menu-icon clickable\" /></div>\r\n				</div>\r\n				<div class=\"col-xs-6 col-xs-offset-1 contenedor-centrado\">\r\n					<div><img src=\"img/logo-header.png\" class=\"logo-header clickable\" data-href=\"ofertas\"></div>\r\n				</div>\r\n				<div class=\"col-xs-3 contenedor-boton-ingresar contenedor-centrado\">\r\n					<div data-href=\"login\" class=\"botonDeIngresar clickable text-center\">Iniciar Sesi&oacute;n</div>\r\n				</div>\r\n";
},"3":function(container,depth0,helpers,partials,data) {
    return "				<div class=\"col-xs-3 contenedor-centrado\">\r\n					<div><span data-accion=\"atras\" class=\"header-back-icon clickable\" /></div>\r\n					<div><span data-accion=\"mostrar-menu\" class=\"header-menu-icon clickable\" /></div>\r\n				</div>\r\n				<div class=\"col-xs-6 contenedor-centrado\">\r\n					<div><img src=\"img/logo-header.png\" class=\"logo-header clickable\" data-href=\"ofertas\"></div>\r\n				</div>\r\n				<div class=\"col-xs-3 contenedor-boton-ingresar contenedor-centrado\">\r\n					<div data-href=\"login\" class=\"botonDeIngresar clickable text-center\">Iniciar Sesi&oacute;n</div>\r\n				</div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function";

  return "<div class=\"panel panel-default\">\r\n	<div class=\"panel-heading\">\r\n		<div class=\"row bck-red\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.ocultarBotonAtras : depth0),{"name":"if","hash":{},"fn":container.program(1, data, 0),"inverse":container.program(3, data, 0),"data":data})) != null ? stack1 : "")
    + "		</div>\r\n		<div class=\"row title\">\r\n			<div class=\"col-xs-12\" id=\"titulo-de-pantalla\">\r\n				"
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<div class=\"panel-body\">\r\n		"
    + ((stack1 = ((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"body","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\r\n	</div>\r\n</div>\r\n"
    + ((stack1 = ((helper = (helper = helpers.DebajoDelPanel || (depth0 != null ? depth0.DebajoDelPanel : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"DebajoDelPanel","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\r\n";
},"useData":true});
})();