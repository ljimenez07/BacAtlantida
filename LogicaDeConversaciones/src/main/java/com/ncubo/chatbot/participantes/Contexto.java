package com.ncubo.chatbot.participantes;

import java.util.Hashtable;

public class Contexto {

	private final Hashtable<String, String> miContexto = new Hashtable<String, String>();
	
	public Contexto(){}
	
	public void agregarAMiContexto(String key, String valor){
		miContexto.put(key, valor);
	}
	
	public boolean verificarSiUnaVariableDeContextoExiste(String key){
		return miContexto.containsKey(key);
	}
	
	public String obtenerUnValorDeMiContexto(String key){
		return miContexto.get(key);
	}
	
	public Hashtable<String, String> obtenerTodoMiContexto(){
		return miContexto;
	}
	
}
