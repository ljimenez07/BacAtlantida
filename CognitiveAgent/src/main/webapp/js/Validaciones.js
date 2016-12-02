/**
 * 
 */
	function validarFecha(desde, hasta)
	{
		var pDesde = document.getElementById("desde-requerido");
		var pHasta = document.getElementById("hasta-requerido");
		if(pDesde != "" || pHasta.visibility != "")
		{
			pDesde.style.visibility = "hidden";
			pHasta.style.visibility = "hidden";
		}
		var flag = true;
		if(desde == "")
		{
			var fechaDesde = document.getElementById("fecha-desde");
			fechaDesde.className += " sobresaltar-field";
			pDesde.style.visibility = "visible";
			flag = false;
		}
		if(hasta == "")
		{
			var fechaHasta = document.getElementById("fecha-hasta");
			fechaHasta.className += " sobresaltar-field";
			pHasta.style.visibility = "visible";
			flag =false;
		}
		else if(desde == "" || hasta == "")
		{
			flag = false;
		}
		else
		{
			return true;	
		}
		return flag;	
	}
	
	function sumarUnDiaAlaFechaDada(fecha)
	{
		var arrayDate = fecha.split("-");
		var date = new Date();
		date.setDate(parseInt(arrayDate[2]) + 1 );
		console.log(date.getDate());
		fecha = date.getFullYear() +"-"+ (date.getMonth() +1) +"-"+ date.getDate();
		return fecha;
	}
	function esPaginaEditar()
	{
		// Dado que ingresar y editar son la misma vista.
		// Se efectua una verificacion de campos para ver si las imagenes estan cargas, en tal caso la vista corresponde a EDITAR.
		imagenComercio = $("#logo-comercio").val();
		imagenPublicidad = $("#imagen-publicidad").val();
		
		if(imagenComercio === '' && imagenPublicidad === '')
		{
			return false;
		}		
		else
		{
			return true;
	  	}
	}
	
	function lasImagenesSeTerminaronDeSubir()
	{
		if(imagenPublicidadStatus == true && imagenComercioStatus == true)
			return true;
	}
