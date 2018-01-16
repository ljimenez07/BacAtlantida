package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ncubo.bambu.data.PersonaContacto;
import com.ncubo.bambu.data.Tipo;
import com.ncubo.bambu.data.PersonaContacto.atributo;
import com.ncubo.db.ConexionALaDB;

public class PersonaContactoDao
{

	private final static Logger LOG;
	private static final String NOMBRE_TABLA = "personacontacto";
	private static final String TABLA_PERSONA = "persona";
	
	static
	{
		LOG = Logger.getLogger(PersonaDao.class);
	}
	
	public PersonaContacto obtenerPersonaContactoPorUsuario(int idUsuario)
	{
		PersonaContacto contacto = null;
		
		final String query = "SELECT * FROM " + NOMBRE_TABLA + " pc "
				+ "LEFT OUTER JOIN " + TABLA_PERSONA + " p "
				+ " ON pc." + PersonaContacto.atributo.ID_PERSONA + " = p."
				+ "WHERE u." + PersonaContacto.atributo.ID_USUARIO.toString() + " = ?";
		
		
		
		return contacto;
	}
	
	public PersonaContacto obtener(int idPersona) throws ClassNotFoundException, SQLException
	{
		PersonaContacto contacto = null;
		final String query = "SELECT * FROM " + NOMBRE_TABLA
				+ " WHERE " + PersonaContacto.atributo.ID_PERSONA.toString() + " = ?" ;
		
		final ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		final Connection con = conALaDb.openConBD();
		final PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, idPersona);
		final ResultSet rs = ps.executeQuery();
		
		if(rs.next())
		{
			contacto = new PersonaContacto();
			contacto.establecerApellidos(rs.getString(PersonaContacto.atributo.APELLIDOS.toString()));
			contacto.establecerNombre(rs.getString(PersonaContacto.atributo.NOMBRE.toString()));
			contacto.establecerEmail(rs.getString(PersonaContacto.atributo.EMAIL.toString()));
			contacto.establecerTelefono(rs.getString(PersonaContacto.atributo.TELEFONO.toString()));
			contacto.establecerCelular(rs.getString(PersonaContacto.atributo.CELULAR.toString()));
		}

		conALaDb.closeConBD();
		return contacto;
	}
	
	public boolean existe(int idPersona) throws SQLException, ClassNotFoundException
	{
		final String query = "SELECT " + PersonaContacto.atributo.ID_PERSONA_CONTACTO.toString()
			+ " FROM " + NOMBRE_TABLA
			+ " WHERE " + PersonaContacto.atributo.ID_PERSONA.toString() + " = ?";

		final ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		final Connection con = conALaDb.openConBD();
		final PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, idPersona);
		final ResultSet rs = ps.executeQuery();
		
		final int count;
		if (rs.next())
		{
			count = rs.getInt(1);
		}
		else 
		{
			count = 0;
		}
		conALaDb.closeConBD();
		return count >= 1;
	}

	public int agregar(HashMap<PersonaContacto.atributo, Object> atributos)
	{
		String query = "INSERT INTO " + NOMBRE_TABLA + "(";
		
		int contador = 0;
		for(atributo atributo : atributos.keySet())
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
		for(atributo atributo : atributos.keySet())
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
		final ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		int idPersona = 0;
		try
		{
			final Connection con = conALaDb.openConBD();
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
				conALaDb.closeConBD();
			} catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return idPersona;
	}
	
	private String agregarComillas(atributo atributo, Object valor)
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
