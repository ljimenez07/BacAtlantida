package com.ncubo.agencias;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.agencias.Agencia.TipoDeAgencia;
import com.ncubo.chatbot.partesDeLaConversacion.Afirmacion;
import com.ncubo.chatbot.partesDeLaConversacion.Frase;
import com.ncubo.dao.AgenciasDao;

@Component
public class Agencias {

	@Autowired
	private AgenciasDao agencias;
	
	private final String TITULO = "<h2 class='tituloEnNegrita'> %s </h2> </br>";
	private final String CONTENIDO_AGENCIA = "<span class='tituloEnNegrita'> %s </span> </br>Dirección: %s </br>Horario: %s </br>Teléfono(s): %s </br></br>";
	
	public FraseDeLaAgencia buscarAgenciasPorCuidadYDepartamento(String ciudad, String departamento){
		
		ArrayList<Agencia> misAgencias = agencias.buscarAgenciasPorCuidadYDepartamento(ciudad, departamento, "and");
		
		if(misAgencias.isEmpty()){
			misAgencias = agencias.buscarAgenciasPorCuidadYDepartamento(ciudad, departamento, "or");
		}
		
		String textoDeLaFraseADecir = "";
		
		ArrayList<Agencia> losAutobancos = extrearLosAutobancos(misAgencias);
		boolean hayAutobancos = ! losAutobancos.isEmpty();
		
		if(hayAutobancos){
			textoDeLaFraseADecir += String.format(TITULO, "Autobancos:");
			for (Agencia elAutobanco: losAutobancos){
				textoDeLaFraseADecir += obtenerContenidoVisualDeLaAgencia(elAutobanco);
			}	
		}
		
		ArrayList<Agencia> lasAgencias = extrearLasAgencias(misAgencias);
		boolean hayAgencias = ! lasAgencias.isEmpty();
		
		if(hayAgencias){
			textoDeLaFraseADecir += String.format(TITULO, "Agencias:");
			for (Agencia laAgencia: lasAgencias){
				textoDeLaFraseADecir += obtenerContenidoVisualDeLaAgencia(laAgencia);
			}
		}
		
		if( (hayAutobancos || hayAgencias) && ! textoDeLaFraseADecir.isEmpty()){
			
			String[] textosDeLaFrase = new String[1];
			textosDeLaFrase[0] = textoDeLaFraseADecir;
			
			Frase frase = new Afirmacion(""+UUID.randomUUID().toString(), textosDeLaFrase, null, null, null);
			return new FraseDeLaAgencia(frase, hayAgencias, hayAutobancos, misAgencias);
		}
		
		return new FraseDeLaAgencia(null, hayAgencias, hayAutobancos, misAgencias);
	}
	
	public FraseDeLaAgencia buscarAgenciasPorNombre(String nombre){
		
		ArrayList<Agencia> misAgencias = agencias.buscarAgenciasPorNombre(nombre);
		
		String textoDeLaFraseADecir = "";
		
		ArrayList<Agencia> losAutobancos = extrearLosAutobancos(misAgencias);
		boolean hayAutobancos = losAutobancos.size() >= 1;
		
		if(hayAutobancos){
			textoDeLaFraseADecir += String.format(TITULO, "Autobancos:");
			for (Agencia elAutobanco: losAutobancos){
				textoDeLaFraseADecir += obtenerContenidoVisualDeLaAgencia(elAutobanco);
			}	
		}
		
		ArrayList<Agencia> lasAgencias = extrearLasAgencias(misAgencias);
		boolean hayAgencias = lasAgencias.size() >= 1;
		
		if(hayAgencias){
			textoDeLaFraseADecir += String.format(TITULO, "Agencias:");
			for (Agencia laAgencia: lasAgencias){
				textoDeLaFraseADecir += obtenerContenidoVisualDeLaAgencia(laAgencia);
			}
		}
		
		if( (hayAutobancos || hayAgencias) && ! textoDeLaFraseADecir.isEmpty()){
			
			String[] textosDeLaFrase = new String[1];
			textosDeLaFrase[0] = textoDeLaFraseADecir;
			
			Frase frase = new Afirmacion(""+UUID.randomUUID().toString(), textosDeLaFrase, null, null, null);
			return new FraseDeLaAgencia(frase, hayAgencias, hayAutobancos, misAgencias);
		}
		
		return new FraseDeLaAgencia(null, hayAgencias, hayAutobancos, misAgencias);
		
	}
	
	private ArrayList<Agencia> extrearLasAgencias(ArrayList<Agencia> misAgencias){
		
		ArrayList<Agencia> respuesta = new ArrayList<Agencia>();
		
		for(Agencia laAgencia: misAgencias){
			if(laAgencia.getTipoDeAgencia().equals(TipoDeAgencia.AGENCIA) || laAgencia.getTipoDeAgencia().equals(TipoDeAgencia.VENTANILLA)){
				respuesta.add(laAgencia);
			}
		}
		
		return respuesta;
	}
	
	private ArrayList<Agencia> extrearLosAutobancos(ArrayList<Agencia> misAgencias){
		
		ArrayList<Agencia> respuesta = new ArrayList<Agencia>();
		
		for(Agencia laAgencia: misAgencias){
			if(laAgencia.getTipoDeAgencia().equals(TipoDeAgencia.AUTOBANCO)){
				respuesta.add(laAgencia);
			}
		}
		
		return respuesta;
	}

	private String obtenerContenidoVisualDeLaAgencia(Agencia agencia){
		
		String textoDeLaFraseADecir = "";
		
		String nombre = "N/A";
		if( ! agencia.getNombre().isEmpty()){
			nombre =  agencia.getNombre(); 
		}
		
		String dirrecion = "N/A";
		if( ! agencia.getDireccion().isEmpty()){
			dirrecion =  agencia.getDireccion(); 
		}
		
		String telefono = "N/A";
		if( ! agencia.getTelefono().isEmpty()){
			telefono =  agencia.getTelefono(); 
		}
		
		String horario = "N/A";
		if( ! agencia.getHorarios().getHorarioLV().isEmpty()){
			horario = "Lunes a Viernes de "+agencia.getHorarios().getHorarioLV();
		}
		
		if( ! agencia.getHorarios().getHorarioSabados().isEmpty()){
			if(horario.contains("N/A")){
				horario = "Sábado de "+agencia.getHorarios().getHorarioSabados();
			}else{
				horario += ", Sábado de "+agencia.getHorarios().getHorarioSabados();
			}
		}
		
		if( ! agencia.getHorarios().getHorarioDomingo().isEmpty()){
			if(horario.contains("N/A")){
				horario = "Domingo de "+agencia.getHorarios().getHorarioDomingo();
			}else{
				horario += ", Domingo de "+agencia.getHorarios().getHorarioDomingo();
			}
		}
		
		textoDeLaFraseADecir += String.format(CONTENIDO_AGENCIA, nombre, dirrecion, horario, telefono);
		
		return textoDeLaFraseADecir;
	}
}
