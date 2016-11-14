package com.ncubo.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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
	@RequestMapping(value="/conversacion/chat/", method = RequestMethod.POST)
	@ResponseBody String chatSinContexto(@RequestBody String mensaje, HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException,  URISyntaxException 
	{
		contadorDeContextos = contadorDeContextos +1;
		return chat( mensaje, ""+contadorDeContextos, request);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/conocerte/", method = RequestMethod.POST)
	@ResponseBody String conocerteSinContexto(@RequestBody String mensaje, HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException 
	{
		contadorDeContextos = contadorDeContextos +1;
		return conocerte( mensaje, ""+contadorDeContextos, request);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/chat/{contexto}", method = RequestMethod.POST)
	@ResponseBody String chat(@RequestBody String mensaje, @PathVariable String contexto,  HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException,  URISyntaxException 
	{
		
		return serverCognitivo.procesarMensajeChat(
				contexto, 
				mensaje, 
				new Date(),
				request
				);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/conocerte/{contexto}", method = RequestMethod.POST)
	@ResponseBody String conocerte(@RequestBody String mensaje, @PathVariable String contexto, HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException 
	{
		
		return serverCognitivo.procesarMensajeConocerte(
				contexto, 
				mensaje, 
				new Date(),
				request
				);
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