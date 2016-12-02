package com.ncubo.chatbot.contexto;

import com.ncubo.evaluador.main.Evaluador;

public class AdministradorDeVariablesDeContexto {

	private final Evaluador miEvaluador;
	private VariablesDeContexto misVariablesDeContexto;
	
	public AdministradorDeVariablesDeContexto(){
		miEvaluador = new Evaluador();
		misVariablesDeContexto = new VariablesDeContexto();
	}
	
	public void agregarVariableDeContexto(String nombreDeLaVariable, String valorDeLaVariable){
		if( ! nombreDeLaVariable.equals("") && ! valorDeLaVariable.equals("")){
			String comando = nombreDeLaVariable+"="+valorDeLaVariable+";";
			miEvaluador.crearContexto(comando);
		}
	}
	
	private String obtenerElValorDeUnaVariable(String nombreDeLaVariable) throws Exception{
		String resultado = "";
		if( ! nombreDeLaVariable.equals("")){
			String comando = "show "+nombreDeLaVariable+";";
			resultado = miEvaluador.ejecutaComando(comando).trim();
		}
		return resultado;
	}

	public void ejecutar(String comando) throws Exception {
		miEvaluador.ejecutaComando(comando);
	}
	
	public String obtenerVariable(String nombreDeLaVariable) throws Exception{
		return obtenerElValorDeUnaVariable(nombreDeLaVariable);
	}
}
