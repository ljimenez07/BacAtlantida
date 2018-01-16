package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.Origen;
import com.ncubo.db.ConexionALaDB;

@Component
public class OrigenDao
{
	private final String NOMBRE_TABLA = "origenmensaje";
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(OrigenDao.class);
	}

	public ArrayList<Origen> obtenerOrigenes()
	{
		ArrayList<Origen> origenes = new ArrayList<Origen>();
		String query = "select * from " + NOMBRE_TABLA;

		ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		Connection con;
		try
		{
			con = conALaDb.openConBD();
			ResultSet rs = con.createStatement().executeQuery(query);
			
			while(rs.next())
			{
				Origen origen = new Origen();
				origen.establecerDescripccionOrigen(rs.getString("descripcionOrigen"));
				origen.establecerIdOrigen(rs.getInt("idOrigenMensaje"));
				origen.establecerRutaImagen(rs.getString("rutaImagen"));
				origenes.add(origen);
			}

		} 
		catch (Exception e)
		{
			LOG.error("Error", e);
		}
		finally
		{
			try
			{
				conALaDb.closeConBD();
			} 
			catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return origenes;
	}

}
