package com.ncubo.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONObject;

public class Usuario implements Serializable
{
	private static final long serialVersionUID = -3968177701312019004L;
	public final static String LLAVE_EN_SESSION="user";
/*	private String contextoDeWatsonParaChats = new JSONObject().toString();
	private String contextoDeWatsonParaConocerte = new JSONObject().toString();*/
	private String usuarioId = "";
	private String usuarioNombre = "";
	private String llaveSession = "";
	private boolean estaLogueado = false;
	private static ArrayList<Usuario> usuariosLogueados = new ArrayList<Usuario>();
	private String idSesion = "";
	private Hashtable<String, String> variablesDeContexto = new Hashtable<String, String>();
	
	protected Usuario(){}
	
	public Usuario(String idSesion)
	{
		this.setIdSesion(idSesion);		
	}

	/*public String getContextoDeWatsonParaChats()
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
	}*/

	public boolean getEstaLogueado()
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

	public String getIdSesion()
	{
		return idSesion;
	}

	public void setIdSesion(String idSesion)
	{
		this.idSesion = idSesion;
	}

	public void setVariablesDeContexto(String variable, String valor){
		this.variablesDeContexto.put(variable, valor);
	}

	public String getVariablesDeContexto(String variable){
		return variablesDeContexto.get(variable);
	}
	
	
}

