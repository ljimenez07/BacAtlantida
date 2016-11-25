package com.ncubo.chatbot.participantes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.chatbot.partesDeLaConversacion.Frase;
import com.ncubo.chatbot.partesDeLaConversacion.Pregunta;
import com.ncubo.chatbot.partesDeLaConversacion.Respuesta;
import com.ncubo.chatbot.watson.ConversationWatson;
import com.ncubo.chatbot.watson.WorkSpace;

// Es como el watson de Ncubo
public class Agente extends Participante{

	private final ArrayList<WorkSpace> miWorkSpaces;
	private static Hashtable<String, String> miContextos = new Hashtable<String, String>();
	private static Hashtable<String, ConversationWatson> miWatsonConversacions = new Hashtable<String, ConversationWatson>();
	private String nombreDelWorkSpaceGeneral;
	private static final int MAXIMO_DE_INTENTOS_OPCIONALES = 4; // Sino se aborda el tema
	private String nombreDeWorkspaceActual;
	private String nombreDeLaIntencionGeneralActiva;
	private boolean estaEnElWorkspaceGeneral;
	private boolean noEntendiLaUltimaRespuesta;
	private int numeroDeIntentosActualesEnRepetirUnaPregunta;
	private boolean cambiarDeTema = false;
	private boolean abordarElTema = false;
	private boolean hayIntencionNoAsociadaANingunWorkspace;
	
	public Agente(ArrayList<WorkSpace> miWorkSpaces){
		this.noEntendiLaUltimaRespuesta = true;
		this.estaEnElWorkspaceGeneral = true;
		this.miWorkSpaces = miWorkSpaces;
		this.nombreDelWorkSpaceGeneral = "";
		this.nombreDeLaIntencionGeneralActiva = "";
		this.numeroDeIntentosActualesEnRepetirUnaPregunta = 1;
		this.hayIntencionNoAsociadaANingunWorkspace = false;
		this.inicializarContextos();
	}
	
	private void inicializarContextos(){
		for(WorkSpace workspace: miWorkSpaces){
			ConversationWatson conversacion = new ConversationWatson(workspace.getUsuarioIBM(), workspace.getContrasenaIBM(), workspace.getIdIBM());
			String contexto = conversacion.enviarMSG("", null).getContext().toString();
			miWatsonConversacions.put(workspace.getNombre(), conversacion);
			miContextos.put(workspace.getNombre(), contexto);
			System.out.println("Contexto: "+contexto);
			if(workspace.getTipo().equals(Constantes.WORKSPACE_GENERAL)){
				nombreDeWorkspaceActual = workspace.getNombre();
				nombreDelWorkSpaceGeneral = workspace.getNombre();
			}
		}
		
		if(nombreDelWorkSpaceGeneral == ""){
			throw new ChatException("No existe un workspace general en para conectarse a Watson IBM");
		}
	}
	
	public void seTieneQueGenerarUnNuevoContextoParaWatsonEnElWorkspaceActual(){
		ConversationWatson conversacion = miWatsonConversacions.get(nombreDeWorkspaceActual);
		String nuevoContexto = conversacion.enviarMSG("", null).getContext().toString();
		miContextos.put(nombreDeWorkspaceActual, nuevoContexto);
	}
	
	public MessageResponse llamarAWatsonGeneral(String mensaje){
		return llamarAWatson(mensaje, nombreDelWorkSpaceGeneral);
	}
	
	private Intent determinarLaIntencionGeneral(String mensaje){
		List<Intent> intenciones = llamarAWatsonGeneral(mensaje).getIntents();
		Intent intencion = null;
		double confidence = 0;
		
		for(int index = 0; index < intenciones.size(); index ++){
			if(intenciones.get(index).getConfidence() > confidence){
				confidence = intenciones.get(index).getConfidence();
				intencion = intenciones.get(index);
			}
		}
		System.out.println("La intencion general es: "+intencion.getIntent());
		return intencion;
	}
	
	private boolean elClienteQuiereCambiarDeIntencionGeneral(Intent intencion){
		if(intencion != null){
			return (intencion.getConfidence() >= Constantes.WATSON_CONVERSATION_CONFIDENCE); 
		}else{
			return false;
		}
	}
	
	public MessageResponse llamarAWatson(String mensaje, String nombreWorkspace){
		return miWatsonConversacions.get(nombreWorkspace).enviarAWatson(mensaje, miContextos.get(nombreWorkspace));
	}
	
	public Respuesta enviarRespuestaAWatson(String respuestaDelClinete, Frase frase){
		Respuesta respuesta = null;
		if(estaEnElWorkspaceGeneral){
			respuesta = analizarRespuestaGeneral(respuestaDelClinete, frase);
		}else{
			respuesta = analizarRespuesta(respuestaDelClinete, frase);
		}
		
		return respuesta;
	}
	
	public Respuesta analizarRespuestaGeneral(String respuestaDelClinete, Frase frase){
		Respuesta respuesta = null;
		
		respuesta = new Respuesta(frase, miWatsonConversacions.get(nombreDeWorkspaceActual), miContextos.get(nombreDeWorkspaceActual));
		respuesta.llamarAWatson(respuestaDelClinete);
		
		noEntendiLaUltimaRespuesta = (! respuesta.entendiLaRespuesta()) && (frase.esMandatorio()) && 
				(numeroDeIntentosActualesEnRepetirUnaPregunta != MAXIMO_DE_INTENTOS_OPCIONALES);
		if(noEntendiLaUltimaRespuesta){
			numeroDeIntentosActualesEnRepetirUnaPregunta ++;
		}else{
			if(numeroDeIntentosActualesEnRepetirUnaPregunta == MAXIMO_DE_INTENTOS_OPCIONALES){
				// Abordar el tema
				abordarElTema = true; // Buscar otro tema
			}else{
				// Actualizar contexto
				miContextos.put(nombreDeWorkspaceActual, respuesta.getMiContexto());
				
				// Analizar si ya tengo que cambiar de workspace
				try{
					String intencionDelCliente = respuesta.obtenerLaIntencionDeConfianzaDeLaRespuesta().getNombre();
					WorkSpace workspace = extraerUnWorkspaceConLaIntencion(intencionDelCliente);
					if( (nombreDeWorkspaceActual.equals(nombreDelWorkSpaceGeneral)) && workspace != null){
						nombreDeWorkspaceActual = workspace.getNombre();
						nombreDeLaIntencionGeneralActiva = intencionDelCliente;
						System.out.println(String.format("Cambiando al workspace %s e intencion %s", nombreDeWorkspaceActual, nombreDeLaIntencionGeneralActiva));
						cambiarDeTema = true; // Buscar otro tema
						abordarElTema = false;
						estaEnElWorkspaceGeneral = false;
						this.hayIntencionNoAsociadaANingunWorkspace = false;
					}else{
						System.out.println("Intencion no asociada a ningun workspace");
						nombreDeLaIntencionGeneralActiva = intencionDelCliente;
						hayIntencionNoAsociadaANingunWorkspace = true;
					}
				}catch(Exception e){
					System.out.println("No hay ninguna intencion real o de confianza");
					nombreDeLaIntencionGeneralActiva = Constantes.INTENCION_FUERA_DE_CONTEXTO;
					hayIntencionNoAsociadaANingunWorkspace = true;
				}
				
			}
			numeroDeIntentosActualesEnRepetirUnaPregunta = 1;
		}
		
		return respuesta;
	}
	
	public Respuesta analizarRespuesta(String respuestaDelClinete, Frase frase){
		Respuesta respuesta = null;
		
		respuesta = new Respuesta(frase, miWatsonConversacions.get(nombreDeWorkspaceActual), miContextos.get(nombreDeWorkspaceActual));
		respuesta.llamarAWatson(respuestaDelClinete);
		
		noEntendiLaUltimaRespuesta = (! (respuesta.entendiLaRespuesta() && ! respuesta.hayAlgunAnythingElse())) && 
				(numeroDeIntentosActualesEnRepetirUnaPregunta != MAXIMO_DE_INTENTOS_OPCIONALES);
		if(noEntendiLaUltimaRespuesta){
			System.out.println("No entendi la respuesta ...");
			// Validar si es que el usuario cambio de intencion
			if(elClienteQuiereCambiarDeIntencionGeneral(determinarLaIntencionGeneral(respuestaDelClinete))){
				abordarElTema = true;
				System.out.println("Se requiere abordar el tema ...");
				// TODO Tengo que cambiar el contexto
				//this.seTieneQueGenerarUnNuevoContextoParaWatsonEnElWorkspaceActual();
			}
			
			if(frase.esMandatorio()){	
				numeroDeIntentosActualesEnRepetirUnaPregunta ++;
			}
		}else{
			if(numeroDeIntentosActualesEnRepetirUnaPregunta == MAXIMO_DE_INTENTOS_OPCIONALES){
				// Abordar el tema
				cambiarDeTema = true; // Buscar otro tema
			}else{
				// Actualizar contexto
				miContextos.put(nombreDeWorkspaceActual, respuesta.getMiContexto());
				
				// Analizar si tengo que cambiar de workspace
				cambiarDeTema = respuesta.seTerminoElTema();
				if(cambiarDeTema){
					// Desactivar flag del contexto
					borrarUnaVariableDelContexto(Constantes.TERMINO_EL_TEMA);
				}
			}
			numeroDeIntentosActualesEnRepetirUnaPregunta = 1;
		}
		borrarUnaVariableDelContexto(Constantes.ANYTHING_ELSE);
		borrarUnaVariableDelContexto(Constantes.NODO_ACTIVADO);
		
		return respuesta;
	}

	private WorkSpace extraerUnWorkspaceConLaIntencion(String nombreDeLaIntencion){
		for(WorkSpace workspace: miWorkSpaces){
			if(workspace.tieneLaIntencion(nombreDeLaIntencion)){
				return workspace;
			}
		}
		return null;
	}
	
	public boolean hayQueCambiarDeTema(){
		return cambiarDeTema;
	}
	
	public void yaNoCambiarDeTema(){
		cambiarDeTema = false;
	}
	
	public void cambiarDeTema(){
		cambiarDeTema = true;
	}
	
	public boolean hayQueAbordarElTema(){
		return abordarElTema;
	}
	
	public void yaNoAbordarElTema(){
		abordarElTema = false;
	}
	
	public boolean entendiLaUltimaPregunta(){
		return ! noEntendiLaUltimaRespuesta;
	}
	
	public String obtenerNombreDelWorkspaceActual(){
		return nombreDeWorkspaceActual;
	}
	
	public void establecerNombreDelWorkspaceActual(String nombreDelWorkSpace){
		estaEnElWorkspaceGeneral = false;
		nombreDeWorkspaceActual = nombreDelWorkSpace;
	}
	
	public void cambiarAWorkspaceGeneral(){
		estaEnElWorkspaceGeneral = true;
		nombreDeWorkspaceActual = nombreDelWorkSpaceGeneral;
	}
	
	public void activarTemaEnElContextoDeWatson(String nombreTema){
		String context = miContextos.get(nombreDeWorkspaceActual);
		System.out.println(context);
		JSONObject obj = null;
		try {
			obj = new JSONObject(context);
			obj.put("idTema", nombreTema);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Contexto modificado: "+obj.toString());
		miContextos.put(nombreDeWorkspaceActual, obj.toString());
	}
	
	public void borrarUnaVariableDelContexto(String nombreDeLaVariable){
		String context = miContextos.get(nombreDeWorkspaceActual);
		JSONObject obj = null;
		try {
			obj = new JSONObject(context);
			obj.remove(nombreDeLaVariable);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Contexto modificado: "+obj.toString());
		miContextos.put(nombreDeWorkspaceActual, obj.toString());
	}
	
	public Respuesta inicializarTemaEnWatson(String respuestaDelCliente){
		Respuesta respuesta = new Respuesta(miWatsonConversacions.get(nombreDeWorkspaceActual), miContextos.get(nombreDeWorkspaceActual));
		//MessageResponse response = miWatsonConversacions.get(nombreDeWorkspaceActual).enviarAWatson(respuestaDelCliente, miContextos.get(nombreDeWorkspaceActual));
		respuesta.llamarAWatson(respuestaDelCliente);
		//String context = response.getContext().toString();
		String context = respuesta.messageResponse().getContext().toString();
		miContextos.put(nombreDeWorkspaceActual, context);
		/*try{
			return response.getContext().get(Constantes.NODO_ACTIVADO).toString();
		}catch(Exception e){
			System.out.println("No existe el id nodo");
			return "";
		}*/
		return respuesta;
	}
	
	public String obtenerNodoActivado(MessageResponse response){
		try{
			return response.getContext().get(Constantes.NODO_ACTIVADO).toString();
		}catch(Exception e){
			System.out.println("No existe el id nodo");
			return "";
		}
	}
	
	public List<String> obtenerIDsDeOracionesAfirmativas(){
		String context = miContextos.get(nombreDeWorkspaceActual);
		JSONArray afirmativas;
		JSONObject obj = null;
		List<String> idsDeOracionesAfirmativas = new ArrayList<String>();
		
		try {
			obj = new JSONObject(context);
			afirmativas = obj.getJSONArray(Constantes.ORACIONES_AFIRMATIVAS);
			System.out.println("Oraciones afirmativas: "+afirmativas);
			for (int i=0; i < afirmativas.length(); i++) {
				idsDeOracionesAfirmativas.add(afirmativas.getString(i) );
			}
			borrarUnaVariableDelContexto(Constantes.ORACIONES_AFIRMATIVAS);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return idsDeOracionesAfirmativas;
	}
	
	public String obtenerNombreDeLaIntencionGeneralActiva() {
		return this.nombreDeLaIntencionGeneralActiva;
	}
	
	public void establecerNombreDeLaIntencionGeneralActiva(String nombreDeLaIntencion) {
		this.nombreDeLaIntencionGeneralActiva = nombreDeLaIntencion;
	}
	
	public boolean hayIntencionNoAsociadaANingunWorkspace(){
		return this.hayIntencionNoAsociadaANingunWorkspace;
	}
	
}
