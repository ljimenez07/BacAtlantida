package com.ncubo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ncubo.conf.Usuario;
import com.ncubo.dao.CategoriaDao;
import com.ncubo.dao.OfertaDao;
import com.ncubo.dao.UsuarioDao;
import com.ncubo.data.Indice;
import com.ncubo.data.Oferta;
import com.ncubo.logica.OfertaService;
import com.ncubo.util.GestorDeArchivos;

@Controller
public class OfertaController
{
	@Autowired
	private CategoriaDao categoriaDao;
	
	@Autowired
	private OfertaService ofertaService;
	@Autowired
	private OfertaDao ofertaDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	private final String ACTION_INSERTAR_OFERTA = "BackOffice/insertarOferta";
	private final String ACTION_MODIFICAR_OFERTA = "BackOffice/modificarOferta";
	
	private final String BOTON_MODIFICAR_OFERTA = "Modificar";
	private final String BOTON_INSERTAR_OFERTA = "Insertar";
	
	private final String IMAGEN_MODIFICAR_OFERTA = "imagen-check";
	private final String IMAGEN_INSERTAR_OFERTA = "imagen-siguiente";
	
	private final String BOTON_SECUNDARIO_MODIFICAR_OFERTA = "imagen-cancelar";
	private final String BOTON_SECUNDARIO_INSERTAR_OFERTA = "imagen-cancelar";
	
	@Autowired
	GestorDeArchivos gestorDeArchivos;
	
	
	//@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas", produces = "application/json")
	@ResponseBody public String ofertas(@RequestParam("pagina") int pagina, HttpSession sesion) throws Exception
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		if( usuario == null )
		{
			usuario = new Usuario(sesion.getId());
		}
		Indice indiceInicial = new Indice( pagina );
				
		List<Oferta>ofertas = ofertaService.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
		
		ObjectMapper mapper = new ObjectMapper();
		JSONArray array = new JSONArray(mapper.writeValueAsString(( ofertas )));

		boolean puedeVerElpopupDeNuevasOfertas = usuario.getEstaLogueado() && usuarioDao.puedeVerElpopupDeNuevasOfertas( usuario );
		
		JSONObject respuesta = new JSONObject();
		respuesta.put("indice", new JSONObject(mapper.writeValueAsString(indiceInicial)));
		respuesta.put("resultados", array);
		respuesta.put("mostrarPopupdeOfertasNuevas", puedeVerElpopupDeNuevasOfertas);
		
		if(  puedeVerElpopupDeNuevasOfertas )
		{
			usuarioDao.marcarComoVistoElPopupDeNuevasOfertas( usuario.getUsuarioId() );
		}
		
		return respuesta.toString();
	}
	
	//@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas/{idOferta}", produces = "application/json")
	@ResponseBody public Oferta oferta(@PathVariable int idOferta, HttpSession sesion) throws ClassNotFoundException, SQLException
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		String idUsuario = usuario == null ? null : usuario.getEstaLogueado() ? usuario.getUsuarioId() : null;
		return ofertaDao.obtener(idOferta, idUsuario);
	}
	
	//@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas/cantidad", produces = "application/json")
	@ResponseBody public String cantidadDeOfertas(HttpSession sesion) throws ClassNotFoundException, SQLException, JSONException
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		JSONObject respuesta = new JSONObject().put("cantidad", ofertaService.obtenerCantidadDeOfertasParaMostrar(usuario));
		respuesta.put("usuarioEstaLogueado", usuario == null ? false : usuario.getEstaLogueado());
		
		return respuesta.toString();
	}
	
	//@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas/compartida/{id}")
	public String ofertaCompartida(@PathVariable("id") int id, HttpServletRequest request) throws ClassNotFoundException, SQLException, JSONException
	{
		request.setAttribute("oferta", ofertaDao.obtener(id, null));
		request.setAttribute("url", request.getRequestURL());
		request.setAttribute("baseURL", request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath());
		return "ofertaCompartida";
	}
	
	@ResponseBody
	@RequestMapping(value = "/BackOffice/subirImagenPublicidad", method = RequestMethod.POST)
	public String subirImagenPublicidad(@RequestParam("imagen-publicidad-input") MultipartFile uploadfile) throws IOException
	{
		System.out.println("Imagen publicidad");
		if( ! gestorDeArchivos.esUnaImagen(uploadfile) && ! gestorDeArchivos.esUnArchivoComprimido(uploadfile))
		{
			return "No es una extension valida";
		}
		return gestorDeArchivos.subirArchivo(uploadfile);
	}

}
