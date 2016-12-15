package com.ncubo.controllers;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ncubo.db.ConsultaDao;
import com.ncubo.logicaDeConversaciones.Conversaciones;
import com.ncubo.util.Validacion;

@Controller
public class ConsultasRealizadasController
{
	
	@Autowired
	private Conversaciones misConversaciones;
	
	@GetMapping("/BackOffice/consultasRealizadas")
	public String visualizarOfertas(Model model) throws ClassNotFoundException, SQLException
	{
		Date date = new Date();		
		String fechaDesde = new SimpleDateFormat("yyyy-MM-dd").format(date);
		String fechaHasta = fechaDesde;
		
		model.addAttribute("fechaDesde", fechaDesde);
		model.addAttribute("fechaHasta", fechaHasta);
		model.addAttribute("consultasFiltradas", misConversaciones.obtenerConsultaDao().obtener(fechaDesde, fechaHasta));
		
		return "consultasRealizadas";
	}
	
	@PostMapping("/BackOffice/filtrarConsultasRealizadas")
	public String filtrarConsultasRealizadas(Model model, @RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta) throws ClassNotFoundException, SQLException, ParseException
	{
		model.addAttribute("fechaDesde", fechaDesde);
		model.addAttribute("fechaHasta", fechaHasta);
		
		if ( ! Validacion.fechaHastaMayorOIgualAFechaDesde(fechaDesde, fechaHasta))
		{
			model.addAttribute("mensajeError", "*Fechas son incorrectas");
			return "consultasRealizadas";
		}
		
		model.addAttribute("consultasFiltradas", misConversaciones.obtenerConsultaDao().obtener(fechaDesde, fechaHasta));
		return "consultasRealizadas";
	}
	
	

}
