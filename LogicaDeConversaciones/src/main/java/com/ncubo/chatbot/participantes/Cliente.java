package com.ncubo.chatbot.participantes;

import java.util.ArrayList;
import com.ncubo.chatbot.contexto.AdministradorDeVariablesDeContexto;

public class Cliente extends Participante{

	private String miNombre;
	private String miId;
	private ArrayList<String> misIdsDeSesiones = new ArrayList<String>();
	private AdministradorDeVariablesDeContexto administradorDeVariablesDeContexto;
	
	public Cliente(){
		miNombre = "";
		miId = "";
		//TODO sergio ir a recuperalos DB o usar los por defecto del xml
		administradorDeVariablesDeContexto = new AdministradorDeVariablesDeContexto();
		administradorDeVariablesDeContexto.agregarVariableDeContexto("leGustaLosHoteles", "0");
		administradorDeVariablesDeContexto.agregarVariableDeContexto("leGustaComerAfuera", "0");
		administradorDeVariablesDeContexto.agregarVariableDeContexto("sePreocupaPorLaSalud", "0");
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

	public void borrarTodosLosIdsDeSesiones() {
		this.misIdsDeSesiones.clear();
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
	
	public void presionarLikeDeHoteles() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaLosHoteles = (leGustaLosHoteles + 1.1) / 2;");
	}
	
	public void presionarDisLikeDeHoteles() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaLosHoteles = (leGustaLosHoteles - 1.1) / 2;");
	}
	
	public void actualizarGustosDeHoteles() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaLosHoteles = (leGustaLosHoteles + 1) / 2;");
	}
	
	public String obtenerValorDeGustosDeHoteles() throws Exception
	{
		return administradorDeVariablesDeContexto.obtenerVariable("leGustaLosHoteles");
	}
}

