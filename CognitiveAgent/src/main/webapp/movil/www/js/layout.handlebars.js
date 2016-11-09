(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['layout'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    var stack1, helper, alias1=depth0 != null ? depth0 : {}, alias2=helpers.helperMissing, alias3="function";

  return "<div class=\"panel panel-default\">\r\n	<div class=\"panel-heading\">\r\n		<div class=\"row bck-red\">\r\n			<div class=\"col-sm-4 col-md-4 col-lg-4	col-xl-4\">\r\n				<div class=\"header-menu-icon\" ></div>	\r\n			</div>\r\n			<div class=\"col-xs-4 col-sm-4 col-md-4 col-lg-4	col-xl-4\"> \r\n				<img src=\"img/logo.png\">\r\n			</div>\r\n			<div class=\"col-xs-4 col-sm-4 col-md-4 col-lg-4	col-xl-4\">\r\n				<div class=\"header-chat-icon\" />\r\n			</div>\r\n		</div>\r\n		<div class=\"row title\">\r\n			<div class=\"col-xs-12 col-sm-12 col-md-12 col-lg-12	col-xl-12\">\r\n				"
    + container.escapeExpression(((helper = (helper = helpers.title || (depth0 != null ? depth0.title : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"title","hash":{},"data":data}) : helper)))
    + "\r\n			</div>\r\n		</div>\r\n	</div>\r\n	<div class=\"panel-body\">\r\n		"
    + ((stack1 = ((helper = (helper = helpers.body || (depth0 != null ? depth0.body : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"body","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\r\n	</div>\r\n</div>\r\n"
    + ((stack1 = ((helper = (helper = helpers.DebajoDelPanel || (depth0 != null ? depth0.DebajoDelPanel : depth0)) != null ? helper : alias2),(typeof helper === alias3 ? helper.call(alias1,{"name":"DebajoDelPanel","hash":{},"data":data}) : helper))) != null ? stack1 : "")
    + "\r\n";
},"useData":true});
})();