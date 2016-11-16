package com.ncubo.data;

public class Reaccion 
{
	private String id_oferta;
	private String fecha;
	private int cantidad_like;
	private int cantidad_dislike;
	private String tituloLabelDislike;
	private String categoriaOferta;
	private String tituloLabel;
	
	public String getTituloLabel()
	{
		return tituloLabel;
	}
	
	public void setTituloLabel(String tituloLabel)
	{
		this.tituloLabel = tituloLabel;
	}
	
	public String getTituloLabelDislike()
	{
		return tituloLabelDislike;
	}
	
	public void setTituloLabelDislike(String nombreOferta)
	{
		this.tituloLabelDislike = nombreOferta;
	}
	
	public String getCategoriaOferta()
	{
		return categoriaOferta;
	}
	
	public void setCategoriaOferta(String categoriaOferta)
	{
		this.categoriaOferta = categoriaOferta;
	}
	
	public String getId_oferta()
	{
		return id_oferta;
	}
	
	public void setId_oferta(String id_oferta)
	{
		this.id_oferta = id_oferta;
	}
	
	public String getFecha()
	{
		return fecha;
	}
	
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	
	public int getCantidad_like()
	{
		return cantidad_like;
	}
	
	public void setCantidad_like(int cantidad_like)
	{
		this.cantidad_like = cantidad_like;
	}
	
	public int getCantidad_dislike()
	{
		return cantidad_dislike;
	}
	
	public void setCantidad_dislike(int cantidad_dislike)
	{
		this.cantidad_dislike = cantidad_dislike;
	}
}
