package com.ncubo.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ncubo.librerias.LRUCache;
import com.ncubo.util.FTPServidor;

@Controller
public class ArchivosController
{
	@Autowired
	private FTPServidor ftp;
	private final int capacidadDeCache = 30;
	
	private LRUCache cacheDeAudios = new LRUCache(capacidadDeCache);

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/archivossubidos/{nombre:.*}", method = RequestMethod.GET)
	void archivossubidos(HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String nombre) throws JSONException, JsonParseException, JsonMappingException, IOException, URISyntaxException, ClassNotFoundException, SQLException 
	{
		String remoteFile2 = nombre.replace("-", "/");
		InputStream streamPorDevolver = null ;
		
		if (remoteFile2.startsWith("audio"))
		{
			byte[] bytesStreamPorDevolver = cacheDeAudios.obtener(remoteFile2);
			if(bytesStreamPorDevolver == null)
			{
				streamPorDevolver = ftp.descargarArchivo(remoteFile2);
				bytesStreamPorDevolver = IOUtils.toByteArray(streamPorDevolver);
				cacheDeAudios.establecer(remoteFile2, bytesStreamPorDevolver);
				
			}
			streamPorDevolver= new ByteArrayInputStream(bytesStreamPorDevolver);
		}
		else
		{
			streamPorDevolver = ftp.descargarArchivo(remoteFile2);
		}
		
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

		while ((bytesRead = streamPorDevolver.read(bytesArray)) != -1)
		{
			outStream.write(bytesArray, 0, bytesRead);
		}

		outStream.close();
		streamPorDevolver.close();
	}
}
