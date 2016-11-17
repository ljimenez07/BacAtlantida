package com.ncubo.chatbot.partesDeLaConversacion;

import com.ncubo.chatbot.exceptiones.ChatException;

public class Tema
{
	private final Frase frases[];
	private final Temas dependencias;
	private final String idDelTema;
	private final String nombreDelWorkspaceAlQuePertenece;
	private final String intencionGeneralAlQuePertenece;
	private final boolean sePuedeRepetir;
	
	public Tema (String idDelTema, String nombreWorkspace, boolean sePuedeRepetir, String idDeLaIntencionGeneral, Frase... frases){
		this.idDelTema = idDelTema;
		this.nombreDelWorkspaceAlQuePertenece = nombreWorkspace;
		this.sePuedeRepetir = sePuedeRepetir;
		this.intencionGeneralAlQuePertenece = idDeLaIntencionGeneral;
		this.frases = frases;
		this.dependencias = new Temas();
	}
	
	public Tema dependeDe(Tema otroTema)
	{
		dependencias.add(otroTema);
		return this;
	}
	
	public String obtenerIntencionGeneralAlQuePertenece() {
		return intencionGeneralAlQuePertenece;
	}

	public String obtenerIdTema(){
		return idDelTema;
	}
	
	public String obtenerElNombreDelWorkspaceAlQuePertenece(){
		return nombreDelWorkspaceAlQuePertenece;
	}
	
	public Frase buscarUnaFrase(String nombreDeLaFrase){
		
		for(int index = 0; index < frases.length; index ++){
			if(frases[index].getIdFrase().equals(nombreDeLaFrase))
				return frases[index];
		}
		
		throw new ChatException(String.format("No existe una frase con id %s en el tema %s", nombreDeLaFrase, this.idDelTema));
	}
	
	public Frase buscarUnaFraseCon(CaracteristicaDeLaFrase caracteristica){
		for(int index = 0; index < frases.length; index ++){
			if(frases[index].buscarCaracteristica(caracteristica))
				return frases[index];
		}
		
		throw new ChatException(String.format("No existe una frase con tipo %s", caracteristica.toString()));
	}

	public boolean tieneDependencias(){
		return dependencias.isEmpty();
	}
	
	public Temas obtenerTodasLasDependencias(){
		return dependencias;
	}
	
	public boolean buscarSiTodasLasDependenciasYaFueronTratadas(Temas temasYaTratados){
		boolean resultado = true;
		for(Tema dependencia: dependencias){
			if( ! temasYaTratados.contains(dependencia)){
				resultado = false;
				break;
			}
		}
		return resultado;
	}

	public boolean sePuedeRepetir(){
		return sePuedeRepetir;
	}
	
}