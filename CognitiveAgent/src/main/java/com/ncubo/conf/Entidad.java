package com.ncubo.conf;

public enum Entidad 
{
	MONEDA("moneda"),
	DOLAR("dolar"),
	EURO("euro"),
	PRODUCTOS("productos"),
	TIEMPO("tiempo");
	
	private String nombre;
	Entidad(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String toString()
	{
		return nombre;
	}
}
