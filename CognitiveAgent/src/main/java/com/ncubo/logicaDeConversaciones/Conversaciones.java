package com.ncubo.logicaDeConversaciones;

import java.util.ArrayList;
import java.util.Hashtable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.chatbot.watson.TextToSpeechWatson;
import com.ncubo.conf.Usuario;

public class Conversaciones {

	// key puede ser el id del usuario o el id de la seccion
	private final static Hashtable<String, Conversacion> misConversaciones = new Hashtable<String, Conversacion>();
	private final static Hashtable<String, Cliente> misClientes = new Hashtable<String, Cliente>();
	private static Temario temarioDelBancoAtlantida;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final static String URL_DE_LOS_AUDIOS = "/archivossubidos/audios-";
	
	public Conversaciones(String pathXML){
		System.out.println("El path xml es: "+pathXML);
		temarioDelBancoAtlantida = new TemarioDelBancoAtlantida(pathXML);
	}
	
	private String crearUnaNuevoConversacion(Usuario usuario){
		String resultado = "";
		Cliente cliente = null;
		
		if(usuario.getEstaLogueado()){
			cliente = new Cliente(usuario.getUsuarioNombre(), usuario.getUsuarioId());
			cliente.agregarIdsDeSesiones(usuario.getIdSesion());
			misClientes.put(cliente.getMiId(), cliente);
			
			if(existeLaConversacion(usuario.getIdSesion())){
				Conversacion coversacion = misConversaciones.get(usuario.getIdSesion());
				coversacion.cambiarParticipante(cliente);
				misConversaciones.put(usuario.getIdSesion(), coversacion);
			}else{
				Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente);
				misConversaciones.put(usuario.getIdSesion(), coversacion);
			}
			resultado = "La nueva conversacion se creo exitosamente.";
			System.out.println(resultado);
		}else{
			if (! usuario.getIdSesion().equals("")){
				if( ! existeLaConversacion(usuario.getIdSesion())){
					cliente = new Cliente();
					Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente);
					misConversaciones.put(usuario.getIdSesion(), coversacion);
					resultado = "La nueva conversacion se creo exitosamente.";
					System.out.println(resultado);
				}
			}else{
				resultado = "La conversacion NO pudo ser creada";
				throw new ChatException(resultado);
			}
		}
		
		return resultado;
	}
	
	public ArrayList<Salida> conversarConElAgente(Usuario cliente, String textoDelCliente, boolean esConocerte){
		ArrayList<Salida> resultado = null;
		logger.debug("Conversar ..........");
		System.out.println("Coversar con "+cliente.getIdSesion());
		
		if( cliente.getUsuarioId().equals("") && cliente.getIdSesion().equals("") || (cliente.getUsuarioId().equals("") && cliente.getEstaLogueado())){ // Esta logueado
			throw new ChatException("No se puede chatear porque no existe usuario ni id de sesion");
		}
		
		if(cliente.getEstaLogueado()){ // Esta logueado
			// Verificar si ya el usuario existe
			if(existeElCliente(cliente.getUsuarioId()) && existeLaConversacion(cliente.getIdSesion())){
				// TODO Verificar si cambio el id de sesion, si es asi agregarla al cliente y hacerlo saber a conversacion
				misClientes.get(cliente.getUsuarioId()).verificarSiExisteElIdSesion(cliente.getIdSesion());
				misConversaciones.get(cliente.getIdSesion()).cambiarParticipante(misClientes.get(cliente.getUsuarioId())); // Actualizar cliente
				resultado = hablarConElAjente(cliente, textoDelCliente, esConocerte);
			}else{ // Crear un nuevo Cliente
				crearUnaNuevoConversacion(cliente);
				resultado = hablarConElAjente(cliente, textoDelCliente, esConocerte);
				/*if(existeLaConversacion(cliente.getIdSesion())){ // Es porque ya se cliente esta conversando y no se habia logueado, eso quiere decir que se tiene que mantener el contexto y NO saludar de nuevo
					resultado = hablarConElAjente(cliente, textoDelCliente, esConocerte);
				}else{
					resultado = inicializarConversacionConElAgente(cliente.getIdSesion());
				}*/
			}
		}else{
			if(! cliente.getIdSesion().equals("")){
				if(existeLaConversacion(cliente.getIdSesion())){
					resultado = hablarConElAjente(cliente, textoDelCliente, esConocerte);
				}else{ // Crear una nueva conversacion
					crearUnaNuevoConversacion(cliente);
					resultado = inicializarConversacionConElAgente(cliente.getIdSesion());
				}
			}else{
				throw new ChatException("No se puede chatear porque no existe id de sesion");
			}
		}
		
		return resultado;
	}
	
	private boolean existeElCliente(String idDelCliente){
		return misClientes.containsKey(idDelCliente);
	}
	
	private boolean existeLaConversacion(String idDelCliente){
		return misConversaciones.containsKey(idDelCliente);
	}
	
	public ArrayList<Salida> inicializarConversacionConElAgente(String idDelCliente){
		return misConversaciones.get(idDelCliente).inicializarLaConversacion();
	}
	
	public ArrayList<Salida> hablarConElAjente(Usuario cliente, String textoDelCliente, boolean esConocerte){
		ArrayList<Salida> resultado = null;
		
		if(esConocerte){
			resultado = misConversaciones.get(cliente.getIdSesion()).analizarLaRespuestaConWatsonEnUnWorkspaceEspecifico(textoDelCliente, "ConocerteGeneral", "conocerte");
		}else{
			resultado = misConversaciones.get(cliente.getIdSesion()).analizarLaRespuestaConWatson(textoDelCliente);
		}
		
		return resultado;
	}
	
	public void generarAudiosEstaticos(String usuarioTTS, String contrasenaTTS, String vozTTS, String pathAGuardar, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP){
		HiloParaGenerarAudiosEstaticos hilo = new HiloParaGenerarAudiosEstaticos(usuarioTTS, contrasenaTTS, vozTTS, pathAGuardar, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP);
		hilo.start();
	}
	
	public void generarAudiosEstaticosDeUnTema(String usuarioTTS, String contrasenaTTS, String vozTTS, String pathAGuardar, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, int index){
		TextToSpeechWatson.getInstance(usuarioTTS, contrasenaTTS, vozTTS, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, pathAGuardar);
		System.out.println(String.format("El path a guardar los audios es %s y la url publica es %s", pathAGuardar, URL_DE_LOS_AUDIOS));
		temarioDelBancoAtlantida.generarAudioEstaticosDeUnTema(pathAGuardar, URL_DE_LOS_AUDIOS, index);
		System.out.println("Se termino de generar audios estaticos.");
	}
	
	public String verMiTemario(){
		return temarioDelBancoAtlantida.verMiTemario();
	}
	
	private class HiloParaGenerarAudiosEstaticos extends Thread{
		private String usuarioTTS;
		private String contrasenaTTS;
		private String vozTTS;
		private String pathAGuardar;
		private String usuarioFTP;
		private String contrasenaFTP;
		private String hostFTP;
		private int puetoFTP;
		
		public HiloParaGenerarAudiosEstaticos(String usuarioTTS, String contrasenaTTS, String vozTTS, String pathAGuardar, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP){
			this.usuarioTTS = usuarioTTS;
			this.contrasenaTTS = contrasenaTTS;
			this.vozTTS = vozTTS;
			this.pathAGuardar = pathAGuardar;
			this.usuarioFTP = usuarioFTP;
			this.contrasenaFTP = contrasenaFTP;
			this.hostFTP = hostFTP;
			this.puetoFTP = puetoFTP;
		}
		
		public void run(){
			TextToSpeechWatson.getInstance(usuarioTTS, contrasenaTTS, vozTTS, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, pathAGuardar);
			System.out.println(String.format("El path a guardar los audios es %s y la url publica es %s", pathAGuardar, URL_DE_LOS_AUDIOS));
			temarioDelBancoAtlantida.generarAudioEstaticosDeTodasLasFrases(pathAGuardar, URL_DE_LOS_AUDIOS);
			System.out.println("Se termino de generar audios estaticos.");
		}
	}

}
