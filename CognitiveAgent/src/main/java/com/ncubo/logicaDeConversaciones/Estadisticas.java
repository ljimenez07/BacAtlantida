package com.ncubo.logicaDeConversaciones;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.ncubo.chatbot.partesDeLaConversacion.Tema;
import com.ncubo.dao.ConsultaDao;
import com.ncubo.data.Consulta;

public class Estadisticas
{
	private Hashtable<Tema, Integer> detalles;
	@Autowired
	private ConsultaDao consultaDao;
	
	public Estadisticas(ConsultaDao consultaDao)
	{
		detalles = new Hashtable<>();
		this.consultaDao = consultaDao;
	}
	
	public void darSeguimiento(Tema tema)
	{
		Integer valorDelKey = detalles.get(tema);
		detalles.put( tema, (valorDelKey == null) ? 1 : ++valorDelKey);
	}
	
	public Hashtable<Tema, Integer> obtenerDetalles()
	{
		return detalles;
	}
	
	public void guardarEstadiscitasEnBaseDeDatos() throws ClassNotFoundException, SQLException
	{
		for(Entry<Tema, Integer> estadistica : detalles.entrySet())
		{
			consultaDao.insertar( new Consulta(estadistica.getKey(), new Timestamp(new Date().getTime()), estadistica.getValue()) );
		}
	}

}
