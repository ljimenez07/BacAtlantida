var mensajeDeAdvertencia;

$(document).ready(function () {
    popup.cargar({ "id": "divContenedorPopup" });
});

function mostrarPopup(mensajeDeAdvertencia, redirecccion, callback, esRequeridaJustificacion) {
    popup.mostrarPopup("", mensajeDeAdvertencia, callback, esRequeridaJustificacion);
    if (redirecccion != null) {
        popup.redireccionar(redirecccion);
    }
}

function estaVacio(str) {
    if (typeof str == 'undefined' || !str || str.length === 0 || str === "" || !/[^\s]/.test(str) || /^\s*$/.test(str)) {
        return true;
    }
    else {
        return false;
    }
}

function chatInteligente(nuevoMiChat) {
    var datosConfiguracionChat = new Object();
    var chatAUsar = nuevoMiChat;
    this.iniciar = function (args) {
        $.ajax({
            type: "POST",
            url: "../../ServicioWebTribu.asmx/DatosConfiguracionChat",
            data: JSON.stringify({ idTabla: args.idTabla, tipoChat: args.tipoChat }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (respuesta) {
                if (respuesta.d) {
                    datosConfiguracionChat = respuesta.d;
                    datosConfiguracionChat.DivContenedor = args.divContenedor;
                    datosConfiguracionChat.HayQueCargarHistoricoDelUsuario = !estaVacio(args.hayQueCargarHistoricoDelUsuario) ? args.hayQueCargarHistoricoDelUsuario : "";
                    datosConfiguracionChat.IdUsuarioACargarHistorico = !estaVacio(args.idUsuarioACargarHistorico) ? args.idUsuarioACargarHistorico : "";

                    var esRequeridoCrearChat = datosConfiguracionChat.IdentificadorDelChat == "" || datosConfiguracionChat.IdentificadorDelChat == null;
                    if (esRequeridoCrearChat) {
                        if (typeof chatAUsar != 'undefined') {
                            var idUsuarioLogueado = args.tipoChat == "BandejaDeMensajes" ? datosConfiguracionChat.IdentificadorDeUsuario : "";
                            chatAUsar.iniciar({
                                urlDelServidor: datosConfiguracionChat.URLBaseServidorChat,
                                idCliente: datosConfiguracionChat.NombreCliente,
                                identificadorDelUsuarioLogueado: idUsuarioLogueado,
                                identificadorDelChat: "",
                                tipoDeProceso: datosConfiguracionChat.TipoProceso
                            });

                            datosConfiguracionChat.IdentificadorDelChat = chatAUsar.obtenerElIdDeLaConversacion();
                            codigoChat = datosConfiguracionChat.IdentificadorDelChat;
                            guardarIdChat(args.idTabla, datosConfiguracionChat.TipoProceso, datosConfiguracionChat.IdentificadorDelChat);
                            esRequeridoCrearChat = false;
                        }
                        else {
                            mensajeDeAdvertencia = 'No es posible iniciar el chat en estos momentos.';
                            mostrarPopup(mensajeDeAdvertencia);
                        }
                    }
                    console.log(datosConfiguracionChat);
                    var existeIdYClienteChat = (!esRequeridoCrearChat && datosConfiguracionChat.NombreCliente != "");
                    if (existeIdYClienteChat) {
                        crearChatSmart(datosConfiguracionChat);
                    }
                    else {
                        mensajeDeAdvertencia = 'No es posible cargar el chat en estos momentos.';
                        mostrarPopup(mensajeDeAdvertencia);
                    }
                }
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        });
    }

    function crearChatSmart(datosConfiguracionChat) {
        if (typeof chatAUsar != 'undefined') {
            chatAUsar.crearChat({
                selector: datosConfiguracionChat.DivContenedor,
                urlDelServidor: datosConfiguracionChat.URLBaseServidorChat,
                idCliente: datosConfiguracionChat.NombreCliente,
                identificadorDelUsuarioLogueado: datosConfiguracionChat.IdentificadorDeUsuario,
                identificadorDelChat: datosConfiguracionChat.IdentificadorDelChat,
                tipoDeProceso: datosConfiguracionChat.TipoProceso,
                ancho: datosConfiguracionChat.Ancho,
                altura: datosConfiguracionChat.Altura,
                hayQueCargarElHistoricoDelUsuario: datosConfiguracionChat.HayQueCargarHistoricoDelUsuario,
                idUsuarioACargarHistorico: datosConfiguracionChat.IdUsuarioACargarHistorico
            });
        }
        else {
            mensajeDeAdvertencia = 'No es posible cargar el chat en estos momentos.';
            mostrarPopup(mensajeDeAdvertencia);
        }

    }

    function guardarIdChat(idTabla, tipoChat, codigoChat) {
        $.ajax({
            type: "POST",
            url: "../../ServicioWebTribu.asmx/GuardarIdChat",
            data: JSON.stringify({ idTabla: idTabla, tipoChat: tipoChat, nuevoCodigoChat: codigoChat }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (respuesta) {
                console.log("guardado");
            },
            error: function (xhr, status, error) {
                console.log(xhr.responseText);
            }
        });
    }
}

var chatSmart = new chatInteligente(miChat);