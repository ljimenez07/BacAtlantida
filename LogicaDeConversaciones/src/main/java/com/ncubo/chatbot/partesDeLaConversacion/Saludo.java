package com.ncubo.chatbot.partesDeLaConversacion;

public class Saludo extends Frase
{  	
	// Id de xml = class.getName()
	protected Saludo(String idFrase, String[] textosDeLaFrase){
		super(idFrase, textosDeLaFrase, null, CaracteristicaDeLaFrase.esUnSaludo);
	}
}
