(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['ofertas'] = template({"1":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "  <div class=\"row\">\r\n    <div class=\"col-xs-12\">\r\n      <div class=\"row\">\r\n        <div class=\"col-xs-12\">\r\n          <img src=\""
    + alias4(((helper = (helper = helpers.imagenPublicidadPath || (depth0 != null ? depth0.imagenPublicidadPath : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"imagenPublicidadPath","hash":{},"data":data}) : helper)))
    + "\" class=\"imagen-de-oferta\" id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\">\r\n        </div>\r\n      </div>\r\n      <div class=\"row contenedor-de-botones\">\r\n        <div class=\"col-xs-6\">\r\n          <div class=\"tiempo-transcurrido\">Hace "
    + alias4(((helper = (helper = helpers.tiempoTranscurrido || (depth0 != null ? depth0.tiempoTranscurrido : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"tiempoTranscurrido","hash":{},"data":data}) : helper)))
    + "</div>\r\n        </div>\r\n        <div class=\"col-xs-6\">\r\n          <div class=\"row\" id=\""
    + alias4(((helper = (helper = helpers.idOferta || (depth0 != null ? depth0.idOferta : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"idOferta","hash":{},"data":data}) : helper)))
    + "\">\r\n"
    + ((stack1 = helpers["if"].call(alias1,(depth0 != null ? depth0.esUnUsuarioConocido : depth0),{"name":"if","hash":{},"fn":container.program(2, data, 0),"inverse":container.program(4, data, 0),"data":data})) != null ? stack1 : "")
    + "            <div class=\"col-xs-3\"><div class=\"boton-escuchar\"></div></div>\r\n            <div class=\"col-xs-3\"><div class=\"boton-compartir\"></div></div>\r\n          </div>\r\n        </div>\r\n      </div>\r\n    </div>\r\n  </div>\r\n";
},"2":function(container,depth0,helpers,partials,data) {
    var helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function", alias4=container.escapeExpression;

  return "              <div class=\"col-xs-3\"><div class=\"boton-like "
    + alias4(((helper = (helper = helpers.like || (depth0 != null ? depth0.like : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"like","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n              <div class=\"col-xs-3\"><div class=\"boton-dislike "
    + alias4(((helper = (helper = helpers.dislike || (depth0 != null ? depth0.dislike : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"dislike","hash":{},"data":data}) : helper)))
    + "\"></div></div>\r\n";
},"4":function(container,depth0,helpers,partials,data) {
    return "              <div class=\"col-xs-6\"></div>\r\n";
},"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1;

  return "<div class=\"row\">\r\n  <div class=\"col-xs-12\">\r\n    <p><strong>¡Hola!</strong></p>\r\n    <p>Inicie sesi&oacute;n para que conozcas tus <strong>mejores beneficios</strong></p>\r\n    <br>\r\n  </div>\r\n</div>\r\n"
    + ((stack1 = helpers.each.call(depth0 != null ? depth0 : {},(depth0 != null ? depth0.ofertas : depth0),{"name":"each","hash":{},"fn":container.program(1, data, 0),"inverse":container.noop,"data":data})) != null ? stack1 : "")
    + "\r\n<script type=\"text/javascript\">\r\n$(\".boton-like, .boton-dislike\").click(function() {\r\n  var activo = $(this).parent().parent().find(\".activo\");\r\n  if(activo.attr('class') != $(this).attr('class')) {\r\n    activo.addClass(\"inactivo\").removeClass(\"activo\");\r\n  }\r\n  $(this).toggleClass(\"activo\").toggleClass(\"inactivo\");\r\n});\r\n\r\n$(\".imagen-de-oferta\").click(function() {\r\n  var id = $(this).attr(\"id\");\r\n  $.ajax({\r\n    url: serverDomain + \"/ofertas/\" + id,\r\n    success: function(data) {\r\n      $(\".app\").html(Handlebars.templates.layout({title: data.tituloDeOferta, body: Handlebars.templates.oferta(data), DebajoDelPanel: \"\"}));\r\n    }\r\n  });\r\n});\r\n</script>\r\n";
},"useData":true});
})();