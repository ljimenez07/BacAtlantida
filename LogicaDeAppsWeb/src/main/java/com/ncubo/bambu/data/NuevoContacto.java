package com.ncubo.bambu.data;

public class NuevoContacto
{
	private String nombre;
	private String apellidos;
	private String correoElectronico;
	private String telefono;
	private String celular;
	private String direccion;
	
	public String obtenerNombre()
	{
		return nombre;
	}
	public void establecerNombre(String nombre)
	{
		this.nombre = nombre;
	}
	public String obtenerApellidos()
	{
		return apellidos;
	}
	public void establecerApellidos(String apellidos)
	{
		this.apellidos = apellidos;
	}
	public String obtenerCorreoElectronico()
	{
		return correoElectronico;
	}
	public void establecerCorreoElectronico(String correoElectronico)
	{
		this.correoElectronico = correoElectronico;
	}
	public String obtenerTelefono()
	{
		return telefono;
	}
	public void establecerTelefono(String telefono)
	{
		this.telefono = telefono;
	}
	public String obtenerCelular()
	{
		return celular;
	}
	public void establecerCelular(String celular)
	{
		this.celular = celular;
	}
	public String obtenerDireccion()
	{
		return direccion;
	}
	public void establecerDireccion(String direccion)
	{
		this.direccion = direccion;
	}
	
	
}
