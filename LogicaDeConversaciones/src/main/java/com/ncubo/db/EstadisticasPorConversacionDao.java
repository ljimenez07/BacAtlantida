package com.ncubo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EstadisticasPorConversacionDao {

private final String NOMBRE_TABLA = "estadisticas_por_conversacion";
	
	private enum atributosDeLasEstadisticasPorConversacionDao
	{
		ID_TEMA("idTema"),
		ID_CONVERSACION("idConversacion");
		
		private String nombre;
		atributosDeLasEstadisticasPorConversacionDao(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public void insertar(String idTema, String idConversacion) throws ClassNotFoundException, SQLException{
		
		String query = "INSERT INTO "+NOMBRE_TABLA
				 + "("+atributosDeLasEstadisticasPorConversacionDao.ID_TEMA+", "+atributosDeLasEstadisticasPorConversacionDao.ID_CONVERSACION+") VALUES (?,?);";

		Connection con = ConexionALaDB.getInstance().openConBD();

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idTema);
		stmt.setString(2, idConversacion);
		
		stmt.executeUpdate();
		
		ConexionALaDB.getInstance().closeConBD();
	}
	
	public ArrayList<String> buscarConversacionesQueNoHanSidoVerificadasPorTemas(String idTema) throws ClassNotFoundException, SQLException{
		
		// TODO Hay que mejorar esto con un join
		ArrayList<String> consultas = new ArrayList<String>();
		String query = "SELECT " + atributosDeLasEstadisticasPorConversacionDao.ID_CONVERSACION + " from "
				+ NOMBRE_TABLA +" where idTema=? ;";

		Connection con = ConexionALaDB.getInstance().openConBD();
		
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idTema);
		
		ResultSet rs = stmt.executeQuery(query);
		
		while (rs.next()){
			consultas.add(rs.getString(atributosDeLasEstadisticasPorConversacionDao.ID_CONVERSACION.toString()));
		}
		
		ConexionALaDB.getInstance().closeConBD();
		return consultas;
	}
	
}
