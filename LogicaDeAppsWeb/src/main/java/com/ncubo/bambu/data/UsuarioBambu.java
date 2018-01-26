package com.ncubo.bambu.data;

import org.mortbay.jetty.security.Password;

import com.google.gson.JsonObject;

public class UsuarioBambu
{
	private int idUsuario;
	private String nombre;
	private String apellidos;
	private int idDepartamento;
	private int idFuncion;
	private String email;
	private String telefono;
	private String extension;
	private int idPerfil;
	private String login;
	private String password;
	private boolean activo;
	private String imagen;
	private int creadorPor;
	private int modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private int idEmpresaPrincipal;
	private String cargo;
	private boolean recienCreado;
	private boolean registroBorrado;
	private int borradoPor;
	private String fechaBorrado;
	private int idMarcador;
	private boolean sesionActiva;
	private int idVendedor;
	private String emailFacebook;
	private String primerNombreFacebook;
	private String segundoNombreFacebook;
	private String generoFacebook;
	private int idFacebook;
	private String apellidosFacebook;
	private String linkFacebook;
	private String lugarFacebook;
	private String nombreFacebook;
	private String zoneHorariaFacebook;
	private String horaActualizadaFacebook;
	private String verificadoFacebook;
	private int idSibu;
	private int idCompaniaSibu;
	private String cedula;
	private String idCompaniaNimbus;
	private String fechaUltimoLogin;
	private int idUsuarioNimbus;
	
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

	public int obtenerCreadorPor()
	{
		return creadorPor;
	}

	public void establecerCreadorPor(int creadorPor)
	{
		this.creadorPor = creadorPor;
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

	public String obtenerEmailFacebook()
	{
		return emailFacebook;
	}

	public void establecerEmailFacebook(String emailFacebook)
	{
		this.emailFacebook = emailFacebook;
	}

	public String obtenerPrimerNombreFacebook()
	{
		return primerNombreFacebook;
	}

	public void establecerPrimerNombreFacebook(String primerNombreFacebook)
	{
		this.primerNombreFacebook = primerNombreFacebook;
	}

	public String obtenerSegundoNombreFacebook()
	{
		return segundoNombreFacebook;
	}

	public void establecerSegundoNombreFacebook(String segundoNombreFacebook)
	{
		this.segundoNombreFacebook = segundoNombreFacebook;
	}

	public String obtenerGeneroFacebook()
	{
		return generoFacebook;
	}

	public void establecerGeneroFacebook(String generoFacebook)
	{
		this.generoFacebook = generoFacebook;
	}

	public int obtenerIdFacebook()
	{
		return idFacebook;
	}

	public void establecerIdFacebook(int idFacebook)
	{
		this.idFacebook = idFacebook;
	}

	public String obtenerApellidosFacebook()
	{
		return apellidosFacebook;
	}

	public void establecerApellidosFacebook(String apellidosFacebook)
	{
		this.apellidosFacebook = apellidosFacebook;
	}

	public String obtenerLinkFacebook()
	{
		return linkFacebook;
	}

	public void establecerLinkFacebook(String linkFacebook)
	{
		this.linkFacebook = linkFacebook;
	}

	public String obtenerLugarFacebook()
	{
		return lugarFacebook;
	}

	public void establecerLugarFacebook(String lugarFacebook)
	{
		this.lugarFacebook = lugarFacebook;
	}

	public String obtenerNombreFacebook()
	{
		return nombreFacebook;
	}

	public void establecerNombreFacebook(String nombreFacebook)
	{
		this.nombreFacebook = nombreFacebook;
	}

	public String obtenerZoneHorariaFacebook()
	{
		return zoneHorariaFacebook;
	}

	public void establecerZoneHorariaFacebook(String zoneHorariaFacebook)
	{
		this.zoneHorariaFacebook = zoneHorariaFacebook;
	}

	public String obtenerHoraActualizadaFacebook()
	{
		return horaActualizadaFacebook;
	}

	public void establecerHoraActualizadaFacebook(String horaActualizadaFacebook)
	{
		this.horaActualizadaFacebook = horaActualizadaFacebook;
	}

	public String obtenerVerificadoFacebook()
	{
		return verificadoFacebook;
	}

	public void establecerVerificadoFacebook(String verificadoFacebook)
	{
		this.verificadoFacebook = verificadoFacebook;
	}

	public String obtenerCedula()
	{
		return cedula;
	}

	public void establecerCedula(String cedula)
	{
		this.cedula = cedula;
	}

	public String obtenerIdCompaniaNimbus()
	{
		return idCompaniaNimbus;
	}

	public void establecerIdCompaniaNimbus(String idCompaniaNimbus)
	{
		this.idCompaniaNimbus = idCompaniaNimbus;
	}

	public String obtenerFechaUltimoLogin()
	{
		return fechaUltimoLogin;
	}

	public void establecerFechaUltimoLogin(String fechaUltimoLogin)
	{
		this.fechaUltimoLogin = fechaUltimoLogin;
	}

	public int obtenerIdUsuarioNimbus()
	{
		return idUsuarioNimbus;
	}

	public void establecerIdUsuarioNimbus(int idUsuarioNimbus)
	{
		this.idUsuarioNimbus = idUsuarioNimbus;
	}

	public UsuarioBambu()
	{
		
	}

	public int obtenerIdUsuario()
	{
		return idUsuario;
	}

	public void establecerIdUsuario(int idUsuario)
	{
		this.idUsuario = idUsuario;
	}

	public int obtenerIdDepartamento()
	{
		return idDepartamento;
	}

	public void establecerIdDepartamento(int idDepartamento)
	{
		this.idDepartamento = idDepartamento;
	}

	public int obtenerIdFuncion()
	{
		return idFuncion;
	}

	public void establecerIdFuncion(int idFuncion)
	{
		this.idFuncion = idFuncion;
	}

	public String obtenerExtension()
	{
		return extension;
	}

	public void establecerExtension(String extension)
	{
		this.extension = extension;
	}

	public int obtenerIdPerfil()
	{
		return idPerfil;
	}

	public void establecerIdPerfil(int idPerfil)
	{
		this.idPerfil = idPerfil;
	}

	public String obtenerLogin()
	{
		return login;
	}

	public void establecerLogin(String login)
	{
		this.login = login;
	}

	public String obtenerPassword()
	{
		return password;
	}

	public void establecerPassword(String password)
	{
		this.password = password;
	}

	public boolean esActivo()
	{
		return activo;
	}

	public void establecerActivo(boolean activo)
	{
		this.activo = activo;
	}

	public String obtenerImagen()
	{
		return imagen;
	}

	public void establecerImagen(String imagen)
	{
		this.imagen = imagen;
	}

	public int obtenerIdEmpresaPrincipal()
	{
		return idEmpresaPrincipal;
	}

	public void establecerIdEmpresaPrincipal(int idEmpresaPrincipal)
	{
		this.idEmpresaPrincipal = idEmpresaPrincipal;
	}

	public String obtenerCargo()
	{
		return cargo;
	}

	public void establecerCargo(String cargo)
	{
		this.cargo = cargo;
	}

	public boolean esRecienCreado()
	{
		return recienCreado;
	}

	public void establecerRecienCreado(boolean recienCreado)
	{
		this.recienCreado = recienCreado;
	}

	public int obtenerIdMarcador()
	{
		return idMarcador;
	}

	public void establecerIdMarcador(int idMarcador)
	{
		this.idMarcador = idMarcador;
	}

	public boolean esSesionActiva()
	{
		return sesionActiva;
	}

	public void establecerSesionActiva(boolean sesionActiva)
	{
		this.sesionActiva = sesionActiva;
	}

	public int obtenerIdVendedor()
	{
		return idVendedor;
	}

	public void establecerIdVendedor(int idVendedor)
	{
		this.idVendedor = idVendedor;
	}

	public int obtenerIdSibu()
	{
		return idSibu;
	}

	public void establecerIdSibu(int idSibu)
	{
		this.idSibu = idSibu;
	}

	public int obtenerIdCompaniaSibu()
	{
		return idCompaniaSibu;
	}

	public void establecerIdCompaniaSibu(int idCompaniaSibu)
	{
		this.idCompaniaSibu = idCompaniaSibu;
	}
	
}
