package com.ncubo.chatbot.partesDeLaConversacion;


public class Afirmacion extends Frase 
{
	protected Afirmacion(String idFrase, String[] textosDeLaFrase)
	{
		super (idFrase, textosDeLaFrase, null, CaracteristicaDeLaFrase.esUnaOracionAfirmativa);
	}
}