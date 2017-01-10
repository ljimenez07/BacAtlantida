package com.ncubo.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("db")
@Scope("prototype")
public class Configuracion 
{
	private String url;
	private String nombreBase;
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
	
	public String getNombreBase()
	{
		return nombreBase;
	}

	public void setNombreBase(String nombreBase)
	{
		this.nombreBase = nombreBase;
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
