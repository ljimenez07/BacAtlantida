package com.ncubo.bambu.data;

import java.util.Date;

public class TokenDeAcceso
{
	private int vecesUtilizado;
	private int cantidadMaximaDeUsos;
	private boolean expirado;
	private String token;
	private Date fechaEmision;
	private Date fechaExpiracion;
	
	public int obtenerVecesUtilizado()
	{
		return vecesUtilizado;
	}
	public void establecerVecesUtilizado(int vecesUtilizado)
	{
		this.vecesUtilizado = vecesUtilizado;
	}
	public boolean estaExpirado()
	{
		return expirado;
	}
	public void establecerExpirado(boolean expirado)
	{
		this.expirado = expirado;
	}
	public String obtenerToken()
	{
		return token;
	}
	public void establecerToken(String token)
	{
		this.token = token;
	}
	public Date obtenerFechaEmision()
	{
		return fechaEmision;
	}
	public void establecerFechaEmision(Date fechaEmision)
	{
		this.fechaEmision = fechaEmision;
	}
	public Date obtenerFechaExpiracion()
	{
		return fechaExpiracion;
	}
	public void establecerFechaExpiracion(Date fechaExpiracion)
	{
		this.fechaExpiracion = fechaExpiracion;
	}
	public int obtenerCantidadMaximaDeUsos()
	{
		return cantidadMaximaDeUsos;
	}
	public void establecerCantidadMaximaDeUsos(int cantidadMaximaDeUsos)
	{
		this.cantidadMaximaDeUsos = cantidadMaximaDeUsos;
	}
}
