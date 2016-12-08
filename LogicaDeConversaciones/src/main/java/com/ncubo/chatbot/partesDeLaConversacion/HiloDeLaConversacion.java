package com.ncubo.chatbot.partesDeLaConversacion;

import java.util.ArrayList;

// Hilo de la converzacion actual - Se puede borrar cuando la seccion caduca
public class HiloDeLaConversacion {

	private Temas temasYaDichos = new Temas();
	private Temas temasYaDichosQueNoPuedoRepetir = new Temas();
	private ArrayList<Frase> loQueYaSeHaDicho = new ArrayList<Frase>();
	private ArrayList<Respuesta> respuestas = new ArrayList<Respuesta>();
	private ArrayList<Salida> misSalidas = new ArrayList<Salida>();
	
	public HiloDeLaConversacion(){}
	
	public void ponerComoDichoEsta(Frase frase){
		loQueYaSeHaDicho.add(frase);
	}

	public void noPuedoRepetir(Tema tema){
		temasYaDichosQueNoPuedoRepetir.add(tema);
		ponerComoDichoEste(tema);
	}
	
	public void ponerComoDichoEste(Tema tema){
		temasYaDichos.add(tema);
	}
	
	public Temas verTemasYaTratados(){
		return temasYaDichos;
	}
	
	public Temas verTemasYaTratadosYQueNoPuedoRepetir(){
		return temasYaDichosQueNoPuedoRepetir;
	}
	
	public void agregarUnaRespuesta(Respuesta miRespuesta){
		respuestas.add(miRespuesta);
	}
	
	public void agregarSalidaAlHilo(Salida salida){
		misSalidas.add(salida);
	}
	
	public boolean existeTema(Tema tema)
	{
		return temasYaDichos.contains(tema);
	}
}
