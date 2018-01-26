package com.ncubo.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncubo.bambu.dao.TokenDao;
import com.ncubo.bambu.util.Mailer;
import com.ncubo.cas.Cas;
import com.ncubo.cas.Jwt;

@Controller
public class CASController
{
	
	@Autowired
	private TokenDao _tokenDao;
	
	@Autowired
	private Jwt _jwt;
	
	@Autowired
	private Mailer _mailer;
	
	@Autowired
	private Cas cas;
	
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(CASController.class);
	}
	
	@GetMapping("/BackOffice/asignarMensaje")
	public String linkTemporal(Model model, HttpServletRequest request)
	{
		return "asignarMensaje";
	}

	@ResponseBody
	@PostMapping("/BackOffice/asignarMensaje")
	public ResponseEntity<?> enviarLinkTemporal(Model model, HttpServletRequest request, 
		@RequestParam("email") String email)
	{
		String token = cas.generarTokenDeAcceso();
		
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(request.getScheme());
		urlBuilder.append("://");
		urlBuilder.append(request.getServerName());
		urlBuilder.append(":");
		if(request.getServerPort() > 0) {
			urlBuilder.append(request.getServerPort());
		}
		urlBuilder.append(request.getContextPath());
		urlBuilder.append("/BackOffice/bandejaDeMensajes/");
		urlBuilder.append(token);
		
		JSONObject res = new JSONObject();
		try
		{
			_mailer.enviarCorreo(email, urlBuilder.toString(), "Link de acceso temporal");
			res.append("status", "success");
			return ResponseEntity.status(HttpStatus.OK).body(res.toString());
		}
		catch(Exception e)
		{
			LOG.error("error", e);
			try
			{
				res.append("status", "error");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res.toString());
			} catch (JSONException e1)
			{
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{status: \"error\"}");
			}
		}
	}
	
	@GetMapping("/BackOffice/bandejaDeMensajes/{token:.+}")
	public String activarToken(Model model, @PathVariable("token") String token)
	{
		boolean esUnLInkvalido = _jwt.esValido(token);
		
		if (esUnLInkvalido)
		{
			_tokenDao.aumentarVecesUsado(token);
			return "bandejaDeMensajes";
		}
		else 
		{
			return "errorToken";
		}
	}
}
