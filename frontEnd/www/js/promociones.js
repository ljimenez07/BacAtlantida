(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['promociones'] = template({"1":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "    <div class=\"row\">\r\n      <div class=\"col-xs-12\">\r\n        <div class=\"row\">\r\n          <div class=\"col-xs-12\">\r\n            <img src=\""
    + alias4(((helper = (helper = helpers.imagen || (depth0 != null ? depth0.imagen : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagen","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\">\r\n          </div>\r\n        </div>\r\n        <div class=\"row\">\r\n          <div class=\"col-xs-8\">\r\n            <div class=\"tiempo-transcurrido\">"
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\r\n          </div>\r\n          <div class=\"col-xs-2\"><div class=\"boton-escuchar\"></div></div>\r\n          <div class=\"col-xs-2\"><div class=\"boton-compartir\"></div></div>\r\n        </div>\r\n      </div>\r\n    </div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "\r\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.ofertas : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "");
},"useData":true});
})();