package com.ncubo.logicaDeConversaciones;

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

public class Conversacion {

	//private Participantes participantes;
	private Cliente participante;
	private HiloDeLaConversacion hilo; // Mantiene el contexto, osea todas las intenciones y entidades, sabe que se dijo 
	private Temario temario;
	private Agente agente;
	private Tema temaActual;
	private Frase fraseActual = null;
	private boolean estaEnWorkSpaceEspecifico = false;
	private ArrayList<Salida> misSalidas = new ArrayList<Salida>();
	
	private final boolean hayQueComenzarDeNuevoLaConverzacion;
	
	public Conversacion(Temario temario, Cliente participante){
		// Hacer lamdaba para agregar los participantes
		//this.participantes = new Participantes();
		this.participante = participante;
		this.agente = new Agente(temario.contenido().getMiWorkSpaces());
		this.agente.manifestarseEnFormaOral();
		
		this.hilo = new HiloDeLaConversacion();
		//this.participantes.agregar(agente).agregar(participante);
		this.temario = temario;
		hayQueComenzarDeNuevoLaConverzacion = false; // Cerro seccion o timeout
	}
	
	public ArrayList<Salida> inicializarLaConversacion(){
		misSalidas.clear();
		
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
		misSalidas.clear();
		estaEnWorkSpaceEspecifico = false;
		if(hayQueComenzarDeNuevoLaConverzacion){
			this.agente = new Agente(temario.contenido().getMiWorkSpaces());
			this.hilo = new HiloDeLaConversacion();
			return inicializarLaConversacion();
		}else{
			Respuesta respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
			this.hilo.agregarUnaRespuesta(respuesta);
			Pregunta miPregunta = null;
			
			verificarIntencionNoAsociadaANingunWorkspace(respuesta);
			
			if(agente.hayQueAbordarElTema()){
				agente.yaNoAbordarElTema();
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
				agente.cambiarAWorkspaceGeneral();
				respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
				this.hilo.agregarUnaRespuesta(respuesta);
			}
			
			if(agente.hayQueCambiarDeTema()){
				if(this.temaActual != null){
					if( (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_SALUDO)) && (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_DESPEDIDA)) )
						ponerComoYaTratado(this.temaActual);
				}
				
				agregarOracionesAfirmativas(agente.obtenerIDsDeOracionesAfirmativas(), respuesta);
				String idFraseActivada = respuesta.obtenerFraseActivada();
				if( ! idFraseActivada.equals("")){
					miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
					misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
					fraseActual = miPregunta;
					ponerComoYaTratado(miPregunta);
				}
				
				this.temaActual = this.temario.proximoTemaATratar(temaActual, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), agente.obtenerNombreDelWorkspaceActual(), agente.obtenerNombreDeLaIntencionGeneralActiva());
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
					agregarOracionesAfirmativas(agente.obtenerIDsDeOracionesAfirmativas(), respuesta);
					
					if( ! idFraseActivada.equals("")){
						miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
						misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
						fraseActual = miPregunta;
						ponerComoYaTratado(miPregunta);
					}
				}
			}else{
				if (agente.entendiLaUltimaPregunta()){
					
					verificarYAgregarOracionesAfirmativas(respuesta);
					
					String fraseAvtiva = respuesta.obtenerFraseActivada();
					if( ! fraseAvtiva.equals("")){
						miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(fraseAvtiva);
						misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
						fraseActual = miPregunta;
						ponerComoYaTratado(miPregunta);
					}
				}else{ 
					// Verificar que fue	
					System.out.println("No entendi la ultima pregunta");
					if(fraseActual.esMandatorio()){
						misSalidas.add(agente.volverAPreguntar(fraseActual, respuesta, temaActual));
					}
				}
			}
			
			if(respuesta.seTerminoElTema()){
				Tema miTema = this.temario.proximoTemaATratar(temaActual, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), agente.obtenerNombreDelWorkspaceActual(), agente.obtenerNombreDeLaIntencionGeneralActiva());
				if(miTema == null){
					this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
					agente.borrarUnaVariableDelContexto(Constantes.TERMINO_EL_TEMA);
					agente.cambiarAWorkspaceGeneral();
				}
			}
		}
		
		return misSalidas;
	}
	
	// Especial para "Conocerte"
	public void establecerUnWorkspaseEspecifico(String nombreDelWorkSpaseAUsar, String nombreDeLaIntencion){
		estaEnWorkSpaceEspecifico = true;
		agente.establecerNombreDelWorkspaceActual(nombreDelWorkSpaseAUsar);
		this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO); // Poner tema saludo del conocerte
		agente.establecerNombreDeLaIntencionGeneralActiva(nombreDeLaIntencion);
	}
	
	public ArrayList<Salida> analizarLaRespuestaConWatsonEnUnWorkspaceEspecifico(String respuestaDelCliente){
		misSalidas.clear();
		Respuesta respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
		this.hilo.agregarUnaRespuesta(respuesta);
		Pregunta miPregunta = null;
		
		if(agente.hayQueAbordarElTema()){
			agente.yaNoAbordarElTema();
			this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
			respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
			this.hilo.agregarUnaRespuesta(respuesta);
		}
		
		if(agente.hayQueCambiarDeTema()){
			if(this.temaActual != null){
				if( (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_SALUDO)) && (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_DESPEDIDA)) )
					ponerComoYaTratado(this.temaActual);
			}
			
			agregarOracionesAfirmativas(agente.obtenerIDsDeOracionesAfirmativas(), respuesta);
			String idFraseActivada = respuesta.obtenerFraseActivada();
			if( ! idFraseActivada.equals("")){
				miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
				misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
				fraseActual = miPregunta;
				ponerComoYaTratado(miPregunta);
			}
			
			this.temaActual = this.temario.proximoTemaATratar(temaActual, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), agente.obtenerNombreDelWorkspaceActual(), agente.obtenerNombreDeLaIntencionGeneralActiva());
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
				agregarOracionesAfirmativas(agente.obtenerIDsDeOracionesAfirmativas(), respuesta);
				
				if( ! idFraseActivada.equals("")){
					miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
					misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
					fraseActual = miPregunta;
					ponerComoYaTratado(miPregunta);
				}
			}
		}else{
			if (agente.entendiLaUltimaPregunta()){
				
				verificarYAgregarOracionesAfirmativas(respuesta);
				
				String fraseAvtiva = respuesta.obtenerFraseActivada();
				if( ! fraseAvtiva.equals("")){
					miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(fraseAvtiva);
					misSalidas.add(agente.decir(miPregunta, respuesta, temaActual));
					fraseActual = miPregunta;
					ponerComoYaTratado(miPregunta);
				}
			}else{ 
				// Verificar que fue	
				System.out.println("No entendi la ultima pregunta");
				if(fraseActual.esMandatorio()){
					misSalidas.add(agente.volverAPreguntar(fraseActual, respuesta, temaActual));
				}
			}
		}
		
		if(respuesta.seTerminoElTema()){
			Tema miTema = this.temario.proximoTemaATratar(temaActual, hilo.verTemasYaTratadosYQueNoPuedoRepetir(), agente.obtenerNombreDelWorkspaceActual(), agente.obtenerNombreDeLaIntencionGeneralActiva());
			if(miTema == null){ // TODO Ya no hay mas temas -  Que hago
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);
				agente.borrarUnaVariableDelContexto(Constantes.TERMINO_EL_TEMA);
			}
		}
		
		return misSalidas;
	}
	
	private void verificarIntencionNoAsociadaANingunWorkspace(Respuesta respuesta){
		if(agente.hayIntencionNoAsociadaANingunWorkspace()){
			if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals("saludos")){
				System.out.println("Quiere saludar ...");
				this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);

				Afirmacion saludar = (Afirmacion) this.temaActual.buscarUnaFrase("saludar");
				misSalidas.add(agente.decir(saludar, respuesta, temaActual));
				ponerComoYaTratado(saludar);
				
				Pregunta queQuiere = (Pregunta) this.temario.extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase.esUnaPregunta);
				misSalidas.add(agente.decir(queQuiere, respuesta, temaActual));
				fraseActual = queQuiere;
				ponerComoYaTratado(queQuiere);
				
			}else if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals("despedidas")){
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
			}
		}
	}
	
	private void verificarYAgregarOracionesAfirmativas(Respuesta respuesta){
		
		if(respuesta.hayOracionesAfirmativasActivas()){
			agregarOracionesAfirmativas(respuesta.obtenerLosIDsDeLasOracionesAfirmativasActivas(), respuesta);
		}
		agente.borrarUnaVariableDelContexto(Constantes.ORACIONES_AFIRMATIVAS);
	}
	
	private void agregarOracionesAfirmativas(List<String> afirmativas, Respuesta respuesta){
		Afirmacion miAfirmacion = null;
		if(afirmativas != null){
			for(int index = 0; index < afirmativas.size(); index++){
				miAfirmacion = (Afirmacion) this.temaActual.buscarUnaFrase(afirmativas.get(index));
				if( ! misSalidas.contains(miAfirmacion)){
					misSalidas.add(agente.decir(miAfirmacion, respuesta, temaActual));
					fraseActual = miAfirmacion;
					ponerComoYaTratado(miAfirmacion);
				}
			}
		}
	}
	
	private void ponerComoYaTratado(Frase frase){
		hilo.ponerComoDichoEsta(frase);
	}
	
	private void ponerComoYaTratado(Tema tema){
		if(tema.sePuedeRepetir()){
			hilo.ponerComoDichoEste(tema);
		}else{
			hilo.noPuedoRepetir(tema);
		}
	}
	
}
