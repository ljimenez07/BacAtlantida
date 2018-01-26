package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.Origen;

@Component
public class OrigenDao
{
	private final String NOMBRE_TABLA = "origenmensaje";
	private final static Logger LOG;
	
	@Autowired
	private PersistenciaBambu dao;
	
	static
	{
		LOG = Logger.getLogger(OrigenDao.class);
	}

	public ArrayList<Origen> obtenerOrigenes()
	{
		ArrayList<Origen> origenes = new ArrayList<Origen>();
		String query = "select * from " + NOMBRE_TABLA;

		Connection con;
		try
		{
			con = dao.openConBD();
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
				dao.closeConBD();
			} 
			catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return origenes;
	}

}
