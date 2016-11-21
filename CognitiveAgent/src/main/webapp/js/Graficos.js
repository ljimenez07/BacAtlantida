/**
 * Graficos para obtener una visualización de cuantos me gusta y no me gusta se han dado a las promociones.
 */

var graficoMeGusta = null;  
var graficoNoMeGusta = null;
var h3MeGusta = null;
var h3NoMeGusta = null;

function graficoDePastel(labelsMegusta, seriesMegusta, labelsNomegusta, seriesNomegusta)
{
	if(graficoMeGusta != null && graficoNoMeGusta != null)
	{
		graficoMeGusta.destroy();
		graficoNoMeGusta.destroy();
	}

	crearTitulosParaLosGraficos();
	
	var ctx = document.getElementById('megusta');
	var ctx2 = document.getElementById('nomegusta');
	
	var colores = [];
	for(etiqueta of labelsMegusta)
	{
		colores.push(generarColor());
	}
	generarColor();
	
	var data = 
		{
		    labels: labelsMegusta,
		    datasets: 
		    	[{
		            data: seriesMegusta,
		            backgroundColor: colores,
		            hoverBackgroundColor:colores
		        }]
		};
	var dataNomegusta = 
	{
	    labels: labelsNomegusta,
	    datasets: 
	    	[{
	            data: seriesNomegusta,
	            backgroundColor: colores,
	            hoverBackgroundColor:colores
	        }]
	};
	
	// me gusta
		graficoMeGusta = new Chart(ctx,{
	    type: 'pie',
	    data: data,
	    options: {
	        scales: {
	            yAxes: [{
	                display:false
	            }]
	        }
	    }
	});
		
	// no me gusta
		graficoNoMeGusta = new Chart(ctx2,{
		    type: 'pie',
		    data: dataNomegusta,
		    options: {
		        scales: {
		            yAxes: [{
		                display:false
		            }]
		        }
		    }
		});	
	
}

function crearTitulosParaLosGraficos()
{
	var ctx = document.getElementById("titulo-megusta");
	var ctx2 = document.getElementById("titulo-Nomegusta");
	
	if(h3MeGusta != null && h3NoMeGusta != null)
	{
		ctx.removeChild(h3MeGusta);
		ctx2.removeChild(h3NoMeGusta);
	}
	var ctx = document.getElementById("titulo-megusta");
	var ctx2 = document.getElementById("titulo-Nomegusta");
	h3MeGusta = document.createElement("h3");
	h3NoMeGusta = document.createElement("h3");
	
	var textMeGusta = document.createTextNode("Me gusta");
	var textNoMeGusta = document.createTextNode("No me gusta");
	
	h3MeGusta.appendChild(textMeGusta);
	h3NoMeGusta.appendChild(textNoMeGusta);	
	ctx.appendChild(h3MeGusta);
	ctx2.appendChild(h3NoMeGusta);
}

function generarColor()
{
	//var color = '#'+Math.floor(Math.random()*16777215).toString(16);
	var color = 'rgb(' + (Math.floor(Math.random() * 256)) + ',' + (Math.floor(Math.random() * 256)) + ',' + (Math.floor(Math.random() * 256)) + ')';
	return color;
}

$(function()
    {
        $("#fecha-desde").datepicker({
        	  dateFormat: "yy-mm-dd",
        	    beforeShow : function(){
                    jQuery( this ).datepicker('option','maxDate', jQuery('#fecha-hasta').val() );
        } 
        });
        $("#fecha-hasta").datepicker({
        	  dateFormat: "yy-mm-dd",
        	  beforeShow : function(){
                  jQuery( this ).datepicker('option','minDate',    jQuery('#fecha-desde').val() );
        	  } 
        });
        
        
    }
 );