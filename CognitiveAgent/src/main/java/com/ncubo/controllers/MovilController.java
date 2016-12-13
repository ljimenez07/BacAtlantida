package com.ncubo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncubo.conf.AgenteCognitivo;
import com.ncubo.conf.ExtraerDatosWebService;
import com.ncubo.conf.Usuario;
import com.ncubo.dao.UsuarioDao;
import com.ncubo.data.Categorias;
import com.ncubo.exceptions.CredencialesInvalidosException;

@Controller
public class MovilController {
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	
	@Autowired
	private ExtraerDatosWebService extraerDatos;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/chat/", method = RequestMethod.POST)
	@ResponseBody String chat(@RequestBody String mensaje, HttpSession session) throws Exception 
	{
		Usuario usuario = (Usuario)session.getAttribute(Usuario.LLAVE_EN_SESSION);
		if( usuario == null)
		{
			usuario = new Usuario(session.getId());
		}
		
		System.out.println("valor "+session.getAttribute(Usuario.LLAVE_EN_SESSION));
		JSONObject object = new JSONObject(serverCognitivo.procesarMensajeChat(
				usuario, 
				mensaje, 
				new Date()));
		
		object.put("usuarioEstaLogueado", usuario.getEstaLogueado());
		object.put("idSesion", usuario.getIdSesion());
		
		session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		
		return object.toString();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/conocerte/", method = RequestMethod.POST)
	@ResponseBody String conocerte(@RequestBody String mensaje, HttpSession session) throws Exception 
	{
		Usuario usuario = (Usuario)session.getAttribute(Usuario.LLAVE_EN_SESSION);
		if( usuario == null)
		{
			usuario = new Usuario(session.getId());
		}
		
		
		JSONObject object = new JSONObject(serverCognitivo.procesarMensajeConocerte(
				usuario, 
				mensaje, 
				new Date()));
		//JSONObject object = new JSONObject();
		object.put("usuarioEstaLogueado", usuario.getEstaLogueado());
		session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		
		return object.toString();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/movil/login", method = RequestMethod.POST)
	@ResponseBody String login(@RequestBody String mensaje, HttpSession sesion, @RequestParam String name, @RequestParam String password) throws JSONException, JsonParseException, JsonMappingException, IOException, ClassNotFoundException, SQLException 
	{
		String[] responseLogin = extraerDatos.login(name , password);
		if(responseLogin[0].equals("S"))
		{
			Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
			Categorias categorias = usuarioDao.obtenerLasCategoriasDeUnUsuario(usuario);
			
			usuario.setLlaveSession(responseLogin[1]);
			usuario.setUsuarioId(responseLogin[2]);
			usuario.setUsuarioNombre(responseLogin[3]);
			usuario.hizologinExitosaMente();
			usuario.setCategorias( categorias );
			
			sesion.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
			JSONObject respuesta = new JSONObject().put("usuarioEstaLogueado", usuario.getEstaLogueado())
					.put("usuarioNombre", usuario.getEstaLogueado() ? usuario.getUsuarioNombre() : "");
			
			Boolean[] cuentas = extraerDatos.tieneCuentas(responseLogin[2]);
			
			
			
			return respuesta.toString();
		}
		
		throw new CredencialesInvalidosException();
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/movil/logout")
	@ResponseBody String logout(HttpSession sesion) throws JSONException
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		borrarTodasLasConversacionesDeUnCliente(usuario.getUsuarioId());
		sesion.setAttribute(Usuario.LLAVE_EN_SESSION, null);
		return new JSONObject().put("usuarioEstaLogueado", false).toString();
	}

	@CrossOrigin(origins = "*")
	@GetMapping("/movil/usuario")
	@ResponseBody String obtenerUsuario(HttpSession sesion) throws JSONException, ClassNotFoundException, SQLException
	{
		Object objeto = sesion.getAttribute(Usuario.LLAVE_EN_SESSION) ;
		JSONObject respuesta = new JSONObject();
		if(objeto != null)
		{
			Usuario usuario = ( Usuario ) objeto;
			
			respuesta.put("usuarioNombre", usuario.getUsuarioNombre());
			respuesta.put("estaLogueado", usuario.getEstaLogueado());
			respuesta.put("mostrarPopConocerte", usuario.getEstaLogueado() && usuarioDao.yaContestoElConocerteAlmenosUnaVez(usuario) );
		}
		else
		{
			respuesta.put("usuarioNombre", "");
			respuesta.put("estaLogueado", false);
			respuesta.put("mostrarPopConocerte", false);
		}
		
	return respuesta.toString();
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
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verElHistoricoDeLaConversacion/{id}", method = RequestMethod.GET)
	@ResponseBody String verElHistoricoDeLaConversacion(@PathVariable("id") String id){
		return serverCognitivo.verElHistoricoDeLaConversacion(id);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verElHistoricoDeUnaConversacionEspecifica/{id}", method = RequestMethod.GET)
	@ResponseBody String verElHistoricoDeUnaConversacionEspecifica(@PathVariable("id") String id){
		return serverCognitivo.verElHistoricoDeUnaConversacionEspecifica(id);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/obtenerValorDeGustosDeHoteles/{id}", method = RequestMethod.GET)
	@ResponseBody String obtenerValorDeGustosDeHoteles(@PathVariable("id") String id) throws Exception{
		return ""+serverCognitivo.obtenerValorDeGustosDeHoteles(id);
	}

}