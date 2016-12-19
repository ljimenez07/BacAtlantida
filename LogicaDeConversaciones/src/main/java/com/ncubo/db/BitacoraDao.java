package com.ncubo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.mysql.jdbc.Blob;
import com.ncubo.db.EstadisticasPorConversacionDao.atributosDeLasEstadisticasPorConversacionDao;

public class BitacoraDao {

	private final String NOMBRE_TABLA_BITACORA = "bitacora_de_conversaciones";
	private final String NOMBRE_TABLA_ESTADISTICAS_CONVERSACION= "estadisticas_por_conversacion";
	
	public enum atributosDeLaBitacoraDao
	{
		ID("id"),
		ID_SESION("id_sesion"),
		ID_USARIO("id_usuario"),
		FECHA("fecha"),
		CONVERSACION("conversacion"),
		HA_SIDO_VERIFICADO("haSidoVerificado");
		
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
	
	public void insertar(String idSesion, String idUsuarioenBA, String historicoDeLaConversacion) throws ClassNotFoundException, SQLException{
		
		// insert into bitacora_de_conversaciones (id_sesion, id_usuario, fecha, conversacion, esConversacionEspecifica) values ("1234", "1234", now(), "", 0);
		String query = "INSERT INTO "+NOMBRE_TABLA_BITACORA
				 + "("+atributosDeLaBitacoraDao.ID_SESION+", "+atributosDeLaBitacoraDao.ID_USARIO+", "+atributosDeLaBitacoraDao.FECHA+", "+
				atributosDeLaBitacoraDao.CONVERSACION+") VALUES (?,?,?,?);";

		Connection con = ConexionALaDB.getInstance().openConBD();
		Calendar calendar = Calendar.getInstance();
	    java.sql.Timestamp miFechaActual = new java.sql.Timestamp(calendar.getTime().getTime());

		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idSesion);
		stmt.setString(2, idUsuarioenBA);
		stmt.setTimestamp(3, miFechaActual);
		stmt.setBytes(4, historicoDeLaConversacion.getBytes());
		
		stmt.executeUpdate();
		
		ConexionALaDB.getInstance().closeConBD();
	}
	
	public String buscarUnaConversacion(String idSesion, String fecha) throws ClassNotFoundException, SQLException{
		String resultado = "";
		
		String query = "select "+atributosDeLaBitacoraDao.CONVERSACION+" from "+NOMBRE_TABLA_BITACORA+" where "+
				atributosDeLaBitacoraDao.ID_SESION+" = ? and "+atributosDeLaBitacoraDao.FECHA+" = ?;";
		
		Connection con = ConexionALaDB.getInstance().openConBD();
		PreparedStatement stmt = con.prepareStatement(query);
		
		stmt.setString(1, idSesion);
		stmt.setString(2, fecha);
		
		ResultSet rs = stmt.executeQuery();
		
		if( rs.next() )
		{
			byte[] blob = rs.getBytes(atributosDeLaBitacoraDao.CONVERSACION.toString());
			resultado = new String(blob);
		}
		
		return resultado;
	}
	
	private int obtenerIdDeLaBitacora(String idTema, String fecha, String idUsuario) throws ClassNotFoundException, SQLException{
		int resultado = 0;
		
		// select bitacora_de_conversaciones.id from estadisticas_por_conversacion join bitacora_de_conversaciones where 
		// estadisticas_por_conversacion.idConversacion = bitacora_de_conversaciones.id_sesion and estadisticas_por_conversacion.idTema = 'quiereTasaDeCambio' 
		// and bitacora_de_conversaciones.fecha = '2016-12-15 16:07:49' and bitacora_de_conversaciones.esConversacionEspecifica = 0 
		//and bitacora_de_conversaciones.haSidoVerificado = 0;
		
		String query = String.format("select %s.%s from %s join %s where %s.%s = %s.%s and %s.%s = ? and %s.%s = ? and %s.%s = ? and %s.%s = 0;", 
				NOMBRE_TABLA_BITACORA, atributosDeLaBitacoraDao.ID, NOMBRE_TABLA_ESTADISTICAS_CONVERSACION, NOMBRE_TABLA_BITACORA, NOMBRE_TABLA_ESTADISTICAS_CONVERSACION, 
				"idConversacion", NOMBRE_TABLA_BITACORA, atributosDeLaBitacoraDao.ID_SESION, NOMBRE_TABLA_ESTADISTICAS_CONVERSACION, atributosDeLasEstadisticasPorConversacionDao.ID_TEMA, NOMBRE_TABLA_BITACORA, 
				atributosDeLaBitacoraDao.FECHA, 
				NOMBRE_TABLA_BITACORA, atributosDeLaBitacoraDao.HA_SIDO_VERIFICADO);
		
		Connection con = ConexionALaDB.getInstance().openConBD();
		PreparedStatement stmt = con.prepareStatement(query);
		
		stmt.setString(1, idTema);
		stmt.setString(2, fecha);
		
		ResultSet rs = stmt.executeQuery();
		
		if( rs.next() ){
			resultado = rs.getInt(atributosDeLaBitacoraDao.ID.toString());
		}
		
		return resultado;
	}
	
	public void cambiarDeEstadoAVerificadoDeLaConversacion(String idTema, String fecha, String idUsuario) throws ClassNotFoundException, SQLException{
		// update bitacora_de_conversaciones set haSidoVerificado = 1 where id = 126;
		
		int idBitacoraConversacion = obtenerIdDeLaBitacora(idTema, fecha, idUsuario);
		String query = String.format("update %s set %s = 1 where id = ?;", NOMBRE_TABLA_BITACORA, atributosDeLaBitacoraDao.HA_SIDO_VERIFICADO);
		
		Connection con = ConexionALaDB.getInstance().openConBD();
		
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, idBitacoraConversacion);
		stmt.executeUpdate();
		
		ConexionALaDB.getInstance().closeConBD();
	}
}
