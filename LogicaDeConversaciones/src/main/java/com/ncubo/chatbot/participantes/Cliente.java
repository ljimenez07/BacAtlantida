package com.ncubo.chatbot.participantes;

public class Cliente extends Participante{

	private String miNombre;
	private String miId;
	
	public Cliente(String nombre, String id){
		miNombre = nombre;
		miId = id;
	}
	
	public String getMiNombre() {
		return miNombre;
	}

	public void setMiNombre(String miNombre) {
		this.miNombre = miNombre;
	}

	public String getMiId() {
		return miId;
	}

	public void setMiId(String miId) {
		this.miId = miId;
	}
	
}
