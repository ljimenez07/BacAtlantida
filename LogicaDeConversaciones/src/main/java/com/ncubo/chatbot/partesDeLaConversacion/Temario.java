package com.ncubo.chatbot.partesDeLaConversacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.chatbot.watson.Entidades;
import com.ncubo.chatbot.watson.Intenciones;

public abstract class Temario 
{
	private Temas temasDelDiscurso;
	private final Contenido contenido;
	private ArrayList<Intenciones> intenciones = new ArrayList<Intenciones>();
	private ArrayList<Entidades> entidades = new ArrayList<Entidades>();
	
	protected Temario()
	{
		temasDelDiscurso = new Temas();
		contenido = cargarContenido();
		cargarTemario(temasDelDiscurso);
		valirQueLasDependenciasEstenEnLosTemas();
		cargarDependencias(temasDelDiscurso);
		//cargarEntidades(entidades);
		//cargarIntenciones(intenciones);
		
	}
	
	protected abstract void cargarTemario(Temas temasDelDiscurso);
	
	protected abstract void cargarDependencias(Temas temasDelDiscurso);
	
	protected abstract void cargarEntidades(List<Entidades> entidades);
		
	protected abstract void cargarIntenciones(List<Intenciones> intenciones);
	
	protected abstract Contenido cargarContenido();
	
	public Contenido contenido(){
		return contenido;
	}
	
	public Frase frase(String idDeLaFrase){
		if(contenido == null){
			// TODO Error
			throw new ChatException("No se ha cargado el contenido del archivo de configuracion");
		}
		return contenido.frase(idDeLaFrase);
	}
	
	private void valirQueLasDependenciasEstenEnLosTemas()
	{
		/*for (Tema dependencia : dependenciasEntreLosTemas)
			if (! temasDelDiscurso.contains(dependencia))
				throw new ChatException(
					String.format("Hay una dependencia %s que no es parte de los temas del discurso", 1)
				);*/
	}
	
	public Tema buscarTema(String nombre){
		for(Tema tema: temasDelDiscurso){
			if(tema.obtenerIdTema().equals(nombre)){
				return tema;
			}
		}
		return null;
	}
	
	public Frase extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase caracteristica){
		Tema miSaludo = buscarTema(Constantes.FRASE_SALUDO);
		return miSaludo.buscarUnaFraseCon(caracteristica);
	}
	
	public Tema proximoTemaATratar(Tema temaActual, Temas temasYaTratados, String nombreDelWorkspace, String nombreIntencionGeneral){
		Collections.shuffle(temasDelDiscurso); // Desordenar el array
		for(Tema tema: temasDelDiscurso){
			if(tema.obtenerElNombreDelWorkspaceAlQuePertenece().equals(nombreDelWorkspace) && tema.obtenerIntencionGeneralAlQuePertenece().equals(nombreIntencionGeneral)){
				if(! tema.obtenerIdTema().equals(temaActual.obtenerIdTema())){
					if(temasYaTratados != null){
						if( ! temasYaTratados.contains(tema)){
							if(tema.buscarSiTodasLasDependenciasYaFueronTratadas(temasYaTratados)){
								return tema;
							}
						}
					}else{
						if( ! tema.tieneDependencias()){
							return tema;
						}
					}
				}
			}
		}	
		return null;
	}
	
	private Tema buscarUnUnicoTemaQueCumplaLaCondicion(String nombreDelWorkspace, String nombreIntencionGeneral){
		int contador = 0;
		Tema respuesta = null;
		
		for(Tema tema: temasDelDiscurso){
			if(tema.obtenerElNombreDelWorkspaceAlQuePertenece().equals(nombreDelWorkspace) && tema.obtenerIntencionGeneralAlQuePertenece().equals(nombreIntencionGeneral)){
				respuesta = tema;
				contador ++;
			}
		}
		
		if (contador == 1)
			return respuesta;
		else
			return null;
	}
	
	public void generarAudioEstaticosDeTodasLasFrases(String pathAGuardar, String ipPublica){
		for(Tema tema: temasDelDiscurso){
			tema.generarAudiosEstaticos(pathAGuardar, ipPublica);
		}
	}
	
}