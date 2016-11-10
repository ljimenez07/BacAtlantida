(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['oferta'] = template({"1":function(container,depth0,helpers,partials,data) {
    return "      <div id=\"html-de-oferta\"></div>\r\n";
},"3":function(container,depth0,helpers,partials,data) {
    var helper;

  return "      <img src=\""
    + container.escapeExpression(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : {},{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\r\n";
},"5":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "            <div class=\"col-xs-3\"><div class=\"boton-like "
    + alias4(((helper = (helper = helpers.like || (depth0 != null ? depth0.like : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"like","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n            <div class=\"col-xs-3\"><div class=\"boton-dislike "
    + alias4(((helper = (helper = helpers.dislike || (depth0 != null ? depth0.dislike : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislike","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {};

  return "<div class=\"row\">\r\n  <div class=\"col-xs-12\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esHtml : depth0),{"name":"if","hash":{},"fn":container.program(1, data, 0),"inverse":container.program(3, data, 0),"data":data})) != null ? stack1 : "")
    + "    <div class=\"row contenedor-de-botones\">\r\n      <div class=\"col-xs-12\">\r\n        <div class=\"tiempo-transcurrido\">Hace "
    + container.escapeExpression(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\r\n      </div>\r\n    </div>\r\n    <div class=\"row\">\r\n      <div class=\"col-xs-12 text-center\">\r\n        <img src=\"./img/icono-94x91.png\">\r\n      </div>\r\n    </div>\r\n    <div class=\"row contenedor-de-botones\">\r\n      <div class=\"col-xs-6\">\r\n      	<div class=\"row\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(5, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "      	</div>\r\n      </div>\r\n      <div class=\"col-xs-6\">\r\n        <div class=\"row\">\r\n          <div class=\"col-xs-9\"></div>\r\n            <div class=\"col-xs-3\"><div class=\"boton-compartir\"></div></div>\r\n        </div>\r\n      </div>\r\n    </div>\r\n  </div>\r\n</div>";
},"useData":true});
})();