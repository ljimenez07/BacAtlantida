package com.ncubo.chatbot.contexto;

import com.ncubo.evaluador.main.Evaluador;

public class AdministradorDeVariablesDeContexto {

	private final Evaluador miEvaluador;
	private VariablesDeContexto misVariablesDeContexto;
	
	public AdministradorDeVariablesDeContexto(){
		miEvaluador = new Evaluador();
		misVariablesDeContexto = new VariablesDeContexto();
	}
	
	public void agregarVariableDeContexto(String nombreDeLaVariable, String valorDeLaVariable) throws Exception{
		if( ! nombreDeLaVariable.equals("") && ! valorDeLaVariable.equals("")){
			String comando = nombreDeLaVariable+"="+valorDeLaVariable+";";
			miEvaluador.crearContexto(comando);
		}
	}
	
	public void agregarVariablesAlContexto(String comando) throws Exception{
		if( ! comando.equals("")){
			miEvaluador.crearContexto(comando);
		}
	}
	
	public String obtenerElValorDeUnaVariable(String nombreDeLaVariable) throws Exception{
		String resultado = "";
		if( ! nombreDeLaVariable.equals("")){
			String comando = "show "+nombreDeLaVariable+";";
			resultado = miEvaluador.ejecutaComando(comando);
		}
		return resultado;
	}
	
}
