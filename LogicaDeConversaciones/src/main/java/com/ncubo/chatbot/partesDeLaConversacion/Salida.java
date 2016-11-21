package com.ncubo.chatbot.partesDeLaConversacion;

public class Salida {

	private String miTexto;
	private Sonido miSonido;
	private Vineta miVineta;
	private Respuesta miRespuesta;
	// tema actual
	
	public Salida(){
		miTexto = "";
		miSonido = null;
		miVineta = null;
		miRespuesta = null;
	}
	
	public void escribir(String texto, Respuesta respuesta){
		this.miTexto = texto;
		this.miRespuesta = respuesta;
		//System.out.println(texto);
	}
	
	public void escribir(Sonido sonido, Respuesta respuesta){
		this.miSonido = sonido;
		this.miRespuesta = respuesta;
		//System.out.println(sonido.url());
	} 
	
	public void escribir(Vineta vineta, Respuesta respuesta){
		this.miVineta = vineta;
		this.miRespuesta = respuesta;
		//System.out.println(vineta.url());
	} 
	
	public void escribir(String texto, Sonido sonido, Respuesta respuesta){
		this.miTexto = texto;
		this.miSonido = sonido;
		this.miRespuesta = respuesta;
	}
	
	public void escribir(String texto, Sonido sonido, Vineta vineta, Respuesta respuesta){
		this.miTexto = texto;
		this.miSonido = sonido;
		this.miVineta = vineta;
		this.miRespuesta = respuesta;
	}
	
	public String getMiTexto() {
		return miTexto;
	}

	public Sonido getMiSonido() {
		return miSonido;
	}

	public Vineta getMiVineta() {
		return miVineta;
	}
	
	public Respuesta obtenerLaRespuestaDeIBM(){
		return miRespuesta;
	}
	
}
