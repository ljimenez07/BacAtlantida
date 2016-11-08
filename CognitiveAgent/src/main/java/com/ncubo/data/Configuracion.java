package com.ncubo.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuracion 
{
	private Properties prop = new Properties();
	
	public enum key
	{
		URL_BASE_DE_DATOS("url.base.datos"),
		USUARIO_BASE_DE_DATOS("usuario.base.datos"),
		CLAVE_BASE_DE_DATOS("clave.base.datos");
		
		private String nombre;
		key(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public Configuracion()
	{
		try {
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Properties getProp()
	{
		return prop;
	}
	
	public String urlBaseDeDatos()
	{
		return prop.getProperty(key.URL_BASE_DE_DATOS.toString());
	}
	
	public String usuarioBaseDeDatos()
	{
		return prop.getProperty(key.USUARIO_BASE_DE_DATOS.toString());
	}
	
	public String claveBaseDeDatos()
	{
		return prop.getProperty(key.CLAVE_BASE_DE_DATOS.toString());
	}
	
}
