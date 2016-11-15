package com.ncubo.chatbot.partesDeLaConversacion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ncubo.chatbot.watson.Entidad;
import com.ncubo.chatbot.watson.Intenciones;

// Hilo de la converzacion actual - Se puede borrar cuando la seccion caduca
public class HiloDeLaConversacion {

	private Temas temasYaDichos = new Temas();
	private ArrayList<Frase> loQueYaSeHaDicho = new ArrayList<Frase>();
	private ArrayList<Respuesta> respuestas = new ArrayList<Respuesta>();
	
	public HiloDeLaConversacion(){}
	
	public void ponerComoDichoEsta(Frase frase){
		loQueYaSeHaDicho.add(frase);
	}

	public void ponerComoDichoEste(Tema tema){
		temasYaDichos.add(tema);
	}
	
	public Temas verTemasYaTratados(){
		return temasYaDichos;
	}
	
	public void agregarUnaRespuesta(Respuesta miRespuesta){
		respuestas.add(miRespuesta);
	}
	
}
