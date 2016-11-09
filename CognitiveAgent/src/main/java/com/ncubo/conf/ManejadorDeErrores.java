package com.ncubo.conf;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("manejadorDeErrores")
public class ManejadorDeErrores {
	private String password;
	private String host;
	private String port;
	private String mailFrom;
	private List<String> mailTo;
	private String subject;
	
	public void enviarCorreo(Exception e) throws MessagingException 
	{
		String body = e.getMessage();
		if(body == null) body = "";
		body += "\n";
				
		StackTraceElement traces[] = e.getStackTrace();
		if( traces == null ) traces = new StackTraceElement[0];
		for(StackTraceElement trace : e.getStackTrace())
		{
			body += trace.toString();
		}
		
		InternetAddress[] toAddresses = new InternetAddress[mailTo.size()];
		int i = 0;
		for (String address : mailTo) 
		{
			toAddresses[i] = new InternetAddress(address);
			i++;
		}
		
		// sets SMTP server properties
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.user", mailFrom);
		properties.put("mail.password", password);

		// creates a new session with an authenticator
		Authenticator auth = new Authenticator()
		{
			public PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(mailFrom, password);
			}
		};
		
		Session session = Session.getInstance(properties, auth);

		// creates a new e-mail message
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(mailFrom));
		msg.setRecipients(Message.RecipientType.TO, toAddresses);
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		
		// creates message part
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(body , "text/html");
		
		// sends the e-mail
		Transport.send(msg);
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

	public String getPort() 
	{
		return port;
	}

	public void setPort(String port) 
	{
		this.port = port;
	}

	public String getMailFrom() 
	{
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) 
	{
		this.mailFrom = mailFrom;
	}

	public List<String> getMailTo() 
	{
		return mailTo;
	}

	public void setMailTo(List<String> mailTo) 
	{
		this.mailTo = mailTo;
	}

	public String getSubject() 
	{
		return subject;
	}

	public void setSubject(String subject) 
	{
		this.subject = subject;
	}
	
	

}
