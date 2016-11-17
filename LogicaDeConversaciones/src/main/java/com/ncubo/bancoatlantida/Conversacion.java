package com.ncubo.bancoatlantida;

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
	
	private ArrayList<Salida> misSalidas = new ArrayList<Salida>();
	
	private final boolean hayQueComenzarDeNuevoLaConverzacion;
	
	public Conversacion(Temario temario, Cliente participante){
		// Hacer lamdaba para agregar los participantes
		//this.participantes = new Participantes();
		this.participante = participante;
		this.agente = new Agente(temario.contenido().getMiWorkSpaces());
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
		misSalidas.add(agente.decir(saludoGeneral));
		ponerComoYaTratado(saludoGeneral);
		
		Pregunta queQuiere = (Pregunta) this.temario.extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase.esUnaPregunta);
		misSalidas.add(agente.decir(queQuiere));
		fraseActual = queQuiere;
		ponerComoYaTratado(queQuiere);
		
		return misSalidas;
	}
	
	public ArrayList<Salida> analizarLaRespuestaConWatson(String respuestaDelCliente){
		misSalidas.clear();
		if(hayQueComenzarDeNuevoLaConverzacion){
			this.agente = new Agente(temario.contenido().getMiWorkSpaces());
			this.hilo = new HiloDeLaConversacion();
			return inicializarLaConversacion();
		}else{
			Respuesta respuesta = agente.enviarRespuestaAWatson(respuestaDelCliente, fraseActual);
			this.hilo.agregarUnaRespuesta(respuesta);
			Pregunta miPregunta = null;
			
			if(agente.hayIntencionNoAsociadaANingunWorkspace()){
				if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals("saludos")){
					System.out.println("Quiere saludar ...");
					this.temaActual = this.temario.buscarTema(Constantes.FRASE_SALUDO);

					Afirmacion saludar = (Afirmacion) this.temaActual.buscarUnaFrase("saludar");
					misSalidas.add(agente.decir(saludar));
					ponerComoYaTratado(saludar);
					
					Pregunta queQuiere = (Pregunta) this.temario.extraerFraseDeSaludoInicial(CaracteristicaDeLaFrase.esUnaPregunta);
					misSalidas.add(agente.decir(queQuiere));
					fraseActual = queQuiere;
					ponerComoYaTratado(queQuiere);
					
				}else if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals("despedidas")){
					System.out.println("Quiere despedirce ...");
					this.temaActual = this.temario.buscarTema(Constantes.FRASE_DESPEDIDA);
					
					Despedida saludar = (Despedida) this.temaActual.buscarUnaFrase(Constantes.FRASE_DESPEDIDA);
					misSalidas.add(agente.decir(saludar));
					fraseActual = saludar;
					ponerComoYaTratado(saludar);
					
				}else if(agente.obtenerNombreDeLaIntencionGeneralActiva().equals("out_of_scope")){
					System.out.println("Esta fuera de contexto ...");
					this.temaActual = this.temario.buscarTema(Constantes.FRASE_FUERA_DE_CONTEXTO);
					
					Afirmacion fueraDeContexto = (Afirmacion) this.temaActual.buscarUnaFrase("fueraDeContextoGeneral");
					misSalidas.add(agente.decir(fueraDeContexto));
					fraseActual = fueraDeContexto;
					ponerComoYaTratado(fueraDeContexto);
				}
			}
			
			if(agente.hayQueCambiarDeTema()){
				if(this.temaActual != null){
					if( (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_SALUDO)) && (! this.temaActual.obtenerIdTema().equals(Constantes.FRASE_DESPEDIDA)) )
						ponerComoYaTratado(this.temaActual);
				}
				
				agregarOracionesAfirmativas(agente.obtenerIDsDeOracionesAfirmativas());
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
					String idFraseActivada = agente.obtenerNodoActivado(respuesta.messageResponse());
					
					/*if(idFraseActivada.equals("")){
						idFraseActivada = agente.inicializarTemaEnWatson(respuestaDelCliente);
						agente.borrarUnaVariableDelContexto(Constantes.ANYTHING_ELSE);
					}*/
					
					System.out.println("Id de la frase a decir: "+idFraseActivada);
					agregarOracionesAfirmativas(agente.obtenerIDsDeOracionesAfirmativas());
					
					if( ! idFraseActivada.equals("")){
						miPregunta = (Pregunta) this.temaActual.buscarUnaFrase(idFraseActivada);
						misSalidas.add(agente.decir(miPregunta));
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
						misSalidas.add(agente.decir(miPregunta));
						fraseActual = miPregunta;
						ponerComoYaTratado(miPregunta);
					}
				}else{ 
					// Verificar que fue	
					System.out.println("No entendi la ultima pregunta");
					if(fraseActual.esMandatorio()){
						misSalidas.add(agente.volverAPreguntar(fraseActual));
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
	
	private void verificarYAgregarOracionesAfirmativas(Respuesta respuesta){
		
		if(respuesta.hayOracionesAfirmativasActivas()){
			agregarOracionesAfirmativas(respuesta.obtenerLosIDsDeLasOracionesAfirmativasActivas());
		}
		agente.borrarUnaVariableDelContexto(Constantes.ORACIONES_AFIRMATIVAS);
	}
	
	private void agregarOracionesAfirmativas(List<String> afirmativas){
		Afirmacion miAfirmacion = null;
		if(afirmativas != null){
			for(int index = 0; index < afirmativas.size(); index++){
				miAfirmacion = (Afirmacion) this.temaActual.buscarUnaFrase(afirmativas.get(index));
				if( ! misSalidas.contains(miAfirmacion)){
					misSalidas.add(agente.decir(miAfirmacion));
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
