package com.ncubo.data;

import java.util.ArrayList;

public class Categorias  extends ArrayList<CategoriaOferta>{

	private static final long serialVersionUID = 8724350546196251284L;

	public Categorias()
	{
		this.add(new CategoriaOferta(1, "Restaurate",0));
		this.add(new CategoriaOferta(2, "Belleza",0));
		this.add(new CategoriaOferta(3, "Hoteles",0));
		
	}
}
