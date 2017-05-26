(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['oferta'] = template({"1":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "					<iframe class=\"html-de-oferta\" frameBorder=\"0\" scrolling=\"no\" id=\"html-de-oferta-"
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\" style=\"display:block; width:100%;\" onload=\"resizeIframe(this)\"></iframe>\r\n					<script type=\"text/javascript\">\r\n					var source = serverDomain + \"/archivossubidos/"
    + alias4(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\";\r\n					var iframe = $('#oferta #html-de-oferta-"
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "');\r\n					iframe.attr(\"src\", source);\r\n					</script>\r\n";
},"3":function(container,depth0,helpers,partials,data) {
    var helper;

  return "					<img src=\"/archivossubidos/"
    + container.escapeExpression(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : (container.nullContext || {}),{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\r\n";
},"5":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "						<div class=\"col-xs-3\"><div class=\"boton-like "
    + alias4(((helper = (helper = helpers.like || (depth0 != null ? depth0.like : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"like","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n						<div class=\"col-xs-3\"><div class=\"boton-dislike "
    + alias4(((helper = (helper = helpers.dislike || (depth0 != null ? depth0.dislike : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislike","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : (container.nullContext || {}), alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "<div class=\"row detalle-de-oferta\">\r\n	<div class=\"col-xs-12\">\r\n		<div class=\"row\">\r\n			<div class=\"col-xs-12\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esHtml : depth0),{"name":"if","hash":{},"fn":container.program(1, data, 0),"inverse":container.program(3, data, 0),"data":data})) != null ? stack1 : "")
    + "			</div>\r\n		</div>\r\n		<div class=\"row nombre-del-comercio\">\r\n			<div class=\"col-xs-12\">\r\n				<strong>"
    + alias4(((helper = (helper = helpers.comercio || (depth0 != null ? depth0.comercio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"comercio","hash":{},"data":data}) : helper)))
    + "</strong>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\">\r\n			<div class=\"col-xs-12\">\r\n				<span>"
    + alias4(((helper = (helper = helpers.tituloDeOferta || (depth0 != null ? depth0.tituloDeOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tituloDeOferta","hash":{},"data":data}) : helper)))
    + "</span>\r\n			</div>\r\n		</div>\r\n		<div class=\"row contenedor-centrado letra-small\">\r\n			<div class=\"col-xs-4 col-sm-2 col-md-1 label-restricciones text-left\">\r\n				<span>Restricciones:</span>\r\n			</div>\r\n			<div class=\"col-xs-8 col-sm-10 col-md-11 text-left\">\r\n				<span>"
    + alias4(((helper = (helper = helpers.restricciones || (depth0 != null ? depth0.restricciones : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"restricciones","hash":{},"data":data}) : helper)))
    + "</span>\r\n			</div>\r\n		</div>\r\n		<div class=\"row contenedor-centrado letra-small\">\r\n			<div class=\"col-xs-4 col-sm-2 col-md-1 label-ciudad text-left\">\r\n				<span>Ciudad:</span>\r\n			</div>\r\n			<div class=\"col-xs-8 col-sm-10 col-md-11 text-left\">\r\n				<span>"
    + alias4(((helper = (helper = helpers.ciudad || (depth0 != null ? depth0.ciudad : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"ciudad","hash":{},"data":data}) : helper)))
    + "</span>\r\n			</div>\r\n		</div>\r\n		<div class=\"row contenedor-de-botones\">\r\n			<div class=\"col-xs-12\">\r\n				<div class=\"tiempo-transcurrido\">Hace "
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\r\n			</div>\r\n		</div>\r\n		<div class=\"row\">\r\n			<div class=\"col-xs-12 boton-escuchar-oferta play\" data-audio=\"/archivossubidos"
    + alias4(((helper = (helper = helpers.descripcionAudio || (depth0 != null ? depth0.descripcionAudio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"descripcionAudio","hash":{},"data":data}) : helper)))
    + "\">\r\n				\r\n			</div>\r\n		</div>\r\n		<div class=\"row contenedor-de-botones\">\r\n			<div class=\"col-xs-6\">\r\n				<div class=\"row\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(5, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "				</div>\r\n			</div>\r\n			<div class=\"col-xs-6\">\r\n				<div class=\"row\">\r\n					<div class=\"col-xs-9\"></div>\r\n					<div class=\"col-xs-3\"><div class=\"boton-compartir\" data-id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\" data-titulo=\""
    + alias4(((helper = (helper = helpers.tituloDeOferta || (depth0 != null ? depth0.tituloDeOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tituloDeOferta","hash":{},"data":data}) : helper)))
    + "\" data-path=\""
    + alias4(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n</div>";
},"useData":true});
})();