package com.ncubo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class BitacoraDao {

	private final String NOMBRE_TABLA = "bitacora_de_conversaciones";
	
	private enum atributosDeLaBitacoraDao
	{
		ID_SESION("id_sesion"),
		ID_USARIO("id_usuario"),
		FECHA("fecha"),
		CONVERSACION("conversacion"),
		ES_CONVERSACION_ESPECIFICA("esConversacionEspecifica");
		
		private String nombre;
		atributosDeLaBitacoraDao(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public void insertar(String idSesion, String idUsuarioenBA, String historicoDeLaConversacion, int esConversacionEspecifica) throws ClassNotFoundException, SQLException
	{
		String query = "INSERT INTO "+NOMBRE_TABLA
				 + "("+atributosDeLaBitacoraDao.ID_SESION+", "+atributosDeLaBitacoraDao.ID_USARIO+", "+atributosDeLaBitacoraDao.FECHA+", "+
				atributosDeLaBitacoraDao.CONVERSACION+", "+atributosDeLaBitacoraDao.ES_CONVERSACION_ESPECIFICA+") VALUES (?,?,?,?,?);";

		Connection con = ConexionALaDB.getInstance().openConBD();
		Calendar calendar = Calendar.getInstance();
	    java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idSesion);
		stmt.setString(2, idUsuarioenBA);
		stmt.setTimestamp(3, ourJavaTimestampObject);
		stmt.setBytes(4, historicoDeLaConversacion.getBytes());
		stmt.setInt(5, esConversacionEspecifica);
		
		stmt.executeUpdate();
		
		ConexionALaDB.getInstance().closeConBD();
	}
	
}
