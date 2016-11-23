package com.ncubo.chatbot.partesDeLaConversacion;

public class Saludo extends Frase
{  	
	// Id de xml = class.getName()
	protected Saludo(String idFrase, String[] textosDeLaFrase, CaracteristicaDeLaFrase[] caracteristicas){
		super(idFrase, textosDeLaFrase, null, caracteristicas);
	}
}
