(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['listaDeOfertas'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "	<div class=\"row\">\r\n		<div class=\"col-xs-12\">\r\n			<div class=\"row datos-de-oferta\" id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\">\r\n				<div class=\"col-xs-12\">\r\n					<div class=\"row\">\r\n						<div class=\"col-xs-12 embed-responsive embed-responsive-4by3\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esHtml : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.program(4, data, 0),"data":data})) != null ? stack1 : "")
    + "						</div>\r\n					</div>\r\n					<div class=\"row nombre-del-comercio\">\r\n						<div class=\"col-xs-12\">\r\n							<strong>"
    + alias4(((helper = (helper = helpers.comercio || (depth0 != null ? depth0.comercio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"comercio","hash":{},"data":data}) : helper)))
    + "</strong>\r\n						</div>\r\n					</div>\r\n					<div class=\"row\">\r\n						<div class=\"col-xs-12\">\r\n							<span>"
    + alias4(((helper = (helper = helpers.tituloDeOferta || (depth0 != null ? depth0.tituloDeOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tituloDeOferta","hash":{},"data":data}) : helper)))
    + "</span>\r\n						</div>\r\n					</div>\r\n				</div>\r\n			</div>\r\n			<div class=\"row contenedor-de-botones\">\r\n				<div class=\"col-xs-6\">\r\n					<div class=\"tiempo-transcurrido\">Hace "
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\r\n				</div>\r\n				<div class=\"col-xs-6\">\r\n					<div class=\"row\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(6, data, 0),"inverse":container.program(8, data, 0),"data":data})) != null ? stack1 : "")
    + "						<div class=\"col-xs-3\"><div data-audio=\"/archivossubidos"
    + alias4(((helper = (helper = helpers.descripcionAudio || (depth0 != null ? depth0.descripcionAudio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"descripcionAudio","hash":{},"data":data}) : helper)))
    + "\" class=\"boton-escuchar\"></div></div>\r\n						<div class=\"col-xs-3\"><div class=\"boton-compartir\" data-id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\" data-path=\""
    + alias4(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n					</div>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>\r\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "								<iframe class=\"html-de-oferta embed-responsive-item\" frameBorder=\"0\" scrolling=\"no\" id=\"html-de-oferta-"
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\" style=\"display:block; width:100%; height:100vh;\"></iframe>\r\n								<script type=\"text/javascript\">\r\n								var source = serverDomain + \"/archivossubidos/"
    + alias4(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\";\r\n								var iframe = $('#ofertas #html-de-oferta-"
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "');\r\n								iframe.attr(\"src\", source);\r\n								/*iframe.ready(function() {\r\n									var newHeight = $(this).find(\"body\")[0].offsetHeight + \"px\";\r\n									iframe.css({height: newHeight});\r\n								});*/\r\n								</script>\r\n";
},"4":function(container,depth0,helpers,partials,data) {
    var helper;

  return "								<img src=\"/archivossubidos/"
    + container.escapeExpression(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : {},{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\r\n";
},"6":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "							<div class=\"col-xs-3\"><div data-id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\" class=\"boton-like estado-"
    + alias4(((helper = (helper = helpers.likes || (depth0 != null ? depth0.likes : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"likes","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n							<div class=\"col-xs-3\"><div data-id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\" class=\"boton-dislike estado-"
    + alias4(((helper = (helper = helpers.dislikes || (depth0 != null ? depth0.dislikes : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislikes","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n";
},"8":function(container,depth0,helpers,partials,data) {
    return "							<div class=\"col-xs-6\"></div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.ofertas : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});
})();