package com.ncubo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Oferta;
import com.ncubo.util.LevenshteinDistance;

@Component
public class OfertaDao
{
	private final String NOMBRE_TABLA = "oferta";
	private final String NOMBRE_TABLA_CATEGORIA_OFERTA = "categoriaoferta";
	private final String NOMBRE_TABLA_REACCION = "reaccion";
	@Autowired
	private Persistencia dao;
	
	public enum atributo
	{
		ID_OFERTA("idOferta"),
		TITULO_DE_OFERTA("tituloDeOferta"),
		COMERCIO("comercio"),
		DESCRIPCION("descripcion"),
		CATEGORIA("categoria"),
		
		ID_CATEGORIA("idCategoriaOferta"),
		NOMBRE_CATEGORIA("nombre"),
		
		CIUDAD("ciudad"),
		ESTADO("estado"),
		RESTRICCIONES("restricciones"),
		VIGENCIA_DESDE("vigenciaDesde"),
		VIGENCIA_HASTA("vigenciaHasta"),
		
		IMAGEN_COMERCIO_PATH("imagenComercioPath"),
		IMAGEN_PUBLICIDAD_PATH("imagenPublicidadPath"),
		FECHA_HORA_REGISTRO("fechaHoraRegistro"),
		
		ELIMINADA("eliminada"),
		ID_USUARIO("idUsuario"),
		REACCION("reaccion"),
		LIKES("likes"),
		DISLIKES("dislikes");
		
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
	
	public ArrayList<Oferta> obtener() throws ClassNotFoundException, SQLException
	{ //TODO dalaian no deneria sacar el universo de ofertas.
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		String query = "SELECT " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + ", "
				+ atributo.TITULO_DE_OFERTA + ", "
				+ atributo.COMERCIO + ", "
				+ atributo.DESCRIPCION + ", "
				+ atributo.CATEGORIA + ", "
				+ atributo.NOMBRE_CATEGORIA + ", "
				+ atributo.CIUDAD + ", "
				+ atributo.ESTADO + ", "
				+ atributo.RESTRICCIONES + ", "
				+ atributo.VIGENCIA_DESDE + ", "
				+ atributo.VIGENCIA_HASTA + ", "
				+ atributo.IMAGEN_COMERCIO_PATH + ", "
				+ atributo.IMAGEN_PUBLICIDAD_PATH + ", "
				+ atributo.FECHA_HORA_REGISTRO
				+ ", SUM(IF(" + atributo.REACCION + " = 1, 1, 0)) AS " + atributo.LIKES
				+ ", SUM(IF(" + atributo.REACCION + " = 0, 1, 0)) AS " + atributo.DISLIKES
				+ " FROM " + NOMBRE_TABLA_CATEGORIA_OFERTA + ", " + NOMBRE_TABLA
				+ " LEFT JOIN " + NOMBRE_TABLA_REACCION + " ON " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + " = " + NOMBRE_TABLA_REACCION + ".idOferta"
				+ " WHERE " + atributo.ELIMINADA + " = 0"
				+ " AND " + atributo.CATEGORIA + " = " + atributo.ID_CATEGORIA
				+ " GROUP BY " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + ";";

		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			ofertas.add(new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					new CategoriaOferta(rs.getInt(atributo.CATEGORIA.toString()), rs.getString(atributo.NOMBRE_CATEGORIA.toString())),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getString(atributo.VIGENCIA_DESDE.toString()),
					rs.getString(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					));
		}
		
		dao.closeConBD();
		return ofertas;
	}
	
	public void insertar(Oferta oferta) throws ClassNotFoundException, SQLException
	{
		String queryDatos = "'" + oferta.getTituloDeOferta()+ "'"
							+ ",'" + oferta.getComercio() + "'"
							+ ",'" + oferta.getDescripcion() + "'"
							+ ",'" + oferta.getCategoria().getId() + "'"
							+ ",'" + oferta.getCiudad() + "'"
							+ "," + (oferta.getEstado() ? 1 : 0)
							+ ",'" + oferta.getRestricciones() + "'"
							+ ",'" + oferta.getVigenciaDesde() + "'"
							+ ",'" + oferta.getVigenciaHasta() + "'"
							+ ",'" + oferta.getImagenComercioPath() + "'"
							+ ",'" + oferta.getImagenPublicidadPath() + "'"
							+ ",'" + oferta.getFechaHoraRegistro() + "'";
		String query = "INSERT INTO " + NOMBRE_TABLA
					 + "(" + atributo.TITULO_DE_OFERTA + ","
					 + atributo.COMERCIO + ","
					 + atributo.DESCRIPCION + ","
					 + atributo.CATEGORIA + ","
					 + atributo.CIUDAD + ","
					 + atributo.ESTADO + ","
					 + atributo.RESTRICCIONES + ","
					 + atributo.VIGENCIA_DESDE + ","
					 + atributo.VIGENCIA_HASTA + ","
					 + atributo.IMAGEN_COMERCIO_PATH + ","
					 + atributo.IMAGEN_PUBLICIDAD_PATH + ","
					 + atributo.FECHA_HORA_REGISTRO + ")"
					 + " VALUES (" + queryDatos + ");";


		Connection con = dao.openConBD();
		con.createStatement().execute(query);
		dao.closeConBD();
	}
	
	public ArrayList<Oferta> ultimasOfertas() throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		String query = "SELECT " + atributo.ID_OFERTA + ", "
				+ atributo.TITULO_DE_OFERTA + ", "
				+ atributo.COMERCIO + ", "
				+ atributo.DESCRIPCION + ", "
				+ atributo.CATEGORIA + ", "
				+ atributo.NOMBRE_CATEGORIA + ", "
				+ atributo.CIUDAD + ", "
				+ atributo.ESTADO + ", "
				+ atributo.RESTRICCIONES + ", "
				+ atributo.VIGENCIA_DESDE + ", "
				+ atributo.VIGENCIA_HASTA + ", "
				+ atributo.IMAGEN_COMERCIO_PATH + ", "
				+ atributo.IMAGEN_PUBLICIDAD_PATH + ", "
				+ atributo.FECHA_HORA_REGISTRO
				+ " FROM " + NOMBRE_TABLA + ", " + NOMBRE_TABLA_CATEGORIA_OFERTA 
				+ " WHERE " + atributo.ELIMINADA + " = 0"
				+ " AND " + atributo.CATEGORIA + " = " + atributo.ID_CATEGORIA
				+ " ORDER BY " + atributo.FECHA_HORA_REGISTRO + " DESC"
				+ " LIMIT 10;";
		
		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			ofertas.add(new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					new CategoriaOferta(rs.getInt(atributo.CATEGORIA.toString()), rs.getString(atributo.NOMBRE_CATEGORIA.toString())),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getString(atributo.VIGENCIA_DESDE.toString()),
					rs.getString(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					));
		}
		
		dao.closeConBD();
		return ofertas;
	}

	public List<Oferta> ultimasDiezOfertasDesde(int indiceInicial, String idUsuario) throws ClassNotFoundException, SQLException
	{
		if(idUsuario == null)
		{
			idUsuario = "NULL";
		}
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		String query = "SELECT " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + ", "
				+ atributo.TITULO_DE_OFERTA + ", "
				+ atributo.COMERCIO + ", "
				+ atributo.DESCRIPCION + ", "
				+ atributo.CATEGORIA + ", "
				+ atributo.NOMBRE_CATEGORIA + ", "
				+ atributo.CIUDAD + ", "
				+ atributo.ESTADO + ", "
				+ atributo.RESTRICCIONES + ", "
				+ atributo.VIGENCIA_DESDE + ", "
				+ atributo.VIGENCIA_HASTA + ", "
				+ atributo.IMAGEN_COMERCIO_PATH + ", "
				+ atributo.IMAGEN_PUBLICIDAD_PATH + ", "
				+ atributo.FECHA_HORA_REGISTRO
				+ ", IF(" + atributo.ID_USUARIO + " = '" + idUsuario + "', IF(" + atributo.REACCION + " = 1, 1, NULL), NULL) AS " + atributo.LIKES
				+ ", IF(" + atributo.ID_USUARIO + " = '" + idUsuario + "', IF(" + atributo.REACCION + " = 0, 1, NULL), NULL) AS " + atributo.DISLIKES
				+ " FROM " + NOMBRE_TABLA_CATEGORIA_OFERTA + ", " + NOMBRE_TABLA
				+ " LEFT JOIN " + NOMBRE_TABLA_REACCION + " ON " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + " = " + NOMBRE_TABLA_REACCION + ".idOferta"
				+ " WHERE " + atributo.ELIMINADA + " = 0"
				+ " AND " + atributo.CATEGORIA + " = " + atributo.ID_CATEGORIA
				+ " GROUP BY " + NOMBRE_TABLA + "." + atributo.ID_OFERTA
				+ " ORDER BY " + atributo.FECHA_HORA_REGISTRO + " DESC"
				+ " LIMIT " + indiceInicial + ", 10;";
		
		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			ofertas.add(new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					new CategoriaOferta(rs.getInt(atributo.CATEGORIA.toString()), rs.getString(atributo.NOMBRE_CATEGORIA.toString())),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getString(atributo.VIGENCIA_DESDE.toString()),
					rs.getString(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					));
	}

		dao.closeConBD();
		return ofertas;
	}
	
	public Oferta obtener(int idOferta, String idUsuario) throws ClassNotFoundException, SQLException
	{
		if(idUsuario == null)
		{
			idUsuario = "NULL";
		}
		String query = "SELECT " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + ", "
				+ atributo.TITULO_DE_OFERTA + ", "
				+ atributo.COMERCIO + ", "
				+ atributo.DESCRIPCION + ", "
				+ atributo.CATEGORIA + ", "
				+ atributo.NOMBRE_CATEGORIA + ", "
				+ atributo.CIUDAD + ", "
				+ atributo.ESTADO + ", "
				+ atributo.RESTRICCIONES + ", "
				+ atributo.VIGENCIA_DESDE + ", "
				+ atributo.VIGENCIA_HASTA + ", "
				+ atributo.IMAGEN_COMERCIO_PATH + ", "
				+ atributo.IMAGEN_PUBLICIDAD_PATH + ", "
				+ atributo.FECHA_HORA_REGISTRO
				+ ", IF(" + atributo.ID_USUARIO + " = '" + idUsuario + "', IF(" + atributo.REACCION + " = 1, 1, NULL), NULL) AS " + atributo.LIKES
				+ ", IF(" + atributo.ID_USUARIO + " = '" + idUsuario + "', IF(" + atributo.REACCION + " = 0, 1, NULL), NULL) AS " + atributo.DISLIKES
				+ " FROM " + NOMBRE_TABLA_CATEGORIA_OFERTA + ", " + NOMBRE_TABLA 
				+ " LEFT JOIN " + NOMBRE_TABLA_REACCION + " ON " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + " = " + NOMBRE_TABLA_REACCION + ".idOferta"
				+ " WHERE " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + " = " + idOferta
				+ " AND " + atributo.ELIMINADA + " = 0"
				+ " AND " + atributo.CATEGORIA + " = " + atributo.ID_CATEGORIA + ";";
		
		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			Oferta oferta = new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					new CategoriaOferta(rs.getInt(atributo.CATEGORIA.toString()), rs.getString(atributo.NOMBRE_CATEGORIA.toString())),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getString(atributo.VIGENCIA_DESDE.toString()),
					rs.getString(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					);
			dao.closeConBD();
			return oferta;
		}
		
		dao.closeConBD();
		return null;
	}

	public int cantidad() throws ClassNotFoundException, SQLException
	{
		String query = "SELECT COUNT(*) AS cantidad"
				+ " FROM " + NOMBRE_TABLA
				+ " WHERE " + atributo.ELIMINADA + " = 0;";
		
		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			int cantidad = rs.getInt("cantidad");
			dao.closeConBD();
			return cantidad;
		}
		
		dao.closeConBD();
		return 0;
	}

	public void modificar(Oferta oferta) throws ClassNotFoundException, SQLException
	{
		String queryDatos =  atributo.TITULO_DE_OFERTA + " = '" + oferta.getTituloDeOferta() + "' , "
				 + atributo.COMERCIO + " = '" + oferta.getComercio() + "' , "
				 + atributo.DESCRIPCION + " = '" + oferta.getDescripcion() + "' , "
				 + atributo.CATEGORIA + " = '" + oferta.getCategoria().getId() + "' , "
				 + atributo.CIUDAD + " = '" + oferta.getCiudad() + "' , "
				 + atributo.ESTADO + " = " + (oferta.getEstado() ? 1 : 0) + ","
				 + atributo.RESTRICCIONES + " = '" + oferta.getRestricciones() + "' , "
				 + atributo.VIGENCIA_DESDE + " = '" + oferta.getVigenciaDesde() + "' , "
				 + atributo.VIGENCIA_HASTA + " = '" + oferta.getVigenciaHasta() + "' , "
				 + atributo.IMAGEN_COMERCIO_PATH + " = '" + oferta.getImagenComercioPath() + "' , "
				 + atributo.IMAGEN_PUBLICIDAD_PATH + " = '" + oferta.getImagenPublicidadPath() + "' , "
				 + atributo.FECHA_HORA_REGISTRO + " = '" + oferta.getFechaHoraRegistro() + "'";
		String query = "UPDATE " + NOMBRE_TABLA 
				+ " SET " + queryDatos
				+ " WHERE " + atributo.ID_OFERTA + " = " +oferta.getIdOferta() + ";";
		Connection con = dao.openConBD();
		con.createStatement().executeUpdate(query);
		dao.closeConBD();
	}
	
	public void eliminar(int idOferta) throws ClassNotFoundException, SQLException
	{
		String queryDatos = atributo.ELIMINADA + " = 1";
		String query = "UPDATE " + NOMBRE_TABLA 
				+ " SET " + queryDatos
				+ " WHERE " + atributo.ID_OFERTA + " = " + idOferta + ";";
		Connection con = dao.openConBD();
		con.createStatement().executeUpdate(query);
		dao.closeConBD();
	}
	
	public ArrayList<Oferta> filtrarOfertasPorComercioYCategoria(String  nombreComercio) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ultimasOfertas = ultimasOfertas();
		Map<Integer, Oferta> valoresSimilitud = new HashMap<Integer, Oferta>();
		
		for(Oferta ofertaActual : ultimasOfertas)
		{
			int levenshteinDistance = LevenshteinDistance.distance( nombreComercio, ofertaActual.getComercio());
			int levenshteinDistanceCategoria = LevenshteinDistance.distance( nombreComercio, ofertaActual.getCategoria().getNombre());
			if ( levenshteinDistance < 6 )
			{
				valoresSimilitud.put(levenshteinDistance, ofertaActual);
			}
			else
			{
				if( levenshteinDistanceCategoria < 3 )
				{
					valoresSimilitud.put(levenshteinDistanceCategoria, ofertaActual);
				}
			}
		}
		
		Map<Integer, Oferta> mapaOrdenado = new TreeMap<Integer, Oferta>(valoresSimilitud);
		ArrayList<Oferta> ofertasFiltradas = new ArrayList<Oferta>();
		for(Map.Entry<Integer, Oferta> entryActual : mapaOrdenado.entrySet())
		{
			ofertasFiltradas.add(entryActual.getValue());
		}

		return ofertasFiltradas;
	}
	
	
}
