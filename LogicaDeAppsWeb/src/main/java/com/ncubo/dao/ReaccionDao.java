package com.ncubo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.Reaccion;

@Component
public class ReaccionDao
{
	private final String NOMBRE_TABLA = "reaccion";
	
	@Autowired
	private Persistencia dao;
	
	public enum atributo
	{
		ID_OFERTA("idOferta"),
		ID_USUARIO("idUsuario"),
		FECHA("fecha"),
		REACCION("reaccion");
		
		private String nombre;
		atributo(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public void guardar(Reaccion reaccion) throws ClassNotFoundException, SQLException
	{
		Connection con = dao.openConBD();
		String query = String.format("SELECT * FROM %s WHERE %s = ? AND %s = ?;", NOMBRE_TABLA, atributo.ID_OFERTA, atributo.ID_USUARIO);
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, reaccion.getIdOferta());
		stmt.setString(2, reaccion.getIdUsuario());
		ResultSet rs = stmt.executeQuery();
		
		if(rs.next())
		{
			dao.closeConBD();
			modificar(reaccion);
		}
		else
		{
			dao.closeConBD();
			insertar(reaccion);
		}
	}
	
	private void insertar(Reaccion reaccion) throws ClassNotFoundException, SQLException
	{
		Connection con = dao.openConBD();
		String query = String.format("INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?);", NOMBRE_TABLA, atributo.ID_OFERTA, atributo.ID_USUARIO, atributo.REACCION);
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, reaccion.getIdOferta());
		stmt.setString(2, reaccion.getIdUsuario());
		stmt.setBoolean(3, reaccion.getReaccion());
		
		stmt.executeUpdate();
		dao.closeConBD();
	}

	public void modificar(Reaccion reaccion) throws ClassNotFoundException, SQLException
	{
		Connection con = dao.openConBD();
		String query = String.format("UPDATE %s SET %s = ? , %s = ? WHERE %s = ? AND %s = ?;", NOMBRE_TABLA, atributo.REACCION, atributo.FECHA, atributo.ID_OFERTA, atributo.ID_USUARIO);
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setBoolean(1, reaccion.getReaccion());
		stmt.setTimestamp(2, reaccion.getFecha());
		stmt.setInt(3, reaccion.getIdOferta());
		stmt.setString(4, reaccion.getIdUsuario());
		
		stmt.executeUpdate();
		dao.closeConBD();
	}
	
	public void eliminar(Reaccion reaccion) throws ClassNotFoundException, SQLException
	{
		Connection con = dao.openConBD();
		String query = String.format("DELETE FROM %s WHERE %s = ? AND %s = ?;", NOMBRE_TABLA, atributo.ID_OFERTA, atributo.ID_USUARIO);
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, reaccion.getIdOferta());
		stmt.setString(2, reaccion.getIdUsuario());
		
		stmt.executeUpdate();
		dao.closeConBD();
	}
}
