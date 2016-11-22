package com.ncubo.chatbot.partesDeLaConversacion;

public class Salida {

	private String miTexto;
	private Sonido miSonido;
	private Vineta miVineta;
	private Respuesta miRespuesta;
	private Tema temaActual;
	private Frase fraseActual;
	
	public Salida(){
		miTexto = "";
		miSonido = null;
		miVineta = null;
		miRespuesta = null;
		temaActual = null;
	}
	
	public void escribir(String texto, Respuesta respuesta, Tema tema, Frase frase){
		this.miTexto = texto;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		this.fraseActual = frase;
		//System.out.println(texto);
	}
	
	public void escribir(Sonido sonido, Respuesta respuesta, Tema tema, Frase frase){
		this.miSonido = sonido;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		this.fraseActual = frase;
		//System.out.println(sonido.url());
	} 
	
	public void escribir(Vineta vineta, Respuesta respuesta, Tema tema, Frase frase){
		this.miVineta = vineta;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		this.fraseActual = frase;
		//System.out.println(vineta.url());
	} 
	
	public void escribir(String texto, Sonido sonido, Respuesta respuesta, Tema tema, Frase frase){
		this.miTexto = texto;
		this.miSonido = sonido;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		this.fraseActual = frase;
	}
	
	public void escribir(String texto, Sonido sonido, Vineta vineta, Respuesta respuesta, Tema tema, Frase frase){
		this.miTexto = texto;
		this.miSonido = sonido;
		this.miVineta = vineta;
		this.miRespuesta = respuesta;
		this.temaActual = tema;
		this.fraseActual = frase;
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
	
	public Tema getTemaActual() {
		return temaActual;
	}

	public Frase getFraseActual() {
		return fraseActual;
	}
	
}
