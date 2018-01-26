package com.ncubo.cas;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ncubo.bambu.dao.TokenDao;
import com.ncubo.bambu.data.TokenDeAcceso;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("jwt")
public class Jwt
{
	private String llave;
	private String emisorDelToken;
	private String tiempoDeVidaDelLink;
	private String numeroMaximoDePeticiones;
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(Jwt.class);
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
	private Algorithm algoritmo;
	
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
	
	@PostConstruct
	public void postContruct() throws IllegalArgumentException, UnsupportedEncodingException
	{
		algoritmo = Algorithm.HMAC512(llave);
	}
	
	public String generarToken(int duracionEnMinutos)
	{
		String token = null;
		
		TokenDao tokenDao = new TokenDao();
		java.util.Date ahora = null;
		java.util.Date expiracion = null;
		
		boolean existe = true;
		while(existe)
		{
			ahora = new java.util.Date();
			Calendar calendario = Calendar.getInstance();
			calendario.setTime(ahora);
			calendario.add(Calendar.MINUTE, duracionEnMinutos);
			expiracion = calendario.getTime();
			
			token = JWT.create()
					.withIssuer(emisorDelToken)
					.withIssuedAt(ahora)
					.withExpiresAt(expiracion)
					.sign(algoritmo);
			
			existe = tokenDao.existe(token);
		}
		return token;
	}
	
	private JWTVerifier buildVerifier(String token)
	{
		return JWT.require(algoritmo)
			    .withIssuer(emisorDelToken)
			    .build();
	}

	public boolean esValido(String token)
	{
		try
		{
			JWTVerifier verifier = buildVerifier(token);
			if (verifier.verify(token) != null)
			{
				TokenDao tokenDao = new TokenDao();
				TokenDeAcceso tokenDeAcceso = tokenDao.obtener(token);
				return tokenDeAcceso.obtenerVecesUtilizado() < tokenDeAcceso.obtenerCantidadMaximaDeUsos();
			}
		}
		catch(Exception e)
		{
			LOG.error(e);
		}
		return false;
	}
	
	public DecodedJWT decodificar(String token)
	{
		return JWT.decode(token);
	}
}
