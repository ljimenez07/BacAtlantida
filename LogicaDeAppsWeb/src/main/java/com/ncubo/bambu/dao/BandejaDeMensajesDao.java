package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.Empresa;
import com.ncubo.bambu.data.BandejaDeMensaje;
import com.ncubo.bambu.data.PersonaContacto;
import com.ncubo.bambu.data.Tipo;
import com.ncubo.bambu.data.BandejaDeMensaje.Atributo;
import com.ncubo.bambu.util.Fecha;
import com.ncubo.db.ConexionALaDB;

@Component
public class BandejaDeMensajesDao
{
	private final static String NOMBRE_TABLA_BANDEJA = "bandejamensaje";
	private final static String NOMBRE_TABLA_USUARIO = "usuario";
	private final static String NOMBRE_TABLA_EMPRESA = "empresa";
	
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(BandejaDeMensajesDao.class);
	}

	public ArrayList<BandejaDeMensaje> obtenerMensajes()
	{
		ArrayList<BandejaDeMensaje> mensajes = new ArrayList<BandejaDeMensaje>();
		String query = "select "
				+ "b.idBandejaMensaje, "
				+ "CONCAT(u.Nombre, ' ' ,u.Apellidos) as 'nombreCompletoCreador', "
				+ "u.email as 'emailCreador', "
				+ "CONCAT(u1.Nombre, ' ' ,u1.Apellidos) as 'nombreCompletoEncargado', "
				+ "e.DescEmpresa as 'nombreEmpresa', "
				+ "b.fechaCreacion as 'fechaCreacion', "
				+ "b.fechaModificacion as 'fechaModificacion', " + "e.identificacionDeLaEmpresa as 'color' "
				+ "from " + NOMBRE_TABLA_BANDEJA + " b " 
				+ "left outer join " + NOMBRE_TABLA_EMPRESA + " e on b.idEmpresa = e.idEmpresa "
				+ "left outer join " + NOMBRE_TABLA_USUARIO + " u on b.idUsuarioCreador = u.idUsuario "
				+ "left outer join " + NOMBRE_TABLA_USUARIO + " u1 on u1.idUsuario = b.idUsuarioEncargado "
				+ "order by b.fechaModificacion desc";

		ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		Connection con;
		try
		{
			con = conALaDb.openConBD();
			ResultSet rs = con.createStatement().executeQuery(query);
			
			while (rs.next())
			{
				BandejaDeMensaje mensaje = new BandejaDeMensaje();
				mensaje.establecerIdBandejaMensaje(rs.getInt("idBandejaMensaje"));
				mensaje.establecerNombreCompletoCreador(rs.getString("nombreCompletoCreador"));
				mensaje.establecerEmailCreador(rs.getString("emailCreador"));
				mensaje.establecerNombreCompletoEncargado(rs.getString("nombreCompletoEncargado"));
				mensaje.establecerFechaCreacion(rs.getDate("fechaCreacion"));
				mensaje.establecerFechaModificacion(rs.getDate("fechaModificacion"));

				String fechaCreacion = rs.getString("fechaCreacion");
				String fechaModificacion = rs.getString("fechaModificacion");
				String fechaInclusiva = fechaModificacion != null ? fechaModificacion : fechaCreacion;
				String tiempoTranscurrido = Fecha.calcularTiempoTranscurrido(fechaInclusiva);
				if (tiempoTranscurrido != null)
				{
					mensaje.establecerTiempoTranscurrido(tiempoTranscurrido);
				}

				Empresa empresa = new Empresa();
				empresa.establecerDescripcion(rs.getString("nombreEmpresa"));
				empresa.establecerColor(rs.getString("color"));

				mensaje.establecerEmpresa(empresa);
				mensajes.add(mensaje);
			}

			conALaDb.closeConBD();
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
		return mensajes;
	}
	
	public BandejaDeMensaje obtenerBandeja(int id)
	{
		String query = "select "
				+ "b.idBandejaMensaje, "
				+ "b.idUsuarioCreador, "
				+ "b.idUsuarioEncargado, "
				+ "CONCAT(u.Nombre, ' ' ,u.Apellidos) as 'nombreCompletoCreador', "
				+ "u.email as 'emailCreador', "
				+ "CONCAT(u1.Nombre, ' ' ,u1.Apellidos) as 'nombreCompletoEncargado', "
				+ "e.DescEmpresa as 'nombreEmpresa', "
				+ "b.fechaCreacion as 'fechaCreacion', "
				+ "b.fechaModificacion as 'fechaModificacion', " + "e.identificacionDeLaEmpresa as 'color' "
				+ "from " + NOMBRE_TABLA_BANDEJA + " b " 
				+ "left outer join " + NOMBRE_TABLA_EMPRESA + " e on b.idEmpresa = e.idEmpresa "
				+ "left outer join " + NOMBRE_TABLA_USUARIO + " u on b.idUsuarioCreador = u.idUsuario "
				+ "left outer join " + NOMBRE_TABLA_USUARIO + " u1 on u1.idUsuario = b.idUsuarioEncargado "
				+ "where b.idBandejaMensaje = " + id;

		ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		BandejaDeMensaje bandeja = null;
		try
		{
			Connection con = conALaDb.openConBD();
			ResultSet rs = con.createStatement().executeQuery(query);
	
			rs.next();
			bandeja = new BandejaDeMensaje();
			bandeja.establecerIdBandejaMensaje(rs.getInt("idBandejaMensaje"));
			bandeja.establecerNombreCompletoCreador(rs.getString("nombreCompletoCreador"));
			bandeja.establecerEmailCreador(rs.getString("emailCreador"));
			bandeja.establecerNombreCompletoEncargado(rs.getString("nombreCompletoEncargado"));
			bandeja.establecerFechaCreacion(rs.getDate("fechaCreacion"));
			bandeja.establecerFechaModificacion(rs.getDate("fechaModificacion"));
			bandeja.establecerIdUsuarioCreador(rs.getInt("idUsuarioCreador"));
			bandeja.establecerIdUsuarioEncargado(rs.getInt("idUsuarioEncargado"));
	
			String fechaCreacion = rs.getString("fechaCreacion");
			String fechaModificacion = rs.getString("fechaModificacion");
			String fechaInclusiva = fechaModificacion != null ? fechaModificacion : fechaCreacion;
			String tiempoTranscurrido = Fecha.calcularTiempoTranscurrido(fechaInclusiva);
			if (tiempoTranscurrido != null)
			{
				bandeja.establecerTiempoTranscurrido(tiempoTranscurrido);
			}
	
			Empresa empresa = new Empresa();
			empresa.establecerDescripcion(rs.getString("nombreEmpresa"));
			empresa.establecerColor(rs.getString("color"));
			
			int idUsuarioCreador = rs.getInt("idUsuarioCreador");
			PersonaContactoDao contactoDao = new PersonaContactoDao();
			boolean existeContacto = contactoDao.existe(idUsuarioCreador);
			if(existeContacto)
			{
				PersonaContacto contacto = contactoDao.obtener(idUsuarioCreador);
				bandeja.establecerContacto(contacto);
			}
			
			bandeja.establecerEmpresa(empresa);
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
			} 
			catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return bandeja;
	}
	
	public boolean actualizar(HashMap<Atributo, Object> atributos, HashMap<Atributo, Object> where)
	{
		boolean actualizado = false;
		String query = "UPDATE " + NOMBRE_TABLA_BANDEJA + " SET";
		int contador = 0;
		for(Atributo atributo : atributos.keySet())
		{
			query += " " + atributo.obtenerNombre() + " = " +  agregarComillas(atributo, atributos.get(atributo));
			contador++;
			if (contador < atributos.size())
			{
				query += ",";
			}
		}
		query += " WHERE";
		contador = 0;
		for(Atributo atributo : where.keySet())
		{
			query += " " + atributo.obtenerNombre() + " = " +  agregarComillas(atributo, where.get(atributo));
			contador++;
			if (contador < where.size())
			{
				query += " &&";
			}
		}
		
		System.out.println(query);
		ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		try
		{
			final Connection con = conALaDb.openConBD();
			final Statement statement = con.createStatement();
			final int result = statement.executeUpdate(query);
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
				conALaDb.closeConBD();
			}
			catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return actualizado;
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
