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
import com.ncubo.dao.ReaccionesDao;
import com.ncubo.data.Reacciones;

@Controller
public class ReaccionesController 
{
	@Autowired
	private ReaccionesDao reaccionesDao;
	
	@RequestMapping("/BackOffice/reaccionDeOfertas")
	public String redireccionarReaccionDeOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		return "reaccionDeOfertas";
	}
	
	@RequestMapping(value = "/BackOffice/reaccionDeOfertasDatos", method = RequestMethod.POST)
	public @ResponseBody ArrayList<Reacciones> obtenerDatosReaccionDeOfertas(@RequestBody String parametros) throws IOException, ClassNotFoundException, SQLException, ParseException
	{
		Gson gson = new Gson();
		Object deserializado = gson.fromJson(parametros, Object.class);
		Map<?,?> mapaDeDatos = (Map<?,?>) deserializado;
		Map<?,?> datos = (Map<?,?>) mapaDeDatos.get("parametros");
		
		String fechaDesde = (String) datos.get("desde");
		String fechaHasta = (String) datos.get("hasta");
		String filtro = (String) datos.get("filtro");

		ArrayList<Reacciones> resultado = reaccionesDao.obtener(fechaDesde, fechaHasta, filtro);		
		return resultado;
	}
}
