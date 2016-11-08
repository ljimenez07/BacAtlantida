package hello;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("servercognitivo")
public class ServerCognitivo 
{
	private String user;
	private String password;
	private String workspace;
	
	public String getUser() 
	{
		return user;
	}
	public String getPassword() 
	{
		return password;
	}
	public String getWorkspace() 
	{
		return workspace;
	}
	public void setUser(String user) 
	{
		this.user = user;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public void setWorkspace(String workspace) 
	{
		this.workspace = workspace;
	}
	

}
