package com.ncubo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("serverFTP")
public class FTPServidor
{
	private String usuario;
	private String password;
	private String host;
	private int puerto;
	private String carpeta;
	
	private boolean subirUnArchivo(FTPClient ftpClient, String localFilePath, String remoteFilePath) throws IOException
	{
		Path path = Paths.get(localFilePath);
		String finalPath = remoteFilePath.equals("") ? path.getFileName().toString() : remoteFilePath;

		InputStream inputStream = new FileInputStream(localFilePath);
		
		try
		{
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			return ftpClient.storeFile(finalPath, inputStream);
		} 
		finally
		{
			inputStream.close();
		}
	}
	
	private String subirDirectorio(FTPClient ftpClient, String remoteDirPath, String localParentDir, String remoteParentDir) throws IOException
	{
		String directorioFinalArchivo = "";
		File localDir = new File(localParentDir);
		File[] subFiles = localDir.listFiles();
		if (subFiles != null && subFiles.length > 0)
		{
			for (File item : subFiles)
			{
				String remoteFilePath = remoteDirPath + "/" + remoteParentDir + "/" + item.getName();
				if (remoteParentDir.equals(""))
				{
					remoteFilePath = remoteDirPath + "/" + item.getName();
				}

				if (item.isFile())
				{
					String extensionArchivo = FilenameUtils.getExtension(item.getAbsolutePath()); 
					if (extensionArchivo.equalsIgnoreCase("html"))
					{
						directorioFinalArchivo = remoteFilePath;
						ajustarRutaDeImagenesEnPlantillaHTML(item, remoteDirPath + "/" + remoteParentDir + "/");	
					}
					String localFilePath = item.getAbsolutePath();
					subirUnArchivo(ftpClient, localFilePath, remoteFilePath);
				}
				else
				{
					ftpClient.makeDirectory(remoteFilePath);
					String parent = remoteParentDir + "/" + item.getName();
					if (remoteParentDir.equals(""))
					{
						parent = item.getName();
					}

					localParentDir = item.getAbsolutePath();
					String stringSubirDirectorio = subirDirectorio(ftpClient, remoteDirPath, localParentDir, parent);
					
					if ( ! stringSubirDirectorio.equals(""))
					{
						directorioFinalArchivo = stringSubirDirectorio;
					}
				}
			}
		}
		
		return directorioFinalArchivo;
	}
	
	public String subirArchivo(String path) throws SocketException, IOException
	{
		String pathArchivoGuardado = "";
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, puerto);
		ftpClient.login(usuario, password);
		ftpClient.enterLocalPassiveMode();
		
		Path file = new File(path).toPath();
		
		ftpClient.changeWorkingDirectory(carpeta);
		int returnCode = ftpClient.getReplyCode();
		if (returnCode == 550)
		{
			ftpClient.makeDirectory(carpeta);
			ftpClient.changeWorkingDirectory(carpeta);
		}
		
		if(Files.isRegularFile(file))
		{
			subirUnArchivo(ftpClient, path, "");
			pathArchivoGuardado = file.getFileName().toString();
		}
		else
		{
			ftpClient.makeDirectory(file.getFileName().toString());
			pathArchivoGuardado = subirDirectorio(ftpClient, file.getFileName().toString(), path, "");
		}
		
		ftpClient.logout();
		ftpClient.disconnect();
		
		return pathArchivoGuardado.replaceAll("-", "_").replaceAll("/", "-");
	}
	
	public InputStream descargarArchivo(String nombreArchivo)
	{
		try{
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(host, puerto);
			ftpClient.login(usuario, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(carpeta);

			return ftpClient.retrieveFileStream(nombreArchivo);
		}catch(Exception e){
			System.out.println(String.format("El audio %s no se encontro en el FTP", nombreArchivo));
			return null;
		}
	}
	
	public void subirUnArchivo(InputStream archivo, String pathDondeGuardar) throws IOException
	{
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, puerto);
		ftpClient.login(usuario, password);
		ftpClient.changeWorkingDirectory(carpeta);
		
		String[] directorio = pathDondeGuardar.split("/");
		String nombreArchivo = directorio[directorio.length-1];
		
		for(int i = 0 ; i < directorio.length - 1 ; i++)
		{
			ftpClient.changeWorkingDirectory(directorio[i]);
			int returnCode = ftpClient.getReplyCode();
			if (returnCode == 550)
			{
				ftpClient.makeDirectory(directorio[i]);
				ftpClient.changeWorkingDirectory(directorio[i]);
			}
		}
		
		try
		{
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.storeFile(nombreArchivo, archivo);
		} 
		finally
		{
			archivo.close();
		}
	}
	
	private void ajustarRutaDeImagenesEnPlantillaHTML(File html, String rutaArchivoEnFTP) throws IOException
	{
		Document doc = Jsoup.parse(html, null);
		Elements imgElementos = doc.getElementsByTag("img");
		Charset charset = doc.charset();
		
		for(Element elemento : imgElementos)
		{
			String srcDeImg = elemento.attr("src");
			String[] imagenPath = srcDeImg.split("/");
			
			String srcModificado = (rutaArchivoEnFTP + imagenPath[ imagenPath.length-1 ] ).replaceAll("-", ".").replaceAll("/", "-");
			elemento.attr("src", srcModificado);
		}
		
		PrintWriter writer = new PrintWriter(html, charset.toString());
		writer.write( doc.html() ) ;
		writer.flush();
		writer.close();
	}
	
	public String getUsuario()
	{
		return usuario;
	}

	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPuerto()
	{
		return puerto;
	}

	public void setPuerto(int puerto)
	{
		this.puerto = puerto;
	}

	public String getCarpeta()
	{
		return carpeta;
	}

	public void setCarpeta(String carpeta)
	{
		this.carpeta = carpeta;
	}
	
}