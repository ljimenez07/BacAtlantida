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
	
	@RequestMapping("/BackOffice/gestionDeOfertas")
	public String visualizarOfertas(Model model) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = ofertaDao.obtener();
		if (ofertas.isEmpty())
		{
			return "redirect:insertarOferta";
		}
		model.addAttribute("listaDeOfertas", ofertas);
		model.addAttribute("cantidadDePaginacion", ofertaDao.cantidadPaginas());
		return "tablaDeOfertas";
	}
	
	@RequestMapping("/BackOffice/gestionDeOfertas/{pagina}")
	public String visualizarOfertasPaginacion(Model model, @PathVariable int pagina) throws ClassNotFoundException, SQLException
	{
		int idDesde = ofertaDao.getCantidadPaginacion() * (pagina -1);
		ArrayList<Oferta> ofertas = ofertaDao.obtener();
		if (ofertas.isEmpty())
		{
			visualizarOfertas(model);
		}
		model.addAttribute("listaDeOfertas", ofertas);
		model.addAttribute("cantidadDePaginacion", ofertaDao.cantidadPaginas());
		return "tablaDeOfertas";
	}
	
	@RequestMapping("/BackOffice/filtrarOfertas")
	public String filtrarOfertas(@RequestParam("busquedaComercio") String nombreComercio, Model model) throws ClassNotFoundException, SQLException
	{
		return filtrarOfertas(nombreComercio, 0, model);
	}
	
	@RequestMapping("/BackOffice/filtrarOfertas/{busquedaComercio}/{desde}")
	public String filtrarOfertasDesde(@PathVariable("busquedaComercio") String nombreComercio, @PathVariable("desde") int desde, Model model) throws ClassNotFoundException, SQLException
	{
		return filtrarOfertas(nombreComercio, desde, model);
	}
	
	private String filtrarOfertas(String nombreComercio, int desde, Model model) throws ClassNotFoundException, SQLException
	{
		int desdeSiguiente = desde + ofertaDao.getCantidadPaginacion();
		int desdeAtras = desde - ofertaDao.getCantidadPaginacion();
		
		model.addAttribute("busquedaComercio", nombreComercio);
		
		ArrayList<Oferta> ofertas = ofertaService.filtrarOferta(nombreComercio, desde);

		if (ofertas == null)
		{
			return visualizarOfertas(model);
		}
		if(desde != 0 && ofertas.isEmpty())
		{
			return filtrarOfertas(nombreComercio, 0, model);
		}
		if (ofertas.isEmpty())
		{
			return "redirect:insertarOferta";
		}
		if (ofertas.size() == ofertaDao.getCantidadPaginacion())
		{
			model.addAttribute("cantidadDePaginacionFiltroSiguiente", desdeSiguiente);
		}
		if (desdeAtras >= 0)
		{
			model.addAttribute("cantidadDePaginacionFiltroAtras", desdeAtras);
		}
		
		model.addAttribute("listaDeOfertas", ofertas);
		model.addAttribute("cantidadDePaginacionFiltro", desde);
		return "tablaDeOfertas";
	}
	
	@GetMapping("/BackOffice/insertarOferta")
	public String cargarInsertarOfertas(Oferta oferta, Model model) throws ClassNotFoundException, SQLException
	{
		model.addAttribute("action", ACTION_INSERTAR_OFERTA);
		model.addAttribute("categorias", categoriaDao.obtener());
		model.addAttribute("nombreBotonPrincipal", BOTON_INSERTAR_OFERTA);
		model.addAttribute("imagenBotonPrincipal", IMAGEN_INSERTAR_OFERTA);
		model.addAttribute("imagenBotonSecundario", BOTON_SECUNDARIO_INSERTAR_OFERTA);
		return "insertarOferta";
	}
	
	@PostMapping(value = "/BackOffice/insertarOferta", params="accion=ingresar")
	public String insertarOfertas(@Valid Oferta oferta, BindingResult bindingResult, Model model) throws ClassNotFoundException, SQLException, ParseException, IOException
	{
		bindingResult = oferta.validarCampos(bindingResult, oferta);
		
		if (bindingResult.hasErrors())
		{
			return cargarInsertarOfertas(oferta, model);
		}
		oferta.setFechaHoraRegistro(new Timestamp(new Date().getTime()));
		ofertaService.insertar(oferta);
		
		return "redirect:gestionDeOfertas";
	}
	
	@PostMapping(value = "/BackOffice/insertarOferta", params="accion=limpiar")
	public String insertarOfertasLimpiar(@Valid Oferta oferta, BindingResult bindingResult) throws ClassNotFoundException, SQLException
	{
		return "redirect:gestionDeOfertas";
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas", produces = "application/json")
	@ResponseBody public List<Oferta> ofertas(@RequestParam("pagina") int pagina, HttpSession sesion) throws Exception
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		String idUsuario = usuario == null ? null : usuario.getEstaLogueado() ? usuario.getUsuarioId() : null;
		int indiceInicial = (pagina - 1) * 10;
		return ofertaService.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas/{idOferta}", produces = "application/json")
	@ResponseBody public Oferta oferta(@PathVariable int idOferta, HttpSession sesion) throws ClassNotFoundException, SQLException
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		String idUsuario = usuario == null ? null : usuario.getEstaLogueado() ? usuario.getUsuarioId() : null;
		return ofertaDao.obtener(idOferta, idUsuario);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(value = "/ofertas/cantidad", produces = "application/json")
	@ResponseBody public String cantidadDeOfertas(HttpSession sesion) throws ClassNotFoundException, SQLException, JSONException
	{
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		JSONObject respuesta = new JSONObject().put("cantidad", ofertaDao.obtenerCantidadDeOfertasParaMostrar());
		respuesta.put("usuarioEstaLogueado", usuario == null ? false : usuario.getEstaLogueado());
		
		return respuesta.toString();
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
	
	@ResponseBody
	@RequestMapping(value = "/BackOffice/subirImagenComercio", method = RequestMethod.POST)
	public String subirImagenComercio(@RequestParam("logo-comercio-input") MultipartFile uploadfile) throws IOException
	{
		System.out.println("Imagen Comercio");
		if( ! gestorDeArchivos.esUnaImagen(uploadfile))
		{
			return "No es una extension valida";
		}
		return gestorDeArchivos.subirArchivo(uploadfile);
	}
	
	@GetMapping(value = "/BackOffice/modificarOferta")
	public String modificarOferta(Model model, int idOferta, Oferta oferta, @RequestParam(value = "idUsuario", required = false) String idUsuario) throws ClassNotFoundException, SQLException
	{
		if(idOferta != 0)
		{
			oferta = ofertaDao.obtener(idOferta, idUsuario);
		}
		model.addAttribute("oferta", oferta);
		model.addAttribute("categorias", categoriaDao.obtener());
		model.addAttribute("action", ACTION_MODIFICAR_OFERTA);
		model.addAttribute("nombreBotonPrincipal", BOTON_MODIFICAR_OFERTA);
		model.addAttribute("imagenBotonPrincipal", IMAGEN_MODIFICAR_OFERTA);
		model.addAttribute("imagenBotonSecundario", BOTON_SECUNDARIO_MODIFICAR_OFERTA);
		return "insertarOferta";
	}
	
	@PostMapping(value = "/BackOffice/modificarOferta", params="accion=ingresar")
	public String modificarOferta(@Valid Oferta oferta, BindingResult bindingResult, Model model, HttpServletRequest request, @RequestParam(value = "idUsuario", required = false) String idUsuario) throws ClassNotFoundException, SQLException, ParseException, IOException
	{
		bindingResult = oferta.validarCampos(bindingResult, oferta);
		
		if (bindingResult.hasErrors())
		{		
			return modificarOferta(model, 0, oferta, idUsuario);
		}
		oferta.setFechaHoraRegistro(new Timestamp(new Date().getTime()));
		ofertaDao.modificar( oferta );
		
		return "redirect:gestionDeOfertas";
	}
	
	@PostMapping(value = "/BackOffice/modificarOferta", params="accion=limpiar")
	public String modificarOfertaLimpiar(@Valid Oferta oferta, BindingResult bindingResult) throws ClassNotFoundException, SQLException
	{
		return "redirect:gestionDeOfertas";
	}
	
	@PostMapping("/BackOffice/eliminarOferta")
	public String eliminarOferta(@RequestParam("idOfertaEliminar") int idOferta) throws ClassNotFoundException, SQLException
	{
		ofertaDao.eliminar(idOferta);
		return "redirect:gestionDeOfertas";
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		binder.registerCustomEditor(java.sql.Date.class, new CustomDateEditor(dateFormat, true));
	}

}
