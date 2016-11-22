package com.ncubo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.SystemPropertyUtils;

@Component
@ConfigurationProperties("serverFTP")
public class FTPServidor
{
	private String usuario;
	private String password;
	private String host;
	private int puerto;

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
					String nombreArchivo = FilenameUtils.getBaseName(item.getAbsolutePath());
					if (extensionArchivo.equalsIgnoreCase("html"))
					{
						if(nombreArchivo.equals(remoteParentDir))
						{
							directorioFinalArchivo = remoteFilePath;
						}
						
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
	
	public InputStream descargarArchivo(String nombreArchivo) throws SocketException, IOException
	{
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, puerto);
		ftpClient.login(usuario, password);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		
		try
		{
			return ftpClient.retrieveFileStream(nombreArchivo);
		}
		finally
		{
			ftpClient.logout();
			ftpClient.disconnect();
		}
		
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

}
