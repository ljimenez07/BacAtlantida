package com.ncubo.logicaDeConversaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.partesDeLaConversacion.Afirmacion;
import com.ncubo.chatbot.partesDeLaConversacion.CaracteristicaDeLaFrase;
import com.ncubo.chatbot.partesDeLaConversacion.Despedida;
import com.ncubo.chatbot.partesDeLaConversacion.Frase;
import com.ncubo.chatbot.partesDeLaConversacion.HiloDeLaConversacion;
import com.ncubo.chatbot.partesDeLaConversacion.Pregunta;
import com.ncubo.chatbot.partesDeLaConversacion.Respuesta;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Saludo;
import com.ncubo.chatbot.partesDeLaConversacion.Tema;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Agente;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.dao.ConsultaDao;

public class Conversacion {

	//private Participantes participantes;
	private Cliente participante;
	private HiloDeLaConversacion hilo; // Mantiene el contexto, osea todas las intenciones y entidades, sabe que se dijo 
	private Temario temario;
	private Agente agente;
	private Tema temaActual;
	private Frase fraseActual = null;
	private Tema temaActualDelWorkSpaceEspecifico = null;
	private Frase fraseActualDelWorkSpaceEspecifico = null;
	private boolean hayUnWorkspaceEspecifico = false;
	
	private Estadisticas estadisticasTemasTratados;
	
	public Conversacion(Temario temario, Cliente participante, ConsultaDao consultaDao){
		// Hacer lamdaba para agregar los participantes
		//this.participantes = new Participantes();
		this.participante = participante;
		this.agente = new Agente(temario.contenido().getMiWorkSpaces());
		this.agente.manifestarseEnFormaOral();
		
		this.hilo = new HiloDeLaConversacion();
		//this.participantes.agregar(agente).agregar(participante);
		this.temario = temario;
		estadisticasTemasTratados = new Estadisticas(consultaDao);
	}
	
	public void cambiarParticipante(Cliente participante){
		this.participante = participante;
	}
	
	public ArrayList<Salida> inicializarLaConversacion(){
		ArrayList<Salida> misSalidas = new ArrayList<Salida>();
		
		System.out.println("");
		System.out.println("Iniciar conversacion ...");
		System.out.println("");
		
		this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
		
		Saludo saludoGeneral = (Saludo) this.temario.extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase.esUnSaludo);
		misSalidas.add(agente.decir(saludoGeneral, null, temaActual));
		ponerComoYaTratado(saludoGeneral);
		
		Pregunta queQuiere = (Pregunta) this.temario.extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase.esUnaPregunta);
		misSalidas.add(agente.decir(queQuiere, null, temaActual));
		fraseActual = queQuiere;
		ponerComoYaTratado(queQuiere);
		return misSalidas;
	}
	
	public ArrayList<Salida> analizarLaRespuestaConWatson(String respuestaDelCliente){
		ArrayList<Salida> misSalidas = new ArrayList<Salida>();
		
		Respuesta respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
		this.hilo.agregarUnaRespuesta(respuesta);

		if(! verificarIntencionNoAsociadaANingunWorkspace(misSalidas, respuesta)){
			String idFraseActivada = respuesta.obtenerFraseActivada();
			if(respuesta.cambiarAGeneral()){
				extraerOracionesAfirmarivasYPreguntas(misSalidas, respuesta, idFraseActivada);
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
				agente.cambiarAWorkspaceGeneral();
				/*respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
				this.hilo.agregarUnaRespuesta(respuesta);
				idFraseActivada = respuesta.obtenerFraseActivada();
				extraerOracionesAfirmarivasYPreguntas(respuesta, idFraseActivada);*/
			}else{
				if(agente.hayQueCambiarDeTema()){
					
					idFraseActivada = respuesta.obtenerFraseActivada();
					extraerOracionesAfirmarivasYPreguntas(misSalidas, respuesta, idFraseActivada);
					
					this.temaActual = this.temario.proximoTemaATratar(temaActual, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), agente.obtenerNombreDelWorkspaceActual(), agente.obtenernombreDeLaIntencionEspecificaActiva());
					agente.yaNoCambiarDeTema();
					if(this.temaActual == null){ // Ya no hay mas temas	
						this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
						agente.cambiarAWorkspaceGeneral();
					}else{
						System.out.println("El proximo tema a tratar es: "+this.temaActual.obtenerIdTema());
						
						// Activar en el contexto el tema
						agente.activarTemaEnElContextoDeWatson(this.temaActual.obtenerIdTema());
						
						// llamar a watson y ver que bloque se activo
						respuesta = agente.inicializarTemaEnWatson(respuestaDelCliente);
						idFraseActivada = agente.obtenerNodoActivado(respuesta.messageResponse());
						
						System.out.println("Id de la frase a decir: "+idFraseActivada);
						extraerOracionesAfirmarivasYPreguntas(misSalidas, respuesta, idFraseActivada);
					}
					
					if(this.temaActual != null){
						if( (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_SALUDO)) && (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_DESPEDIDA)) )
							ponerComoYaTratado(this.temaActual);
					}
					
				}else{
					if (agente.entendiLaUltimaPregunta()){
						
						idFraseActivada = respuesta.obtenerFraseActivada();
						extraerOracionesAfirmarivasYPreguntas(misSalidas, respuesta, idFraseActivada);
						
					}else{ 
						// Verificar que fue	
						System.out.println("No entendi la ultima pregunta");
						if(fraseActual.esMandatorio()){
							misSalidas.add(agente.volverAPreguntar(fraseActual, respuesta, temaActual));
						}
					}
				}
				
				if(respuesta.seTerminoElTema()){
					Tema miTema = this.temario.proximoTemaATratar(temaActual, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), agente.obtenerNombreDelWorkspaceActual(), agente.obtenernombreDeLaIntencionEspecificaActiva());
					if(miTema == null){
						this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
						agente.borrarUnaVariableDelContexto(Constantes.TERMINO_EL_TEMA);
						agente.cambiarAWorkspaceGeneral();
					}
				}
			}
		}
		
		return misSalidas;
	}
	
	public ArrayList<Salida> analizarLaRespuestaConWatsonEnUnWorkspaceEspecifico(String respuestaDelCliente, String nombreDelWorkSpaseAUsar, String nombreDeLaIntencion){
		
		ArrayList<Salida> misSalidas = new ArrayList<Salida>();
		Respuesta respuesta = null;
		if(! hayUnWorkspaceEspecifico){
			hayUnWorkspaceEspecifico = true;
			agente.cambiarDeTemaWSEspecifico();
		}else{
			respuesta = agente.analizarRespuestaWSEspecifico(respuestaDelCliente, fraseActualDelWorkSpaceEspecifico, nombreDelWorkSpaseAUsar);
			this.hilo.agregarUnaRespuesta(respuesta);
		}
		
		if(agente.hayQueCambiarDeTemaWSEspecifico()){
			if(this.temaActualDelWorkSpaceEspecifico != null){
				ponerComoYaTratado(this.temaActualDelWorkSpaceEspecifico);
			}
			
			String idFraseActivada = "";
			if (respuesta != null){
				idFraseActivada = respuesta.obtenerFraseActivada();
				extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecifico(misSalidas, respuesta, idFraseActivada, true);
			}
			
			this.temaActualDelWorkSpaceEspecifico = this.temario.proximoTemaATratar(temaActualDelWorkSpaceEspecifico, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), nombreDelWorkSpaseAUsar, nombreDeLaIntencion);
			agente.yaNoCambiarDeTemaWSEspecifico();
			if(this.temaActualDelWorkSpaceEspecifico == null){ // Ya no hay mas temas	
				this.temaActualDelWorkSpaceEspecifico = this.temario.buscarTema("saludarConocerte");
				//agente.cambiarAWorkspaceGeneral();
			}else{
				System.out.println("El proximo tema a tratar es: "+this.temaActualDelWorkSpaceEspecifico.obtenerIdTema());
				
				// Activar en el contexto el tema
				agente.activarTemaEnElContextoDeWatsonEnWorkspaceEspecifico(this.temaActualDelWorkSpaceEspecifico.obtenerIdTema(), nombreDelWorkSpaseAUsar);
				
				// llamar a watson y ver que bloque se activo
				respuesta = agente.inicializarTemaEnWatsonWorkspaceEspecifico(respuestaDelCliente, nombreDelWorkSpaseAUsar);
				idFraseActivada = agente.obtenerNodoActivado(respuesta.messageResponse());
				
				System.out.println("Id de la frase a decir: "+idFraseActivada);
				agente.borrarUnaVariableDelContextoEnUnWorkspace(Constantes.NODO_ACTIVADO, nombreDelWorkSpaseAUsar);
				agente.borrarUnaVariableDelContextoEnUnWorkspace(Constantes.ORACIONES_AFIRMATIVAS, nombreDelWorkSpaseAUsar);
				agente.borrarUnaVariableDelContextoEnUnWorkspace(Constantes.TERMINO_EL_TEMA, nombreDelWorkSpaseAUsar);
				agente.borrarUnaVariableDelContextoEnUnWorkspace(Constantes.ANYTHING_ELSE, nombreDelWorkSpaseAUsar);
				
				extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecifico(misSalidas, respuesta, idFraseActivada, true);
			}
		}else{
			String idFraseActivada = respuesta.obtenerFraseActivada();
			
			if (agente.entendiLaUltimaPreguntaWSEspecifico()){				
				extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecifico(misSalidas, respuesta, idFraseActivada, true);
			}else{	
				System.out.println("No entendi la ultima pregunta de "+nombreDelWorkSpaseAUsar);
				extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecificoSinActualizar(misSalidas, respuesta, idFraseActivada, true);
				if(fraseActualDelWorkSpaceEspecifico.esMandatorio()){
					misSalidas.add(agente.volverAPreguntar(fraseActualDelWorkSpaceEspecifico, respuesta, temaActualDelWorkSpaceEspecifico));
				}
			}
		}
		
		return misSalidas;
	}
	
	private boolean verificarIntencionNoAsociadaANingunWorkspace(ArrayList<Salida> misSalidas, Respuesta respuesta){
		if(agente.hayIntencionNoAsociadaANingunWorkspace()){
			if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals(Constantes.INTENCION_SALUDAR)){
				System.out.println("Quiere saludar ...");
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);

				Afirmacion saludar = (Afirmacion) this.temaActual.buscarUnaFrase("saludar");
				misSalidas.add(agente.decir(saludar, respuesta, temaActual));
				ponerComoYaTratado(saludar);
				
				Pregunta queQuiere = (Pregunta) this.temario.extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase.esUnaPregunta);
				misSalidas.add(agente.decir(queQuiere, respuesta, temaActual));
				fraseActual = queQuiere;
				ponerComoYaTratado(queQuiere);
				
			}else if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals(Constantes.INTENCION_DESPEDIDA)){
				System.out.println("Quiere despedirce ...");
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_DESPEDIDA);
				
				Despedida saludar = (Despedida) this.temaActual.buscarUnaFrase(Constantes.FRASE_DESPEDIDA);
				misSalidas.add(agente.decir(saludar, respuesta, temaActual));
				fraseActual = saludar;
				ponerComoYaTratado(saludar);
				
			}else if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals(Constantes.INTENCION_FUERA_DE_CONTEXTO)){
				System.out.println("Esta fuera de contexto ...");
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_FUERA_DE_CONTEXTO);
				
				Afirmacion fueraDeContexto = (Afirmacion) this.temaActual.buscarUnaFrase("fueraDeContextoGeneral");
				misSalidas.add(agente.decir(fueraDeContexto, respuesta, temaActual));
				fraseActual = fueraDeContexto;
				ponerComoYaTratado(fueraDeContexto);
				
			}else if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals(Constantes.INTENCION_NO_ENTIENDO)){
				System.out.println("No entendi bien ...");
				this.temaActual = this.temario.buscarTema(Constantes.INTENCION_NO_ENTIENDO);
				
				Afirmacion fueraDeContexto = (Afirmacion) this.temaActual.buscarUnaFrase(Constantes.INTENCION_NO_ENTIENDO);
				misSalidas.add(agente.decir(fueraDeContexto, respuesta, temaActual));
				fraseActual = fueraDeContexto;
				ponerComoYaTratado(fueraDeContexto);
			}
			return true;
		}else{
			return false;
		}
	}
	
	private void extraerOracionesAfirmarivasYPreguntas(ArrayList<Salida> misSalidas, Respuesta respuesta, String idFraseActivada){
		extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecifico(misSalidas, respuesta, idFraseActivada, false);
	}
	
	private void extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecifico(ArrayList<Salida> misSalidas, Respuesta respuesta, String idFraseActivada, Boolean estaEnWorkSpaceEspecifico){
		Pregunta miPregunta = null;
		agregarOracionesAfirmativasDeWorkspaceEspecifico(misSalidas, respuesta.obtenerLosIDsDeLasOracionesAfirmativasActivas(), respuesta, estaEnWorkSpaceEspecifico);
		if( ! idFraseActivada.equals("")){
			
			if(estaEnWorkSpaceEspecifico){
				miPregunta = (Pregunta) this.temaActualDelWorkSpaceEspecifico.buscarUnaFrase(idFraseActivada);
				misSalidas.add(agente.decir(miPregunta, respuesta, temaActualDelWorkSpaceEspecifico));
				fraseActualDelWorkSpaceEspecifico = miPregunta;
			}else{
				miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
				misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
				fraseActual = miPregunta;
			}
			ponerComoYaTratado(miPregunta);
		}
	}
	
	private void extraerOracionesAfirmarivasYPreguntasDeWorkspaceEspecificoSinActualizar(ArrayList<Salida> misSalidas, Respuesta respuesta, String idFraseActivada, Boolean estaEnWorkSpaceEspecifico){
		Pregunta miPregunta = null;
		agregarOracionesAfirmativasDeWorkspaceEspecificoSinActualizar(misSalidas, respuesta.obtenerLosIDsDeLasOracionesAfirmativasActivas(), respuesta, estaEnWorkSpaceEspecifico);
		if( ! idFraseActivada.equals("")){
			
			if(estaEnWorkSpaceEspecifico){
				miPregunta = (Pregunta) this.temaActualDelWorkSpaceEspecifico.buscarUnaFrase(idFraseActivada);
				misSalidas.add(agente.decir(miPregunta, respuesta, temaActualDelWorkSpaceEspecifico));
			}else{
				miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
				misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
			}
			ponerComoYaTratado(miPregunta);
		}
	}
	
	private void agregarOracionesAfirmativasDeWorkspaceEspecifico(ArrayList<Salida> misSalidas, List<String> afirmativas, Respuesta respuesta, boolean estaEnWorkSpaceEspecifico){
		Afirmacion miAfirmacion = null;
		if(afirmativas != null && respuesta != null){
			for(int index = 0; index < afirmativas.size(); index++){
				if(estaEnWorkSpaceEspecifico){
					miAfirmacion = (Afirmacion) this.temaActualDelWorkSpaceEspecifico.buscarUnaFrase(afirmativas.get(index));
					if( ! yaExisteEstaSalida(misSalidas, miAfirmacion.getIdFrase()) ){
						misSalidas.add(agente.decir(miAfirmacion, respuesta, temaActualDelWorkSpaceEspecifico));
						fraseActualDelWorkSpaceEspecifico = miAfirmacion;
					}
				}else{
					miAfirmacion = (Afirmacion) this.temaActual.buscarUnaFrase(afirmativas.get(index));
					if( ! yaExisteEstaSalida(misSalidas, miAfirmacion.getIdFrase()) ){
						misSalidas.add(agente.decir(miAfirmacion, respuesta, temaActual));
						fraseActual = miAfirmacion;
					}
				}
				ponerComoYaTratado(miAfirmacion);
			}
		}
	}
	
	private void agregarOracionesAfirmativasDeWorkspaceEspecificoSinActualizar(ArrayList<Salida> misSalidas, List<String> afirmativas, Respuesta respuesta, boolean estaEnWorkSpaceEspecifico){
		Afirmacion miAfirmacion = null;
		if(afirmativas != null && respuesta != null){
			for(int index = 0; index < afirmativas.size(); index++){
				if(estaEnWorkSpaceEspecifico){
					miAfirmacion = (Afirmacion) this.temaActualDelWorkSpaceEspecifico.buscarUnaFrase(afirmativas.get(index));
					if( ! yaExisteEstaSalida(misSalidas, miAfirmacion.getIdFrase()) ){
						misSalidas.add(agente.decir(miAfirmacion, respuesta, temaActualDelWorkSpaceEspecifico));
					}
				}else{
					miAfirmacion = (Afirmacion) this.temaActual.buscarUnaFrase(afirmativas.get(index));
					if( ! yaExisteEstaSalida(misSalidas, miAfirmacion.getIdFrase()) ){
						misSalidas.add(agente.decir(miAfirmacion, respuesta, temaActual));
					}
				}
				ponerComoYaTratado(miAfirmacion);
			}
		}
	}
	
	private boolean yaExisteEstaSalida(ArrayList<Salida> misSalidas, String idFrase){
		boolean resultado = false;
		
		for(int index = 0; index < misSalidas.size(); index ++){
			if(misSalidas.get(index).getFraseActual().getIdFrase().equals(idFrase)){
				resultado = true;
				break;
			}
		}
		
		return resultado;
	}
	
	private void ponerComoYaTratado(Frase frase){
		hilo.ponerComoDichoEsta(frase);
	}
	
	private void ponerComoYaTratado(Tema tema)
	{
		//if ( ! hilo.existeTema(temaActual)){ //si quiere que solo lo cuente una vez
			estadisticasTemasTratados.darSeguimiento(temaActual);
		//}
		if(tema.sePuedeRepetir()){
			hilo.ponerComoDichoEste(tema);
		}else{
			hilo.noPuedoRepetir(tema);
		}
	}
	
	public void guardarEstadiscitas() throws ClassNotFoundException, SQLException
	{
		estadisticasTemasTratados.guardarEstadiscitasEnBaseDeDatos();
	}
	
}
