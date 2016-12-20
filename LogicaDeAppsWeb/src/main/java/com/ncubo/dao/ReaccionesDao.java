package com.ncubo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.Reacciones;

@Component
public class ReaccionesDao 
{
	ArrayList<Reacciones> reacciones;
	private final String QUERY_LIKES_POR_OFERTA = "SELECT  oferta.tituloDeOferta"
												+ " AS tituloLabel, count(*) as meGusta FROM oferta "
												+ "JOIN reaccion WHERE oferta.idOferta = reaccion.idOferta "
												+ "AND reaccion='1' "
												+ "AND oferta.estado = 1 "
												+ "AND oferta.eliminada = 0 "
												+ "AND  reaccion.fecha BETWEEN ? AND ? "
												+ "AND oferta.vigenciaHasta >= ? "
												+ "GROUP BY oferta.tituloDeOferta;";
	
	private final String QUERY_LIKES_POR_CATEGORIA = "SELECT categoriadeoferta.nombre "
													+ "AS tituloLabel, count(peso) AS meGusta "
													+ "FROM reaccion JOIN oferta on oferta.idOferta = reaccion.idOferta "
													+ "JOIN  categoria_con_oferta_y_peso on categoria_con_oferta_y_peso.idOferta = oferta.idOferta "
													+ "JOIN  categoriadeoferta on categoriadeoferta.id = categoria_con_oferta_y_peso.idCategoria "
													+ "AND reaccion='1' "
													+ "AND oferta.estado=1 "
													+ "AND oferta.eliminada=0 "
													+ "AND fecha BETWEEN ? AND ? "
													+ "AND oferta.vigenciaHasta >= ? "
													+ "AND peso > 0 "
													+ "GROUP BY categoriadeoferta.nombre;";
	
	private final String QUERY_DISLIKES_POR_OFERTA = "SELECT  oferta.tituloDeOferta "
													+ "AS tituloLabelDislike, count(*) as noMegusta FROM oferta "
													+ "JOIN reaccion WHERE oferta.idOferta = reaccion.idOferta "
													+ "AND reaccion='0' "
													+ "AND oferta.estado=1 "
													+ "AND oferta.eliminada = 0 "
													+ "AND  reaccion.fecha BETWEEN ? AND ? "
													+ "AND oferta.vigenciaHasta >= ? "
													+ "GROUP BY oferta.tituloDeOferta;";
	
	private String QUERY_DISLIKES_POR_CATEGORIA = "SELECT categoriadeoferta.nombre "
													+ "AS tituloLabelDislike, count(peso) AS noMegusta "
													+ "FROM reaccion JOIN oferta on oferta.idOferta = reaccion.idOferta "
													+ "JOIN  categoria_con_oferta_y_peso on categoria_con_oferta_y_peso.idOferta = oferta.idOferta "
													+ "JOIN  categoriadeoferta on categoriadeoferta.id = categoria_con_oferta_y_peso.idCategoria "
													+ "AND reaccion='0' "
													+ "AND oferta.estado=1 "
													+ "AND oferta.eliminada = 0 "
													+ "AND peso > 0 "
													+ "AND fecha BETWEEN ? AND ? "
													+ "AND oferta.vigenciaHasta >= ? "
													+ "GROUP BY categoriadeoferta.nombre;";
	
	@Autowired
	private Persistencia dao;
	
	public enum atributo
	{
		ID_OFERTA("idOferta"),
		ID_USUARIO("idUsuario"),
		FECHA("fecha"),
		REACCION("reaccion"),
		CANTIDAD_LIKE("cantidad_like"),
		CANTIDAD_DISKLIKE("cantidad_dislike");
		
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
	
	public ArrayList<Reacciones> obtener(String desde, String hasta, String filtro) throws ClassNotFoundException, SQLException, ParseException
	{	
		Connection connection = dao.openConBD();
		PreparedStatement preStatementLikes = null;
		PreparedStatement preStatementDisLikes = null;
		reacciones = new ArrayList<Reacciones>();
		
		if("oferta".equalsIgnoreCase(filtro))
		{
			preStatementLikes = connection.prepareStatement(QUERY_LIKES_POR_OFERTA);
			ejecutarComandosParaObtenerDatosMegusta(desde, hasta, preStatementLikes);
			preStatementDisLikes = connection.prepareStatement(QUERY_DISLIKES_POR_OFERTA);
			ejecutarComandosParaObtenerDatosNoMegusta(desde, hasta, preStatementDisLikes);
		}
		else if("categoria".equalsIgnoreCase(filtro))
		{
			preStatementLikes = connection.prepareStatement(QUERY_LIKES_POR_CATEGORIA);
			ejecutarComandosParaObtenerDatosMegusta(desde, hasta, preStatementLikes);
			preStatementDisLikes = connection.prepareStatement(QUERY_DISLIKES_POR_CATEGORIA);
			ejecutarComandosParaObtenerDatosNoMegusta(desde, hasta, preStatementDisLikes);
		}
		
		return reacciones;
	}

	private void ejecutarComandosParaObtenerDatosMegusta(String desde, String hasta, PreparedStatement preparedStatement) throws SQLException {
		preparedStatement.setString(1, desde);
		preparedStatement.setString(2, hasta);
		preparedStatement.setString(3, hasta);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{	
			Reacciones reaccion = new Reacciones();
			reaccion.setCantidadLikes(resultSet.getInt("meGusta"));
			reaccion.setTituloLabel(resultSet.getString("tituloLabel"));
			reacciones.add(reaccion);
		}
	}
	
	private void ejecutarComandosParaObtenerDatosNoMegusta(String desde, String hasta, PreparedStatement preparedStatement)throws SQLException
	{
		preparedStatement.setString(1, desde);
		preparedStatement.setString(2, hasta);
		preparedStatement.setString(3, hasta);
		
		
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			Reacciones reaccion = new Reacciones();
			reaccion.setCantidadDislikes(resultSet.getInt("noMegusta"));
			reaccion.setTituloLabelDislike(resultSet.getString("tituloLabelDislike"));
			reacciones.add(reaccion);
		}
	}
}
