package com.ncubo.bambu.data;

import java.util.Date;

public class BandejaDeMensaje
{
	private int idBandejaMensaje;
	private int idUsuarioCreador;
	private int idUsuarioEncargado;
	private int idCliente;
	private int idOrigen;
	private int idEmpresa;
	private String motivoFinalizacion;
	private String codigoChat;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private boolean procesado;
	private boolean visto;
	private boolean favorito;
	
	private UsuarioBambu usuarioCreador;
	private UsuarioBambu usuarioEncargado;
	private Origen origen;
	private PersonaContacto cliente;
	private Empresa empresa;

	private boolean asignadoAUsuarioLogueado;
	private String nombreCompletoCreador;
	private String emailCreador;
	private String nombreCompletoEncargado;
	private String stringFechaDeCreacion;
	private String stringFechaDeCreacionCompleta;
	private String stringFechaModificacionCompleta;
	private String tiempoTranscurrido;
	
	public enum Atributo
	{
		ID_BANDEJA("idBandejaMensaje", Tipo.INT),
		ID_CREADOR("idUsuarioCreador", Tipo.INT),
		ID_ENCARGADO("idUsuarioEncargado", Tipo.INT),
		CODIGO_CHAT("codigoChat", Tipo.STRING),
		MOTIVO_FINALIZACION("motivoFinalizacion", Tipo.STRING),
		FECHA_CREACION("fechaCreacion", Tipo.DATE),
		FECHA_MODIFICACION("fechaModificacion", Tipo.DATE),
		PROCESADO("procesado", Tipo.BOOLEAN),
		VISTO("visto", Tipo.BOOLEAN),
		FAVORITO("favorito", Tipo.BOOLEAN),
		ID_CLIENTE("idCliente", Tipo.INT),
		ID_EMPRESA("idEmpresa", Tipo.INT);
		
		private String nombre;
		private Tipo tipo;
		
		private Atributo(String nombre, Tipo tipo)
		{
			this.nombre = nombre;
			this.tipo = tipo;
		}
		
		public Tipo obtenerTipo()
		{
			return tipo;
		}
		
		public String obtenerNombre()
		{
			return nombre;
		}
		
		public String toString()
		{
			return nombre;
		}
	}
	
	public UsuarioBambu obtenertUsuarioEncargado()
	{
		return usuarioEncargado;
	}
	public void establecerUsuarioEncargado(UsuarioBambu usuarioEncargado)
	{
		this.usuarioEncargado = usuarioEncargado;
	}
	public UsuarioBambu obtenerUsuarioCreador()
	{
		return usuarioCreador;
	}
	public void establecerUsuarioCreador(UsuarioBambu usuarioCreador)
	{
		this.usuarioCreador = usuarioCreador;
	}
	public int obtenerIdBandejaMensaje()
	{
		return idBandejaMensaje;
	}
	public void establecerIdBandejaMensaje(int idBandejaMensaje)
	{
		this.idBandejaMensaje = idBandejaMensaje;
	}
	public int obtenerIdUsuarioCreador()
	{
		return idUsuarioCreador;
	}
	public void establecerIdUsuarioCreador(int idUsuarioCreador)
	{
		this.idUsuarioCreador = idUsuarioCreador;
	}
	public int obtenerIdUsuarioEncargado()
	{
		return idUsuarioEncargado;
	}
	public void establecerIdUsuarioEncargado(int idUsuarioEncargado)
	{
		this.idUsuarioEncargado = idUsuarioEncargado;
	}
	public int obtenerIdCliente()
	{
		return idCliente;
	}
	public void establecerIdCliente(int idCliente)
	{
		this.idCliente = idCliente;
	}
	public int obtenerIdOrigen()
	{
		return idOrigen;
	}
	public void establecerIdOrigen(int idOrigen)
	{
		this.idOrigen = idOrigen;
	}
	public int obtenerIdEmpresa()
	{
		return idEmpresa;
	}
	public void establecerIdEmpresa(int idEmpresa)
	{
		this.idEmpresa = idEmpresa;
	}
	public PersonaContacto obtenerContacto()
	{
		return cliente;
	}
	public void establecerContacto(PersonaContacto contacto)
	{
		this.cliente = contacto;
	}
	public Empresa obtenerEmpresa()
	{
		return empresa;
	}
	public void establecerEmpresa(Empresa empresa)
	{
		this.empresa = empresa;
	}
	public Origen obtenerOrigen()
	{
		return origen;
	}
	public void establecerOrigen(Origen origen)
	{
		this.origen = origen;
	}
	public String obtenerMotivoFinalizacion()
	{
		return motivoFinalizacion;
	}
	public void establecerMotivoFinalizacion(String motivoFinalizacion)
	{
		this.motivoFinalizacion = motivoFinalizacion;
	}
	public String obtenerCodigoChat()
	{
		return codigoChat;
	}
	public void establecerCodigoChat(String codigoChat)
	{
		this.codigoChat = codigoChat;
	}
	public String obtenerNombreCompletoCreador()
	{
		return nombreCompletoCreador;
	}
	public void establecerNombreCompletoCreador(String nombreCompletoCreador)
	{
		this.nombreCompletoCreador = nombreCompletoCreador;
	}
	public String obtenerEmailCreador()
	{
		return emailCreador;
	}
	public void establecerEmailCreador(String emailCreador)
	{
		this.emailCreador = emailCreador;
	}
	public String obtenerNombreCompletoEncargado()
	{
		return nombreCompletoEncargado;
	}
	public void establecerNombreCompletoEncargado(String nombreCompletoEncargado)
	{
		this.nombreCompletoEncargado = nombreCompletoEncargado;
	}
	public Date obtenerFechaCreacion()
	{
		return fechaCreacion;
	}
	public void establecerFechaCreacion(Date fechaCreacion)
	{
		this.fechaCreacion = fechaCreacion;
	}
	public Date obtenerFechaModificacion()
	{
		return fechaModificacion;
	}
	public void establecerFechaModificacion(Date fechaModificacion)
	{
		this.fechaModificacion = fechaModificacion;
	}
	public boolean esProcesado()
	{
		return procesado;
	}
	public void establecerProcesado(boolean procesado)
	{
		this.procesado = procesado;
	}
	public boolean esVisto()
	{
		return visto;
	}
	public void establecerVisto(boolean visto)
	{
		this.visto = visto;
	}
	public boolean esFavorito()
	{
		return favorito;
	}
	public void establecerFavorito(boolean favorito)
	{
		this.favorito = favorito;
	}
	public boolean esAsignadoAUsuarioLogueado()
	{
		return asignadoAUsuarioLogueado;
	}
	public void establecerAsignadoAUsuarioLogueado(boolean asignadoAUsuarioLogueado)
	{
		this.asignadoAUsuarioLogueado = asignadoAUsuarioLogueado;
	}
	public String obtenerStringFechaDeCreacion()
	{
		return stringFechaDeCreacion;
	}
	public void establecerStringFechaDeCreacion(String stringFechaDeCreacion)
	{
		this.stringFechaDeCreacion = stringFechaDeCreacion;
	}
	public String obtenerStringFechaDeCreacionCompleta()
	{
		return stringFechaDeCreacionCompleta;
	}
	public void establecerStringFechaDeCreacionCompleta(String stringFechaDeCreacionCompleta)
	{
		this.stringFechaDeCreacionCompleta = stringFechaDeCreacionCompleta;
	}
	public String obtenerStringFechaModificacionCompleta()
	{
		return stringFechaModificacionCompleta;
	}
	public void establecerStringFechaModificacionCompleta(String stringFechaModificacionCompleta)
	{
		this.stringFechaModificacionCompleta = stringFechaModificacionCompleta;
	}
	public String obtenerTiempoTranscurrido()
	{
		return tiempoTranscurrido;
	}
	public void establecerTiempoTranscurrido(String tiempoTranscurrido)
	{
		this.tiempoTranscurrido = tiempoTranscurrido;
	}
	
}
