package com.ncubo.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validacion
{
	public static boolean fechaHastaMayorOIgualAFechaDesde(String fechaDesde, String fechaHasta) throws ParseException
	{
		if(fechaDesde == null || fechaHasta == null || fechaDesde.equals("") || fechaHasta.equals(""))
		{
			return false;
		}
		DateFormat formatter;
		formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date fechaDesdeDate = formatter.parse(fechaDesde.replace("-", "/"));
		Timestamp timeStampFechaDesde = new Timestamp(fechaDesdeDate.getTime());
		Date fechaHastaDate = formatter.parse(fechaHasta.replace("-", "/"));
		Timestamp timeStampFechaHasta = new Timestamp(fechaHastaDate.getTime());
		
		long fechaDesdeEnMilisegundos = timeStampFechaDesde.getTime();
		long fechaHastaEnMilisegundos = timeStampFechaHasta.getTime();
		
		return fechaDesdeEnMilisegundos <= fechaHastaEnMilisegundos;
	}
}
