package com.ncubo.bambu.data;

public class Origen
{
	private int idOrigen;
	private String descripcionOrigen;
	private String rutaImagen;
	
	public int obtenerIdOrigen()
	{
		return idOrigen;
	}
	public void establecerIdOrigen(int idOrigen)
	{
		this.idOrigen = idOrigen;
	}
	public String obtenerDescripcionOrigen()
	{
		return descripcionOrigen;
	}
	public void establecerDescripccionOrigen(String descripcionOrigen)
	{
		this.descripcionOrigen = descripcionOrigen;
	}
	public String obtenerRutaImagen()
	{
		return rutaImagen;
	}
	public void establecerRutaImagen(String rutaImagen)
	{
		this.rutaImagen = rutaImagen;
	}
}
