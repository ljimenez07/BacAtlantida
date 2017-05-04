package com.ncubo.controllers;

import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	
	//@CrossOrigin(origins = "*")
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
		
		session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		
		return object.toString();
	}
	
	//@CrossOrigin(origins = "*")
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
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/movil/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody String mensaje, HttpSession sesion, @RequestParam("name") String name, @RequestParam("password") String password) throws ClassNotFoundException, SQLException, JSONException 
	{
		String[] resultadosLogin = extraerDatos.login(name, password);
		if(resultadosLogin[0].equals("true"))
		{
			String[] responsePreLogin = extraerDatos.preLogin(name);
			Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
			
			if(responsePreLogin[0].equals("S")){
				usuario.setLlaveSession(responsePreLogin[1]);
				usuario.setUsuarioId(responsePreLogin[2]);
				usuario.setUsuarioNombre(responsePreLogin[3]);
				usuario.setHeaderTokenKey(resultadosLogin[1]);
				usuario.setHeaderToken(resultadosLogin[2]);
				usuario.setResponseLogin(resultadosLogin[3]);
				usuario.setCookie(resultadosLogin[4]);
				
				usuario.hizologinExitosaMente();
				
				Categorias categorias = usuarioDao.obtenerLasCategoriasDeUnUsuario(usuario);
				usuario.setCategorias( categorias );
				
				boolean[] cuentasvacio = new boolean[2];
				boolean[] cuentas = extraerDatos.tieneCuentas(responsePreLogin[2],responsePreLogin[1]);
				
				if(!cuentas.equals(cuentasvacio))
				{
					usuario.setTieneTarjetaCredito(cuentas[0]);
					usuario.setTieneCuentaAhorros(cuentas[1]);
				}
				
				sesion.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
				JSONObject respuesta = new JSONObject().put("usuarioEstaLogueado", usuario.getEstaLogueado())
						.put("usuarioNombre", usuario.getEstaLogueado() ? usuario.getUsuarioNombre() : "")
						.put("idUsuario", usuario.getEstaLogueado() ? usuario.getUsuarioId() : "");
				
				
				return new ResponseEntity<>(respuesta.toString(), HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(responsePreLogin[0], HttpStatus.UNAUTHORIZED);
		}
		else
			return new ResponseEntity<>(resultadosLogin[0], HttpStatus.UNAUTHORIZED);
	}

	//@CrossOrigin(origins = "*")
	@GetMapping("/movil/logout")
	@ResponseBody String logout(HttpSession sesion) throws JSONException
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		
		if(usuario == null)
		{
			usuario = new Usuario(sesion.getId());
		}
		if(extraerDatos.logout(usuario.getHeaderTokenKey(), usuario.getHeaderToken(), usuario.getResponseLogin(), usuario.getCookie()))
		{
			borrarTodasLasConversacionesDeUnCliente(sesion);
			sesion.setAttribute(Usuario.LLAVE_EN_SESSION, null);
			return new JSONObject().put("usuarioEstaLogueado", false).toString();
		}
		return new JSONObject().put("usuarioEstaLogueado", true).toString();
	}

	//@CrossOrigin(origins = "*")
	@GetMapping("/movil/usuario")
	@ResponseBody String obtenerUsuario(HttpSession sesion) throws JSONException, ClassNotFoundException, SQLException
	{
		Object objeto = sesion.getAttribute(Usuario.LLAVE_EN_SESSION) ;
		JSONObject respuesta = new JSONObject();
		if(objeto != null)
		{
			Usuario usuario = ( Usuario ) objeto;
			
			respuesta.put("usuarioNombre", usuario.getUsuarioNombre());
			respuesta.put("idUsuario", usuario.getUsuarioId());
			respuesta.put("estaLogueado", usuario.getEstaLogueado());
			respuesta.put("mostrarPopConocerte", usuario.getEstaLogueado() && ! usuarioDao.yaContestoElConocerteAlmenosUnaVez(usuario) );
		}
		else
		{
			respuesta.put("usuarioNombre", "");
			respuesta.put("idUsuario", "");
			respuesta.put("estaLogueado", false);
			respuesta.put("mostrarPopConocerte", false);
		}
		
		return respuesta.toString();
	}

	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/generarAudiosEstaticos", method = RequestMethod.GET)
	@ResponseBody String generarAudiosEstaticos(){
		serverCognitivo.generarTodosLosAudiosEstaticos();
		return "Ok";
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/generarAudioEstatico/{id}", method = RequestMethod.GET)
	@ResponseBody String generarAudioEstatico(@PathVariable("id") String id){
		serverCognitivo.generarAudioEstatico(id);
		return "Ok";
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/generarAudioEstaticoParaUnaFrase", method = RequestMethod.GET)
	@ResponseBody String generarAudioEstaticoParaUnaFrase(
			@RequestParam(value="indexTema") int indexTema,
			@RequestParam(value="indexFrase") int indexFrase,
			@RequestParam(value="idtema") String nombreTema,
			@RequestParam(value="archivo") String nombreDelArchivo){
		// http://localhost:8080/conversacion/generarAudioEstaticoParaUnaFrase?idtema=saludo&archivo=xxx.wav&indexTema=0&indexFrase=0
		serverCognitivo.cargarElNombreDeUnSonidoEstaticoEnMemoria(indexTema, indexFrase, nombreTema, nombreDelArchivo);
		return "Ok";
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verTodasLasCoversacionesActivas", method = RequestMethod.GET)
	@ResponseBody String verTodasLasCoversacionesActivas(){
		return serverCognitivo.verTodasLasCoversacionesActivas();
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verTodosLosClientesActivos", method = RequestMethod.GET)
	@ResponseBody String verTodosLosClientesActivos(){
		return serverCognitivo.verTodosLosClientesActivos();
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verLosIdsDeLasConversacionesActivasPorCliente/{id}", method = RequestMethod.GET)
	@ResponseBody String verLosIdsDeLasConversacionesActivasPorCliente(@PathVariable("id") String id){
		return serverCognitivo.verLosIdsDeLasConversacionesActivasPorCliente(id);
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/borrarTodasLasConversacionesDeUnCliente", method = RequestMethod.GET)
	@ResponseBody String borrarTodasLasConversacionesDeUnCliente(HttpSession sesion){
		return borrarUnaConversacion(sesion);
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/borrarUnaConversacion", method = RequestMethod.GET)
	@ResponseBody String borrarUnaConversacion(HttpSession sesion){
		
		Object objeto = sesion.getAttribute(Usuario.LLAVE_EN_SESSION) ;
		if(objeto != null)
		{
			Usuario usuario = ( Usuario ) objeto;
			if (usuario.getUsuarioId().isEmpty()){
				return serverCognitivo.borrarUnaConversacion(usuario.getIdSesion());
			}else{
				return serverCognitivo.borrarTodasLasConversacionesDeUnCliente(usuario.getUsuarioId());
			}
		}
		
		return "";
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verMiTemario", method = RequestMethod.GET)
	@ResponseBody String verMiTemario(){
		return serverCognitivo.verMiTemario();
	}
	
	/*//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/verElHistoricoDeLaConversacion", method = RequestMethod.GET)
	@ResponseBody String verElHistoricoDeLaConversacion(@RequestParam(value="id") String id, @RequestParam(value="fecha") String feha){
		// http://localhost:8080/conversacion/verElHistoricoDeLaConversacion?id=3485fe88-b63c-4502-8ce1-d2519fcf60e3&fecha=2016-12-14%2017:32:49
		return serverCognitivo.verElHistoricoDeLaConversacion(id, feha); // "2016-12-12 15:31:23"
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/buscarConversacionesQueNoHanSidoVerificadasPorTema", method = RequestMethod.GET)
	@ResponseBody String buscarConversacionesQueNoHanSidoVerificadasPorTema(@RequestParam(value="idTema") String idTema) throws ClassNotFoundException, SQLException{
		// http://localhost:8080/conversacion/buscarConversacionesQueNoHanSidoVerificadasPorTema?idTema=saludo
		return serverCognitivo.buscarConversacionesQueNoHanSidoVerificadasPorTema(idTema);
	}
	
	//@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/cambiarDeEstadoLaConversacion", method = RequestMethod.GET)
	@ResponseBody String cambiarDeEstadoAVerificadoDeLaConversacion(@RequestParam(value="idSesion") String idSesion, @RequestParam(value="fecha") String fecha) throws ClassNotFoundException, SQLException{
		// http://localhost:8080/conversacion/cambiarDeEstadoLaConversacion?idSesion=5cf8066d-f023-44e6-9000-99138a2fab6e&fecha=2016-12-19%2014:36:59
		return serverCognitivo.cambiarDeEstadoAVerificadoDeLaConversacion("", idSesion, fecha);
	}

	@RequestMapping(value="/conversacion/cambiarDeEstadoLaConversacionConUsuario", method = RequestMethod.GET)
	@ResponseBody String cambiarDeEstadoAVerificadoDeLaConversacionConUsuario(@RequestParam(value="idCliente") String idCliente, @RequestParam(value="idSesion") String idSesion, @RequestParam(value="fecha") String fecha) throws ClassNotFoundException, SQLException{
		// http://localhost:8080/conversacion/verElHistoricoDeLaConversacion?idCliente=123456&idSesion=3485fe88-b63c-4502-8ce1-d2519fcf60e3&fecha=2016-12-14%2017:32:49
		return serverCognitivo.cambiarDeEstadoAVerificadoDeLaConversacion(idCliente, idSesion, fecha);
	}*/
}