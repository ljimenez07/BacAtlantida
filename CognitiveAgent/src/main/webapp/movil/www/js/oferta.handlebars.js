(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['oferta'] = template({"1":function(container,depth0,helpers,partials,data) {
    return "			<div class=\"html-de-oferta\"></div>\n";
},"3":function(container,depth0,helpers,partials,data) {
    var helper;

  return "			<img src=\"/archivossubidos/"
    + container.escapeExpression(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : {},{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\n";
},"5":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "						<div class=\"col-xs-3\"><div class=\"boton-like "
    + alias4(((helper = (helper = helpers.like || (depth0 != null ? depth0.like : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"like","hash":{},"data":data}) : helper)))
    + "\"></div></div>\n						<div class=\"col-xs-3\"><div class=\"boton-dislike "
    + alias4(((helper = (helper = helpers.dislike || (depth0 != null ? depth0.dislike : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislike","hash":{},"data":data}) : helper)))
    + "\"></div></div>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div class=\"row\">\n	<div class=\"col-xs-12\">\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esHtml : depth0),{"name":"if","hash":{},"fn":container.program(1, data, 0),"inverse":container.program(3, data, 0),"data":data})) != null ? stack1 : "")
    + "		<div class=\"row nombre-del-comercio\">\n			<div class=\"col-xs-12\">\n				<strong>"
    + alias4(((helper = (helper = helpers.comercio || (depth0 != null ? depth0.comercio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"comercio","hash":{},"data":data}) : helper)))
    + "</strong>\n			</div>\n		</div>\n		<div class=\"row\">\n			<div class=\"col-xs-12\">\n				<span>"
    + alias4(((helper = (helper = helpers.tituloDeOferta || (depth0 != null ? depth0.tituloDeOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tituloDeOferta","hash":{},"data":data}) : helper)))
    + "</span>\n			</div>\n		</div>\n		<div class=\"row contenedor-de-botones\">\n			<div class=\"col-xs-12\">\n				<div class=\"tiempo-transcurrido\">Hace "
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\n			</div>\n		</div>\n		<div class=\"row\">\n			<div class=\"col-xs-12 text-center boton-escuchar-oferta\" data-audio=\""
    + alias4(((helper = (helper = helpers.descripcionAudio || (depth0 != null ? depth0.descripcionAudio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"descripcionAudio","hash":{},"data":data}) : helper)))
    + "\">\n				<img src=\"./img/icono-94x91.png\">\n			</div>\n		</div>\n		<div class=\"row contenedor-de-botones\">\n			<div class=\"col-xs-6\">\n				<div class=\"row\">\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(5, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "				</div>\n			</div>\n			<div class=\"col-xs-6\">\n				<div class=\"row\">\n					<div class=\"col-xs-9\"></div>\n						<div class=\"col-xs-3\"><div class=\"boton-compartir\"></div></div>\n				</div>\n			</div>\n		</div>\n	</div>\n</div>";
},"useData":true});
})();