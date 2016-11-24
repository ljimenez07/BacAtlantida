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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.path.xml.element.Node;
import com.ncubo.conf.AgenteCognitivo;
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
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/chat/", method = RequestMethod.POST)
	@ResponseBody String chat(@RequestBody String mensaje, HttpSession session) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException, ClassNotFoundException, SQLException, ParseException 
	{
		Usuario usuario = (Usuario)session.getAttribute(Usuario.LLAVE_EN_SESSION) ;
		if( usuario  == null)
		{
			usuario = new Usuario(session.getId());
			session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		}
		
		System.out.println("valor "+session.getAttribute(Usuario.LLAVE_EN_SESSION));
		JSONObject object = new JSONObject(serverCognitivo.procesarMensajeChat(
				usuario, 
				mensaje, 
				new Date()));
		
		object.put("usuarioEstaLogueado", usuario.estaLogueado());
		
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
		object.put("usuarioEstaLogueado", usuario.estaLogueado());
		
		session.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
		
		return object.toString();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/movil/login", method = RequestMethod.POST)
	@ResponseBody String login(@RequestBody String mensaje, HttpSession sesion, @RequestParam String name, @RequestParam String password) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";
		String responseXML = new Stub().login(requestBody);//given().body(requestBody).post("http://localhost:8080/Ecommerce/login/").andReturn().asString();
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		Node validaPreLoginColeccion = body.get(0).getNode("Respuesta").getNode("validaPreLoginColeccion");
		if( validaPreLoginColeccion.getNode("valido").toString().equals("S") )
		{
			Object objeto = sesion.getAttribute(Usuario.LLAVE_EN_SESSION) ;
			Usuario usuario;
			if( objeto == null)
			{
				usuario = new Usuario( sesion.getId() );
			}
			else
			{
				usuario = (Usuario)objeto;
			}
			
			usuario.setLlaveSession(validaPreLoginColeccion.getNode("llaveSession").toString());
			usuario.setUsuarioId(validaPreLoginColeccion.getNode("usuarioId").toString());
			usuario.setUsuarioNombre(validaPreLoginColeccion.getNode("usuarioNombre").toString());
			
			usuario.hizologinExitosaMente();
			sesion.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
			JSONObject respuesta = new JSONObject().put("usuarioEstaLogueado", usuario.estaLogueado());
			
			return respuesta.toString();
		}
		
		throw new CredencialesInvalidosException();
	}
	
	@GetMapping("/movil/logout")
	@ResponseBody String logout(HttpSession sesion) throws JSONException
	{
		sesion.setAttribute(Usuario.LLAVE_EN_SESSION, null);
		return new JSONObject().put("usuarioEstaLogueado", false).toString();
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