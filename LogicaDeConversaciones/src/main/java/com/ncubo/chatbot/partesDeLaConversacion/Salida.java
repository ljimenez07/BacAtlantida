package com.ncubo.chatbot.partesDeLaConversacion;

public class Salida {

	private String miTexto;
	private Sonido miSonido;
	private Vineta miVineta;
	private Respuesta miRespuesta;
	private Tema temaActual;
	
	public Salida(){
		miTexto = "";
		miSonido = null;
		miVineta = null;
		miRespuesta = null;
		temaActual = null;
	}
	
	public void escribir(String texto, Respuesta respuesta, Tema tema){
		this.miTexto = texto;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		//System.out.println(texto);
	}
	
	public void escribir(Sonido sonido, Respuesta respuesta, Tema tema){
		this.miSonido = sonido;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		//System.out.println(sonido.url());
	} 
	
	public void escribir(Vineta vineta, Respuesta respuesta, Tema tema){
		this.miVineta = vineta;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		//System.out.println(vineta.url());
	} 
	
	public void escribir(String texto, Sonido sonido, Respuesta respuesta, Tema tema){
		this.miTexto = texto;
		this.miSonido = sonido;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
	}
	
	public void escribir(String texto, Sonido sonido, Vineta vineta, Respuesta respuesta, Tema tema){
		this.miTexto = texto;
		this.miSonido = sonido;
		this.miVineta = vineta;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
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
