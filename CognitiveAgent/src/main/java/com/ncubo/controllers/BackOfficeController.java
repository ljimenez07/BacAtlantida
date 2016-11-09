package com.ncubo.controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncubo.dao.CategoriaDao;
import com.ncubo.dao.OfertaDao;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Oferta;

@Controller
public class BackOfficeController
{
	private OfertaDao ofertaDao = new OfertaDao();
	private CategoriaDao categoriaDao = new CategoriaDao();
	
	@RequestMapping("/gestionDeOfertas")
	public String visualizarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		request.setAttribute("listaDeOfertas", ofertaDao.obtener());
		return "gestionDeOfertas";
	}
	
	@RequestMapping("/cargarTablaDeOfertas")
	public String cargarTablaDeOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
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
	
	@RequestMapping("/cargarInsertarOfertas")
	public String cargarInsertarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		request.setAttribute("categorias", categoriaDao.obtener());
		return "insertarOferta";
	}
	
	@RequestMapping("/insertarOferta")
	public String insertarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
	
		Oferta oferta = new Oferta(0, request.getParameter("tituloOferta-input"), 
				request.getParameter("nombreComercio-input"), 
				request.getParameter("descripcion-textarea"), 
				new CategoriaOferta(Integer.parseInt(request.getParameter("categoria-select")), ""), 
				request.getParameter("ciudad-combobox"), 
				(request.getParameter("estado-select").equals("1") ? true : false), 
				request.getParameter("restricciones-textarea"), 
				request.getParameter("vigenciaDesde-input"), 
				request.getParameter("vigenciaHasta-input"));
		
		ofertaDao.insertar(oferta);
		
		return "gestionDeOfertas";
	}
	
	@RequestMapping(value = "/ofertas", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody public ArrayList<Oferta> ofertas(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{
		response.setHeader("Access-Control-Allow-Origin", "*");
		return ofertaDao.obtener();
	}
}
