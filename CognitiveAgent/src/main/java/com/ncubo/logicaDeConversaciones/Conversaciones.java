package com.ncubo.logicaDeConversaciones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.chatbot.watson.TextToSpeechWatson;
import com.ncubo.conf.Usuario;
import com.ncubo.dao.ConsultaDao;

@Component
public class Conversaciones {

	// key puede ser el id del usuario o el id de la seccion
	private final static Hashtable<String, Conversacion> misConversaciones = new Hashtable<String, Conversacion>();
	private final static Hashtable<String, Cliente> misClientes = new Hashtable<String, Cliente>();
	private static Temario temarioDelBancoAtlantida;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ConsultaDao consultaDao;
	
	public Conversaciones(){
	}
	
	private String crearUnaNuevoConversacion(Usuario usuario){
		String resultado = "";
		Cliente cliente = null;
		
		if(usuario.getEstaLogueado()){
			cliente = new Cliente(usuario.getUsuarioNombre(), usuario.getUsuarioId());
			cliente.agregarIdsDeSesiones(usuario.getIdSesion());
			
			synchronized(misClientes){
				misClientes.put(cliente.getMiId(), cliente);
			}
			
			synchronized(misConversaciones){
				if(existeLaConversacion(usuario.getIdSesion())){
					Conversacion coversacion = misConversaciones.get(usuario.getIdSesion());
					coversacion.cambiarParticipante(cliente);
					misConversaciones.put(usuario.getIdSesion(), coversacion);
				}else{
					Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente, consultaDao);
					misConversaciones.put(usuario.getIdSesion(), coversacion);
				}
			}
			
			resultado = "La nueva conversacion se creo exitosamente.";
			System.out.println(resultado);
		}else{
			if (! usuario.getIdSesion().equals("")){
				if( ! existeLaConversacion(usuario.getIdSesion())){
					cliente = new Cliente();
					Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente, consultaDao);
					synchronized(misConversaciones){
						misConversaciones.put(usuario.getIdSesion(), coversacion);
					}
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
				resultado = hablarConElAgente(cliente, textoDelCliente, esConocerte);
			}else{ // Crear un nuevo Cliente
				crearUnaNuevoConversacion(cliente);
				resultado = hablarConElAgente(cliente, textoDelCliente, esConocerte);
				/*if(existeLaConversacion(cliente.getIdSesion())){ // Es porque ya se cliente esta conversando y no se habia logueado, eso quiere decir que se tiene que mantener el contexto y NO saludar de nuevo
					resultado = hablarConElAjente(cliente, textoDelCliente, esConocerte);
				}else{
					resultado = inicializarConversacionConElAgente(cliente.getIdSesion());
				}*/
			}
		}else{
			if(! cliente.getIdSesion().equals("")){
				if(existeLaConversacion(cliente.getIdSesion())){
					resultado = hablarConElAgente(cliente, textoDelCliente, esConocerte);
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
	
	public ArrayList<Salida> hablarConElAgente(Usuario cliente, String textoDelCliente, boolean esConocerte){
		ArrayList<Salida> resultado = null;
		
		if(esConocerte){
			resultado = misConversaciones.get(cliente.getIdSesion()).analizarLaRespuestaConWatsonEnUnWorkspaceEspecifico(textoDelCliente, "ConocerteGeneral", "conocerte");
		}else{
			resultado = misConversaciones.get(cliente.getIdSesion()).analizarLaRespuestaConWatson(textoDelCliente);
		}
		
		return resultado;
	}
	
	public void generarAudiosEstaticos(String usuarioTTS, String contrasenaTTS, String vozTTS, String pathAGuardar, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, String url){
		HiloParaGenerarAudiosEstaticos hilo = new HiloParaGenerarAudiosEstaticos(usuarioTTS, contrasenaTTS, vozTTS, pathAGuardar, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, url);
		hilo.start();
	}
	
	public void generarAudiosEstaticosDeUnTema(String usuarioTTS, String contrasenaTTS, String vozTTS, String pathAGuardar, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, int index, String url){
		TextToSpeechWatson.getInstance(usuarioTTS, contrasenaTTS, vozTTS, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, pathAGuardar);
		System.out.println(String.format("El path a guardar los audios es %s y la url publica es %s", pathAGuardar, url));
		temarioDelBancoAtlantida.generarAudioEstaticosDeUnTema(pathAGuardar, url, index);
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
		private String urlAReproducir;
		
		public HiloParaGenerarAudiosEstaticos(String usuarioTTS, String contrasenaTTS, String vozTTS, String pathAGuardar, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, String url){
			this.usuarioTTS = usuarioTTS;
			this.contrasenaTTS = contrasenaTTS;
			this.vozTTS = vozTTS;
			this.pathAGuardar = pathAGuardar;
			this.usuarioFTP = usuarioFTP;
			this.contrasenaFTP = contrasenaFTP;
			this.hostFTP = hostFTP;
			this.puetoFTP = puetoFTP;
			this.urlAReproducir = url;
		}
		
		public void run(){
			TextToSpeechWatson.getInstance(usuarioTTS, contrasenaTTS, vozTTS, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, pathAGuardar);
			System.out.println(String.format("El path a guardar los audios es %s y la url publica es %s", pathAGuardar, urlAReproducir));
			temarioDelBancoAtlantida.generarAudioEstaticosDeTodasLasFrases(pathAGuardar, urlAReproducir);
			System.out.println("Se termino de generar audios estaticos.");
		}
	}

	public String borrarUnaConversacion(String idSesion){
		String resultado = "La conversación con id "+idSesion+" no existe.";
		if(existeLaConversacion(idSesion)){
			try {
				misConversaciones.get(idSesion).guardarEstadiscitas();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			synchronized(misConversaciones){
				misConversaciones.remove(idSesion);
				resultado = "La conversación con id "+idSesion+" se borró exitosamente.";
			}
		}
		System.out.println(resultado);
		return resultado;
	}
	
	public String verTodasLasCoversacionesActivas(){
		String resultado = "";
		
		Enumeration<String> keys = misConversaciones.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			resultado += " -> "+key+" \n";
		}
		
		return resultado;
	}
	
	public String verTodosLosClientesActivos(){
		String resultado = "";
		
		Enumeration<String> keys = misClientes.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			resultado += " -> "+key+" \n";
		}
		
		return resultado;
	}
	
	public String verLosIdsDeLasConversacionesActivasPorCliente(String idCliente){

		String resultado = "El cliente con id "+idCliente+" no existe.";
		if(existeElCliente(idCliente)){
			resultado = "";
			Cliente miCliente = misClientes.get(idCliente);
			ArrayList<String> idsSeseiones = miCliente.getMisIdsDeSesiones();
			for (String idSesion: idsSeseiones){
				resultado += " -> "+idSesion+" \n";
			}
		}
		
		return resultado;
	}
	
	public String borrarTodasLasConversacionesDeUnCliente(String idCliente){
		String resultado = "El cliente con id "+idCliente+" no existe.";
		if(existeElCliente(idCliente)){
			synchronized(misConversaciones){
				Cliente miCliente = misClientes.get(idCliente);
				ArrayList<String> idsSeseiones = miCliente.getMisIdsDeSesiones();
				for (String idSesion: idsSeseiones){
					borrarUnaConversacion(idSesion);
				}
				miCliente.borrarTodosLosIdsDeSesiones();
				resultado = "Las conversaciones del cliente "+idCliente+" se borraron exitosamente.";
			}
		}
		return resultado;
	}
	
	public ArrayList<String> obtenerLosIdsDeSesionDeUnCliente(String idCliente){
		ArrayList<String> resultado = null;
		if(existeElCliente(idCliente)){
			Cliente miCliente = misClientes.get(idCliente);
			resultado = miCliente.getMisIdsDeSesiones();
		}
		
		return resultado;
	}

	public void inicializar(String pathXML, ConsultaDao consultaDao) {
		System.out.println("El path xml es: "+pathXML);
		temarioDelBancoAtlantida = new TemarioDelBancoAtlantida(pathXML);
		this.consultaDao = consultaDao;
	}
	
}
