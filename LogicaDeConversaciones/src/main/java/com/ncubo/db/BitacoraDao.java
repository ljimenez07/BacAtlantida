package com.ncubo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.mysql.jdbc.Blob;

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
	
	public void insertar(String idSesion, String idUsuarioenBA, String historicoDeLaConversacion, int esConversacionEspecifica) throws ClassNotFoundException, SQLException{
		
		// insert into bitacora_de_conversaciones (id_sesion, id_usuario, fecha, conversacion, esConversacionEspecifica) values ("1234", "1234", now(), "", 0);
		String query = "INSERT INTO "+NOMBRE_TABLA
				 + "("+atributosDeLaBitacoraDao.ID_SESION+", "+atributosDeLaBitacoraDao.ID_USARIO+", "+atributosDeLaBitacoraDao.FECHA+", "+
				atributosDeLaBitacoraDao.CONVERSACION+", "+atributosDeLaBitacoraDao.ES_CONVERSACION_ESPECIFICA+") VALUES (?,?,?,?,?);";

		Connection con = ConexionALaDB.getInstance().openConBD();
		Calendar calendar = Calendar.getInstance();
	    java.sql.Timestamp miFechaActual = new java.sql.Timestamp(calendar.getTime().getTime());

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idSesion);
		stmt.setString(2, idUsuarioenBA);
		stmt.setTimestamp(3, miFechaActual);
		stmt.setBytes(4, historicoDeLaConversacion.getBytes());
		stmt.setInt(5, esConversacionEspecifica);
		
		stmt.executeUpdate();
		
		ConexionALaDB.getInstance().closeConBD();
	}
	
	public String buscarUnaConversacion(String idSesion, String fecha, int esConversacionEspecifica) throws ClassNotFoundException, SQLException{
		String resultado = "";
		
		String query = "select "+atributosDeLaBitacoraDao.CONVERSACION+" from "+NOMBRE_TABLA+" where "+
				atributosDeLaBitacoraDao.ID_SESION+" = ? and "+atributosDeLaBitacoraDao.FECHA+" = ? and "+atributosDeLaBitacoraDao.ES_CONVERSACION_ESPECIFICA+" = ?;";
		
		Connection con = ConexionALaDB.getInstance().openConBD();
		PreparedStatement stmt = con.prepareStatement(query);
		
		stmt.setString(1, idSesion);
		stmt.setString(2, fecha);
		stmt.setInt(3, esConversacionEspecifica);
		
		ResultSet rs = stmt.executeQuery();
		
		if( rs.next() )
		{
			byte[] blob = rs.getBytes(atributosDeLaBitacoraDao.CONVERSACION.toString());
			resultado = new String(blob);
		}
		
		return resultado;
	}
	
}
