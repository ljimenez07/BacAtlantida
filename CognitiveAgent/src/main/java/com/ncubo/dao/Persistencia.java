package com.ncubo.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.ncubo.data.Configuracion;

public class Persistencia
{
	private Connection conector;
	
	public Connection openConBD() throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		conector = (Connection) DriverManager.getConnection
				("jdbc:mysql://"+ new Configuracion().urlBaseDeDatos() +"/cognitiveagent",
						new Configuracion().usuarioBaseDeDatos(), 
						new Configuracion().claveBaseDeDatos());
		return conector;
	}
	
	public void closeConBD() throws SQLException
	{
		this.conector.close();
	}

}
