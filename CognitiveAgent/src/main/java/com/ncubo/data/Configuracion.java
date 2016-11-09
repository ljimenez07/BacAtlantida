package com.ncubo.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("db")
public class Configuracion 
{
	private String url;
	private String usuario;
	private String clave;
	public String getUrl() 
	{
		return url;
	}
	public void setUrl(String url) 
	{
		this.url = url;
	}
	public String getUsuario() 
	{
		return usuario;
	}
	public void setUsuario(String usuario) 
	{
		this.usuario = usuario;
	}
	public String getClave() 
	{
		return clave;
	}
	public void setClave(String clave) 
	{
		this.clave = clave;
	}
	
	
}
