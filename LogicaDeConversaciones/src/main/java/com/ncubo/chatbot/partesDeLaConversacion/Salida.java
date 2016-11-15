package com.ncubo.chatbot.partesDeLaConversacion;

public class Salida {

	private String miTexto;
	private Sonido miSonido;
	private Vineta miVineta;
	
	public Salida(){}
	
	public void escribir(String texto){
		this.miTexto = texto;
		//System.out.println(texto);
	}
	
	public void escribir(Sonido sonido){
		this.miSonido = sonido;
		//System.out.println(sonido.url());
	} 
	
	public void escribir(Vineta vineta){
		this.miVineta = vineta;
		//System.out.println(vineta.url());
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
}
