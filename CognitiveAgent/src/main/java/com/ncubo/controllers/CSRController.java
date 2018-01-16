package com.ncubo.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ncubo.bambu.bl.BandejaDeMensajeBL;
import com.ncubo.bambu.bl.PersonaContactoBL;
import com.ncubo.bambu.dao.BandejaDeMensajesDao;
import com.ncubo.bambu.dao.CotizacionDao;
import com.ncubo.bambu.dao.OrigenDao;
import com.ncubo.bambu.dao.PersonaContactoDao;
import com.ncubo.bambu.dao.TokenDao;
import com.ncubo.bambu.dao.UsuarioBambuDao;
import com.ncubo.bambu.data.BandejaDeMensaje;
import com.ncubo.bambu.data.Cotizacion;
import com.ncubo.bambu.data.EEtapaCotizacion;
import com.ncubo.bambu.data.EFuncionUsuario;
import com.ncubo.bambu.data.NuevoContacto;
import com.ncubo.bambu.data.Origen;
import com.ncubo.bambu.data.PersonaContacto;
import com.ncubo.bambu.data.UsuarioBambu;
import com.ncubo.bambu.util.Mailer;
import com.ncubo.cas.Cas;
import com.ncubo.cas.Jwt;

@Controller
public class CSRController
{
	@Autowired
	private BandejaDeMensajeBL bandejaBL;

	@Autowired
	private OrigenDao _origenes;

	@Autowired
	private UsuarioBambuDao _usuarios;
	
	@Autowired
	private PersonaContactoBL _contactos;
	
	@Autowired
	private CotizacionDao _cotizaciones;
	
	@Autowired
	private Mailer _mailer;
	
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(CSRController.class);
	}
	
	// Quemado porque aún no se manejan users en el session, eso es stage 2
	private int idUsuarioEnSesion = 4789;
	
	@RequestMapping("/BackOffice/bandejaDeMensajes")
	public String verBandejaDeMensajes(Model model)
	{
		return "bandejaDeMensajes";
	}
	
	@RequestMapping("/BackOffice/iframeBandejaDeMensajes")
	public String verIframeBandejaDeMensajes(Model model)
	{
		ArrayList<BandejaDeMensaje> mensajes = new ArrayList<BandejaDeMensaje>();
		mensajes = bandejaBL.obtenerMensajes();
		model.addAttribute("mensajes", mensajes);
		return "iframeBandejaDeMensajes";
	}
	
	@RequestMapping("/BackOffice/nuevoMensaje")
	public String crearNuevoMensaje(Model model)
	{
		return "nuevoMensaje";
	}
	
	@RequestMapping("/BackOffice/iframeNuevoMensaje")
	public String verIframeNuevoMensaje(Model model)
	{
		ArrayList<Origen> origenes = new ArrayList<Origen>();
		origenes = _origenes.obtenerOrigenes();
		model.addAttribute("origenes", origenes);
		return "iframeNuevoMensaje";
	}

	@RequestMapping("/BackOffice/detalleDeMensaje/{idBandejaMensaje}")
	public String verDetalleDeMensaje(Model model, @PathVariable("idBandejaMensaje") int idBandejaMensaje)
	{
		return "detalleDeMensaje";
	}
	
	@RequestMapping("/BackOffice/iframeDetalleDeMensaje/{idBandejaMensaje}")
	public String verIframeDetalleMensaje(Model model, @PathVariable("idBandejaMensaje") int idBandejaMensaje)
	{
		BandejaDeMensaje mensaje = bandejaBL.obtenerBandeja(idBandejaMensaje);
		model.addAttribute("mensaje", mensaje);
		
		int idCreador = mensaje.obtenerIdUsuarioCreador();
		if (mensaje.obtenerContacto() != null)
		{
			model.addAttribute("contacto", mensaje.obtenerContacto());
			model.addAttribute("contactoExiste", true);
		}
		else
		{
			UsuarioBambu usuarioCreador = _usuarios.obtenerUsuario(idCreador);
			PersonaContacto contacto = new PersonaContacto();
			contacto.establecerNombre(usuarioCreador.obtenerNombre());
			contacto.establecerApellidos(usuarioCreador.obtenerApellidos());
			contacto.establecerTelefono(usuarioCreador.obtenerTelefono());
			
			model.addAttribute("contacto", contacto);
			model.addAttribute("contactoExiste", false);
		}
		
		return "iframeDetalleDeMensaje";
	}
	
	@ResponseBody
	@PostMapping(value = "/BackOffice/bandejaMensaje", produces = "application/json")
	public ResponseEntity<?> detallesDelMensaje(Model model, @RequestParam("idBandejaMensaje") int idBandejaMensaje)
	{
		BandejaDeMensaje mensaje = bandejaBL.obtenerBandeja(idBandejaMensaje);
		Gson gson = new Gson();
		return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(mensaje));
	}
	
	@ResponseBody
	@PostMapping(value = "/BackOffice/usuariosAsesores", produces = "application/json")
	public ResponseEntity<?> usuariosAsesores(Model model)
	{
		JsonArray listaAsesores = new JsonArray();
		ArrayList<UsuarioBambu> asesores = _usuarios.listaDeUsuarioSegunFuncion(EFuncionUsuario.ASESORES.obtenerId());

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		for(UsuarioBambu asesor : asesores)
		{
			JsonObject jsonObject = new JsonObject();
			jsonObject = (JsonObject) parser.parse(gson.toJson(asesor));
			listaAsesores.add(jsonObject);
		}
		JsonObject respuesta = new JsonObject();
		respuesta.add("d", listaAsesores);
		return ResponseEntity.status(HttpStatus.OK).body(respuesta.toString());
	}
	
	@ResponseBody
	@PostMapping(value = "/BackOffice/actualizarUsuarioEncargado", produces = "application/json")
	public ResponseEntity<?> actualizarUsuarioEncargado(Model model,
			HttpServletRequest request,
			@RequestParam("idBandejaMensaje") int idBandejaMensaje,
			@RequestParam("idUsuarioEncargado") int idUsuarioEncargado)
	{
		boolean actualizado = bandejaBL.actualizarEncargado(idBandejaMensaje, idUsuarioEnSesion, idUsuarioEncargado);
		
		if (actualizado)
		{
			return new ResponseEntity(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ResponseBody
	@PostMapping(value = "/BackOffice/finalizarMensaje", produces = "application/json")
	public ResponseEntity<?> finalizarMensaje(Model model,
			@RequestParam("idBandejaMensaje") int idBandejaMensaje,
			@RequestParam("justificacion") String justificacion,
			@RequestParam("esRequeridoEnviarCorreo") boolean esRequeridoEnviarCorreo)
	{
		boolean actualizado = bandejaBL.finalizarMensaje(idBandejaMensaje, idUsuarioEnSesion, justificacion, esRequeridoEnviarCorreo);
		
		if(actualizado)
		{
			return new ResponseEntity(HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ResponseBody
	@PostMapping(value = "/BackOffice/enviarCorreoDeConversacion", produces = "application/json")
	public ResponseEntity<?> enviarCorreoDeConversacion(Model model, 
			@RequestParam("idBandejaMensaje") int idBandejaMensaje)
	{
		ResponseEntity respuesta = new ResponseEntity(HttpStatus.BAD_REQUEST);
		int idUsuarioLogueado = this.idUsuarioEnSesion;
		BandejaDeMensaje bandeja = bandejaBL.obtenerBandeja(idBandejaMensaje);
		String correo = bandeja.obtenerEmailCreador();
		correo = "rmoreno@testingsoft.com";
		
		if (correo != null && idUsuarioLogueado > 0)
		{
			try
			{
				ClassLoader cargador = CSRController.class.getClassLoader();
				File archivoPlantilla = new File(cargador.getResource("CorreoDetalleDeConversacion.html").getFile());
				String textoPlantilla = new String(Files.readAllBytes(archivoPlantilla.toPath()));
				
				boolean exitoDeEnvio = _mailer.enviarCorreo(correo, textoPlantilla, "Conversación");
				if (exitoDeEnvio)
				{
					respuesta = new ResponseEntity(HttpStatus.OK);
				}
				else
				{
					respuesta = new ResponseEntity(HttpStatus.BAD_REQUEST);
				}
			} 
			catch (IOException e)
			{
				LOG.error("Error", e);
				respuesta = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return respuesta;
	}
	
	/*
	 * usuarioCandidato
	 * 
	 */
	@ResponseBody
	@PostMapping(value = "/BackOffice/crearContacto", produces = "application/json")
	public ResponseEntity<?> crearContacto(@RequestBody NuevoContacto usuarioCandidato)
	{
		ResponseEntity respuesta = new ResponseEntity(HttpStatus.BAD_REQUEST);
		int idContacto = _contactos.crearContacto(usuarioCandidato, idUsuarioEnSesion);
		if(idContacto > 0)
		{
			JsonObject body = new JsonObject();
			body.addProperty("idContacto", idContacto);
			respuesta = ResponseEntity.status(HttpStatus.OK).body(body.toString());
		}
		return respuesta;
	}

	@RequestMapping("/BackOffice/iframeChatHistorico")
	public String verIframeChatHistorico(Model model)
	{
		return "iframeChatHistorico";
	}
}
