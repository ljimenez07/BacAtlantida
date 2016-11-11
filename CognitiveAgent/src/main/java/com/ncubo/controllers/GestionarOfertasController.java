package com.ncubo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping("/gestionDeOfertas")
	public String visualizarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = ofertaDao.obtener();
		if (ofertas.isEmpty())
		{
			request.setAttribute("categorias", categoriaDao.obtener());
			return "insertarOferta";
		}
		request.setAttribute("listaDeOfertas", ofertas);
		return "tablaDeOfertas";
	}
	
	@GetMapping("/insertarOfertas")
	public String cargarInsertarOfertas(HttpServletRequest request, Oferta oferta) throws ClassNotFoundException, SQLException
	{
		request.setAttribute("categorias", categoriaDao.obtener());
		return "insertarOferta";
	}
	
	@PostMapping("/insertarOfertas")
	public String insertarOfertas(HttpServletRequest request, @Valid Oferta oferta, BindingResult bindingResult) throws ClassNotFoundException, SQLException
	{
		request.setAttribute("fields", bindingResult);
		if (bindingResult.hasErrors())
		{
			return "redirect:insertarOfertas";
		}
		oferta.setFechaHoraRegistro(new Timestamp(new Date().getTime()));
		ofertaDao.insertar(oferta);
		
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
		return gestorDeArchivos.subirArchivo(uploadfile);
	}
	
	@ResponseBody
	@RequestMapping(value = "/subirImagenComercio", method = RequestMethod.POST)
	public String subirImagenComercio(@RequestParam("logo-comercio-input") MultipartFile uploadfile) throws IOException
	{
		String asd = gestorDeArchivos.subirArchivo(uploadfile);
		return asd;
	}
	
}
