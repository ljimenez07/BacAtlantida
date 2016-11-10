package com.ncubo.data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Oferta
{
	private int idOferta;
	private String tituloDeOferta;
	private String comercio;
	private String descripcion;
	private CategoriaOferta categoria;
	private String ciudad;
	private boolean estado;
	private String restricciones;
	private String vigenciaDesde;
	private String vigenciahasta;
	private String imagenComercioPath;
	private String imagenPublicidadPath;
	private Timestamp fechaHoraRegistro;

	public Oferta(int idOferta, String tituloDeOferta, String comercio, String descripcion, CategoriaOferta categoria,
			String ciudad, boolean estado, String restricciones, String vigenciaDesde, String vigenciahasta,
			String imagenComercioPath, String imagenPublicidadPath, Timestamp fechaHoraRegistro)
	{
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
		this.imagenComercioPath = imagenComercioPath;
		this.imagenPublicidadPath = imagenPublicidadPath;
		this.fechaHoraRegistro = fechaHoraRegistro;
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
	
	public CategoriaOferta getCategoria()
	{
		return categoria;
	}
	
	public void setCategoria(CategoriaOferta categoria)
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

	public String getImagenComercioPath()
	{
		return imagenComercioPath;
	}

	public void setImagenComercioPath(String imagenComercioPath)
	{
		this.imagenComercioPath = imagenComercioPath;
	}

	public String getImagenPublicidadPath()
	{
		return imagenPublicidadPath;
	}

	public void setImagenPublicidadPath(String imagenPublicidadPath)
	{
		this.imagenPublicidadPath = imagenPublicidadPath;
	}

	public Timestamp getFechaHoraRegistro()
	{
		return fechaHoraRegistro;
	}

	public void setFechaHoraRegistro(Timestamp fechaHoraRegistro)
	{
		this.fechaHoraRegistro = fechaHoraRegistro;
	}
	
	public String getTiempoTranscurrido()
	{
		long fechaHoraRegistroEnMilisegundos = fechaHoraRegistro.getTime();
		long fechaHoraActualEnMilisegundos = new Date().getTime();
		long tiempoTranscurridoEnMilisegundos = fechaHoraActualEnMilisegundos - fechaHoraRegistroEnMilisegundos;
		
		long dias = TimeUnit.MILLISECONDS.toDays(tiempoTranscurridoEnMilisegundos);
		long horas = TimeUnit.MILLISECONDS.toHours(tiempoTranscurridoEnMilisegundos);
		long minutos = TimeUnit.MILLISECONDS.toMinutes(tiempoTranscurridoEnMilisegundos);
		
		if(dias > 0)
		{
			return String.format("%d días", dias);
		}
		else if(horas > 0)
		{
			return String.format("%d horas", horas);
		}
		else if(minutos > 0)
		{
			return String.format("%d minutos", minutos);
		}
		else
		{
			return String.format("%d segundos", TimeUnit.MILLISECONDS.toSeconds(tiempoTranscurridoEnMilisegundos));
		}
	}

}
