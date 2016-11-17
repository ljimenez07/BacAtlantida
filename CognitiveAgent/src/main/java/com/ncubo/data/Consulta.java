package com.ncubo.data;

import java.sql.Timestamp;

public class Consulta
{
	private String intent;
	private Timestamp fecha;
	private String descripcion;
	private int vecesConsultado;
	
	public Consulta()
	{
	}
	
	public Consulta(String intent, Timestamp fecha, String descripcion, int vecesConsultado)
	{
		this.intent = intent;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.vecesConsultado = vecesConsultado;
	}

	public String getIntent()
	{
		return intent;
	}
	
	public void setIntent(String intent)
	{
		this.intent = intent;
	}
	
	public Timestamp getFecha()
	{
		return fecha;
	}
	
	public void setFecha(Timestamp fecha)
	{
		this.fecha = fecha;
	}
	
	public String getDescripcion()
	{
		return descripcion;
	}
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	
	public int getVecesConsultado()
	{
		return vecesConsultado;
	}
	
	public void setVecesConsultado(int vecesConsultado)
	{
		this.vecesConsultado = vecesConsultado;
	}

}
