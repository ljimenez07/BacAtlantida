package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.TokenDeAcceso;

@Component
public class TokenDao
{
	private final static String NOMBRE_TABLA = "tokenblacklist";
	private final static Logger LOG;
	
	@Autowired
	private PersistenciaBambu dao;
	
	static
	{
		LOG = Logger.getLogger(TokenDao.class);
	}
	
	private enum atributo
	{
		TOKEN("token"),
		EXPIRADO("expirado"),
		FECHA_EMISION("fechaEmision"),
		FECHA_EXPIRACION("fechaExpiracion"),
		VECES_UTILIZADO("vecesUtilizado"),
		CANTIDAD_MAXIMA_DE_USOS("cantidadMaximaDeUsos");
		
		private String nombre;
		
		private atributo(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public boolean existe(String token)
	{
		final String query = "SELECT " + atributo.TOKEN.toString()
				+ " FROM " + NOMBRE_TABLA
				+ " WHERE " + atributo.TOKEN.toString() + " = ?";

		boolean existe = false;
		try
		{
			final Connection con = dao.openConBD();
			final PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, token);
			final ResultSet rs = ps.executeQuery();
			
			existe = rs.next();
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
		return existe;
	}
	
	public boolean guardar(String token, Timestamp fechaEmision, Timestamp fechaExpiracion, int numeroMaximoDePeticiones)
	{
		String query = "INSERT INTO " + NOMBRE_TABLA
				+ " (" + atributo.TOKEN.toString() + ","
				+ atributo.FECHA_EMISION.toString() + ","
				+ atributo.FECHA_EXPIRACION.toString() + ","
				+ atributo.CANTIDAD_MAXIMA_DE_USOS.toString() + ") "
				+ "VALUES (?, ?, ?, ?)";
		
		boolean guardado = false;
		try
		{
			final Connection con = dao.openConBD();
			final PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, token);
			ps.setString(2, fechaEmision.toString());
			ps.setString(3, fechaExpiracion.toString());
			ps.setInt(4, numeroMaximoDePeticiones);
			final int result = ps.executeUpdate();
			guardado = result >= 1;
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
		return guardado;
	}

	public TokenDeAcceso obtener(String token)
	{
		final String query = "SELECT * FROM " + NOMBRE_TABLA
		+ " WHERE " + atributo.TOKEN.toString() + " = ?";

		TokenDeAcceso tokenDeAcceso = null;
		try
		{
			final Connection con = dao.openConBD();
			final PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, token);
			final ResultSet rs = ps.executeQuery();
			
			tokenDeAcceso = new TokenDeAcceso();
			if(rs.next())
			{
				tokenDeAcceso.establecerCantidadMaximaDeUsos(rs.getInt(atributo.CANTIDAD_MAXIMA_DE_USOS.toString()));
				tokenDeAcceso.establecerExpirado(rs.getBoolean(atributo.EXPIRADO.toString()));
				tokenDeAcceso.establecerFechaEmision(rs.getDate(atributo.FECHA_EMISION.toString()));
				tokenDeAcceso.establecerFechaExpiracion(rs.getDate(atributo.FECHA_EXPIRACION.toString()));
				tokenDeAcceso.establecerToken(rs.getString(atributo.TOKEN.toString()));
				tokenDeAcceso.establecerVecesUtilizado(rs.getInt(atributo.VECES_UTILIZADO.toString()));
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
			}
			catch(Exception e)
			{
				LOG.error("Error", e);
			}
		}
		return tokenDeAcceso;
	}

	public boolean aumentarVecesUsado(String token)
	{
		String query = "UPDATE " + NOMBRE_TABLA
				+ " SET " + atributo.VECES_UTILIZADO.toString() + " = (" + atributo.VECES_UTILIZADO + " + 1)"
				+ " WHERE " + atributo.TOKEN.toString() + " = ?";
		
		boolean actualizado = false;
		try
		{
			final Connection con = dao.openConBD();
			final PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, token);
			final int result = ps.executeUpdate();
			actualizado = result >= 1;
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
		return actualizado;
	}
}
