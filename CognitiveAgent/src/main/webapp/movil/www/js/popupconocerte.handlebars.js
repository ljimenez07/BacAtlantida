(function() {
  var template = Handlebars.template, templates = Handlebars.templates = Handlebars.templates || {};
templates['popupconocerte'] = template({"compiler":[7,">= 4.0.0"],"main":function(container,depth0,helpers,partials,data) {
    return "<div id=\"popupconocerte\" class=\"modal fade\" style=\"display:none;\"role=\"dialog\">\r\n	<div class=\"modal-dialog modal-sm\" style=\"background-color: white;\">\r\n		<div class=\"modal-body\">\r\n			 Permíteme conocerte, para ofrecerte mejores ofertas\r\n		</div>\r\n		<div class=\"modal-footer\">\r\n			<div class=\"row\">\r\n				<div class=\"col-xs-12\">\r\n					<button data-href=\"conocerte\" data-after=\"quitarModal()\" class=\"btn btn-default clickable\">Si</button>\r\n					<button data-after=\"quitarModal()\" class=\"btn btn-default clickable\">Ahora no</button>\r\n				</div>\r\n			</div>\r\n		</div>\r\n	</div>		\r\n</div>\r\n";
},"useData":true});
})();