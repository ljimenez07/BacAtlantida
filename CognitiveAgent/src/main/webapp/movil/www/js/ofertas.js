$(".boton-like, .boton-dislike").click(function() {
  var activo = $(this).parent().parent().find(".activo");
  if(activo.attr('class') != $(this).attr('class')) {
    activo.addClass("inactivo").removeClass("activo");
  }
  $(this).toggleClass("activo").toggleClass("inactivo");
});

function ofertas() {
  var context = {ofertas: []};
  $.ajax({
    url: serverDomain +"/ofertas",
    done: function(data) 
	{
        context.ofertas.push(data);
      }
    }
  });
  return Handlebars.templates.ofertas(context);
}
