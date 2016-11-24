package com.ncubo.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncubo.conf.Usuario;
import com.ncubo.dao.ReaccionDao;
import com.ncubo.data.Reaccion;

@Controller
public class ReaccionController
{
	@Autowired
	private ReaccionDao reaccionDao;
	
	@CrossOrigin(origins = "*")
	@PostMapping(value = "/reaccion/oferta", produces = "application/json")
	@ResponseBody public String reaccionAOferta(@RequestParam("idOferta") int idOferta, @RequestParam(value = "reaccion", required = false) Boolean reaccion, HttpSession sesion) throws ClassNotFoundException, SQLException
	{
		JSONObject respuesta = new JSONObject();
		Usuario usuario = (Usuario)sesion.getAttribute(Usuario.LLAVE_EN_SESSION);
		
		if(usuario != null)
		{
			String idUsuario = usuario.getUsuarioId();
			if(reaccion == null)
			{
				Reaccion objReaccion = new Reaccion(idOferta, idUsuario);
				reaccionDao.eliminar(objReaccion);
				respuesta = new JSONObject(objReaccion);
			}
			else
			{
				Reaccion objReaccion = new Reaccion(idOferta, idUsuario, reaccion);
				reaccionDao.guardar(objReaccion);
				respuesta = new JSONObject(objReaccion);
			}
			respuesta.put("usuarioEstaLogueado", usuario.getEstaLogueado());
		}
		else
		{
			respuesta.put("usuarioEstaLogueado", false);
		}

		return respuesta.toString();
	}
}
