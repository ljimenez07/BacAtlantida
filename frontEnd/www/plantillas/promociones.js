(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['promociones'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "    <div class=\"row\">\n      <div class=\"col-xs-12\">\n        <div class=\"row\">\n          <div class=\"col-xs-12\">\n            <img src=\""
    + alias4(((helper = (helper = helpers.imagen || (depth0 != null ? depth0.imagen : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagen","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\n          </div>\n        </div>\n        <div class=\"row contenedor-de-botones\">\n          <div class=\"col-xs-6\">\n            <div class=\"tiempo-transcurrido\">"
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\n          </div>\n          <div class=\"col-xs-6\">\n            <div class=\"row\" id=\""
    + alias4(((helper = (helper = helpers.idDeLaOferta || (depth0 != null ? depth0.idDeLaOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idDeLaOferta","hash":{},"data":data}) : helper)))
    + "\">\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.program(4, data, 0),"data":data})) != null ? stack1 : "")
    + "              <div class=\"col-xs-3\"><div class=\"boton-escuchar\"></div></div>\n              <div class=\"col-xs-3\"><div class=\"boton-compartir\"></div></div>\n            </div>\n          </div>\n        </div>\n      </div>\n    </div>\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "                <div class=\"col-xs-3\"><div class=\"boton-like "
    + alias4(((helper = (helper = helpers.like || (depth0 != null ? depth0.like : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"like","hash":{},"data":data}) : helper)))
    + "\"></div></div>\n                <div class=\"col-xs-3\"><div class=\"boton-dislike "
    + alias4(((helper = (helper = helpers.dislike || (depth0 != null ? depth0.dislike : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislike","hash":{},"data":data}) : helper)))
    + "\"></div></div>\n";
},"4":function(container,depth0,helpers,partials,data) {
    return "                <div class=\"col-xs-6\"></div>\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.ofertas : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});
})();