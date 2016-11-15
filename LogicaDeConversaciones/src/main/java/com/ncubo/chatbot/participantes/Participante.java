package com.ncubo.chatbot.participantes;

import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Sonido;
import com.ncubo.chatbot.partesDeLaConversacion.Vineta;
import com.ncubo.chatbot.partesDeLaConversacion.Frase;
import com.ncubo.chatbot.partesDeLaConversacion.Pregunta;

public class Participante{ 
	
	enum Manifestarse{ // https://howtoprogramwithjava.com/enums/
		
		EnFormaEscrita(0b001), 
		EnFormaOral(0b010), 
		EnFormaVisual(0b100);
		
		private int valorDeManifestarse;
		 
		private Manifestarse (int valor){
			this.valorDeManifestarse = this.valorDeManifestarse | valor;
		}

		public boolean esEnFormaEscrita() {
			return (valorDeManifestarse & 0b001) != 0;
		}
		
		public boolean esEnFormaOral() {
			return (valorDeManifestarse & 0b010) != 0;
		}
		
		public boolean esFormaVisual() {
			return (valorDeManifestarse & 0b100) != 0;
		}
		
	}
	
	private Manifestarse formaDeManifestarse = Manifestarse.EnFormaEscrita;
	
	
	public Participante(){
	}
	
	public void formaDeManifestarse(Manifestarse forma){
		this.formaDeManifestarse = forma;
	}
	
	public Salida decir(Frase frase){
		Salida salida = new Salida();
		
		if (formaDeManifestarse.esEnFormaEscrita()){
			//String texto = frase.muletilla() +" "+frase.texto();
			String texto = frase.texto();
			salida.escribir(texto);
		}
		
		if (formaDeManifestarse.esEnFormaOral()){
			Sonido sonido = frase.sonido();
			salida.escribir(sonido);
		}
		
		if (formaDeManifestarse.esFormaVisual()){
			Vineta vineta = frase.vineta();
			salida.escribir(vineta);
		}
		return salida;
	}
	
	public Salida volverAPreguntar(Frase pregunta){
		
		Salida salida = new Salida();
		if (formaDeManifestarse.esEnFormaEscrita()){
			String texto = "";
			if(pregunta.hayTextosImpertinetes()){
				texto = pregunta.textoImpertinete();
			}else{
				texto = pregunta.conjuncionParaRepreguntar()+" "+pregunta.texto();
			}
			
			salida.escribir(texto);
		}
		if (formaDeManifestarse.esEnFormaOral()){
			Sonido sonido = pregunta.sonido();
			salida.escribir(sonido);
		}
		
		if (formaDeManifestarse.esFormaVisual()){
			Vineta vineta = pregunta.vineta();
			salida.escribir(vineta);
		}
		
		return salida;
	}
	
}