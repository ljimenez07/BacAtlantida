package com.ncubo.chatbot.partesDeLaConversacion;

public class Sonido {

	private String miURL;
	
	public Sonido(String url){
		this.miURL = url;
	}
	
	public String url(){
		return this.miURL;
	}
}
