package com.ncubo.bambu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.log4j.Logger;

public class Fecha
{
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(Fecha.class);
	}
	
	public static String obtenerCadenaFechaActual()
	{

		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		LocalDateTime ahora = LocalDateTime.now();
		return formatter.format(ahora);
	}
	
	public static Date obtenerFechaActual()
	{
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String stringFechaActual = obtenerCadenaFechaActual();
		Date fechaActual = null;
		try
		{
			fechaActual = formatter.parse(stringFechaActual);
		} 
		catch (ParseException e)
		{
			LOG.error("error", e);
		}
		return fechaActual;
	}
	
	public static String calcularTiempoTranscurrido(String fechaInclusiva)
	{
		if (fechaInclusiva != null)
		{
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			LocalDateTime _fechaExclusiva = LocalDateTime.now();
			LocalDateTime _fechaInclusiva = LocalDateTime.parse(fechaInclusiva, formatter);
			final long years = ChronoUnit.YEARS.between(_fechaInclusiva, _fechaExclusiva);
			final long months = ChronoUnit.MONTHS.between(_fechaInclusiva, _fechaExclusiva);
			final long days = ChronoUnit.DAYS.between(_fechaInclusiva, _fechaExclusiva);
			final long hours = ChronoUnit.HOURS.between(_fechaInclusiva, _fechaExclusiva);
			final long minutes = ChronoUnit.MINUTES.between(_fechaInclusiva, _fechaExclusiva);

			String tiempoTranscurrido = "hace pocos segundos";
			if (years > 0)
			{
				tiempoTranscurrido = years > 1 ? "hace " + years + " años" : "hace un año";
			} else if (months > 0)
			{
				tiempoTranscurrido = months > 1 ? "hace " + months + " meses" : "hace un mes";
			} else if (days > 0)
			{
				tiempoTranscurrido = days > 1 ? "hace " + days + " días" : "hace un día";
			} else if (hours > 0)
			{
				tiempoTranscurrido = hours > 1 ? "hace " + hours + " horas" : "hace una hora";
			} else if (minutes > 0)
			{
				tiempoTranscurrido = minutes > 1 ? "hace " + minutes + " minutos" : "hace un minuto";
			}
			return tiempoTranscurrido;
		}
		return null;
	}
}
