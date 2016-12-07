package com.ncubo.data;

import java.io.Serializable;

public class CategoriaOferta implements Serializable
{
	private int id;
	private String nombre;
	private double peso;
	
	public CategoriaOferta(){ }
	
	public CategoriaOferta(int id, String nombre, double d)
	{
		this.id = id;
		this.nombre = nombre;
		this.peso = d;
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

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof CategoriaOferta) )
			return false;
		CategoriaOferta other = (CategoriaOferta) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
