package com.ncubo.bambu.util;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("mailer")
public class Mailer
{
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(Mailer.class);
	}
	
	private String correoRemitente;
	private String contrasenaRemitente;
	
	private Properties propiedadesDeSmtp;
	private Session sesion;
	private MimeMessage mensaje;
	
	public String getCorreoRemitente()
	{
		return correoRemitente;
	}

	public void setCorreoRemitente(String correoRemitente)
	{
		this.correoRemitente = correoRemitente;
	}
	
	public String getContrasenaRemitente()
	{
		return contrasenaRemitente;
	}

	public void setContrasenaRemitente(String contrasenaRemitente)
	{
		this.contrasenaRemitente = contrasenaRemitente;
	}
	
	public boolean enviarCorreo(String correoDelDestinatario, String contenidoDelCorreo, 
			String sujeto)
	{
		boolean correoFueEnviado = false;
		
		propiedadesDeSmtp = System.getProperties();
		propiedadesDeSmtp.put("mail.smtp.port", "587");
		propiedadesDeSmtp.put("mail.smtp.auth", "true");
		propiedadesDeSmtp.put("mail.smtp.starttls.enable", "true");

		sesion = Session.getDefaultInstance(propiedadesDeSmtp, null);
		mensaje = new MimeMessage(sesion);
		try
		{
			mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDelDestinatario));
			mensaje.setSubject(sujeto);
			
			mensaje.setContent(contenidoDelCorreo, "text/html");
			
			Transport transport = sesion.getTransport("smtp");
			
			transport.connect("smtp.gmail.com", correoRemitente, contrasenaRemitente);
			transport.sendMessage(mensaje, mensaje.getAllRecipients());
			
			transport.close();
			correoFueEnviado = true;
		}
		catch(Exception e)
		{
			LOG.error("Error", e);
			System.out.println(e);
			correoFueEnviado = false;
		}
		
		return correoFueEnviado;
	}
}
