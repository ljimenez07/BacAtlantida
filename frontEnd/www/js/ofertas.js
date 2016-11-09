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
    url: "//localhost:8080/ofertas",
    done: function(data) 
	{
		for(var i = 0; i < data.length; i++) 
		{
			var oferta = data[i];
			var datosDeLaOferta = {
				idOferta: oferta.idOferta,
				imagen: "",
				tiempoTranscurrido: oferta.idOferta,
				esUnUsuarioConocido: false
        };
        context.ofertas.push(datosDeLaOferta);
      }
    }
  });
  return Handlebars.templates.ofertas(context);
}
