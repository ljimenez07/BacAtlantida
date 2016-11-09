(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['layout'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function";

  return "<div class=\"panel panel-default\">\n  <div class=\"panel-heading\">\n    <div class=\"row header\">\n      <div class=\"col-md-4\">\n        <div class=\"header-menu-icon\"></div>\n      </div>\n      <div class=\"col-md-4\">\n        <img src=\"img/logo.png\">\n      </div>\n      <div class=\"col-md-4\">\n        <div class=\"header-chat-icon\"></div>\n      </div>\n    </div>\n    <div class=\"row text-center\">\n      <div class=\"col-md-12\">\n        "
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\n      </div>\n    </div>\n  </div>\n  <div class=\"panel-body\">\n    "
    + ((stack1 = ((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"body","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\n  </div>\n</div>\n"
    + ((stack1 = ((helper = (helper = helpers.DebajoDelPanel || (depth0 != null ? depth0.DebajoDelPanel : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"DebajoDelPanel","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\n";
},"useData":true});
})();