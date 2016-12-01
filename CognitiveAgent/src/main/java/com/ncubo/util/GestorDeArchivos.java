package com.ncubo.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ncubo.conf.AgenteCognitivo;

@Component
@ConfigurationProperties("archivos")
public class GestorDeArchivos
{
	private String[] formatoDeImagenes = { "PNG", "JPG", "TIF","BMP", "GIF", "JPEG"};
	private String[] extensionesDeArchivosComprimidos = {"zip"};
	private String path;
	@Autowired
	private AgenteCognitivo agenteCognitivo;
	@Autowired
	private FTPServidor ftp;
	
	
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
		new Descompresor("").crearCarpetaDeArchivos(archivoDestino);
		
		if(esUnArchivoComprimido)
		{
			String urlDeAlmacenamiento = path + "/" + UUIDGenerado + nombreArchivo;
			Descompresor descomprimir = new Descompresor(urlDeAlmacenamiento);
			
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
		
		String direccionArchivo = ftp.subirArchivo(filepath);
		borrarDirectorio(filepath);
		
		return direccionArchivo;
	}
	
	public String textoAAudio(String subPath, String mensaje) throws IOException
	{
		TextToSpeech textService = new TextToSpeech();
		textService.setUsernameAndPassword(agenteCognitivo.getUserTextToSpeech(), agenteCognitivo.getPasswordTextToSpeech());
		
		String absolutePath= path+File.separator+subPath;
		
		InputStream in = null;
		File directory = new File(absolutePath);
		directory.mkdirs();
		String pathArchivo = absolutePath;

		absolutePath += File.separator+"descripcion.wav";
		
		File file = null;
		file = new File(absolutePath);
		
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));

		String voice = agenteCognitivo.getVoiceTextToSpeech();
		in = textService.synthesize(mensaje, new Voice(voice, null, null), AudioFormat.WAV).execute();

		byte[] buffer = new byte[2048];
		int read;
		while ((read = in.read(buffer)) != -1) 
		{
			stream.write(buffer, 0, read);
		}

		stream.close();
		
		ftp.subirArchivo(pathArchivo);
		borrarDirectorio(pathArchivo);

		return path;
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
