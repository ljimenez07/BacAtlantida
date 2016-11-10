$(".boton-like, .boton-dislike").click(function() {
  var activo = $(this).parent().parent().find(".activo");
  if(activo.attr('class') != $(this).attr('class')) {
    activo.addClass("inactivo").removeClass("activo");
  }
  $(this).toggleClass("activo").toggleClass("inactivo");
});

function ofertas() {
  var context;
  console.log(context);
  $.ajax({
    url: serverDomain+"/ofertas",
    async: false,
    success: function(data) {
      context = {ofertas: data};
      console.log(context);
      }
  });

  return Handlebars.templates.ofertas(context);
}
