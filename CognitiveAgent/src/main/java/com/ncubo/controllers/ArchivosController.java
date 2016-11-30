package com.ncubo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ncubo.util.FTPServidor;

@Controller
public class ArchivosController
{
	@Autowired
	private FTPServidor ftp;

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/archivossubidos/{nombre:.*}", method = RequestMethod.GET)
	void archivossubidos(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String nombre) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException, ClassNotFoundException, SQLException 
	{
		String remoteFile2 = nombre.replace("-", "/");
		InputStream inputStream = ftp.descargarArchivo(remoteFile2);

		byte[] bytesArray = new byte[4096];
		int bytesRead = -1;

		ServletContext context = request.getServletContext();
		String mimetype = context.getMimeType(remoteFile2);

		if (mimetype == null)
		{
			mimetype = "application/octet-stream";
		}
		
		response.setContentType(mimetype);
		OutputStream outStream = response.getOutputStream();

		if(inputStream != null)
		{
			while ((bytesRead = inputStream.read(bytesArray)) != -1)
			{
				outStream.write(bytesArray, 0, bytesRead);
			}
			inputStream.close();
		}

		outStream.close();
	}
}
