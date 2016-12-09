package com.ncubo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.conf.Usuario;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Indice;
import com.ncubo.data.Oferta;
import com.ncubo.util.LevenshteinDistance;

@Component
public class OfertaDao
{
	private final String NOMBRE_TABLA = "oferta";
	private final String NOMBRE_TABLA_CATEGORIA_OFERTA = "categoriadeoferta";
	private final String NOMBRE_TABLA_REACCION = "reaccion";
	private final int LIMITE = 50;
	private final int CANTIDAD_PAGINACION = 20;
	private final String CAMPOS_PARA_SELECT = String.format("%s.%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, IF(%s = ?, IF(%s = 1, 1, NULL), NULL) AS %s, IF(%s = ?, IF(%s = 0, 1, NULL), NULL) AS %s", NOMBRE_TABLA, atributo.ID_OFERTA, atributo.TITULO_DE_OFERTA, atributo.COMERCIO, atributo.DESCRIPCION, atributo.CATEGORIA, atributo.NOMBRE_CATEGORIA, atributo.CIUDAD, atributo.ESTADO, atributo.RESTRICCIONES, atributo.VIGENCIA_DESDE, atributo.VIGENCIA_HASTA, atributo.IMAGEN_COMERCIO_PATH, atributo.IMAGEN_PUBLICIDAD_PATH, atributo.FECHA_HORA_REGISTRO, atributo.ID_USUARIO, atributo.REACCION, atributo.LIKES, atributo.ID_USUARIO, atributo.REACCION, atributo.DISLIKES);
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
	{
		String query = "SELECT oferta.idOferta, tituloDeOferta, comercio, descripcion, "
				+ "idCategoria, peso, nombre, ciudad, estado, restricciones, vigenciaDesde, vigenciaHasta, imagenComercioPath, "
				+ "imagenPublicidadPath, fechaHoraRegistro "
				+ "FROM oferta "
				+ "LEFT JOIN reaccion ON oferta.idOferta = reaccion.idOferta "
				+ "LEFT JOIN categoria_con_oferta_y_peso ON oferta.idOferta = categoria_con_oferta_y_peso.idOferta "
				+ "LEFT JOIN categoriadeoferta ON categoriadeoferta.id = categoria_con_oferta_y_peso.idCategoria "
				+ "WHERE eliminada = 0 "
				+ "ORDER BY fechaHoraRegistro "
				+ "DESC LIMIT 150;"; //El 150 es porque cada oferta sale 3 veces.

		Connection con = dao.openConBD();
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		HashMap<String, Oferta> ofertasMap = new HashMap<String, Oferta>();
		
		while (rs.next())
		{
			String id = rs.getString(atributo.ID_OFERTA.toString());
			String idCategoria = rs.getString("idCategoria");
			
			if ( !ofertasMap.containsKey( id ) )
			{
				ofertasMap.put(id,new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getDate(atributo.VIGENCIA_DESDE.toString()),
					rs.getDate(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					0,
					0
					));
				
				if( idCategoria != null )
				{
					CategoriaOferta categoria = new CategoriaOferta(
							rs.getInt("idCategoria"),
							rs.getString("nombre"),
							rs.getDouble("peso"));
					ofertasMap.get( id ).agregarCategoria(categoria);
				}
			}
					
		}
		
		Collection<Oferta> values = ofertasMap.values();
		ArrayList<Oferta> listaDeOfertas = new ArrayList<Oferta>(values);
		
		dao.closeConBD();
		return listaDeOfertas;
	}
	
	public void insertar(Oferta oferta) throws ClassNotFoundException, SQLException
	{
		oferta.cambiarApostrofes();
		String queryDatos = "'" + oferta.getTituloDeOferta()+ "'"
							+ ",'" + oferta.getComercio() + "'"
							+ ",'" + oferta.getDescripcion() + "'"
						//	+ ",'" + oferta.getCategoria().getId() + "'"
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
		PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if (rs.next())
		{
			oferta.setIdOferta(rs.getInt(1));
		}
		
		dao.closeConBD();
	}
	
	public void insertarCategorias(int idOferta, ArrayList< CategoriaOferta> categorias) throws ClassNotFoundException, SQLException
	{
		String query = "INSERT INTO categoria_con_oferta_y_peso"
					 + "(idCategoria, idOferta, peso) VALUES ";

		int cantidad = categorias.size();
		for( CategoriaOferta categoria : categorias)
		{
			query+= "("+categoria.getId()+", "+idOferta+","+categoria.getPeso()+")";
			
			if( cantidad > 1)
			{
				query+= ", ";
				cantidad = cantidad-1;
			}
		}
		

		Connection con = dao.openConBD();
		PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		
		dao.closeConBD();
	}
	
	public List<Oferta> obtenerUltimasDiezOfertasParaMostrarDesde(Indice indiceInicial, Usuario usuario) throws ClassNotFoundException, SQLException
	{
		boolean esUnUsuarioConocido = true;
		String idUsuario;
		
		if(usuario == null || usuario.getUsuarioId() == null || usuario.getUsuarioId().isEmpty())
		{
			idUsuario = "NULL";
			esUnUsuarioConocido = false;
		}
		else
		{
			idUsuario =  usuario.getUsuarioId();
		}
		
		HashMap<String, Oferta> ofertasMap = new HashMap<String, Oferta>();
		
		Connection con = dao.openConBD();
		String query = 
			"SELECT oferta.idOferta, tituloDeOferta, comercio, descripcion, "
			+ "idCategoria, peso, nombre, ciudad, estado, restricciones, vigenciaDesde, "
			+ "vigenciaHasta, imagenComercioPath, imagenPublicidadPath, fechaHoraRegistro, "
			+ "IF(idUsuario = ?, IF(reaccion = 1, 1, NULL), NULL) AS likes, "
			+ "IF(idUsuario = ?, IF(reaccion = 0, 1, NULL), NULL) AS dislikes "
			+ "FROM oferta "
			+ "LEFT JOIN reaccion ON oferta.idOferta = reaccion.idOferta "
			+ "LEFT JOIN categoria_con_oferta_y_peso ON oferta.idOferta = categoria_con_oferta_y_peso.idOferta "
			+ "LEFT JOIN categoriadeoferta ON categoriadeoferta.id = categoria_con_oferta_y_peso.idCategoria "
			+ "WHERE eliminada = 0 "
			+ "AND estado = 1 "
			+ "AND vigenciaHasta >= ? "
			+ "ORDER BY fechaHoraRegistro "
			+ "DESC LIMIT ?, 30;"; //El 30 es por que cada oferta sale 3 veces
				
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idUsuario);
		stmt.setString(2, idUsuario);
		stmt.setDate(3, new Date(Calendar.getInstance().getTimeInMillis()));
		stmt.setInt(4, indiceInicial.valorEntero());
		
		ResultSet rs = stmt.executeQuery();
		
		
		while (rs.next())
		{
			String id = rs.getString(atributo.ID_OFERTA.toString());
			String idCategoria = rs.getString("idCategoria");
			
			if ( !ofertasMap.containsKey( id ) )
			{
				Oferta oferta = new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					//new CategoriaOferta(rs.getInt(atributo.CATEGORIA.toString()), rs.getString(atributo.NOMBRE_CATEGORIA.toString())),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getDate(atributo.VIGENCIA_DESDE.toString()),
					rs.getDate(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					);
				oferta.setEsUnUsuarioConocido(esUnUsuarioConocido);
				ofertasMap.put(id, oferta);
			}
			
			if( idCategoria != null )
			{
				CategoriaOferta categoria = new CategoriaOferta(
						rs.getInt("idCategoria"),
						rs.getString("nombre"),
						rs.getDouble("peso"));
				ofertasMap.get( id ).agregarCategoria(categoria);
			}
			

		}
		
		Collection<Oferta> values = ofertasMap.values();
		ArrayList<Oferta> listaDeOfertas = new ArrayList<Oferta>(values);

		dao.closeConBD();
		return listaDeOfertas;
	}
	
	public Oferta obtener(int idOferta, String idUsuario) throws ClassNotFoundException, SQLException
	{
		boolean esUnUsuarioConocido = true;
		if(idUsuario == null || idUsuario.isEmpty())
		{
			idUsuario = "NULL";
			esUnUsuarioConocido = false;
		}
		String query = 
				"SELECT oferta.idOferta, tituloDeOferta, comercio, descripcion, idCategoria, peso, nombre, ciudad, estado, restricciones, vigenciaDesde, "
				+ "vigenciaHasta, imagenComercioPath, imagenPublicidadPath, fechaHoraRegistro, "
				+ "IF(idUsuario = ?, IF(reaccion = 1, 1, NULL), NULL) AS likes, IF(idUsuario = ?, "
				+ "IF(reaccion = 0, 1, NULL), NULL) AS dislikes "
				+ "FROM oferta "
				+ "LEFT JOIN reaccion ON oferta.idOferta = reaccion.idOferta "
				+ "LEFT JOIN categoria_con_oferta_y_peso ON oferta.idOferta = categoria_con_oferta_y_peso.idOferta "
				+ "LEFT JOIN categoriadeoferta ON categoriadeoferta.id = categoria_con_oferta_y_peso.idCategoria "
				+ "WHERE oferta.idOferta = ? "
				+ "ORDER BY oferta.idOferta";
		
		Connection con = dao.openConBD();
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idUsuario);
		stmt.setString(2, idUsuario);
		stmt.setInt(3, idOferta);
		
		ResultSet rs = stmt.executeQuery();
		Oferta oferta = null;
		while (rs.next())
		{
			String id = rs.getString(atributo.ID_OFERTA.toString());
			String idCategoria = rs.getString("idCategoria");
			
			if( oferta == null)
			{
				oferta = new Oferta(
					rs.getInt(atributo.ID_OFERTA.toString()),
					rs.getString(atributo.TITULO_DE_OFERTA.toString()),
					rs.getString(atributo.COMERCIO.toString()),
					rs.getString(atributo.DESCRIPCION.toString()),
					//new CategoriaOferta(rs.getInt(atributo.CATEGORIA.toString()), rs.getString(atributo.NOMBRE_CATEGORIA.toString())),
					rs.getString(atributo.CIUDAD.toString()),
					rs.getBoolean(atributo.ESTADO.toString()),
					rs.getString(atributo.RESTRICCIONES.toString()),
					rs.getDate(atributo.VIGENCIA_DESDE.toString()),
					rs.getDate(atributo.VIGENCIA_HASTA.toString()),
					rs.getString(atributo.IMAGEN_COMERCIO_PATH.toString()),
					rs.getString(atributo.IMAGEN_PUBLICIDAD_PATH.toString()),
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString()),
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					);
				oferta.setEsUnUsuarioConocido(esUnUsuarioConocido);
			}
			
			if( idCategoria != null )
			{
				CategoriaOferta categoria = new CategoriaOferta(
						rs.getInt("idCategoria"),
						rs.getString("nombre"),
						rs.getDouble("peso"));
				oferta.agregarCategoria(categoria);
			}
			

		}
		
		dao.closeConBD();
		return oferta;
	}

	public int obtenerCantidadDeOfertasParaMostrar() throws ClassNotFoundException, SQLException
	{
		String query = "SELECT COUNT(oferta.idOferta) AS cantidad FROM oferta WHERE eliminada = 0 AND vigenciaHasta >= ? AND estado = 1;";
		
		Connection con = dao.openConBD();
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
		ResultSet rs = stmt.executeQuery();
		
		rs.next();
		int cantidad = rs.getInt("cantidad");
		dao.closeConBD();
		return cantidad;
	}

	public void modificar(Oferta oferta) throws ClassNotFoundException, SQLException
	{
		oferta.cambiarApostrofes();
		String queryDatos =  atributo.TITULO_DE_OFERTA + " = '" + oferta.getTituloDeOferta() + "' , "
				 + atributo.COMERCIO + " = '" + oferta.getComercio() + "' , "
				 + atributo.DESCRIPCION + " = '" + oferta.getDescripcion() + "' , "
				// + atributo.CATEGORIA + " = '" + oferta.getCategoria().getId() + "' , "
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
	
	public ArrayList<Oferta> filtrarOfertasPorComercioYCategoria(String nombreComercio, int desde) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ultimasOfertas = obtener();
		Map<Integer, List<Oferta>> valoresSimilitud = new HashMap<Integer, List<Oferta>>();
		int cantidadDeResultados = 0;
		int cantidadQueLleva = 0;
		
		for(Oferta ofertaActual : ultimasOfertas)
		{
			int levenshteinDistance = LevenshteinDistance.distance( nombreComercio, ofertaActual.getComercio());
			
			if ( levenshteinDistance < 6 )
			{
				if(cantidadQueLleva >= desde)
				{
				if (valoresSimilitud.get(levenshteinDistance) == null)
				{
					ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
					ofertas.add(ofertaActual);
					valoresSimilitud.put(levenshteinDistance, ofertas);
				}
				else
				{
					valoresSimilitud.get(levenshteinDistance).add(ofertaActual);
				}
					cantidadDeResultados++;
			}
			else
			{
					cantidadQueLleva++;
				}
			}
			
			if(cantidadDeResultados == CANTIDAD_PAGINACION)
			{
				break;
		}
		}
		
		Map<Integer, List<Oferta>> mapaOrdenado = new TreeMap<Integer, List<Oferta>>(valoresSimilitud);
		ArrayList<Oferta> ofertasFiltradas = new ArrayList<Oferta>();
		for(Map.Entry<Integer, List<Oferta>> entryActual : mapaOrdenado.entrySet())
		{
			for(Oferta ofertaActual : entryActual.getValue())
			ofertasFiltradas.add(ofertaActual);
		}

		return ofertasFiltradas;
	}

	public int cantidadPaginas() throws ClassNotFoundException, SQLException
	{
		int cantidadDeofertas = obtenerCantidadDeOfertasParaMostrar();
		int cantidadDePaginas = cantidadDeofertas / CANTIDAD_PAGINACION;
	
		if(cantidadDeofertas % CANTIDAD_PAGINACION != 0)
		{
			cantidadDePaginas++;
		}
		return cantidadDePaginas;
	}
	
	public int cantidadPaginas(int resultados) throws ClassNotFoundException, SQLException
	{
		int cantidadDePaginas = resultados / CANTIDAD_PAGINACION;
		
		if(resultados % CANTIDAD_PAGINACION != 0)
		{
			cantidadDePaginas++;
		}
		return cantidadDePaginas;
	}
	
	public int getCantidadPaginacion()
	{
		return CANTIDAD_PAGINACION;
	}
}
