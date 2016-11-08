package com.ncubo.data;

public class Oferta
{
	private int idOferta;
	private String tituloDeOferta;
	private String comercio;
	private String descripcion;
	private String categoria;
	private String ciudad;
	private boolean estado;
	private String restricciones;
	private String vigenciaDesde;
	private String vigenciahasta;
	
	public Oferta(int idOferta, String tituloDeOferta, String comercio, String descripcion, String categoria,
			String ciudad, boolean estado, String restricciones, String vigenciaDesde, String vigenciahasta)
	{
		super();
		this.idOferta = idOferta;
		this.tituloDeOferta = tituloDeOferta;
		this.comercio = comercio;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.ciudad = ciudad;
		this.estado = estado;
		this.restricciones = restricciones;
		this.vigenciaDesde = vigenciaDesde;
		this.vigenciahasta = vigenciahasta;
	}

	public int getIdOferta()
	{
		return idOferta;
	}
	
	public void setIdOferta(int idOferta)
	{
		this.idOferta = idOferta;
	}
	
	public String getTituloDeOferta()
	{
		return tituloDeOferta;
	}
	
	public void setTituloDeOferta(String tituloDeOferta)
	{
		this.tituloDeOferta = tituloDeOferta;
	}
	
	public String getComercio()
	{
		return comercio;
	}
	
	public void setComercio(String comercio)
	{
		this.comercio = comercio;
	}
	
	public String getDescripcion()
	{
		return descripcion;
	}
	
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	
	public String getCategoria()
	{
		return categoria;
	}
	
	public void setCategoria(String categoria)
	{
		this.categoria = categoria;
	}
	
	public String getCiudad()
	{
		return ciudad;
	}
	
	public void setCiudad(String ciudad)
	{
		this.ciudad = ciudad;
	}
	
	public boolean getEstado()
	{
		return estado;
	}
	
	public void setEstado(boolean estado)
	{
		this.estado = estado;
	}
	
	public String getRestricciones()
	{
		return restricciones;
	}
	
	public void setRestricciones(String restricciones)
	{
		this.restricciones = restricciones;
	}
	
	public String getVigenciaDesde()
	{
		return vigenciaDesde;
	}
	
	public void setVigenciaDesde(String vigenciaDesde)
	{
		this.vigenciaDesde = vigenciaDesde;
	}
	
	public String getVigenciahasta()
	{
		return vigenciahasta;
	}
	
	public void setVigenciahasta(String vigenciahasta)
	{
		this.vigenciahasta = vigenciahasta;
	}

}
