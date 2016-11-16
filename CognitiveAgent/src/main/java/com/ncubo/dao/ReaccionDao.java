package com.ncubo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.Reaccion;

@Component
public class ReaccionDao 
{
	ArrayList<Reaccion> reacciones;
	private final String QUERY_LIKES_POR_OFERTA = "SELECT distinct oferta.tituloDeOferta AS tituloLabel, count(*) as meGusta FROM oferta JOIN reaccion WHERE oferta.idOferta = reaccion.idOferta AND reaccion='1' AND  reaccion.fecha BETWEEN ? AND ? GROUP BY oferta.tituloDeOferta;";
	private final String QUERY_LIKES_POR_CATEGORIA = "SELECT categoriaoferta.nombre AS tituloLabel, count(*) AS meGusta FROM categoriaoferta JOIN oferta,reaccion WHERE categoriaoferta.idCategoriaOferta = oferta.categoria AND reaccion='1' AND oferta.idOferta = reaccion.idOferta AND fecha BETWEEN ? AND ? GROUP BY categoriaoferta.nombre;";
	private final String QUERY_DISLIKES_POR_OFERTA = "SELECT distinct oferta.tituloDeOferta AS tituloLabelDislike, count(*) as noMegusta FROM oferta JOIN reaccion WHERE oferta.idOferta = reaccion.idOferta AND reaccion='0' AND  reaccion.fecha BETWEEN ? AND ? GROUP BY oferta.tituloDeOferta;";
	private String QUERY_DISLIKES_POR_CATEGORIA = "SELECT categoriaoferta.nombre AS tituloLabelDislike, count(*) AS noMegusta FROM categoriaoferta JOIN oferta,reaccion WHERE categoriaoferta.idCategoriaOferta = oferta.categoria AND reaccion='0' AND oferta.idOferta = reaccion.idOferta AND fecha BETWEEN ? AND ? GROUP BY categoriaoferta.nombre;";
	
	@Autowired
	private Persistencia dao;
	
	public enum atributo
	{
		ID_OFERTA("id_oferta"),
		FECHA("fecha"),
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
	
	public ArrayList<Reaccion> obtener(String desde, String hasta, String filtro) throws ClassNotFoundException, SQLException, ParseException
	{	
		Connection connection = dao.openConBD();
		PreparedStatement preStatementLikes = null;
		PreparedStatement preStatementDisLikes = null;
		reacciones = new ArrayList<Reaccion>();
		
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

		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{	
			Reaccion reaccion = new Reaccion();
			reaccion.setCantidad_like(resultSet.getInt("meGusta"));
			reaccion.setTituloLabel(resultSet.getString("tituloLabel"));
			reacciones.add(reaccion);
		}

	}
	
	private void ejecutarComandosParaObtenerDatosNoMegusta(String desde, String hasta, PreparedStatement preparedStatement)throws SQLException
	{
		preparedStatement.setString(1, desde);
		preparedStatement.setString(2, hasta);

		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next())
		{
			Reaccion reaccion = new Reaccion();
			reaccion.setCantidad_dislike(resultSet.getInt("noMegusta"));
			reaccion.setTituloLabelDislike(resultSet.getString("tituloLabelDislike"));
			reacciones.add(reaccion);
		}
		

	}
}
