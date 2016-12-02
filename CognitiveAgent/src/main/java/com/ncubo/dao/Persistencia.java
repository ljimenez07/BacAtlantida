package com.ncubo.dao;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Connection;
import com.ncubo.data.Configuracion;

@Component
public class Persistencia
{
	@Autowired
	private Configuracion config;
	Connection conector;
	
	public Connection openConBD() throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		conector = (Connection) DriverManager.getConnection
				("jdbc:mysql://"+ config.getUrl() +"/" + config.getNombreBase(), config.getUsuario(), config.getClave());
		return conector;
	}
	
	public void closeConBD() throws SQLException
	{
		conector.close();
	}

}
