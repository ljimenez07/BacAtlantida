var context = {
  ofertas: [{
    idDeLaOferta: "1",
    imagen: "../img/oferta-dummy.png",
    tiempoTranscurrido: "Hace 5 horas",
    esUnUsuarioConocido: false,
    like: "inactivo",
    dislike: "inactivo"
  },
  {
    idDeLaOferta: "2",
    imagen: "../img/oferta-dummy.png",
    tiempoTranscurrido: "Hace 8 horas",
    esUnUsuarioConocido: true,
    like: "activo",
    dislike: "inactivo"
  }]
};

$(".boton-like, .boton-dislike").click(function() {
  var activo = $(this).parent().parent().find(".activo");
  if(activo.attr('class') != $(this).attr('class')) {
    activo.addClass("inactivo").removeClass("activo");
  }
  $(this).toggleClass("activo").toggleClass("inactivo");
});

function ofertas() {
  return Handlebars.templates.ofertas(context);
}
