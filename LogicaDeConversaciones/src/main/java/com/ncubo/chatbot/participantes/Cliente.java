package com.ncubo.chatbot.participantes;

import java.util.ArrayList;

import com.ncubo.evaluador.main.Evaluador;

public class Cliente extends Participante{

	private String miNombre;
	private String miId;
	private ArrayList<String> misIdsDeSesiones = new ArrayList<String>();
	private Evaluador misVariablesDeContexto;
	
	public Cliente(){
		miNombre = "";
		miId = "";
		misVariablesDeContexto = new Evaluador();
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

	public void agregarIdsDeSesiones(String idDeSesion) {
		this.misIdsDeSesiones.add(idDeSesion);
	}
	
	public boolean contieneElIdSesion(String idSesion){
		return misIdsDeSesiones.contains(idSesion);
	}
	
	public void verificarSiExisteElIdSesion(String idSesion){
		if( ! contieneElIdSesion(idSesion)){
			agregarIdsDeSesiones(idSesion);
		}
	}
	
}
