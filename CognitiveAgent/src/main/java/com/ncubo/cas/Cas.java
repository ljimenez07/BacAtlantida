package com.ncubo.cas;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ncubo.bambu.dao.TokenDao;
import com.ncubo.bambu.data.TokenDeAcceso;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("jwt")
public class Cas
{

	private Jwt jwt;
	private String llave;
	private String emisorDelToken;
	private String tiempoDeVidaDelLink;
	private String numeroMaximoDePeticiones;
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(Cas.class);
	}
	
	public String getTiempoDeVidaDelLink()
	{
		return tiempoDeVidaDelLink;
	}

	public void setTiempoDeVidaDelLink(String tiempoDeVidaDelLink)
	{
		this.tiempoDeVidaDelLink = tiempoDeVidaDelLink;
	}

	public String getNumeroMaximoDePeticiones()
	{
		return numeroMaximoDePeticiones;
	}

	public void setNumeroMaximoDePeticiones(String numeroMaximoDePeticiones)
	{
		this.numeroMaximoDePeticiones = numeroMaximoDePeticiones;
	}
	
	public String getLlave()
	{
		return llave;
	}

	public void setLlave(String llave)
	{
		this.llave = llave;
	}

	public String getEmisorDelToken()
	{
		return emisorDelToken;
	}

	public void setEmisorDelToken(String emisorDelToken)
	{
		this.emisorDelToken = emisorDelToken;
	}
	
	public Cas()
	{
		jwt = new Jwt();
	}

	public String generarTokenDeAcceso()
	{
		int tiempoDeVidaDelLink = Integer.parseInt(getTiempoDeVidaDelLink());
		int numeroMaximoDePeticiones = Integer.parseInt(getNumeroMaximoDePeticiones());
		
		String token = null;
		try
		{
			token = jwt.generarToken(tiempoDeVidaDelLink);

			DecodedJWT datos = jwt.decodificar(token);
			Timestamp fechaEmision = new Timestamp(datos.getIssuedAt().getTime());
			Timestamp fechaExpiracion = new Timestamp(datos.getExpiresAt().getTime());
			
			TokenDao tokenDao = new TokenDao();
			if (tokenDao.guardar(token, fechaEmision, fechaExpiracion, numeroMaximoDePeticiones))
			{
				return token;
			}
		}
		catch (Exception e)
		{
			LOG.error(e);
		}
		return token;
	}
	
	public boolean elTokenEsValido(String token)
	{
		int numeroMaximoDePeticiones = Integer.parseInt(getNumeroMaximoDePeticiones());
		boolean esValido = jwt.esValido(token);
		if (esValido)
		{
			TokenDao tokenDao = new TokenDao();
			TokenDeAcceso tokenDeAcceso = tokenDao.obtener(token);
			esValido = tokenDeAcceso.obtenerVecesUtilizado() < numeroMaximoDePeticiones;
		}
		return esValido;
	}
	
}
