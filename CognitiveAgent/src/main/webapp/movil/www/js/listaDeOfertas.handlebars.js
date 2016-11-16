(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['listaDeOfertas'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "  <div class=\"row\">\r\n    <div class=\"col-xs-12\">\r\n      <div class=\"row datos-de-oferta\" id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\">\r\n        <div class=\"col-xs-12\">\r\n          <div class=\"row\">\r\n            <div class=\"col-xs-12\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esHtml : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.program(4, data, 0),"data":data})) != null ? stack1 : "")
    + "            </div>\r\n          </div>\r\n          <div class=\"row nombre-del-comercio\">\r\n            <div class=\"col-xs-12\">\r\n              <strong>"
    + alias4(((helper = (helper = helpers.comercio || (depth0 != null ? depth0.comercio : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"comercio","hash":{},"data":data}) : helper)))
    + "</strong>\r\n            </div>\r\n          </div>\r\n          <div class=\"row\">\r\n            <div class=\"col-xs-12\">\r\n              <span>"
    + alias4(((helper = (helper = helpers.tituloDeOferta || (depth0 != null ? depth0.tituloDeOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tituloDeOferta","hash":{},"data":data}) : helper)))
    + "</span>\r\n            </div>\r\n          </div>\r\n        </div>\r\n      </div>\r\n      <div class=\"row contenedor-de-botones\">\r\n        <div class=\"col-xs-6\">\r\n          <div class=\"tiempo-transcurrido\">Hace "
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\r\n        </div>\r\n        <div class=\"col-xs-6\">\r\n          <div class=\"row\" idOferta=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(6, data, 0),"inverse":container.program(8, data, 0),"data":data})) != null ? stack1 : "")
    + "            <div class=\"col-xs-3\"><div class=\"boton-escuchar\"></div></div>\r\n            <div class=\"col-xs-3\"><div class=\"boton-compartir\"></div></div>\r\n          </div>\r\n        </div>\r\n      </div>\r\n    </div>\r\n  </div>\r\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "                <div class=\"html-de-oferta\" id=\"html-de-oferta-"
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\"></div>\r\n                <script type=\"text/javascript\">\r\n                $.get(\""
    + alias4(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\", function(data) {\r\n                  $(\"#html-de-oferta-"
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\").html(data);\r\n                });\r\n                </script>\r\n";
},"4":function(container,depth0,helpers,partials,data) {
    var helper;

  return "                <img src=\""
    + container.escapeExpression(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : helpers.helperMissing),(typeof helper === "function" ? helper.call(depth0 != null ? depth0 : {},{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\r\n";
},"6":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "              <div class=\"col-xs-3\"><div class=\"boton-like "
    + alias4(((helper = (helper = helpers.like || (depth0 != null ? depth0.like : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"like","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n              <div class=\"col-xs-3\"><div class=\"boton-dislike "
    + alias4(((helper = (helper = helpers.dislike || (depth0 != null ? depth0.dislike : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislike","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n";
},"8":function(container,depth0,helpers,partials,data) {
    return "              <div class=\"col-xs-6\"></div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.ofertas : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});
})();