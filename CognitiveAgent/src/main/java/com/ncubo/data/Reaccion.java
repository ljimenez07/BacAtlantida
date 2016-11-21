package com.ncubo.data;

import java.sql.Timestamp;

public class Reaccion
{
	private int idOferta;
	private String idUsuario;
	private Timestamp fecha;
	private boolean reaccion;
	
	public Reaccion(int idOferta, String idUsuario, Timestamp fecha, boolean reaccion)
	{
		this.idOferta = idOferta;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.reaccion = reaccion;
	}
	
	public Reaccion(int idOferta, String idUsuario, boolean reaccion)
	{
		this.idOferta = idOferta;
		this.idUsuario = idUsuario;
		this.reaccion = reaccion;
		this.fecha = new Timestamp(System.currentTimeMillis());
	}
	
	public Reaccion(int idOferta, String idUsuario)
	{
		this.idOferta = idOferta;
		this.idUsuario = idUsuario;
	}

	public int getIdOferta()
	{
		return idOferta;
	}

	public void setIdOferta(int idOferta)
	{
		this.idOferta = idOferta;
	}

	public String getIdUsuario()
	{
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario)
	{
		this.idUsuario = idUsuario;
	}

	public Timestamp getFecha()
	{
		return fecha;
	}

	public void setFecha(Timestamp fecha)
	{
		this.fecha = fecha;
	}

	public boolean getReaccion()
	{
		return reaccion;
	}

	public void setReaccion(boolean reaccion)
	{
		this.reaccion = reaccion;
	}
}
