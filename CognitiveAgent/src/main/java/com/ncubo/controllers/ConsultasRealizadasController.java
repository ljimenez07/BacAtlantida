package com.ncubo.controllers;

import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ncubo.dao.ConsultaDao;
import com.ncubo.util.Validacion;

@Controller
public class ConsultasRealizadasController
{
	@Autowired
	private ConsultaDao consultaDao;
	
	@GetMapping("/consultasRealizadas")
	public String visualizarOfertas(Model model) throws ClassNotFoundException, SQLException
	{
		model.addAttribute("fechaDesde", "");
		model.addAttribute("fechaHasta", "");
		
		return "consultasRealizadas";
	}
	
	@PostMapping("/filtrarConsultasRealizadas")
	public String filtrarConsultasRealizadas(Model model, @RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta) throws ClassNotFoundException, SQLException, ParseException
	{
		model.addAttribute("fechaDesde", fechaDesde);
		model.addAttribute("fechaHasta", fechaHasta);
		
		if ( ! Validacion.fechaHastaMayorOIgualAFechaDesde(fechaDesde, fechaHasta))
		{
			model.addAttribute("mensajeError", "*Fechas son incorrectas");
			return "consultasRealizadas";
		}
		
		model.addAttribute("consultasFiltradas", consultaDao.obtener(fechaDesde, fechaHasta));
		return "consultasRealizadas";
	}
	
	

}
