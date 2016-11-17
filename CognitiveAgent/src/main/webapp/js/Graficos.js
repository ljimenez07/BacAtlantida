/**
 * Dont repeat yourself... cambiar para usar solo una función de graficos.
 */

        
function graficarMegusta(seriesMegusta, labelsMegusta)
{
    var data = 
    {
        labels: labelsMegusta,
        series: 
        [
        	seriesMegusta
        ]
    };

    var options = { seriesBarDistance: 15 };

    var responsiveOptions = [
      ['screen and (min-width: 641px) and (max-width: 1024px)', {
        seriesBarDistance: 5,
        axisX: {
          labelInterpolationFnc: function (value) {
            return value;
          }
        }
      }],
      ['screen and (max-width: 640px)', {
        seriesBarDistance: 10,
        axisX: {
          labelInterpolationFnc: function (value) {
            return value[0];
          }
        }
      }]
    ];

    new Chartist.Bar('#grafico-megusta', data, options, responsiveOptions);
}

function graficarNoMegusta(seriesNoMegusta, labelsNoMegusta)
{
    var data = 
    {
        labels: labelsNoMegusta,
        series: 
        [
                seriesNoMegusta
        ]
    };

    var options = { seriesBarDistance: 15 };

    var responsiveOptions = [
      ['screen and (min-width: 641px) and (max-width: 1024px)', {
        seriesBarDistance: 5,
        axisX: {
          labelInterpolationFnc: function (value) {
            return value;
          }
        }
      }],
      ['screen and (max-width: 640px)', {
        seriesBarDistance: 10,
        axisX: {
          labelInterpolationFnc: function (value) {
            return value[0];
          }
        }
      }]
    ]; 
    
    new Chartist.Bar('#grafico-no-megusta', data, options, responsiveOptions);
}

function graficar(seriesMegusta, labelsMegusta, seriesNoMegusta, labelsNoMegusta)
{
    graficarMegusta(seriesMegusta, labelsMegusta);
    graficarNoMegusta(seriesNoMegusta, labelsNoMegusta);
}
        
$(function()
    {
        $("#fecha-desde").datepicker({
        	  dateFormat: "yy-mm-dd"
        });
        $("#fecha-hasta").datepicker({
        	  dateFormat: "yy-mm-dd"
        });
    }
 );