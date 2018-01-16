package com.ncubo.bambu.data;

public class PersonaContacto
{
	private int idPersonaContacto;
	private String nombre;
	private String apellidos;
	private String email;
	private String telefono;
	private int idEmpresa;
	private int idPersona;
	private int creadoPor;
	private int modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private boolean registroBorrado;
	private int borradoPor;
	private String fechaBorrado;
	private String usuarioSkype;
	private String celular;
	private String direccionEntrega;
	private double latitud;
	private double longitud;
	private int idCompania;
	private int idUsuario;
	private String facebook;
	private String whatsapp;
	
	public enum atributo
	{
		ID_PERSONA_CONTACTO("idPersonaContacto", Tipo.INT),
		NOMBRE("Nombre", Tipo.STRING),
		APELLIDOS("Apellidos", Tipo.STRING),
		EMAIL("Email", Tipo.STRING),
		TELEFONO("Telefono", Tipo.STRING),
		ID_EMPRESA("idEmpresa", Tipo.INT),
		ID_PERSONA("idPersona", Tipo.INT),
		CREADO_POR("CreadoPor", Tipo.INT),
		MODIFICADO_POR("ModificadoPor", Tipo.INT),
		FECHA_CREACION("FechaCreacion", Tipo.DATE),
		FECHA_MODIFICACION("FechaModificacion", Tipo.DATE),
		REGISTRO_BORRADO("RegistroBorrado", Tipo.INT),
		BORRADO_POR("BorradoPor", Tipo.INT),
		FECHA_BORRADO("FechaBorrado", Tipo.DATE),
		USUARIO_SKYPE("UsuarioSkype", Tipo.STRING),
		CELULAR("Celular", Tipo.STRING),
		DIRECCION_ENTREGA("DireccionEntrega", Tipo.STRING),
		LATITUD("latitud", Tipo.FLOAT),
		LONGITUD("longitud", Tipo.FLOAT),
		ID_COMPANIA("idCompania", Tipo.INT),
		ID_USUARIO("idUsuario", Tipo.INT),
		FACEBOOK("facebook", Tipo.STRING),
		WHATSAPP("whatsapp", Tipo.STRING);
		
		private String nombre;
		private Tipo tipo;
		
		private atributo(String nombre, Tipo tipo)
		{
			this.nombre = nombre;
			this.tipo = tipo;
		}
		
		public String obtenerNombre()
		{
			return nombre;
		}
		
		public Tipo obtenerTipo()
		{
			return tipo;
		}
		
		public String toString()
		{
			return nombre;
		}
	}
	
	public int obtenerIdPersonaContacto()
	{
		return idPersonaContacto;
	}
	public void establecerIdPersonaContacto(int idPersonaContacto)
	{
		this.idPersonaContacto = idPersonaContacto;
	}
	public String obtenerNombre()
	{
		return nombre;
	}
	public void establecerNombre(String nombre)
	{
		this.nombre = nombre;
	}
	public String obtenerApellidos()
	{
		return apellidos;
	}
	public void establecerApellidos(String apellidos)
	{
		this.apellidos = apellidos;
	}
	public String obtenerEmail()
	{
		return email;
	}
	public void establecerEmail(String email)
	{
		this.email = email;
	}
	public String obtenerTelefono()
	{
		return telefono;
	}
	public void establecerTelefono(String telefono)
	{
		this.telefono = telefono;
	}
	public int obtenerIdEmpresa()
	{
		return idEmpresa;
	}
	public void establecerIdEmpresa(int idEmpresa)
	{
		this.idEmpresa = idEmpresa;
	}
	public int obtenerIdPersona()
	{
		return idPersona;
	}
	public void establecerIdPersona(int idPersona)
	{
		this.idPersona = idPersona;
	}
	public int obtenerCreadoPor()
	{
		return creadoPor;
	}
	public void establecerCreadoPor(int creadoPor)
	{
		this.creadoPor = creadoPor;
	}
	public int obtenerModificadoPor()
	{
		return modificadoPor;
	}
	public void establecerModificadoPor(int modificadoPor)
	{
		this.modificadoPor = modificadoPor;
	}
	public String obtenerFechaCreacion()
	{
		return fechaCreacion;
	}
	public void establecerFechaCreacion(String fechaCreacion)
	{
		this.fechaCreacion = fechaCreacion;
	}
	public String obtenerFechaModificacion()
	{
		return fechaModificacion;
	}
	public void establecerFechaModificacion(String fechaModificacion)
	{
		this.fechaModificacion = fechaModificacion;
	}
	public boolean esRegistroBorrado()
	{
		return registroBorrado;
	}
	public void establecerRegistroBorrado(boolean registroBorrado)
	{
		this.registroBorrado = registroBorrado;
	}
	public int obtenerBorradoPor()
	{
		return borradoPor;
	}
	public void establecerBorradoPor(int borradoPor)
	{
		this.borradoPor = borradoPor;
	}
	public String obtenerFechaBorrado()
	{
		return fechaBorrado;
	}
	public void establecerFechaBorrado(String fechaBorrado)
	{
		this.fechaBorrado = fechaBorrado;
	}
	public String obtenerUsuarioSkype()
	{
		return usuarioSkype;
	}
	public void establecerUsuarioSkype(String usuarioSkype)
	{
		this.usuarioSkype = usuarioSkype;
	}
	public String obtenerCelular()
	{
		return celular;
	}
	public void establecerCelular(String celular)
	{
		this.celular = celular;
	}
	public String obtenerDireccionEntrega()
	{
		return direccionEntrega;
	}
	public void establecerDireccionEntrega(String direccionEntrega)
	{
		this.direccionEntrega = direccionEntrega;
	}
	public double obtenerLatitud()
	{
		return latitud;
	}
	public void establecerLatitud(double latitud)
	{
		this.latitud = latitud;
	}
	public double obtenerLongitud()
	{
		return longitud;
	}
	public void establecerLongitud(double longitud)
	{
		this.longitud = longitud;
	}
	public int obtenerIdCompania()
	{
		return idCompania;
	}
	public void establecerIdCompania(int idCompania)
	{
		this.idCompania = idCompania;
	}
	public int obtenerIdUsuario()
	{
		return idUsuario;
	}
	public void establecerIdUsuario(int idUsuario)
	{
		this.idUsuario = idUsuario;
	}
	public String obtenerFacebook()
	{
		return facebook;
	}
	public void establecerFacebook(String facebook)
	{
		this.facebook = facebook;
	}
	public String obtenerWhatsapp()
	{
		return whatsapp;
	}
	public void establecerWhatsapp(String whatsapp)
	{
		this.whatsapp = whatsapp;
	}
	
	
}
