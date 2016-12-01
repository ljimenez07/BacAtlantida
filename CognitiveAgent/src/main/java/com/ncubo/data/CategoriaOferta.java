package com.ncubo.data;

public class CategoriaOferta
{
	private int id;
	private String nombre;
	private int peso;
	
	public CategoriaOferta(){ }
	
	public CategoriaOferta(int id, String nombre, int peso)
	{
		this.id = id;
		this.nombre = nombre;
		this.peso = peso;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getNombre()
	{
		return nombre;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
}
