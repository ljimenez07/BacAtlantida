package com.ncubo.data;

import java.util.ArrayList;

public class Categorias extends ArrayList<CategoriaOferta>
{
	private static final long serialVersionUID = 8724350546196251284L;

	public Categorias()
	{
		this.add(new Restaurante(0));
		this.add(new Belleza(0));
		this.add(new Hotel(0));
	}
	
	public Categorias(double pesoRestaurante, double pesoBelleza, double pesoHoteles)
	{
		this.add(new Restaurante(pesoRestaurante));
		this.add(new Belleza(pesoBelleza));
		this.add(new Hotel(pesoHoteles));
	}

	public void agregar(CategoriaOferta categoria)
	{
		int indice = obtenerIndiceDeLaCategoriaConId(categoria.getId());
		if (indice == -1)
		{
			this.add(categoria);
		}
		else
		{
			this.get(indice).setPeso(categoria.getPeso());
		}
	}
	
	private int obtenerIndiceDeLaCategoriaConId(int id)
	{
		for(CategoriaOferta categoria : this)
		{
			if(categoria.getId() == id)
			{
				return this.indexOf(categoria);
			}
		}
		return -1;
	}
	
	public Belleza obtenerCategoriaDeBelleza()
	{
		int indice = this.indexOf( new Belleza(0) );
		return (Belleza) this.get(indice);
	}
	
	public Hotel obtenerCategoriaDeHotel()
	{
		int indice = this.indexOf( new Hotel(0) );
		return (Hotel) this.get(indice);
	}
	
	public Restaurante obtenerCategoriaDeRestaurante()
	{
		int indice = this.indexOf( new Restaurante(0) );
		return (Restaurante) this.get(indice);
	}
	
}
