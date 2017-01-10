package com.ncubo.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Descompresor
{
	private String urlDeRepo = "";
	private String nombreDeCarpetaGenerado = "";
	
	public Descompresor(String urlRepo) 
	{
		this.urlDeRepo = urlRepo;
	}

	final static int BUFFER = 2048;
	
	public JSONArray descomprimir(String rutaDeDestino, String rutaDelArchivo)
	{
		JSONArray rutasDeArchivos = new JSONArray();
		try 
		{
			File directorioDestino = new File(rutaDeDestino);
			BufferedOutputStream dest = null;
			FileInputStream fis = new FileInputStream(rutaDelArchivo);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			FileOutputStream fos = null;
			ZipEntry entry;
			int count;
			byte data[] = new byte[BUFFER];
			String directorioRelativo = null;
			while ((entry = zis.getNextEntry()) != null) 
			{
				if(entry.getName().contains("__MACOSX"))
				{
					continue;
				}
				System.out.println("Extrayendo: " + entry);
				directorioRelativo = entry.getName();
				
				// tenemos que quitar el primer directorio
				int index = directorioRelativo.indexOf("/");
				String nombreDelArchivo = directorioRelativo.substring(index + 1);
				
				if (directorioRelativo.trim().length() > 0) 
				{
					// escribir los archivos en el disco
					try 
					{
						dest = null;
						File fileDestino = new File(directorioDestino.getAbsolutePath() + "/" + directorioRelativo);
						if (entry.isDirectory()) 
						{
							fileDestino.mkdirs();
						} 
						else 
						{
							if (fileDestino.getParentFile().exists() == false)
								fileDestino.getParentFile().mkdirs();

							fos = new FileOutputStream(fileDestino);
							dest = new BufferedOutputStream(fos, BUFFER);
							while ((count = zis.read(data, 0, BUFFER)) != -1) 
							{
								dest.write(data, 0, count);
							}
							dest.flush();
						}
					} 
					finally 
					{
						try 
						{
							if (dest != null)
							{
								dest.close();
								rutasDeArchivos.put(new JSONObject()
										.put("nombreDelArchivo", nombreDelArchivo)
										.put("urlDelArchivo", urlDeRepo + nombreDeCarpetaGenerado + "/" + directorioRelativo));
							}
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
				}
			}
			zis.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return rutasDeArchivos;
	}
	
	public void crearCarpetaDeArchivos(String nombreDeCarpeta)
	{
		File carpetaDeCompania =  new File(nombreDeCarpeta);
		if (!carpetaDeCompania.exists() || !carpetaDeCompania.isDirectory())
		{
			carpetaDeCompania.mkdirs();
		}
	}
	
}
