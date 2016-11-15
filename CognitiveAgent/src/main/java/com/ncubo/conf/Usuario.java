package com.ncubo.conf;

import java.io.Serializable;

import org.json.JSONObject;

public class Usuario implements Serializable
{
	public final static String LLAVE_EN_SESSION="user";
	private String contextoDeWatson = new JSONObject().toString();
	private String usuarioId;
	private String usuarioNombre;
	private String llaveSession;
	private boolean estaLogueado;
	
	public Usuario()
	{
		
	}

	public String getContextoDeWatson()
	{
		return contextoDeWatson;
	}
	
	public void setContextoDeWatson(String contextoDeWatson)
	{
		this.contextoDeWatson =  contextoDeWatson;
	}

	@Override
	public String toString() {
		return "Usuario [contextoDeWatson=" + contextoDeWatson + "]";
	}
	
	public boolean estaLogueado()
	{
		return this.estaLogueado;
	}
	
	public void hizologinExitosaMente()
	{
		this.estaLogueado = true;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getLlaveSession() {
		return llaveSession;
	}

	public void setLlaveSession(String llaveSession) {
		this.llaveSession = llaveSession;
	}
	
	

	
}

