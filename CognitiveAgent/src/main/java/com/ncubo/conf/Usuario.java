package com.ncubo.conf;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

public class Usuario implements Serializable
{
	public final static String LLAVE_EN_SESSION="user";
	private String contextoDeWatsonParaChats = new JSONObject().toString();
	private String contextoDeWatsonParaConocerte = new JSONObject().toString();
	private String usuarioId;
	private String usuarioNombre;
	private String llaveSession;
	private boolean estaLogueado;
	private static ArrayList<Usuario> usuariosLogueados = new ArrayList<Usuario>();
	
	public Usuario()
	{
		
	}

	public String getContextoDeWatsonParaChats()
	{
		return contextoDeWatsonParaChats;
	}
	
	public void setContextoDeWatsonParaChats(String contextoDeWatsonParaChats)
	{
		this.contextoDeWatsonParaChats =  contextoDeWatsonParaChats;
	}
	
	public String getContextoDeWatsonParaConocerte()
	{
		return contextoDeWatsonParaConocerte;
	}
	
	public void setContextoDeWatsonParaConocerte(String contextoDeWatsonParaConocerte)
	{
		this.contextoDeWatsonParaConocerte =  contextoDeWatsonParaConocerte;
	}


	@Override
	public String toString() {
		return "Usuario [contextoDeWatsonParaChats=" + contextoDeWatsonParaChats + ", contextoDeWatsonParaConocerte="
				+ contextoDeWatsonParaConocerte + ", usuarioId=" + usuarioId + ", usuarioNombre=" + usuarioNombre
				+ ", llaveSession=" + llaveSession + ", estaLogueado=" + estaLogueado + "]";
	}

	public boolean estaLogueado()
	{
		return this.estaLogueado;
	}
	
	public void hizologinExitosaMente()
	{
		this.estaLogueado = true;
		usuariosLogueados.add( this );
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

