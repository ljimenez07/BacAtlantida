package com.ncubo.data;

import java.nio.channels.NetworkChannel;
import java.util.ArrayList;

public class Categorias  extends ArrayList<CategoriaOferta>{

	private static final long serialVersionUID = 8724350546196251284L;

	public Categorias()
	{
		this.add(new Restaurate(0));
		this.add(new Belleza(0));
		this.add(new Hotel(0));
		
	}
	
	public Categorias(double pesoRestaurante, double pesoBelleza, double pesoHoteles)
	{
		this.add(new Restaurate(pesoRestaurante));
		this.add(new Belleza(pesoBelleza));
		this.add(new Hotel(pesoHoteles));
		
	}

	public void agregar(CategoriaOferta categoria)
	{
		int indice = this.indexOf( categoria );
		if ( indice == -1 )
		{
			this.add( categoria );
		}
		else
		{
			this.get( indice ).setPeso( categoria.getPeso() );
		}
		
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
	
	public Restaurate obtenerCategoriaDeRestaurante()
	{
		int indice = this.indexOf( new Restaurate(0) );
		return (Restaurate) this.get(indice);
	}
	
	
}
