package com.ncubo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
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

import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.path.xml.element.Node;
import com.ncubo.conf.AgenteCognitivo;
import com.ncubo.conf.ExtraerDatosWebService;
import com.ncubo.conf.ManejadorDeErrores;
import com.ncubo.conf.Usuario;
import com.ncubo.exceptions.CredencialesInvalidosException;
import com.ncubo.exceptions.NoEmailException;
import com.ncubo.util.FTPServidor;


@Controller
public class MovilController {
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	@Autowired
	private ManejadorDeErrores manejadorDeErrores;
	@Autowired
	private FTPServidor ftp;
	private ExtraerDatosWebService extraerDatos;
	
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
	@RequestMapping(value="/archivossubidos/{nombre:.*}", method = RequestMethod.GET)
	void archivossubidos(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String nombre) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException, ClassNotFoundException, SQLException 
	{
		String remoteFile2 = nombre.replace("-", "/");

		InputStream inputStream = ftp.descargarArchivo(remoteFile2);

		byte[] bytesArray = new byte[4096];
		int bytesRead = -1;

		ServletContext context = request.getServletContext();
		String mimetype = context.getMimeType(remoteFile2);

		if (mimetype == null)
		{
			mimetype = "application/octet-stream";
		}
		
		response.setContentType(mimetype);
		// response.setHeader("Content-Disposition", "attachment; filename=\"" +
		// remoteFile2.substring(1) + "\"");

		OutputStream outStream = response.getOutputStream();

		while ((bytesRead = inputStream.read(bytesArray)) != -1)
		{
			outStream.write(bytesArray, 0, bytesRead);
		}

		outStream.close();
		inputStream.close();
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
		extraerDatos = new ExtraerDatosWebService();
		String[] responseLogin = extraerDatos.login(name , password);
		if( responseLogin[0].equals("S") )
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
			
			usuario.setLlaveSession(responseLogin[1]);
			usuario.setUsuarioId(responseLogin[2]);
			usuario.setUsuarioNombre(responseLogin[3]);
			
			usuario.hizologinExitosaMente();
			sesion.setAttribute(Usuario.LLAVE_EN_SESSION, usuario);
			JSONObject respuesta = new JSONObject().put("usuarioEstaLogueado", usuario.estaLogueado());
			
			String[] cuentas = extraerDatos.tieneCuentas(responseLogin[2]);
			
			for(int i = 0; i < cuentas.length; i++){
				usuario.setVariablesDeContexto(cuentas[i], "true");
			}
			
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