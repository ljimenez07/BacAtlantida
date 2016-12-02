package com.ncubo.chatbot.contexto;

import java.util.Hashtable;

public class VariablesDeContexto {

	private final static Hashtable<String, String> misVariables = new Hashtable<String, String>();
	
	public VariablesDeContexto(){}
	
	public void agregarVariableAMiContexto(String variable, String valor){
		misVariables.put(variable, valor);
	}
	
	public boolean verificarSiUnaVariableDeContextoExiste(String variable){
		return misVariables.containsKey(variable);
	}
	
	public String obtenerUnaVariableDeMiContexto(String variable){
		if (verificarSiUnaVariableDeContextoExiste(variable))
			return misVariables.get(variable);
		else
			return "";
	}
	
	public Hashtable<String, String> obtenerTodasLasVariablesDeMiContexto(){
		return misVariables;
	}
}
