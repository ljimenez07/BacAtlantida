package com.ncubo.logicaDeConversaciones;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.conf.Usuario;

public class Coversaciones {

	// key puede ser el id del usuario o el id de la seccion
	private final static Hashtable<String, Conversacion> misConversaciones = new Hashtable<String, Conversacion>();
	private final static Hashtable<String, Cliente> misClientes = new Hashtable<String, Cliente>();
	private final static Temario temarioDelBancoAtlantida = new TemarioDelBancoAtlantida();
	
	private String crearUnaNuevoConversacion(Usuario usuario){
		String resultado = "El usuario no se pudo agregar";
		try{
			Cliente cliente = null;
			if(! usuario.getUsuarioId().equals("") && ! usuario.getLlaveSession().equals("")){
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
			}else if (! usuario.getLlaveSession().equals("")){
				if( ! existeLaConversacion(usuario.getLlaveSession())){
					cliente = new Cliente();
					Conversacion coversacion = new Conversacion(temarioDelBancoAtlantida, cliente);
					misConversaciones.put(usuario.getLlaveSession(), coversacion);
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
	
	public ArrayList<Salida> converzarConElAgente(Usuario cliente, String textoDelCliente){
		ArrayList<Salida> resultado = null;
		
		if( ! cliente.getUsuarioId().equals("") && ! cliente.getLlaveSession().equals("") && cliente.estaLogueado()){ // Esta logueado
			// Verificar si ya el usuario existe
			if(existeElCliente(cliente.getUsuarioId())){
				// TODO Verificar si cambio el id de sesion, si es asi agregarla al cliente y hacerlo saber a conversacion
				
				resultado = misConversaciones.get(cliente.getUsuarioId()).analizarLaRespuestaConWatson(textoDelCliente);
			}else{ // Crear un nuevo Usuaio
				crearUnaNuevoConversacion(cliente);
				resultado = inicializarConversacionConElAgente(cliente.getUsuarioId());
			}
		}else if(! cliente.getLlaveSession().equals("")){
			if(existeLaConversacion(cliente.getLlaveSession())){
				resultado = misConversaciones.get(cliente.getLlaveSession()).analizarLaRespuestaConWatson(textoDelCliente);
			}else{ // Crear una nueva conversacion
				crearUnaNuevoConversacion(cliente);
				resultado = inicializarConversacionConElAgente(cliente.getLlaveSession());
			}
		}else{
			System.out.println("No existe el usuario o conversacion en el sistema");
		}
		return resultado;
	}
	
}
