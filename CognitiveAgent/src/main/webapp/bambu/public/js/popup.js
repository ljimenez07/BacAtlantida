function popup() {
    var cargadoEnDiv = false;
    var esRequeridoJustificacion = false;

    this.solicitarJustificacion = function () {
        esRequeridoJustificacion = true;
    }

    this.cargar = function (args) {
        if (!cargadoEnDiv) {
            var target = document.getElementById(args.id);
            var contenidoDiv = '<div id="modalMensaje" class="modal fade" tabindex="-1" role="dialog">';
            contenidoDiv += '<div class="modal-dialog">';
            contenidoDiv += '<div class="modal-content bordePopup">';
            contenidoDiv += '<div class="modal-header">';
            contenidoDiv += '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
            contenidoDiv += '<h4 class="modal-title text-center tituloPopup">';
            contenidoDiv += '<label id="lblTituloAdvertencia"></label>';
            contenidoDiv += '</h4>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-body container-fluid textoCentral">';
            contenidoDiv += '<div class="row col-md-12 form-group">';
            contenidoDiv += '<label id="lblMensajeAdvertencia" ></label>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="row col-md-12">';
            contenidoDiv += '<input type="text" name="justificacion" id="iptJustificacion" class="form-control" style="display:none;">';
            contenidoDiv += '</div>';
            contenidoDiv += '<div id="divEnviarCorreo" class="row col-md-12 checkbox" style="display:none;margin-top: 10px;margin-bottom: 10px;">';
            contenidoDiv += '<label><input type="checkbox" name="cbEnviarCorreo" id="cbEnviarCorreo" style="vertical-align: middle;margin-right:2px;">Enviar correo al cliente con el detalle de la conversación</label>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-footer">';
            contenidoDiv += '<button id="btnOK" type="button" class="btn btn-default center-block botonPopup" data-dismiss="modal">OK</button>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            target.innerHTML = contenidoDiv;
            cargadoEnDiv = true;
        }
    }
    
    this.mostrarPopup = function (titulo, mensaje, callback, esRequeridoJustificacion) {
        var selectorEtiquetaTituloAdvertencia = $('#lblTituloAdvertencia');
        var selectorEtiquetaMensajeAdvertencia = $('#lblMensajeAdvertencia');
        var selectorModalMensaje = $('#modalMensaje');
        var selectorJustificacion = $('#iptJustificacion');
        var selectorDivEnviarCorreo = $('#divEnviarCorreo');
        var selectorBotonOK = $('#btnOK');

        selectorEtiquetaTituloAdvertencia.text(titulo);
        selectorEtiquetaMensajeAdvertencia.text(mensaje);
        var esVisible = selectorModalMensaje.is(':visible');
        if (!esVisible) {
            selectorModalMensaje.modal('show');
        } else {
            selectorModalMensaje.modal('hide');
        	selectorModalMensaje.on('hidden.bs.modal', function () {
                selectorModalMensaje.modal('show');
        	})
        }
        
        if (callback != null) {
            selectorBotonOK.off("click");
            selectorBotonOK.on("click", callback);
        }
        else {
            selectorBotonOK.off("click");
        }
        if (esRequeridoJustificacion) {
            selectorJustificacion.show();
            selectorDivEnviarCorreo.show();
        }
        else {
            selectorJustificacion.hide();
            selectorDivEnviarCorreo.hide();
        }
    }

    this.redireccionar = function (url) {
        $('#modalMensaje #btnOK').click(function () {
            window.top.location.href = url;
        });
    }

    this.redireccionarAutomaticamente = function (url, paramTiempoEspera) {
        var tiempoEspera = 2000;
        if (paramTiempoEspera != null) {
            tiempoEspera = paramTiempoEspera;
        }
        setInterval(function () {
            window.location.href = url;
        }, tiempoEspera);
    }
}

function popupDosBotones() {
    var cargadoEnDiv = false;

    this.callback = function () {
        console.log("callback en blanco");
    }

    this.cargar = function (args) {
        if (!cargadoEnDiv) {
            var target = document.getElementById(args.id);
            var contenidoDiv = '<div id="modalMensajePopupDosBotones" class="modal fade" tabindex="-1" role="dialog">';
            contenidoDiv += '<div class="modal-dialog">';
            contenidoDiv += '<div class="modal-content bordePopup">';
            contenidoDiv += '<div class="modal-header">';
            contenidoDiv += '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
            contenidoDiv += '<h4 class="modal-title text-center tituloPopup tonalidadCeleste">';
            contenidoDiv += '<asp:Label ID="lblTituloAdvertenciaPopupDosBotones" runat="server" Text="" ClientIDMode="Static"></asp:Label>';
            contenidoDiv += '</h4>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-body textoCentral">';
            contenidoDiv += '<asp:Label ID="lblMensajeAdvertenciaPopupDosBotones" runat="server" Text="" ClientIDMode="Static"></asp:Label>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-footer text-center btn-group-ls center-block">';
            var tieneDosBotones = args.cantidadBotones == 2;
            if (tieneDosBotones) {
                contenidoDiv += '<button type="button" class="btn btn-default botonPopup" onclick="' + args.callback + '(); return false;" data-dismiss="modal">Si</button>';
                contenidoDiv += '<button type="button" class="btn btn-default botonPopup" data-dismiss="modal">No</button>';
            }
            else {
                contenidoDiv += '<button type="button" class="btn btn-default center-block botonPopup" data-dismiss="modal">OK</button>';
            }

            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';

            if (args.callback != null)
            {
                this.callback = args.callback;
            }
            target.innerHTML = contenidoDiv;
            cargadoEnDiv = true;
        }
    }

    this.mostrarPopup = function (titulo, mensaje) {
        var selectorEtiquetaTituloAdvertencia = $('#lblTituloAdvertenciaPopupDosBotones');
        var selectorEtiquetaMensajeAdvertencia = $('#lblMensajeAdvertenciaPopupDosBotones');
        var selectorModalMensaje = $('#modalMensajePopupDosBotones');

        selectorEtiquetaTituloAdvertencia.text(titulo);
        selectorEtiquetaMensajeAdvertencia.text(mensaje);
        selectorModalMensaje.modal('show');
    }
}

function popupCorreoMismaClinica() {
    var cargadoEnDiv = false;

    this.cargar = function (args) {
        if (!cargadoEnDiv) {
            var target = document.getElementById(args.id);
            var contenidoDiv = '<div id="modalMensajeMismaClinica" class="modal fade" tabindex="-1" role="dialog">';
            contenidoDiv += '<div class="modal-dialog">';
            contenidoDiv += '<div class="modal-content bordePopup">';
            contenidoDiv += '<div class="modal-header">';
            contenidoDiv += '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
            contenidoDiv += '<h4 class="modal-title text-center tituloPopup tonalidadCeleste">';
            contenidoDiv += '<asp:Label ID="lblTituloAdvertencia" runat="server" Text="" ClientIDMode="Static"></asp:Label>';
            contenidoDiv += '</h4>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-body textoCentral">';
            contenidoDiv += '<asp:Label ID="lblMensajeAdvertencia" runat="server" Text="" ClientIDMode="Static"></asp:Label>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-footer">';
            contenidoDiv += '<button type="button" class="btn btn-default center-block botonPopup" data-dismiss="modal">Aceptar</button>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            target.innerHTML = contenidoDiv;
            cargadoEnDiv = true;
        }
    }

    this.mostrarPopup = function (titulo, mensaje) {
        var selectorEtiquetaTituloAdvertencia = $('#lblTituloAdvertencia');
        var selectorEtiquetaMensajeAdvertencia = $('#lblMensajeAdvertencia');
        var selectorModalMensaje = $('#modalMensajeMismaClinica');

        selectorEtiquetaTituloAdvertencia.text(titulo);
        selectorEtiquetaMensajeAdvertencia.text(mensaje);
        selectorModalMensaje.modal('show');
    }
}

function popupCorreoClinicaDistinta() {
    var cargadoEnDiv = false;

    this.cargar = function (args) {
        if (!cargadoEnDiv) {
            var target = document.getElementById(args.id);
            var contenidoDiv = '<div id="modalMensajeClinicaDistinta" class="modal fade" tabindex="-1" role="dialog">';
            contenidoDiv += '<div class="modal-dialog">';
            contenidoDiv += '<div class="modal-content bordePopup">';
            contenidoDiv += '<div class="modal-header">';
            contenidoDiv += '<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
            contenidoDiv += '<h4 class="modal-title text-center tituloPopup tonalidadCeleste">';
            contenidoDiv += '<asp:Label ID="lblTituloAdvertencia" runat="server" Text="" ClientIDMode="Static"></asp:Label>';
            contenidoDiv += '</h4>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-body textoCentral">';
            contenidoDiv += '<asp:Label ID="lblMensajeAdvertencia" runat="server" Text="" ClientIDMode="Static"></asp:Label>';
            contenidoDiv += '</div>';
            contenidoDiv += '<div class="modal-footer center-block">';
            contenidoDiv += '<button type="button" class="btn btn-default botonPopup"  onclick="AceptarObtenerDatosDelPaciente();" data-dismiss="modal">Sí</button>';
            contenidoDiv += '<button type="button" class="btn btn-default botonPopup" data-dismiss="modal">No</button>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            contenidoDiv += '</div>';
            target.innerHTML = contenidoDiv;
            cargadoEnDiv = true;
        }
    }

    this.mostrarPopup = function (titulo, mensaje) {
        var selectorEtiquetaTituloAdvertencia = $('#lblTituloAdvertencia');
        var selectorEtiquetaMensajeAdvertencia = $('#lblMensajeAdvertencia');
        var selectorModalMensaje = $('#modalMensajeClinicaDistinta');

        selectorEtiquetaTituloAdvertencia.text(titulo);
        selectorEtiquetaMensajeAdvertencia.text(mensaje);
        selectorModalMensaje.modal('show');
    }
}

var popup = new popup();
var popupDosBotones = new popupDosBotones();
var popupCorreoMismaClinica = new popupCorreoMismaClinica();
var popupCorreoClinicaDistinta = new popupCorreoClinicaDistinta();