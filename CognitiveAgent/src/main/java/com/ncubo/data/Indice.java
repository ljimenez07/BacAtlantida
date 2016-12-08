package com.ncubo.data;

public class Indice {
	
	private final int MARGEN = 10;
	private int indice;
	private int pagina;
	
	public Indice(int pagina)
	{
		this.indice = (pagina - 1) * 10 ;
		this.pagina = pagina;
	}

	public int valorEntero() {
		return indice;
	}

	public void agregarleDiez() {
		indice = indice + MARGEN;
		pagina = pagina +1;
	}

	public int getIndice() {
		return indice;
	}

	public int getPagina() {
		return pagina;
	}

}
