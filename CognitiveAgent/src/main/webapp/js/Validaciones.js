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
	function verificarSiSeCargaronLasImagenes()
	{
		var logoComercio = document.getElementById("logo-comercio").value;
		var imagenPublicidad = document.getElementById("imagen-publicidad").value;
		if(logoComercio === '' || imagenPublicidad === '')
		{
			console.log('Las imagenes no se han terminado de cargar. ');
			return false;
		}
		else
			return true;
	}
