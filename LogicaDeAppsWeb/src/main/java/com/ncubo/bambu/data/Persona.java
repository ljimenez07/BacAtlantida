package com.ncubo.bambu.data;

import java.util.Date;

public class Persona{
	private int idPersona;
	private String cedula;
	private String direccion;
	private int idZona;
	private int idSegmento;
	private int idDivisa;
	private int idAsesor;
	private int creadoPor;
	private int modificadoPor;
	private Date fechaCreacion;
	private Date fechaModificacion;
	private boolean registroBorrado;
	private int borradoPor;
	private Date fechaBorrado;
	private String login;
	private boolean loginRecienCreado;
	private String codigoDelPais;
	private String codigoDeLaMoneda;
	private String companiaConvenio;
	private String idCompaniaSibu;
	private String nombre;
	private String telefono;
	private String correoElectronico;
	private String idSibu;
	private Date fechaNacimiento;
	
	public enum Atributo
	{
		ID_PERSONA("idPersona", Tipo.INT),
		CEDULA("Cedula", Tipo.STRING),
		DIRECCION("Direccion", Tipo.STRING),
		ID_ZONA("idZona", Tipo.INT),
		ID_SEGMENTO("idSegmento", Tipo.INT),
		ID_DIVISA("idDivisa", Tipo.INT),
		ID_ASESOR("idAsesor", Tipo.INT),
		CREADO_POR("CreadoPor", Tipo.INT),
		MODIFICADO_POR("ModificadoPor", Tipo.INT),
		FECHA_CREACION("FechaCreacion", Tipo.DATE),
		FECHA_MODIFICACION("FechaModificacion", Tipo.DATE),
		REGISTRO_BORRADO("RegistroBorrado", Tipo.BOOLEAN),
		BORRADO_POR("BorradoPor", Tipo.INT),
		FECHA_BORRADO("FechaBorrado",Tipo.DATE ),
		LOGIN("Login", Tipo.STRING),
		LOGIN_RECIEN_CREDO("LoginRecienCreado", Tipo.BOOLEAN),
		CODIGO_DEL_PAIS("codigoDelPais", Tipo.STRING),
		CODIGO_DE_MONEDA("codigoDeLaMoneda", Tipo.STRING),
		COMPANIA_CONVENIO("companiaConvenio", Tipo.STRING),
		ID_COMPANIA_SIBU("idCompaniaSibu", Tipo.STRING),
		NOMBRE("nombre", Tipo.STRING),
		TELEFONO("telefono", Tipo.STRING),
		CORREO_ELECTRONICO("correoElectronico", Tipo.STRING),
		ID_SIBU("idSibu", Tipo.STRING),
		FECHA_NACIMIENTO("FechaNacimiento", Tipo.DATE),
		GENERO("Genero", Tipo.STRING);
		
		private String nombre;
		private Tipo tipo;
		
		private Atributo(String nombre, Tipo tipo)
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
	}

	public int obtenerIdPersona()
	{
		return idPersona;
	}

	public void establecerIdPersona(int idPersona)
	{
		this.idPersona = idPersona;
	}

	public String obtenerCedula()
	{
		return cedula;
	}

	public void establecerCedula(String cedula)
	{
		this.cedula = cedula;
	}

	public String obtenerDireccion()
	{
		return direccion;
	}

	public void establecerDireccion(String direccion)
	{
		this.direccion = direccion;
	}

	public int obtenerIdZona()
	{
		return idZona;
	}

	public void establecerIdZona(int idZona)
	{
		this.idZona = idZona;
	}

	public int obtenerIdSegmento()
	{
		return idSegmento;
	}

	public void establecerIdSegmento(int idSegmento)
	{
		this.idSegmento = idSegmento;
	}

	public int obtenerIdDivisa()
	{
		return idDivisa;
	}

	public void establecerIdDivisa(int idDivisa)
	{
		this.idDivisa = idDivisa;
	}

	public int obtenerIdAsesor()
	{
		return idAsesor;
	}

	public void establecerIdAsesor(int idAsesor)
	{
		this.idAsesor = idAsesor;
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

	public Date obtenerFechaBorrado()
	{
		return fechaBorrado;
	}

	public void establecerFechaBorrado(Date fechaBorrado)
	{
		this.fechaBorrado = fechaBorrado;
	}

	public String obtenerLogin()
	{
		return login;
	}

	public void establecerLogin(String login)
	{
		this.login = login;
	}

	public boolean esLoginRecienCreado()
	{
		return loginRecienCreado;
	}

	public void establecerLoginRecienCreado(boolean loginRecienCreado)
	{
		this.loginRecienCreado = loginRecienCreado;
	}

	public String obtenerCodigoDelPais()
	{
		return codigoDelPais;
	}

	public void establecerCodigoDelPais(String codigoDelPais)
	{
		this.codigoDelPais = codigoDelPais;
	}

	public String obtenerCodigoDeLaMoneda()
	{
		return codigoDeLaMoneda;
	}

	public void establecerCodigoDeLaMoneda(String codigoDeLaMoneda)
	{
		this.codigoDeLaMoneda = codigoDeLaMoneda;
	}

	public String obtenerCompaniaConvenio()
	{
		return companiaConvenio;
	}

	public void establecerCompaniaConvenio(String companiaConvenio)
	{
		this.companiaConvenio = companiaConvenio;
	}

	public String obtenerIdCompaniaSibu()
	{
		return idCompaniaSibu;
	}

	public void establecerIdCompaniaSibu(String idCompaniaSibu)
	{
		this.idCompaniaSibu = idCompaniaSibu;
	}

	public String obtenerNombre()
	{
		return nombre;
	}

	public void establecerNombre(String nombre)
	{
		this.nombre = nombre;
	}

	public String obtenerTelefono()
	{
		return telefono;
	}

	public void establecerTelefono(String telefono)
	{
		this.telefono = telefono;
	}

	public String obtenerCorreoElectronico()
	{
		return correoElectronico;
	}

	public void establecerCorreoElectronico(String correoElectronico)
	{
		this.correoElectronico = correoElectronico;
	}

	public String obtenerIdSibu()
	{
		return idSibu;
	}

	public void establecerIdSibu(String idSibu)
	{
		this.idSibu = idSibu;
	}

	public Date obtenerFechaNacimiento()
	{
		return fechaNacimiento;
	}

	public void establecerFechaNacimiento(Date fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}
	
}