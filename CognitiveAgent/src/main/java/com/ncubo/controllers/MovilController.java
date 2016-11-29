package com.ncubo.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncubo.conf.AgenteCognitivo;
import com.ncubo.conf.ExtraerDatosWebService;
import com.ncubo.conf.ManejadorDeErrores;
import com.ncubo.conf.Usuario;
import com.ncubo.exceptions.CredencialesInvalidosException;
import com.ncubo.exceptions.NoEmailException;

@Controller
public class MovilController {
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	@Autowired
	private ManejadorDeErrores manejadorDeErrores;
	
	@Autowired
	private ExtraerDatosWebService extraerDatos;
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/chat/", method = RequestMethod.POST)
	@ResponseBody String chat(@RequestBody String mensaje, HttpSession session) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException, ClassNotFoundException, SQLException, ParseException 
	{
		Usuario usuario = obtenerUsuario(session);
		
		System.out.println("valor "+session.getAttribute(Usuario.LLAVE_EN_SESSION));
		JSONObject object = new JSONObject(serverCognitivo.procesarMensajeChat(
				usuario, 
				mensaje, 
				new Date()));
		
		object.put("usuarioEstaLogueado", usuario.getEstaLogueado());
		
		session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		
		return object.toString();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/conocerte/", method = RequestMethod.POST)
	@ResponseBody String conocerte(@RequestBody String mensaje, HttpSession session, HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException, ClassNotFoundException, SQLException, ParseException 
	{
		Usuario usuario = (Usuario)session.getAttribute(Usuario.LLAVE_EN_SESSION) ;
		if( usuario  == null)
		{
			usuario = new Usuario(session.getId());
			session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		}
		
		/*JSONObject object = new JSONObject(serverCognitivo.procesarMensajeConocerte(
				usuario, 
				mensaje, 
				new Date()));*/
		JSONObject object = new JSONObject();
		object.put("usuarioEstaLogueado", usuario.getEstaLogueado());
		
		session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		
		return object.toString();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/movil/login", method = RequestMethod.POST)
	@ResponseBody String login(@RequestBody String mensaje, HttpSession sesion, @RequestParam String name, @RequestParam String password) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		String[] responseLogin = extraerDatos.login(name , password);
		if( responseLogin[0].equals("S") )
		{
			Usuario usuario = obtenerUsuario(sesion);
			
			usuario.setLlaveSession(responseLogin[1]);
			usuario.setUsuarioId(responseLogin[2]);
			usuario.setUsuarioNombre(responseLogin[3]);
			
			usuario.hizologinExitosaMente();
			sesion.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
			JSONObject respuesta = new JSONObject().put("usuarioEstaLogueado", usuario.getEstaLogueado());
			
			String[] cuentas = extraerDatos.tieneCuentas(responseLogin[2]);
			
			for(int i = 0; i < cuentas.length; i++){
				usuario.setVariablesDeContexto(cuentas[i], "true");
			}
			
			return respuesta.toString();
		}
		
		throw new CredencialesInvalidosException();
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/movil/logout")
	@ResponseBody String logout(HttpSession sesion) throws JSONException
	{
		sesion.setAttribute(Usuario.LLAVE_EN_SESSION, null);
		return new JSONObject().put("usuarioEstaLogueado", false).toString();
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/movil/usuario")
	@ResponseBody Usuario obtenerUsuario(HttpSession sesion) throws JSONException
	{
		Object objeto = sesion.getAttribute(Usuario.LLAVE_EN_SESSION) ;
		if(objeto == null)
		{
			return new Usuario( sesion.getId() );
		}
		else
		{
			return (Usuario)objeto;
		}
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/generarAudiosEstaticos", method = RequestMethod.GET)
	@ResponseBody String generarAudiosEstaticos(){
		serverCognitivo.generarTodosLosAudiosEstaticos();
		return "Ok";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/generarAudioEstatico/{id}", method = RequestMethod.GET)
	@ResponseBody String generarAudioEstatico(@PathVariable("id") String id){
		serverCognitivo.generarAudioEstatico(id);
		return "Ok";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verTodasLasCoversacionesActivas", method = RequestMethod.GET)
	@ResponseBody String verTodasLasCoversacionesActivas(){
		return serverCognitivo.verTodasLasCoversacionesActivas();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verTodosLosClientesActivos", method = RequestMethod.GET)
	@ResponseBody String verTodosLosClientesActivos(){
		return serverCognitivo.verTodosLosClientesActivos();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verLosIdsDeLasConversacionesActivasPorCliente/{id}", method = RequestMethod.GET)
	@ResponseBody String verLosIdsDeLasConversacionesActivasPorCliente(@PathVariable("id") String id){
		return serverCognitivo.verLosIdsDeLasConversacionesActivasPorCliente(id);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/borrarTodasLasConversacionesDeUnCliente/{id}", method = RequestMethod.GET)
	@ResponseBody String borrarTodasLasConversacionesDeUnCliente(@PathVariable("id") String id){
		return serverCognitivo.borrarTodasLasConversacionesDeUnCliente(id);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/borrarUnaConversacion/{id}", method = RequestMethod.GET)
	@ResponseBody String borrarUnaConversacion(@PathVariable("id") String id){
		return serverCognitivo.borrarUnaConversacion(id);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verMiTemario", method = RequestMethod.GET)
	@ResponseBody String verMiTemario(){
		return serverCognitivo.verMiTemario();
	}
	
	@ExceptionHandler(Throwable.class)
	public @ResponseBody String handleAllException(final HttpServletRequest req, HttpServletResponse response, final Exception ex) throws MessagingException 
	{
		
		System.err.println("Error: "+ex.toString());
		
		if( ! (ex instanceof NoEmailException) )
		{
			response.setStatus(500);
		}
		
		manejadorDeErrores.enviarCorreo(ex);
		
		return ex.toString();
	}
}