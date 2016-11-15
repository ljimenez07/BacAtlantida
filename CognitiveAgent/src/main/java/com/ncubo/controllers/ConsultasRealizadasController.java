package com.ncubo.controllers;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConsultasRealizadasController
{
	@RequestMapping("/consultasRealizadas")
	public String visualizarOfertas(Model model) throws ClassNotFoundException, SQLException
	{
		return "consultasRealizadas";
	}

}
