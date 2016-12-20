package com.ncubo.conf;

public enum Intencion 
{
	SALDO("saldo"),
	SALDO_DESCRIPCION("Saldo"),
	MOVIMIENTOS("movimientos"),
	DISPONIBLE("disponible"),
	MOVIMIENTOS_DESCRIPCION("Movimientos"),
	PUNTOS("puntos"),
	TASA_DE_CAMBIO("tasa_cambio"),
	TASA_DE_CAMBIO_DESCRIPCION("Tasa de cambio");
	
	private String nombre;
	Intencion(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String toString()
	{
		return nombre;
	}
}
