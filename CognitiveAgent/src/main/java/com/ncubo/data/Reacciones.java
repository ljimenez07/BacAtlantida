package com.ncubo.data;

public class Reacciones 
{
	private String idOferta;
	private String idUsuario;
	private String fecha;
	private int cantidadLikes;
	private int cantidadDislikes;
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
	
	public String getIdOferta()
	{
		return idOferta;
	}
	
	public void setIdOferta(String idOferta)
	{
		this.idOferta = idOferta;
	}
	
	public String getIdUsuario()
	{
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario)
	{
		this.idUsuario = idUsuario;
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
		return cantidadLikes;
	}
	
	public void setCantidadLikes(int cantidadLikes)
	{
		this.cantidadLikes = cantidadLikes;
	}
	
	public int getCantidadDislikes()
	{
		return cantidadDislikes;
	}
	
	public void setCantidadDislikes(int cantidadDislikes)
	{
		this.cantidadDislikes = cantidadDislikes;
	}
}
