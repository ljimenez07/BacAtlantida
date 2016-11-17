package com.ncubo.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ncubo.dao.ReaccionDao;
import com.ncubo.data.Reaccion;

@Controller
public class ReaccionController 
{
	@Autowired
	private ReaccionDao reaccionDao;
	
	@RequestMapping("/reaccionDeOfertas")
	public String redireccionarReaccionDeOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		return "reaccionDeOfertas";
	}
	
	@RequestMapping(value = "/reaccionDeOfertasDatos", method = RequestMethod.POST)
	public @ResponseBody ArrayList<Reaccion> obtenerDatosReaccionDeOfertas(@RequestBody String parametros) throws IOException, ClassNotFoundException, SQLException, ParseException
	{
		Gson gson = new Gson();
		Object deserializado = gson.fromJson(parametros, Object.class);
		Map<?,?> mapaDeDatos = (Map<?,?>) deserializado;
		Map<?,?> datos = (Map<?,?>) mapaDeDatos.get("parametros");
		
		String fechaDesde = (String) datos.get("desde");
		String fechaHasta = (String) datos.get("hasta");
		String filtro = (String) datos.get("filtro");

		ArrayList<Reaccion> resultado = reaccionDao.obtener(fechaDesde, fechaHasta, filtro);		
		return resultado;
	}
	
}