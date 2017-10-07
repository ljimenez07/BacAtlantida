package com.ncubo.agencias;

import java.util.ArrayList;

import com.ncubo.chatbot.partesDeLaConversacion.Frase;

public class FraseDeLaAgencia {

	private final Frase frase;
	private final boolean hayAgencias;
	private final boolean hayAutobancos;
	private final ArrayList<Agencia> agencias;
	
	public FraseDeLaAgencia(Frase frase, boolean hayAgencias, boolean hayAutobancos, ArrayList<Agencia> agencias){
		this.frase = frase;
		this.hayAgencias = hayAgencias;
		this.hayAutobancos = hayAutobancos;
		this.agencias = agencias;
	}
	
	public Frase getFrase() {
		return frase;
	}

	public boolean isHayAgencias() {
		return hayAgencias;
	}

	public boolean isHayAutobancos() {
		return hayAutobancos;
	}

	public ArrayList<Agencia> getAgencias() {
		return agencias;
	}
	
}
