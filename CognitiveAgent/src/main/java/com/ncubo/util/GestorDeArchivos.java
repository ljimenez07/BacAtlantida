package com.ncubo.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@ConfigurationProperties("imagenes")
public class GestorDeArchivos
{
	private String[] formatoDeImagenes = { "PNG", "JPG", "TIF","BMP", "GIF", "JPEG"};
	private String[] extensionesDeArchivosComprimidos = {"zip"};
	private String path;
	
	public boolean esUnArchivoComprimido(MultipartFile uploadfile)
	{
		String nombreArchivo = uploadfile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(nombreArchivo);
		boolean esUnArchivoComprimido = Arrays.asList(extensionesDeArchivosComprimidos).contains(extension.toLowerCase()); 
		return esUnArchivoComprimido;
	}
	
	public boolean esUnaImagen(MultipartFile uploadfile)
	{
		String nombreArchivo = uploadfile.getOriginalFilename();
		String extension = FilenameUtils.getExtension(nombreArchivo);
		boolean esUnArchivoComprimido = Arrays.asList(formatoDeImagenes).contains(extension.toUpperCase());
		return esUnArchivoComprimido;
	}
	
	public String subirArchivo(MultipartFile uploadfile) throws IOException, ZipException
	{
		String nombreArchivo = uploadfile.getOriginalFilename();
		path = path.replace("\\", "/");
		String archivoDestino = path;
		String filepath = Paths.get(archivoDestino, nombreArchivo).toString();
		
		String UUIDGenerado = UUID.randomUUID().toString().replace("-", "");
		
		String extension = FilenameUtils.getExtension(nombreArchivo);
		boolean esUnArchivoComprimido = Arrays.asList(extensionesDeArchivosComprimidos).contains(extension.toLowerCase()); 
		boolean esUnaImagen = Arrays.asList(formatoDeImagenes).contains(extension.toUpperCase());
		new Descomprimir("").crearCarpetaDeArchivos(archivoDestino);
		
		if(esUnArchivoComprimido)
		{
			String urlDeAlmacenamiento = path + "/" + UUIDGenerado + nombreArchivo;
			Descomprimir descomprimir = new Descomprimir(urlDeAlmacenamiento);
			
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
			urlDeAlmacenamiento = urlDeAlmacenamiento.replace(".zip", "");

			descomprimir.descomprimir(urlDeAlmacenamiento, filepath);
			borrarDirectorio(filepath);
			filepath = urlDeAlmacenamiento;
		}
		if(esUnaImagen)
		{
			filepath = path + "/"  + UUIDGenerado + nombreArchivo;
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadfile.getBytes());
			stream.close();
		}
		
		return filepath;
	}
	
	public void borrarDirectorio(String direccionABorrar) throws IOException
	{
		FileUtils.forceDelete(new File(direccionABorrar));
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}
	
}
