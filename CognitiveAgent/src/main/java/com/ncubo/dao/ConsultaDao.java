package com.ncubo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.Consulta;

@Component
public class ConsultaDao
{
	@Autowired
	private Persistencia dao;
	private final String NOMBRE_TABLA = "consultas";
	
	private enum atributo
	{
		INTENT("intent"),
		FECHA("fecha"),
		DESCRIPCION("descripcion"),
		VECES_CONSULTADO("vecesConsultado"),
		TOTAL_CONSULTADO("TotalConsultado");
		
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
	
	public ArrayList<Consulta> obtener() throws ClassNotFoundException, SQLException
	{
		ArrayList<Consulta> consultas = new ArrayList<Consulta>();
		String query = "SELECT " 
				+ atributo.INTENT + ", "
				+ atributo.FECHA + ", "
				+ atributo.DESCRIPCION + ", "
				+ atributo.VECES_CONSULTADO
				+ " FROM " + NOMBRE_TABLA + " ;";

		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			consultas.add(new Consulta(
					rs.getString(atributo.INTENT.toString()),
					rs.getTimestamp(atributo.FECHA.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					rs.getInt(atributo.VECES_CONSULTADO.toString())
				));
		}
		
		dao.closeConBD();
		return consultas;
	}
	
	public ArrayList<Consulta> obtener(String fechaDesde, String fechaHasta) throws ClassNotFoundException, SQLException
	{
		ArrayList<Consulta> consultas = new ArrayList<Consulta>();
		String query = "SELECT " 
				+ atributo.INTENT + ", "
				+ atributo.DESCRIPCION + ", "
				+ "SUM(" + atributo.VECES_CONSULTADO + ") as '" + atributo.TOTAL_CONSULTADO + "'"
				+ " FROM " + NOMBRE_TABLA
				+ " WHERE " + atributo.FECHA + " BETWEEN '" + fechaDesde + "' AND '" + fechaHasta +"'"
				+ " group by " + atributo.INTENT + " , "
				+  atributo.DESCRIPCION + " ;";

		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			consultas.add(new Consulta(
					rs.getString(atributo.INTENT.toString()),
					null,
					rs.getString(atributo.DESCRIPCION.toString()),
					rs.getInt(atributo.TOTAL_CONSULTADO.toString())
				));
		}
		
		dao.closeConBD();
		return consultas;
	}
	
	public void insertar(Consulta consulta) throws ClassNotFoundException, SQLException
	{
		String queryDatos = "'" + consulta.getIntent()+ "'"
				+ ",'" + consulta.getFecha() + "'"
				+ ",'" + consulta.getDescripcion() + "'"
				+ ",'" + consulta.getVecesConsultado() + "'";
		String query = "INSERT INTO " + NOMBRE_TABLA
				 + "(" + atributo.INTENT + ","
				 + atributo.FECHA + ","
				 + atributo.DESCRIPCION + ","
				 + atributo.VECES_CONSULTADO + ")"
				 + " VALUES (" + queryDatos + ") "
				 + " ON DUPLICATE KEY UPDATE " + atributo.VECES_CONSULTADO + " = " +atributo.VECES_CONSULTADO + " + 1";
		
		Connection con = dao.openConBD();
		con.createStatement().execute(query);
		dao.closeConBD();
	}

}