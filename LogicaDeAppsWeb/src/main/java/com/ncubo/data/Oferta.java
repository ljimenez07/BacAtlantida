package com.ncubo.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.BindingResult;

import com.ncubo.util.AtLeastToday;

public class Oferta implements Comparable<Oferta>
{
	private int idOferta;
	
	@NotEmpty(message = "*Campo requerido")
	private String tituloDeOferta;
	
	@NotEmpty(message = "*Campo requerido")
	private String comercio;
	
	@NotEmpty(message = "*Campo requerido")
	private String descripcion;
	private String descripcionAnterior;
	private String descripcionAudio;
		
	private Categorias categorias = new Categorias();
	
	@NotEmpty(message = "*Campo requerido")
	private String ciudad;
	private boolean estado;
	
	@NotEmpty(message = "*Campo requerido")
	private String restricciones;
	
	@NotNull(message = "*Campo requerido")
	@com.ncubo.util.Date
	private Date vigenciaDesde;

	@NotNull(message = "*Campo requerido")
	@com.ncubo.util.Date
	@AtLeastToday
	private Date vigenciaHasta;
	
	@NotEmpty(message = "*Campo requerido")
	private String imagenComercioPath;
	
	@NotEmpty(message = "*Campo requerido")
	private String imagenPublicidadPath;
	private Timestamp fechaHoraRegistro;
	private int likes;
	private int dislikes;
	private boolean esUnUsuarioConocido;
	
	
	public Oferta(){	}
	
	public Oferta(int idOferta, String tituloDeOferta, String comercio, String descripcion, String ciudad, boolean estado, String restricciones, Date vigenciaDesde, Date vigenciahasta, String imagenComercioPath, String imagenPublicidadPath, Timestamp fechaHoraRegistro, int likes, int dislikes)
	{
		this.idOferta = idOferta;
		this.tituloDeOferta = tituloDeOferta;
		this.comercio = comercio;
		this.descripcion = descripcion;
		this.ciudad = ciudad;
		this.estado = estado;
		this.restricciones = restricciones;
		this.vigenciaDesde = vigenciaDesde;
		this.vigenciaHasta = vigenciahasta;
		this.imagenComercioPath = imagenComercioPath;
		this.imagenPublicidadPath = imagenPublicidadPath;
		this.fechaHoraRegistro = fechaHoraRegistro;
		this.likes = likes;
		this.setDislikes(dislikes);
		this.descripcionAudio = "/"+idOferta+"-descripcion.mp3";
		this.descripcionAnterior = descripcion;
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
	
	public Date getVigenciaDesde()
	{
		return vigenciaDesde;
	}
	
	public void setVigenciaDesde(String vigenciaDesde)
	{
		try
		{
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsed = formatter.parse(vigenciaDesde);
			this.vigenciaDesde = new Date(parsed.getTime());
		}
		catch(Exception ex)
		{
		}
	}
	
	public Date getVigenciaHasta()
	{
		return vigenciaHasta;
	}
	
	public void setVigenciaHasta(String vigenciaHasta)
	{
		try
		{
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date parsed = formatter.parse(vigenciaHasta);
			this.vigenciaHasta = new Date(parsed.getTime());
		}
		catch(Exception ex)
		{
		}
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
	
	public boolean getEsHtml()
	{
		return imagenPublicidadPath.endsWith(".html");
	}
	
	public String getImagenParaMostrar()
	{
		return getEsHtml() ? getImagenComercioPath() : getImagenPublicidadPath();
	}
	
	public Timestamp getFechaHoraRegistro()
	{
		return fechaHoraRegistro;
	}
	
	public void setFechaHoraRegistro(Timestamp fechaHoraRegistro)
	{
		this.fechaHoraRegistro = fechaHoraRegistro;
	}
	
	public int getLikes()
	{
		return likes;
	}
	
	public void setLikes(int likes)
	{
		this.likes = likes;
	}
	
	public int getDislikes()
	{
		return dislikes;
	}

	public void setDislikes(int dislikes)
	{
		this.dislikes = dislikes;
	}

	public void setEsUnUsuarioConocido(boolean esUnUsuarioConocido)
	{
		this.esUnUsuarioConocido = esUnUsuarioConocido;
	}

	public boolean getEsUnUsuarioConocido()
	{
		return esUnUsuarioConocido;
	}

	public boolean fechaHastaMayorAFechaDesde() throws ParseException
	{
		if(vigenciaDesde == null || vigenciaHasta == null || vigenciaDesde.equals("") || vigenciaHasta.equals(""))
		{
			return false;
		}
		
		long fechaDesdeEnMilisegundos = vigenciaDesde.getTime();
		long fechaHastaEnMilisegundos = vigenciaHasta.getTime();
		
		return fechaDesdeEnMilisegundos <= fechaHastaEnMilisegundos;
	}
	
	public String getTiempoTranscurrido()
	{
		long fechaHoraRegistroEnMilisegundos = fechaHoraRegistro.getTime();
		long fechaHoraActualEnMilisegundos = new java.util.Date().getTime();
		long tiempoTranscurridoEnMilisegundos = fechaHoraActualEnMilisegundos - fechaHoraRegistroEnMilisegundos;
		
		long dias = TimeUnit.MILLISECONDS.toDays(tiempoTranscurridoEnMilisegundos);
		long horas = TimeUnit.MILLISECONDS.toHours(tiempoTranscurridoEnMilisegundos);
		long minutos = TimeUnit.MILLISECONDS.toMinutes(tiempoTranscurridoEnMilisegundos);
		
		if(dias > 0)
		{
			return String.format("%d día%s", dias, dias == 1 ? "" : "s");
		}
		else if(horas > 0)
		{
			return String.format("%d hora%s", horas, horas == 1 ? "" : "s");
		}
		else if(minutos > 0)
		{
			return String.format("%d minuto%s", minutos, minutos == 1 ? "" : "s");
		}
		else
		{
			return "un momento";
		}
	}
	
	public int compareTo(Oferta oferta)
	{
		return oferta.getFechaHoraRegistro().compareTo(getFechaHoraRegistro());
	}
	
	public void cambiarApostrofes()
	{
		tituloDeOferta = tituloDeOferta.replace("'", "''");
		ciudad = ciudad.replace("'", "''");
		comercio = comercio.replace("'", "''");
		descripcion = descripcion.replace("'", "''");
		restricciones = restricciones.replace("'", "''");
	}

	public String getDescripcionAnterior() 
	{
		return descripcionAnterior;
	}

	public void setDescripcionAnterior(String descripcionAnterior) 
	{
		this.descripcionAnterior = descripcionAnterior;
	}
	
	public boolean cambioLaDescripcion()
	{
		return ! descripcion.trim().equals( descripcionAnterior.trim() );
	}

	public String getDescripcionAudio() {
		return descripcionAudio;
	}

	public void setDescripcionAudio(String descripcionAudio) {
		this.descripcionAudio = descripcionAudio;
	}
	
	public BindingResult validarCampos(BindingResult bindingResult, Oferta oferta) throws ParseException
	{
		if( ! bindingResult.hasFieldErrors("vigenciaHasta") && ! bindingResult.hasFieldErrors("vigenciaDesde"))
		{
			if( ! oferta.fechaHastaMayorAFechaDesde())
			{
				bindingResult.rejectValue("vigenciaHasta", "1", "*Fechas incorrectas");
			}
		}
		return bindingResult;
	}

	public Categorias getCategorias() {
		return categorias;
	}

	public void setCategorias( Categorias categorias) 
	{
		this.categorias = categorias;
	}
	
	public void agregarCategoria( CategoriaOferta  categoria )
	{
		categorias.agregar( categoria );
	}

	public double distanciaEuclidianaDeCategoria(Belleza bellezaArg, Hotel hotelArg, Restaurate restaurateArg) 
	{
		//TODO debe sacarse la distancia por el mismo tipo de categoria
		int index = categorias.indexOf( bellezaArg );
		CategoriaOferta belleza = categorias.get( index );
		
		index = categorias.indexOf( hotelArg );
		CategoriaOferta hotel = categorias.get( index );
		
		index = categorias.indexOf( restaurateArg );
		CategoriaOferta restaurate = categorias.get( index );
		
		double  distancia = Math.sqrt(
			Math.pow(bellezaArg.getPeso() - belleza.getPeso(), 2 ) +
			Math.pow(hotelArg.getPeso() - hotel.getPeso(), 2 ) +
			Math.pow(restaurateArg.getPeso() - restaurate.getPeso(), 2 ) 
		);
		
		return distancia;
	}
	
	
		
}
