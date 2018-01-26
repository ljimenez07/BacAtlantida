
var selectorOpcionTabTienda, selectorOpcionTabResumen, selectorOpcionesDeConversion, selectorTabs, selectorDivBotonFinalizarMensaje, selectorAsignaEncargado;
var selectorNombre, selectorApellidos, selectorEmail, selectorTelefono, selectorCelular, selectorDireccion;
var selectorNombreFinal, selectorEmailFinal, selectorTelefonoFinal, selectorFechaEnvio, selectorFechaModificacion;
var mensajeActual = new Object();
var usuarioAtendido = new Object();
var idBandejaMensaje, idCarrito, tipoTramite;
var selectorContenedorTramitesPendientes, selectorContenedorTramitesProcesados, selectorTramitePendientes, selectorTramiteProcesados;
var tablaTramitesPendientes, tablaTramitesProcesados;
var PlantillaLinkVer = '<a href="$$LinkVer$$" target="_blank">Ver</a>';
var PlantillaOpcionSelector = '<option value="$$AtributoId$$">$$AtributoNombre$$</option>';
var esTabHistoricoActivo = true;

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function obtenerIdBandejaDelUrl() {
	var url = window.location.href;
	var pattern = new RegExp("iframeDetalleDeMensaje\/(\\d*)");
	var results = pattern.exec(url);
	return results[1];
}

function mostrarPopup(mensajeDeAdvertencia, redirecccion, callback, esRequeridaJustificacion) {
    popup.mostrarPopup("", mensajeDeAdvertencia, callback, esRequeridaJustificacion);
    if (redirecccion != null) {
        popup.redireccionar(redirecccion);
    }
}

$(document).ready(function () {
    selectorOpcionTabTienda = $("#opcionTabTienda");
    selectorOpcionTabResumen = $("#opcionTabResumen");
    selectorOpcionesDeConversion = $("#divOpcionesDeConversion");
    selectorTabs = $("#tabs");
    selectorDivBotonFinalizarMensaje = $("#divBotonFinalizarMensaje");
    selectorAsignaEncargado = $('#asignaEncargado');

    selectorNombre = $("#txtNombre");
    selectorApellidos = $("#txtApellidos");
    selectorEmail = $("#txtEmail");
    selectorTelefono = $("#txtTelefono");
    selectorCelular = $("#txtCelular");
    selectorDireccion = $("#txaDireccion");

    selectorNombreFinal = $("#txtNombreFinal");
    selectorEmailFinal = $("#txtEmailFinal");
    selectorTelefonoFinal = $("#txtTelefonoFinal");
    selectorFechaEnvio = $("#txtFechaEnvio");
    selectorFechaModificacion = $("#txtFechaModificacion");

    selectorContenedorTramitesPendientes = $("#divContenedorTramitesPendientes");
    selectorContenedorTramitesProcesados = $("#divContenedorTramitesProcesados");
    var plantilla = PlantillaTablaTramite.replace('$$IdTablaTramite$$', 'tablaTramitesPendientes');
    selectorContenedorTramitesPendientes.append(plantilla);
    plantilla = PlantillaTablaTramite.replace('$$IdTablaTramite$$', 'tablaTramitesProcesados');
    selectorContenedorTramitesProcesados.append(plantilla);
    selectorTramitePendientes = $("#tablaTramitesPendientes");
    selectorTramiteProcesados = $("#tablaTramitesProcesados");
    tablaTramitesPendientes = inicializarTabla(selectorTramitePendientes);
    tablaTramitesProcesados = inicializarTabla(selectorTramiteProcesados);

    $('.selectpicker').selectpicker();
    idBandejaMensaje = obtenerIdBandejaDelUrl()
    var existeIdBandejaMensaje = idBandejaMensaje != 0;
    if (existeIdBandejaMensaje) {
        cargarDatosRelacionadosAMensaje(idBandejaMensaje);
    }

    selectorOpcionTabTienda.hide();
    selectorOpcionTabResumen.hide();
    popup.cargar({ "id": "divContenedorPopup" });
    cargarEventoTab();
});

function abrirPaginaDetalleDeContacto() {
    var existeUsuarioCreador = mensajeActual.IdUsuarioCreador != null;
}

function cargarEventoTab() {
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        var tabSeleccionada = $(e.target).attr("href");
        var esTabResumenConfirmacion = tabSeleccionada == '#tabResumen';
        var esTabTienda = tabSeleccionada == '#tabTienda';
        var esTabDatosContacto = tabSeleccionada == '#tabContacto';
        if (esTabDatosContacto) {
        }
        if (esTabTienda) {
            cargarTienda();
        }
        if (esTabResumenConfirmacion) {
            cargarCarritoNoEditable();
        }
    });
}

function cargarDatosRelacionadosAMensaje(idBandejaMensaje) {
	var formData = new FormData();
	formData.append('idBandejaMensaje', idBandejaMensaje);
    $.ajax({
        type: "POST",
        url: "../bandejaMensaje",
        data: formData,
        processData: false,
        contentType: false,
        success: function (respuesta) {
            if (respuesta.d) {
                mensajeActual = respuesta.d;
                selectorFechaEnvio.text(mensajeActual.StringFechaCreacionCompleta);
                selectorFechaModificacion.text(mensajeActual.StringFechaModificacionCompleta);
                cargarUsuariosAsesores();

                var existeUsuarioCreador = mensajeActual.IdUsuarioCreador != null;
                if (existeUsuarioCreador) {
                    cargarDatosContactoCompania();
                }
                else {
                    $("#btnCrearOActualizarContacto span").text("Crear");
                }

                chatSmart.iniciar({
                    divContenedor: '#contenedorChat',
                    tipoChat: 'BandejaDeMensajes',
                    idTabla: idBandejaMensaje
                });

                chatSmart.iniciar({
                    divContenedor: '#contenedorChatHistorico',
                    tipoChat: 'BandejaDeMensajes',
                    idTabla: idBandejaMensaje,
                    hayQueCargarHistoricoDelUsuario: true,
                    idUsuarioACargarHistorico: mensajeActual.Contacto.IdUsuario
                });

                $("a").click(soloMostrarUnChat);
                soloMostrarUnChat();
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            mensajeDeAdvertencia = 'Error al cargar datos de mensaje';
            mostrarPopup(mensajeDeAdvertencia);
        }
    });
}

function soloMostrarUnChat() {
    var esTabChatHistorico = $(this).attr("href") == '#tabChatHistorico';
    if (esTabChatHistorico) {
        esTabHistoricoActivo = true;
        $('#contenedorChat').hide();
    }
    else {
        if (esTabHistoricoActivo) {
            $('#contenedorChat').show();
            esTabHistoricoActivo = false;
        }
    }
}

function cargarDatosContactoCompania() {
    $.ajax({
        type: "POST",
        url: "frm_wdgDetalleMensaje.aspx/ContactoPersona",
        data: JSON.stringify({ idUsuarioCreador: mensajeActual.IdUsuarioCreador, idContactoPersona: mensajeActual.IdCliente }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                usuarioAtendido = respuesta.d;

                cargarDatosDePersona();
                cargarOportunidades(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/OportunidadesContacto");
                cargarOportunidades(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/OportunidadesContacto");
                cargarCotizaciones(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/CotizacionesContacto");
                cargarCotizaciones(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/CotizacionesContacto");
                cargarCasos(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/CasosContacto");
                cargarCasos(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/CasosContacto");
                cargarTareas(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/TareasContacto");
                cargarTareas(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/TareasContacto");
                $("#btnCrearOActualizarContacto span").text("Actualizar");
            }
            else {
                cargarDatosUsuario();
                $("#btnCrearOActualizarContacto span").text("Crear");
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}

function cargarDatosUsuario() {
    $.ajax({
        type: "POST",
        url: "frm_wdgDetalleMensaje.aspx/Usuario",
        data: JSON.stringify({ idUsuarioCreador: mensajeActual.IdUsuarioCreador }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                usuarioAtendido = respuesta.d;
                cargarDatosDePersona();

                cargarOportunidades(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/Oportunidades");
                cargarOportunidades(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/Oportunidades");
                cargarCotizaciones(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/Cotizaciones");
                cargarCotizaciones(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/Cotizaciones");
                cargarCasos(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/Casos");
                cargarCasos(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/Casos");
                cargarTareas(tablaTramitesPendientes, false, "frm_wdgDetalleMensaje.aspx/Tareas");
                cargarTareas(tablaTramitesProcesados, true, "frm_wdgDetalleMensaje.aspx/Tareas");
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}

function convertirAOportunidad() {
    idCarrito = guid();
    tipoTramite = 'Oportunidad';
    selectorOpcionTabTienda.hide();
    selectorOpcionTabResumen.hide();
    crearTramite();
}

function convertirACaso() {
    idCarrito = guid();
    tipoTramite = 'Caso';
    selectorOpcionTabTienda.hide();
    selectorOpcionTabResumen.hide();
    crearTramite();
}

function convertirATarea() {
    idCarrito = guid();
    tipoTramite = 'Tarea';
    selectorOpcionTabTienda.hide();
    selectorOpcionTabResumen.hide();
    crearTramite();
}

function solicitarJustificacion(nombreTramite) {
    if (nombreTramite != null) {
        convertirMensaje(nombreTramite);
    }
    else {
        mensajeDeAdvertencia = 'Indique el motivo por el cual se finaliza el mensaje:';
        mostrarPopup(mensajeDeAdvertencia, null, finalizarMensaje, true);
    }
}

function finalizarMensaje() {
    convertirMensaje();
}

function enviarCorreoDetalleConversacion() {
    var formData = new FormData();
    formData.append("idBandejaMensaje", obtenerIdBandejaDelUrl());
    
    $.ajax({
        type: "POST",
        url: "../enviarCorreoDeConversacion",
        data: formData,
        processData: false,
        contentType: false,
        success: function (respuesta) {
            if (respuesta.status && respuesta.status === 200) {
                mensajeDeAdvertencia = 'Detalle de la conversación enviado correctamente';
            }
            else {
                mensajeDeAdvertencia = 'No se puede enviar correo a usuario anónimo';
            }
            mostrarPopup(mensajeDeAdvertencia);
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            mensajeDeAdvertencia = 'Error al enviar correo';
            mostrarPopup(mensajeDeAdvertencia);
        }
    });
}

function convertirMensaje(nombreTramite) {
    var justificacion = $('#iptJustificacion').val();
    var esRequeridoEnviarCorreo = $('#cbEnviarCorreo').is(':checked');
    
    var formData = new FormData();
    formData.append("idBandejaMensaje", obtenerIdBandejaDelUrl());
    formData.append("justificacion", justificacion);
    formData.append("esRequeridoEnviarCorreo", esRequeridoEnviarCorreo);
    
    $.ajax({
        type: "POST",
        url: "../finalizarMensaje",
        data: formData,
        processData: false,
        contentType: false,
        success: function (respuesta) {
            if (nombreTramite != null) {
                mensajeDeAdvertencia = 'Mensaje convertido a ' + nombreTramite + ' correctamente';
            }
            else {
                mensajeDeAdvertencia = 'Mensaje finalizado correctamente';
            }
            mostrarPopup(mensajeDeAdvertencia, '../bandejaDeMensajes');
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
            mensajeDeAdvertencia = 'Error al finalizar mensaje';
            mostrarPopup(mensajeDeAdvertencia);
        }
    });
}

/* Tabla tramites */
function inicializarTabla(selectorTabla) {
    var tabla = selectorTabla.DataTable({
        "deferRender": true,
        "scrollCollapse": true,
        "stateSave": true,
        "lengthChange": false,
        "info": false,
        "paging": true,
        "searching": false,
        "language": {
            "zeroRecords": "Sin datos para mostrar",
            "paginate": {
                "first": "Primero",
                "last": "Último",
                "next": "Siguiente",
                "previous": "Anterior"
            }
        }
    });
    return tabla;
}

function cargarOportunidades(tablaTramiteUsuario, procesada, urlAjax) {
    $.ajax({
        type: "POST",
        url: urlAjax,
        data: JSON.stringify({ usuarioCandidato: usuarioAtendido, procesada: procesada }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                for (var indice = 0; indice < respuesta.d.length; indice++) {
                    var oportunidad = respuesta.d[indice];
                    var datosOportunidad = ['Oportunidad', oportunidad.Descripcion, oportunidad.NumeroAtencion, oportunidad.StringFechaLlamada];
                    var paginaRedireccion = '../../Llamadas/frm_LibroLlamadaEditar.aspx?data=';
                    generarQueryString(tablaTramiteUsuario, datosOportunidad, paginaRedireccion, 'idLlamada', oportunidad.IdOportunidad);
                }

                tablaTramiteUsuario.page("first").draw(false);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}

function cargarCotizaciones(tablaTramiteUsuario, procesada, urlAjax) {
    $.ajax({
        type: "POST",
        url: urlAjax,
        data: JSON.stringify({ usuarioCandidato: usuarioAtendido, procesada: procesada }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                for (var indice = 0; indice < respuesta.d.length; indice++) {
                    var cotizacion = respuesta.d[indice];
                    var datosCotizacion = ['Cotización', cotizacion.Descripcion, cotizacion.NumeroReferencia, cotizacion.StringFechaCotizacion];
                    var paginaRedireccion = '../../Cotizaciones/frm_ModificarCotizacion.aspx?data=';
                    generarQueryString(tablaTramiteUsuario, datosCotizacion, paginaRedireccion, 'idCotizacion', cotizacion.IdCotizacion);
                }

                tablaTramiteUsuario.page("first").draw(false);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}

function cargarCasos(tablaTramiteUsuario, procesada, urlAjax) {
    $.ajax({
        type: "POST",
        url: urlAjax,
        data: JSON.stringify({ usuarioCandidato: usuarioAtendido, procesada: procesada }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                for (var indice = 0; indice < respuesta.d.length; indice++) {
                    var caso = respuesta.d[indice];
                    var datosCaso = ['Caso', caso.Asunto, caso.NumeroCaso, caso.StringFechaCreacion];
                    var paginaRedireccion = '../../Clientes/frm_CasosEditar.aspx?data=';
                    generarQueryString(tablaTramiteUsuario, datosCaso, paginaRedireccion, 'idCaso', caso.IdCaso);
                }

                tablaTramiteUsuario.page("first").draw(false);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}

function cargarTareas(tablaTramiteUsuario, procesada, urlAjax) {
    $.ajax({
        type: "POST",
        url: urlAjax,
        data: JSON.stringify({ usuarioCandidato: usuarioAtendido, procesada: procesada }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                for (var indice = 0; indice < respuesta.d.length; indice++) {
                    var tarea = respuesta.d[indice];
                    var datosTarea = ['Tarea', tarea.Asunto, tarea.NoReferenciaAtencion, tarea.StringFechaCreacion];
                    var paginaRedireccion = '../../Cotizaciones/frm_ModificarActividadCotizacion.aspx?data=';
                    generarQueryString(tablaTramiteUsuario, datosTarea, paginaRedireccion, 'idTarea', tarea.IdTarea);
                }

                tablaTramiteUsuario.page("first").draw(false);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}

function generarQueryString(tablaTramiteUsuario,datosTramite, paginaRedireccion, nombreId, id) {
    $.ajax({
        type: "POST",
        url: "frm_wdgDetalleMensaje.aspx/QueryString",
        data: JSON.stringify({ nombreId: nombreId, id: id }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (respuesta) {
            if (respuesta.d) {
                var linkVer = PlantillaLinkVer.replace('$$LinkVer$$', paginaRedireccion + respuesta.d);
                tablaTramiteUsuario.row.add([linkVer, datosTramite[0], datosTramite[1], datosTramite[2], datosTramite[3]]);
            }
        },
        error: function (xhr, status, error) {
            console.log(xhr.responseText);
        }
    });
}
