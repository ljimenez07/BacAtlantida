package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ncubo.bambu.data.Tipo;
import com.ncubo.bambu.data.Persona.Atributo;

public class PersonaDao
{
	
	private final static Logger LOG;
	private final static String NOMBRE_TABLA = "persona";
	
	@Autowired
	private PersistenciaBambu dao;
	
	static
	{
		LOG = Logger.getLogger(PersonaDao.class);
	}
	
	
	public int agregar(HashMap<Atributo, Object> atributos)
	{
		String query = "INSERT INTO " + NOMBRE_TABLA + "(";
		
		int contador = 0;
		for(Atributo atributo : atributos.keySet())
		{
			query += atributo.obtenerNombre();
			contador++;
			if (contador < atributos.size())
			{
				query += ",";
			}
		}
		query += ") VALUES (";
		contador = 0;
		for(Atributo atributo : atributos.keySet())
		{
			query += agregarComillas(atributo, atributos.get(atributo));
			contador++;
			if (contador < atributos.size())
			{
				query += ",";
			}
		}
		query += ")";
		System.out.println(query);
		int idPersona = 0;
		try
		{
			final Connection con = dao.openConBD();
			final PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.executeUpdate();
			final ResultSet rs = statement.getGeneratedKeys();
			if(rs.next())
			{
				idPersona = rs.getInt(1);
			}
		}
		catch(Exception e)
		{
			LOG.error("Error", e);
		}
		finally
		{
			try
			{
				dao.closeConBD();
			} catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return idPersona;
	}
	
	private String agregarComillas(Atributo atributo, Object valor)
	{
		if(atributo.obtenerTipo().equals(Tipo.INT) || atributo.obtenerTipo().equals(Tipo.BOOLEAN))
		{
			return valor.toString();
		}
		else
		{
			return "'" + valor + "'";
		}
	}
}
