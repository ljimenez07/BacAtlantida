package com.ncubo;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ncubo.dao.OfertaDao;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Oferta;

@Controller
public class BackOfficeController
{
	private OfertaDao ofertaDao = new OfertaDao();
	
	@RequestMapping("/gestionDeOfertas")
	public String visualizarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		request.setAttribute("listaDeOfertas", ofertaDao.obtener());
		return "gestionDeOfertas";
	}
	
	@RequestMapping("/insertarOferta")
	public String insertarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
	
		Oferta oferta = new Oferta(0, request.getParameter("tituloOferta-input"), 
				request.getParameter("nombreComercio-input"), 
				request.getParameter("descripcion-textarea"), 
				new CategoriaOferta(1, ""), 
				request.getParameter("ciudad-combobox"), 
				(request.getParameter("estado-select").equals("1") ? true : false), 
				request.getParameter("restricciones-textarea"), 
				request.getParameter("vigenciaDesde-input"), 
				request.getParameter("vigenciaHasta-input"));
		
		ofertaDao.insertar(oferta);
		
		return "gestionDeOfertas";
	}

}
