package com.ncubo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ncubo.dao.CategoriaDao;
import com.ncubo.dao.OfertaDao;
import com.ncubo.data.Oferta;
import com.ncubo.util.GestorDeArchivos;

@Controller
public class GestionarOfertasController
{
	@Autowired
	private OfertaDao ofertaDao;
	@Autowired
	private CategoriaDao categoriaDao;
	@Autowired
	private GestorDeArchivos gestorDeArchivos;
	private final String ACTION_INSERTAR_OFERTA = "insertarOferta";
	private final String ACTION_MODIFICAR_OFERTA = "modificarOferta";
	
	private final String BOTON_MODIFICAR_OFERTA = "Modificar";
	private final String BOTON_INSERTAR_OFERTA = "Insertar";
	
	private final String IMAGEN_MODIFICAR_OFERTA = "imagen-check";
	private final String IMAGEN_INSERTAR_OFERTA = "imagen-siguiente";
	
	
	@RequestMapping("/gestionDeOfertas")
	public String visualizarOfertas(Model model) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = ofertaDao.obtener();
		if (ofertas.isEmpty())
		{
			return "redirect:insertarOferta";
		}
		model.addAttribute("listaDeOfertas", ofertas);
		return "tablaDeOfertas";
	}
	
	@GetMapping("/insertarOferta")
	public String cargarInsertarOfertas(Oferta oferta, Model model) throws ClassNotFoundException, SQLException
	{
		model.addAttribute("action", ACTION_INSERTAR_OFERTA);
		model.addAttribute("categorias", categoriaDao.obtener());
		model.addAttribute("nombreBotonPrincipal", BOTON_INSERTAR_OFERTA);
		model.addAttribute("imagenBotonPrincipal", IMAGEN_INSERTAR_OFERTA);
		return "insertarOferta";
	}
	
	@PostMapping(value = "/insertarOferta", params="accion=ingresar")
	public String insertarOfertas(@Valid Oferta oferta, BindingResult bindingResult, Model model) throws ClassNotFoundException, SQLException, ParseException
	{
		if( ! bindingResult.hasFieldErrors("vigenciaHasta"))
		{
			if( ! oferta.fechaHastaMayorAFechaDesde())
			{
				bindingResult.rejectValue("vigenciaHasta", "1", "*Fechas incorrectas");
			}
		}
		
		if (bindingResult.hasErrors())
		{
			model.addAttribute("categorias", categoriaDao.obtener());
			model.addAttribute("action", ACTION_INSERTAR_OFERTA);
			model.addAttribute("nombreBotonPrincipal", BOTON_INSERTAR_OFERTA);
			model.addAttribute("imagenBotonPrincipal", IMAGEN_INSERTAR_OFERTA);
			return null;
		}
		oferta.setFechaHoraRegistro(new Timestamp(new Date().getTime()));
		ofertaDao.insertar(oferta);
		
		return "redirect:gestionDeOfertas";
	}
	
	@PostMapping(value = "/insertarOferta", params="accion=limpiar")
	public String insertarOfertasLimpiar(@Valid Oferta oferta, BindingResult bindingResult) throws ClassNotFoundException, SQLException
	{
		return "redirect:gestionDeOfertas";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ofertas", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody public List<Oferta> ofertas(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{
		return ofertaDao.ultimasOfertas();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ofertas/{idOferta}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody public Oferta oferta(@PathVariable int idOferta, HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{
		return ofertaDao.obtener(idOferta);
	}
	
	@ResponseBody
	@RequestMapping(value = "/subirImagenPublicidad", method = RequestMethod.POST)
	public String subirImagenPublicidad(@RequestParam("imagen-publicidad-input") MultipartFile uploadfile) throws IOException
	{
		System.out.println("Imagen publicidad");
		if( ! gestorDeArchivos.esUnaImagen(uploadfile) && ! gestorDeArchivos.esUnArchivoComprimido(uploadfile))
		{
			return "";
		}
		return gestorDeArchivos.subirArchivo(uploadfile);
	}
	
	@ResponseBody
	@RequestMapping(value = "/subirImagenComercio", method = RequestMethod.POST)
	public String subirImagenComercio(@RequestParam("logo-comercio-input") MultipartFile uploadfile) throws IOException
	{
		System.out.println("Imagen Comercio");
		if( ! gestorDeArchivos.esUnaImagen(uploadfile))
		{
			return "";
		}
		return gestorDeArchivos.subirArchivo(uploadfile);
	}
	
	@GetMapping(value = "/modificarOferta")
	public String modificarOferta(Model model, int idOferta, Oferta oferta) throws ClassNotFoundException, SQLException
	{
		if(idOferta != 0)
		{
			oferta = ofertaDao.obtener(idOferta);
		}
		model.addAttribute("oferta", oferta);
		model.addAttribute("categorias", categoriaDao.obtener());
		model.addAttribute("action", ACTION_MODIFICAR_OFERTA);
		model.addAttribute("nombreBotonPrincipal", BOTON_MODIFICAR_OFERTA);
		model.addAttribute("imagenBotonPrincipal", IMAGEN_MODIFICAR_OFERTA);
		return "insertarOferta";
	}
	
	@PostMapping(value = "/modificarOferta", params="accion=ingresar")
	public String modificarOferta(@Valid Oferta oferta, BindingResult bindingResult, Model model, HttpServletRequest request) throws ClassNotFoundException, SQLException, ParseException
	{
		if( ! bindingResult.hasFieldErrors("vigenciaHasta"))
		{
			if( ! oferta.fechaHastaMayorAFechaDesde())
			{
				bindingResult.rejectValue("vigenciaHasta", "1", "*Fechas incorrectas");
			}
		}
		
		if (bindingResult.hasErrors())
		{
			model.addAttribute("categorias", categoriaDao.obtener());
			model.addAttribute("action", ACTION_MODIFICAR_OFERTA);
			model.addAttribute("nombreBotonPrincipal", BOTON_MODIFICAR_OFERTA);
			model.addAttribute("imagenBotonPrincipal", IMAGEN_MODIFICAR_OFERTA);
			
			request.setAttribute("idOferta", 0);
			return modificarOferta(model, 0, oferta);
		}
		oferta.setFechaHoraRegistro(new Timestamp(new Date().getTime()));
		ofertaDao.modificar(oferta);
		
		return "redirect:gestionDeOfertas";
	}
	
	@PostMapping(value = "/modificarOferta", params="accion=limpiar")
	public String modificarOfertaLimpiar(@Valid Oferta oferta, BindingResult bindingResult) throws ClassNotFoundException, SQLException
	{
		return "redirect:gestionDeOfertas";
	}
	
	@GetMapping("/eliminarOferta/{idOferta}")
	public String eliminarOferta(@PathVariable int idOferta)
	{
		
		return "insertarOferta";
	}
	
}
