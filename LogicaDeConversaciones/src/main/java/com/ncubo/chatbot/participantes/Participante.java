package com.ncubo.chatbot.participantes;

import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Sonido;
import com.ncubo.chatbot.partesDeLaConversacion.Tema;
import com.ncubo.chatbot.partesDeLaConversacion.Vineta;
import com.ncubo.chatbot.watson.TextToSpeechWatson;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ncubo.chatbot.partesDeLaConversacion.Frase;
import com.ncubo.chatbot.partesDeLaConversacion.Respuesta;

public class Participante{ 
	
	public enum Manifestarse{ // https://howtoprogramwithjava.com/enums/
		
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
	
	private Manifestarse formaDeManifestarseEscrita = Manifestarse.EnFormaEscrita;
	private Manifestarse formaDeManifestarseOral = Manifestarse.EnFormaEscrita;
	private Manifestarse formaDeManifestarseVisual = Manifestarse.EnFormaEscrita;
	
	private Contexto miContexto = new Contexto();
	
	public Participante(){}
	
	public void manifestarseEnFormaOral(){
		formaDeManifestarseOral = Manifestarse.EnFormaOral;
	}
	
	public void manifestarseEnFormaVisual(){
		formaDeManifestarseVisual = Manifestarse.EnFormaVisual;
	}
	
	public Salida decir(Frase frase, Respuesta respuesta, Tema tema){
		Salida salida = new Salida();
		
		if (formaDeManifestarseEscrita.esEnFormaEscrita()){
			String texto = frase.texto();
			salida.escribir(texto, respuesta, tema, frase);
		}
		
		if (formaDeManifestarseOral.esEnFormaOral()){
			Sonido sonido = frase.obtenerSonidoAUsar();
			if (sonido != null)
				salida.escribir(sonido, respuesta, tema, frase);
		}
		
		if (formaDeManifestarseVisual.esFormaVisual()){
			//Vineta vineta = frase.vineta();
			//salida.escribir(vineta, respuesta, tema, frase);
		}
		return salida;
	}
	
	public Salida volverAPreguntar(Frase pregunta, Respuesta respuesta, Tema tema){
		
		Salida salida = new Salida();
		String texto = "";
		if (formaDeManifestarseEscrita.esEnFormaEscrita()){
			if(pregunta.hayTextosImpertinetes()){
				texto = pregunta.textoImpertinete();
			}else{
				texto = pregunta.conjuncionParaRepreguntar()+" "+pregunta.texto();
			}		
			salida.escribir(texto, respuesta, tema, pregunta);
		}
		
		if (formaDeManifestarseOral.esEnFormaOral()){
			Sonido sonido = null;
			if(pregunta.hayTextosImpertinetes()){
				sonido = pregunta.obtenerSonidoImpertienteAUsar();
			}else{
				if(! texto.equals("")){
					try{
						String nombreDelArchivo = TextToSpeechWatson.getInstance().getAudioToURL(texto, pregunta.getPathAGuardarLosAudiosTTS());
						String path = pregunta.getPathAGuardarLosAudiosTTS()+File.separator+nombreDelArchivo;
						String miIp = pregunta.getIpPublicaAMostrarLosAudioTTS()+File.separator+nombreDelArchivo;
						sonido = new Sonido(miIp, path);
					}catch(Exception e){
						
					}
					
				}
			}
			if(sonido != null)
				salida.escribir(sonido, respuesta, tema, pregunta);
		}
		
		if (formaDeManifestarseVisual.esFormaVisual()){
			//Vineta vineta = pregunta.vineta();
			//salida.escribir(vineta, respuesta, tema, pregunta);
		}
		
		return salida;
	}
	
	public void agregarVariableDeContexto(String key, String valor){
		miContexto.agregarAMiContexto(key, valor);
	}
	
	public void agregarVariablesDeContexto(Hashtable<String, String> variables){
		Enumeration<String> misLlaves = variables.keys();
		while(misLlaves.hasMoreElements()){
			String key = misLlaves.nextElement();
			miContexto.agregarAMiContexto(key, variables.get(key));
		}
	}
	
}