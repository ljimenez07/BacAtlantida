package com.ncubo.logicaDeConversaciones;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.conf.Usuario;

public class Conversaciones {

	// key puede ser el id del usuario o el id de la seccion
	private final static Hashtable<String, Conversacion> misConversaciones = new Hashtable<String, Conversacion>();
	private final static Hashtable<String, Cliente> misClientes = new Hashtable<String, Cliente>();
	private final static Temario temarioDelBancoAtlantida = new TemarioDelBancoAtlantida();
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String crearUnaNuevoConversacion(Usuario usuario){
		String resultado = "El usuario no se pudo agregar";
		try{
			Cliente cliente = null;
			/*try{
				if(! usuario.getUsuarioId().equals("") && ! usuario.getIdSesion().equals("")){
					if( ! existeElCliente(usuario.getUsuarioId())){
						cliente = new Cliente(usuario.getUsuarioNombre(), usuario.getUsuarioId());
						misClientes.put(cliente.getMiId(), cliente);
						Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente);
						misConversaciones.put(cliente.getMiId(), coversacion);
						resultado = "La conversacion se creo exitosamente";
						System.out.println(resultado);
					}else{
						resultado = "El cliente ya existe";
						System.out.println(resultado);
					}
				}
			}catch(Exception e){
				System.out.println("Error al extraer el id del usuario y de la sesion");
			}*/
			
			if (cliente == null){
				if (! usuario.getIdSesion().equals("")){
					if( ! existeLaConversacion(usuario.getIdSesion())){
						cliente = new Cliente();
						Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente);
						misConversaciones.put(usuario.getIdSesion(), coversacion);
						resultado = "La conversacion se creo exitosamente";
						System.out.println(resultado);
					}else{
						resultado = "La conversacion ya existe";
						System.out.println(resultado);
					}
				}else{
					resultado = "La conversacion NO pudo ser creada";
					System.out.println(resultado);
				}
			}
			
		}catch(Exception e){
			System.out.println("Error al crear una conversacion: "+e.getMessage());
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
	
	public ArrayList<Salida> conversarConElAgente(Usuario cliente, String textoDelCliente){
		ArrayList<Salida> resultado = null;
		logger.debug("Conversar ..........");
		System.out.println("Coversar con "+cliente.getIdSesion());
		/*try{
			if( ! cliente.getUsuarioId().equals("") && ! cliente.getIdSesion().equals("") && cliente.estaLogueado()){ // Esta logueado
				// Verificar si ya el usuario existe
				if(existeElCliente(cliente.getUsuarioId())){
					// TODO Verificar si cambio el id de sesion, si es asi agregarla al cliente y hacerlo saber a conversacion
					resultado = misConversaciones.get(cliente.getUsuarioId()).analizarLaRespuestaConWatson(textoDelCliente);
					
				}else{ // Crear un nuevo Usuaio
					crearUnaNuevoConversacion(cliente);
					if(existeLaConversacion(cliente.getIdSesion())){ // Es porque ya se cliente esta conversando y no se habia logueado, eso quiere decir que se tiene que mantener el contexto y NO saludar de nuevo
						resultado = misConversaciones.get(cliente.getUsuarioId()).analizarLaRespuestaConWatson(textoDelCliente);
					}else{
						resultado = inicializarConversacionConElAgente(cliente.getUsuarioId());
					}
				}
			}
		}catch(Exception e){
			resultado = null;
		}*/
		
		if (resultado == null){
			if(! cliente.getIdSesion().equals("")){
				if(existeLaConversacion(cliente.getIdSesion())){
					resultado = misConversaciones.get(cliente.getIdSesion()).analizarLaRespuestaConWatson(textoDelCliente);
				}else{ // Crear una nueva conversacion
					crearUnaNuevoConversacion(cliente);
					resultado = inicializarConversacionConElAgente(cliente.getIdSesion());
				}
			}else{
				System.out.println("No existe el usuario o conversacion en el sistema");
			}
		}
		
		return resultado;
	}
	
}
