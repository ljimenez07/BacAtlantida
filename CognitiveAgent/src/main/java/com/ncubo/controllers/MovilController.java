package com.ncubo.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jdom.JDOMException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncubo.conf.AgenteCognitivo;
import com.ncubo.conf.ManejadorDeErrores;

@Controller
public class MovilController {
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	@Autowired
	private ManejadorDeErrores manejadorDeErrores;
	
	private static int contadorDeContextos = 0;//TODO quitarlo de aquí y meterlo en la session
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/", method = RequestMethod.POST)
	@ResponseBody String conversacionSinContexto(@RequestBody String mensaje, HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException, JDOMException 
	{
		contadorDeContextos = contadorDeContextos +1;
		return conversacion( mensaje, ""+contadorDeContextos );
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/{contexto}", method = RequestMethod.POST)
	@ResponseBody String conversacion(@RequestBody String mensaje, @PathVariable String contexto) throws JSONException, JsonParseException, JsonMappingException, IOException, JDOMException 
	{
		mensaje = URLDecoder.decode(mensaje, "UTF-8");
		mensaje = mensaje.substring(0, mensaje.lastIndexOf('='));
		return serverCognitivo.procesarMensaje(
				contexto, 
				mensaje, 
				new Date() );
	}
	
	@ExceptionHandler(Throwable.class)
	public @ResponseBody String handleAllException(final HttpServletRequest req, HttpServletResponse response, final Exception ex) throws MessagingException 
	{
		
		System.err.println("aaa "+ex.toString());
		response.setStatus(500);
		
		manejadorDeErrores.enviarCorreo(ex);
		
		return ex.toString();
	}
}