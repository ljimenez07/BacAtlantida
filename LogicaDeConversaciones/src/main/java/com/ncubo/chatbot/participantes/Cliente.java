package com.ncubo.chatbot.participantes;

import java.util.ArrayList;

public class Cliente extends Participante{

	private String miNombre;
	private String miId;
	private ArrayList<String> misIdsDeSesiones = new ArrayList<String>();
	
	public Cliente(){
		miNombre = "";
		miId = "";
	}
	
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
	
	public ArrayList<String> getMisIdsDeSesiones() {
		return misIdsDeSesiones;
	}

	public void setMisIdsDeSesiones(String idDeSesion) {
		this.misIdsDeSesiones.add(idDeSesion);
	}
	
}
