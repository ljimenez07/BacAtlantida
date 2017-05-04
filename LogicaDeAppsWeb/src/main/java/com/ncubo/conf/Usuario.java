package com.ncubo.conf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

import com.ncubo.data.Categorias;

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
	private boolean tieneTarjetaCredito = false;
	private boolean tieneCuentaAhorros = false;
	private static ArrayList<Usuario> usuariosLogueados = new ArrayList<Usuario>();
	private String idSesion = "";
	private Hashtable<String, String> variablesDeContexto = new Hashtable<String, String>();
	private Categorias categorias = new Categorias();
	private String headerTokenKey = "";
	private String headerToken = "";
	private String responseLogin = "";
	private String cookie = "";
	
	protected Usuario(){}
	
	public Usuario(String idSesion)
	{
		this.setIdSesion(idSesion);		
	}

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

	public Categorias getCategorias() {
		return categorias;
	}

	public void setCategorias(Categorias categorias) {
		this.categorias = categorias;
	}
	
	public boolean isTieneTarjetaCredito() {
		return tieneTarjetaCredito;
	}

	public void setTieneTarjetaCredito(boolean tieneTarjetaCredito) {
		this.tieneTarjetaCredito = tieneTarjetaCredito;
	}

	public boolean isTieneCuentaAhorros() {
		return tieneCuentaAhorros;
	}

	public void setTieneCuentaAhorros(boolean tieneCuentaAhorros) {
		this.tieneCuentaAhorros = tieneCuentaAhorros;
	}

	public String getHeaderTokenKey() {
		return headerTokenKey;
	}

	public void setHeaderTokenKey(String headerTokenKey) {
		this.headerTokenKey = headerTokenKey;
	}

	public String getHeaderToken() {
		return headerToken;
	}

	public void setHeaderToken(String headerToken) {
		this.headerToken = headerToken;
	}

	public String getResponseLogin() {
		return responseLogin;
	}

	public void setResponseLogin(String responseLogin) {
		this.responseLogin = responseLogin;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}

