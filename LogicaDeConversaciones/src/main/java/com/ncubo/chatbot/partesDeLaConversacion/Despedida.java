package com.ncubo.chatbot.partesDeLaConversacion;

public class Despedida extends Frase 
{
	protected Despedida(String idFrase, String[] textosDeLaFrase)
	{
		super (idFrase, textosDeLaFrase, null, CaracteristicaDeLaFrase.esUnaDespedida);
	}
}