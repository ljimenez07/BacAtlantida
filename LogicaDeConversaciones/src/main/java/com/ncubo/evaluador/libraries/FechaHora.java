package com.ncubo.evaluador.libraries;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import com.ncubo.evaluador.interprete.libraries.LanguageException;

public class FechaHora extends Objeto implements Comparable<FechaHora>
{
	private int dia;
	private int mes;
	private int anno;
	private int hora;
	private int minutos;
	private int segundos;

	public FechaHora(int dia, int mes, int anno, int hora, int minutos, int segundos)
	{	
		validaCreacionDeFechaHora(dia, mes, anno, hora, minutos, segundos);
		this.hora = hora;
		this.minutos = minutos;
		this.segundos = segundos;
		this.dia = dia;
		this.mes = mes;
		this.anno = anno;
	}
	
	private void validaCreacionDeFechaHora(int dia, int mes, int anno, int hora, int minutos, int segundos)
	{
		new Fecha(dia, mes, anno);
		if(hora >= 24)
		{
			throw new LanguageException(String.format("La hora ingresada %s no es valido debe estar contenido entre 0-23", hora));
		}

		if(minutos >= 60)
		{
			throw new LanguageException(String.format("Los minutos ingresados %s no son validos debe estar contenido entre 0-59", minutos));
		}

		if(segundos >= 60)
		{
			throw new LanguageException(String.format("Los segundos ingresados %s no son validos debe estar contenido entre 0-59", segundos));
		}
	}

	public int getDia() 
	{
		return dia;
	}

	public int getMes()
	{
		return mes;
	}

	public int getAnno() 
	{
		return anno;
	}

	public int getHora() 
	{
		return hora;
	}

	public int getMinutos() 
	{
		return minutos;
	}

	public int getSegundos() 
	{
		return segundos;
	}

	public int cantidadDeHorasQueHayAl( FechaHora otraFechaHora)
	{
		Date miFecha = covertirFechaADate();
		Date laOtraFecha = otraFechaHora.covertirFechaADate();
		

		int resultado = (int) ( ( laOtraFecha.getTime() - miFecha.getTime() ) / (1000 * 60 * 60));
		return resultado ;
	}
	
	private static final Calendar cal = GregorianCalendar.getInstance();
	private Date covertirFechaADate()
	{
		cal.clear();
		cal.set(anno, mes-1, dia, hora, minutos, segundos);
		Date dateRepresentation = cal.getTime();
		return dateRepresentation;
	}

	public Mes mesDeLaFecha()
	{
		return new Mes(Meses.obtenerMes(mes), anno);
	}

	public String toMySQLFormat()
	{
		return "\'" + anno + '-' + mes + '-' + dia + ' ' + hora + ':' + minutos + ':' + segundos + "\'";
	}

	@Override
	public String show()
	{
		String result = toString();
		return result;
	}
	
	@Override
	public String toString()
	{
		String resultado = (dia < 10 ? "0" + dia : "" + dia) + (mes < 10 ? "/0" + mes : "/" + mes) + "/" + anno + ' ' +
				(hora < 10 ? "0" + hora : "" + hora) + (minutos < 10 ? ":0" + minutos : ":" + minutos) + (segundos < 10 ? ":0" + segundos : ":" + segundos);	
		return resultado;  
	}

	public boolean esMayorQue(FechaHora otraFecha) 
	{
		Date miFecha = covertirFechaADate();
		Date laOtraFecha = otraFecha.covertirFechaADate();
		
		return miFecha.after(laOtraFecha);
	}
	
	public boolean esMayorOIgualQue(FechaHora otraFecha)
	{	
		Date miFecha = covertirFechaADate();
		Date laOtraFecha = otraFecha.covertirFechaADate();

		boolean esMayor = miFecha.after(laOtraFecha);
		boolean sonIguales = miFecha.equals(laOtraFecha);
		
		if( esMayor || sonIguales )
		{
			return true;
		}
		else
		{
			return false;	
		}
	}
	
	public boolean esMenorOIgualQue(FechaHora otraFecha)
	{
		int fechaActual = convertirAEntero(this);
		int fechaAComparar = convertirAEntero(otraFecha);      
		
		return fechaActual <= fechaAComparar;
	}
	
	int convertirAEntero(FechaHora fecha)
	{
		return fecha.anno * 1000000 + fecha.mes * 100000 + fecha.dia * 1000 + fecha.hora * 100 + fecha.minutos * 10 + fecha.segundos;
	}
	
	public Fecha Fecha()
	{
		return new Fecha(dia, mes, anno);
	}

	public boolean esIgualQue(FechaHora fechaUltimoComando)
	{
		long fechaActual = Long.parseLong
				(
						""+anno +
						mes +
						dia +
						hora +
						minutos +
						segundos
						);
		long fechaAComparar = Long.parseLong
				(
						""+
								fechaUltimoComando.anno +
								fechaUltimoComando.mes +
								fechaUltimoComando.dia +
								fechaUltimoComando.hora +
								fechaUltimoComando.minutos +
								fechaUltimoComando.segundos
						);   
		return fechaActual == fechaAComparar ;
	}

	@Override
	public boolean esIgualQue(Objeto objeto) 
	{
		FechaHora fechaHora = null;
		try
		{
			fechaHora = (FechaHora)objeto;
		}
		catch(ClassCastException e)
		{
			throw new LanguageException(
					String.format("En la comparación se esperaba el valor de tipo [%s] pero se encontro un valor de tipo [%s]", Hilera.class.getSimpleName(), objeto.getClass().getSimpleName())
					);
		}
		return dia == fechaHora.dia && mes == fechaHora.mes && anno == fechaHora.anno && hora == fechaHora.hora &&
				minutos == fechaHora.minutos && segundos == fechaHora.segundos;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + anno;
		result = prime * result + dia;
		result = prime * result + hora;
		result = prime * result + mes;
		result = prime * result + minutos;
		result = prime * result + segundos;
		return result;
	}

	@Override
	public boolean equals(Object objeto) 
	{
		FechaHora otraFecha = (FechaHora) objeto;
		
		return anno == otraFecha.anno && mes == otraFecha.mes && dia == otraFecha.dia && hora == otraFecha.hora && minutos == otraFecha.minutos && segundos == otraFecha.segundos;
	}

	@Override
	public boolean noEsIgualQue(Objeto objeto)
	{
		return ! esIgualQue(objeto);
	}
	
	public Fecha toFecha()
	{
		return new Fecha(dia, mes, anno);
	}
	
	public int compareTo( FechaHora fechaHora )
	{
		int id = hashCode();
		int idArgumento = fechaHora.hashCode();
		
		return id - idArgumento;
	}
	
	public int cantidadDiasQueHayAl(FechaHora otraFecha)
	{
		Date miFecha = this.covertirFechaADate();
		Date laOtraFecha = otraFecha.covertirFechaADate();

		int resultado = (int) ( ( laOtraFecha.getTime() - miFecha.getTime() ) / (1000 * 60 * 60 * 24));
		return resultado; 
	}
	
	public static FechaHora demeLaFechaDiasAtras(Numero cantidad)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.DATE, -cantidad.valor);
		FechaHora fechaActual = new FechaHora(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		
		return fechaActual;
	}
	
	public static FechaHora demeLaFechaMesesAtras(Numero cantidad)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.add(Calendar.MONTH, -cantidad.valor);
		FechaHora fechaActual = new FechaHora(calendar.get(Calendar.DATE), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		
		return fechaActual;
	}
	
	Hilera fechaCompleta() 
	{
		Fecha fecha = new Fecha(dia, mes, anno);
		Hashtable<Integer,String> mes = new Hashtable<Integer,String>();

		mes.put(new Integer(1), "enero");
		mes.put(new Integer(2), "febrero");
		mes.put(new Integer(3), "marzo");
		mes.put(new Integer(4), "abril");
		mes.put(new Integer(5), "mayo");
		mes.put(new Integer(6), "junio");
		mes.put(new Integer(7), "julio");
		mes.put(new Integer(8), "agosto");
		mes.put(new Integer(9), "septiembre");
		mes.put(new Integer(10), "octubre");
		mes.put(new Integer(11), "noviembre");
		mes.put(new Integer(12), "diciembre");

		String fechaEnCadena = fecha.getDiaDeLaSemana() + " " + fecha.getDia() + " de " + mes.get(fecha.getMes()) +" del " + fecha.getAnno()+", "+hora+":"+minutos;
		return new Hilera(fechaEnCadena);
	}
	
	public static FechaHora fromString(Fecha fecha, String unaFecha)
	{
		int hora = hora(unaFecha);
		int minuto = minuto(unaFecha);
		int segundo = segundo(unaFecha);
		return new FechaHora(fecha.getDia(), fecha.getMes(), fecha.getAnno(), hora, minuto, segundo);
	}
	
	private static int hora (String unaFecha)
	{
		final char SEPARADOR = ':';
		int index = unaFecha.indexOf(SEPARADOR);
		return Integer.parseInt(unaFecha.substring(0, index));
	}
	
	private static int minuto (String unaFecha)
	{
		final char SEPARADOR = ':';
		int indexIni = unaFecha.indexOf(SEPARADOR);
		int indexFin = unaFecha.lastIndexOf(SEPARADOR);
		return Integer.parseInt(unaFecha.substring(indexIni + 1, indexFin));
	}
	
	private static int segundo (String unaFecha)
	{
		final char SEPARADOR = ':';
		int index = unaFecha.lastIndexOf(SEPARADOR);
		return Integer.parseInt(unaFecha.substring(index+1));
	}

}

