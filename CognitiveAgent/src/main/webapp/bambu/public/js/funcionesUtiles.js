function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function guid() {
    return string4Aleatorio() + string4Aleatorio() + "-" + string4Aleatorio() + "-" + string4Aleatorio() + "-" +
      string4Aleatorio() + "-" + string4Aleatorio() + string4Aleatorio() + string4Aleatorio();
}

function guidSinGuiones() {
    return string4Aleatorio() + string4Aleatorio() + "" + string4Aleatorio() + "" + string4Aleatorio() + "" +
      string4Aleatorio() + "" + string4Aleatorio() + string4Aleatorio() + string4Aleatorio();
}

function string4Aleatorio() {
    return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
}

function redimensionarIframe() {
    jQuery(function ($) {
        var lastHeight = 0, curHeight = 0, $frame = $('iframe:eq(0)');
        setInterval(function () {
            curHeight = $frame.contents().find('body').height();
            if (curHeight != lastHeight) {
                $frame.css('height', (lastHeight = curHeight) + 'px');
            }
        }, 500);
    });
}

function fechaJS(fechaCSharp) {
    return new Date(parseInt(fechaCSharp.substr(6)));
}

function escapeRegExp(str) {
    return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
}

function reemplazarTodosCaracteresEncontrados(str, find, replace) {
    return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
}