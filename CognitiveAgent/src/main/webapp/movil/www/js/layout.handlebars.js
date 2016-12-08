(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['layout'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function";

  return "<div class=\"panel panel-default\">\n	<div class=\"panel-heading\">\n		<div class=\"row bck-red\">\n			<div class=\"col-xs-2\">\n				<div data-accion=\"mostrar-menu\" class=\"header-menu-icon clickable\" ></div>	\n			</div>\n			<div class=\"col-xs-2\">\n				<div data-href=\"chat\" class=\"header-chat-icon no-logueado clickable\" />\n			</div>\n			<div class=\"col-xs-5\"> \n				<img src=\"img/logo-header.png\" class=\"logo-header clickable\" data-href=\"ofertas\">\n			</div>\n			<div class=\"col-xs-3 contenedor-boton-ingresar\">\n				<div data-href=\"login\" class=\"botonDeIngresar clickable text-right\">INICIAR SESI&Oacute;N</div>\n			</div>\n			<div class=\"col-xs-2 col-xs-offset-1 chat-icon-logueado\" style=\"display: none;\">\n				<div data-href=\"chat\" class=\"header-chat-icon clickable\" />\n			</div>\n		</div>\n		<div class=\"row title\">\n			<div class=\"col-xs-12\" id=\"titulo-de-pantalla\">\n				"
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\n			</div>\n		</div>\n	</div>\n	<div class=\"panel-body\">\n		"
    + ((stack1 = ((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"body","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\n	</div>\n</div>\n"
    + ((stack1 = ((helper = (helper = helpers.DebajoDelPanel || (depth0 != null ? depth0.DebajoDelPanel : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"DebajoDelPanel","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\n";
},"useData":true});
})();