package com.ncubo.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ncubo.dao.CategoriaDao;
import com.ncubo.dao.OfertaDao;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Oferta;

@Controller
public class BackOfficeController
{
	@Autowired
	private OfertaDao ofertaDao;
	@Autowired
	private CategoriaDao categoriaDao;
	
	private static String pathImagenPublicidad;
	private static String pathImagenComercio;
	private String pathCarpetaImagenes = "./imagenes";
	
	@CrossOrigin(origins = "*")
	@RequestMapping("/gestionDeOfertas")
	public String visualizarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		return "gestionDeOfertas";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping("/cargarTablaDeOfertas")
	public String cargarTablaDeOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = ofertaDao.obtener();
		if (ofertas.isEmpty())
		{
			request.setAttribute("categorias", categoriaDao.obtener());
			return "insertarOferta";
		}
		request.setAttribute("listaDeOfertas", ofertas);
		return "tablaDeOfertas";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping("/cargarInsertarOfertas")
	public String cargarInsertarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		request.setAttribute("categorias", categoriaDao.obtener());
		return "insertarOferta";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping("/insertarOferta")
	public String insertarOfertas(HttpServletRequest request) throws ClassNotFoundException, SQLException
	{
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date ahora = new Date();
		
		Oferta oferta = new Oferta(0, request.getParameter("tituloOferta-input"), 
				request.getParameter("nombreComercio-input"), 
				request.getParameter("descripcion-textarea"), 
				new CategoriaOferta(Integer.parseInt(request.getParameter("categoria-select")), ""), 
				request.getParameter("ciudad-combobox"), 
				(request.getParameter("estado-select").equals("1") ? true : false), 
				request.getParameter("restricciones-textarea"), 
				request.getParameter("vigenciaDesde-input"), 
				request.getParameter("vigenciaHasta-input"),
				pathImagenComercio,
				pathImagenPublicidad,
				formatoFecha.format(ahora));
		
		ofertaDao.insertar(oferta);
		
		return "gestionDeOfertas";
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/ofertas", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody public ArrayList<Oferta> ofertas(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, SQLException
	{
		return ofertaDao.obtener();
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/subirImagenPublicidad", method = RequestMethod.POST)
	@ResponseBody
	public void subirImagenPublicidad(@RequestParam("imagen-publicidad-input") MultipartFile uploadfile) throws IOException
	{
		pathImagenPublicidad = subirArchivo(uploadfile);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/subirImagenComercio", method = RequestMethod.POST)
	@ResponseBody
	public void subirImagenComercio(@RequestParam("logo-comercio-input") MultipartFile uploadfile) throws IOException
	{
		pathImagenComercio = subirArchivo(uploadfile);
	}
	
	private String subirArchivo(MultipartFile uploadfile) throws IOException
	{
		String filename = uploadfile.getOriginalFilename();
		String directory = pathCarpetaImagenes;
		String filepath = Paths.get(directory, filename).toString();

		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
		stream.write(uploadfile.getBytes());
		stream.close();
		return filepath.replace("\\", "/");
	}
}
