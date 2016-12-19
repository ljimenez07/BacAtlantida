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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.conf.Usuario;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Categorias;
import com.ncubo.data.Indice;
import com.ncubo.data.Oferta;
import com.ncubo.util.LevenshteinDistance;

@Component
public class OfertaDao
{
	private final String NOMBRE_TABLA = "oferta";
	private final String NOMBRE_TABLA_REACCION = "reaccion";
	private final int LIMITE = 50;
	private final int CANTIDAD_PAGINACION_BO = 20;
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
	
	public ArrayList<Oferta> obtener(int indicaInicial) throws ClassNotFoundException, SQLException
	{
		return obtener(indicaInicial, CANTIDAD_PAGINACION_BO);
	}
	
	public ArrayList<Oferta> obtener(int indicaInicial, int hasta) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		String query = "SELECT " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + ", "
				+ atributo.TITULO_DE_OFERTA + ", "
				+ atributo.COMERCIO + ", "
				+ atributo.DESCRIPCION + ", "
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
				+ " FROM "  + NOMBRE_TABLA
				+ " LEFT JOIN " + NOMBRE_TABLA_REACCION + " ON " + NOMBRE_TABLA + "." + atributo.ID_OFERTA + " = " + NOMBRE_TABLA_REACCION + ".idOferta"
				+ " WHERE " + atributo.ELIMINADA + " = 0"
				+ " GROUP BY " + NOMBRE_TABLA + "." + atributo.ID_OFERTA 
				+ " ORDER BY " + atributo.FECHA_HORA_REGISTRO + " DESC "
				+ " LIMIT " + indicaInicial + ", " + hasta + ";";

		Connection con = dao.openConBD();
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		while (rs.next())
		{
			ofertas.add(new Oferta(
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
					rs.getInt(atributo.LIKES.toString()),
					rs.getInt(atributo.DISLIKES.toString())
					));
		}
		
		dao.closeConBD();
		return ofertas;
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
	
	public void insertarCategorias(int idOferta, ArrayList<CategoriaOferta> categorias) throws ClassNotFoundException, SQLException
	{
		Connection con = dao.openConBD();
		for( CategoriaOferta categoria : categorias)
		{
			String query ="";
			query += "INSERT INTO categoria_con_oferta_y_peso (idCategoria, idOferta, peso) VALUES ";
			query += "("+categoria.getId()+", "+idOferta+","+categoria.getPeso()+") ";
			query += "ON DUPLICATE KEY UPDATE peso = "+categoria.getPeso()+" ; ";
			
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.executeUpdate();
			
		}

		
		dao.closeConBD();

	}
	
	private Categorias categoriasPorOferta( Connection con, int idOferta) throws SQLException
	{
		String query = 
			"SELECT "
			+ "peso, idCategoria "
			+ "FROM categoria_con_oferta_y_peso "
			+ "WHERE idOferta = ? ";
		
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setInt(1, idOferta);
		
		ResultSet rs = stmt.executeQuery();
		
		Categorias categorias = new Categorias();
		
		while (rs.next())
		{
			int id = rs.getInt("idCategoria");
			double peso = rs.getDouble("peso");
			
			CategoriaOferta categoria = new CategoriaOferta( id, "", peso);
			categorias.agregar(categoria);
	
		}
		
		return categorias;
	}
	
	public List<Oferta> obtenerUltimasDiezOfertasParaMostrarDesde(Indice indiceInicial, Usuario usuario, double distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas) throws ClassNotFoundException, SQLException
	{
		boolean esUnUsuarioConocido = true;
		
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		
		Connection con = dao.openConBD();
		String query = 
			"SELECT "
			+ "o.idOferta, tituloDeOferta, comercio, descripcion, "
			+ "ciudad, estado, restricciones, vigenciaDesde, "
			+ "vigenciaHasta, imagenComercioPath, imagenPublicidadPath, fechaHoraRegistro, "
			+ "IF(idUsuario = ?, IF(reaccion = 1, 1, NULL), NULL) AS likes, "
			+ "IF(idUsuario = ?, IF(reaccion = 0, 1, NULL), NULL) AS dislikes, "
			+ " belleza.peso as belleza, "
			+ " hoteles.peso as hoteles, "
			+ " restaurante.peso as restaurante "
			+ "FROM oferta o "
			+ "LEFT JOIN reaccion ON o.idOferta = reaccion.idOferta "
			
				+ "INNER JOIN ( "
				+ "SELECT c.peso, c.idOferta, c.idCategoria from oferta o2 "
				+ "INNER JOIN categoria_con_oferta_y_peso c ON o2.idOferta = c.idOferta " 
				+ ") as belleza on o.idOferta = belleza.idOferta and belleza.idCategoria = 3  "
				
				+ "INNER JOIN ( "
				+ "SELECT c.peso, c.idOferta, c.idCategoria from oferta o2 "
				+ "INNER JOIN categoria_con_oferta_y_peso c ON o2.idOferta = c.idOferta " 
				+ ") as hoteles on o.idOferta = hoteles.idOferta and hoteles.idCategoria = 2  "
				
				+ "INNER JOIN ( "
				+ "SELECT c.peso, c.idOferta, c.idCategoria from oferta o2 "
				+ "INNER JOIN categoria_con_oferta_y_peso c ON o2.idOferta = c.idOferta " 
				+ ") as restaurante on o.idOferta = restaurante.idOferta and restaurante.idCategoria = 1  "
					
			
			+ "WHERE eliminada = 0 "
			+ "AND estado = 1 "
			+ "AND SQRT( POW( belleza.peso - ?, 2 ) + POW( hoteles.peso - ?, 2 ) + POW( restaurante.peso - ?, 2 ) ) <= ?"
			+ "AND vigenciaHasta >= ? "
			+ "ORDER BY fechaHoraRegistro "
			+ "DESC LIMIT ?, 10;"; 
				
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, usuario.getUsuarioId());
		stmt.setString(2, usuario.getUsuarioId());
		stmt.setDouble(3, usuario.getCategorias().obtenerCategoriaDeBelleza().getPeso());
		stmt.setDouble(4, usuario.getCategorias().obtenerCategoriaDeHotel().getPeso());
		stmt.setDouble(5, usuario.getCategorias().obtenerCategoriaDeRestaurante().getPeso());
		stmt.setDouble(6, distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas );
		stmt.setDate(7, new Date(Calendar.getInstance().getTimeInMillis()));
		stmt.setInt(8, indiceInicial.valorEntero() );
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
		{
			int id = rs.getInt(atributo.ID_OFERTA.toString());

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
			
			oferta.setCategorias( categoriasPorOferta(con, id ) );
			ofertas.add( oferta );
		}
		
		dao.closeConBD();
		
		return ofertas;
	}
	
	
	public List<Oferta> obtenerUltimasDiezOfertasParaMostrarDesde(Indice indiceInicial) throws ClassNotFoundException, SQLException
	{
		boolean esUnUsuarioConocido = false;
		String idUsuario = "NULL";
		
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		
		Connection con = dao.openConBD();
		String query = 
			"SELECT "
			+ "o.idOferta, tituloDeOferta, comercio, descripcion, "
			+ "ciudad, estado, restricciones, vigenciaDesde, "
			+ "vigenciaHasta, imagenComercioPath, imagenPublicidadPath, fechaHoraRegistro, "
			+ "IF(idUsuario = ?, IF(reaccion = 1, 1, NULL), NULL) AS likes, "
			+ "IF(idUsuario = ?, IF(reaccion = 0, 1, NULL), NULL) AS dislikes "
			+ "FROM oferta o "
			+ "LEFT JOIN reaccion ON o.idOferta = reaccion.idOferta "
			+ "WHERE eliminada = 0 "
			+ "AND estado = 1 "
			+ "AND vigenciaHasta >= ? "
			+ "ORDER BY fechaHoraRegistro "
			+ "DESC LIMIT ?, 10;"; 
				
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, idUsuario);
		stmt.setString(2, idUsuario);
		stmt.setDate(3, new Date(Calendar.getInstance().getTimeInMillis()));
		stmt.setInt(4, indiceInicial.valorEntero());
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
		{
			int id = rs.getInt(atributo.ID_OFERTA.toString());

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
			
			oferta.setCategorias( categoriasPorOferta(con, id ) );
			
			ofertas.add( oferta);	
		}

		dao.closeConBD();

		return ofertas;
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
				"SELECT oferta.idOferta, tituloDeOferta, comercio, descripcion, ciudad, estado, restricciones, vigenciaDesde, "
				+ "vigenciaHasta, imagenComercioPath, imagenPublicidadPath, fechaHoraRegistro, "
				+ "IF(idUsuario = ?, IF(reaccion = 1, 1, NULL), NULL) AS likes, IF(idUsuario = ?, "
				+ "IF(reaccion = 0, 1, NULL), NULL) AS dislikes "
				+ "FROM oferta "
				+ "LEFT JOIN reaccion ON oferta.idOferta = reaccion.idOferta "
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
			int id = rs.getInt(atributo.ID_OFERTA.toString());
			
			if( oferta == null)
			{
				oferta = new Oferta(
					id,
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
				oferta.setCategorias( categoriasPorOferta(con, id ) );
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
	
	public int obtenerCantidadDeOfertasParaMostrar(Usuario usuario, double distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas) throws ClassNotFoundException, SQLException
	{
		String query = 
			"SELECT "
			+ " count(o.idOferta) as cantidad "
			+ "FROM oferta o "
			
				+ "INNER JOIN ( "
				+ "SELECT c.peso, c.idOferta, c.idCategoria from oferta o2 "
				+ "INNER JOIN categoria_con_oferta_y_peso c ON o2.idOferta = c.idOferta " 
				+ ") as belleza on o.idOferta = belleza.idOferta and belleza.idCategoria = 3  "
				
				+ "INNER JOIN ( "
				+ "SELECT c.peso, c.idOferta, c.idCategoria from oferta o2 "
				+ "INNER JOIN categoria_con_oferta_y_peso c ON o2.idOferta = c.idOferta " 
				+ ") as hoteles on o.idOferta = hoteles.idOferta and hoteles.idCategoria = 2  "
				
				+ "INNER JOIN ( "
				+ "SELECT c.peso, c.idOferta, c.idCategoria from oferta o2 "
				+ "INNER JOIN categoria_con_oferta_y_peso c ON o2.idOferta = c.idOferta " 
				+ ") as restaurante on o.idOferta = restaurante.idOferta and restaurante.idCategoria = 1  "
					
			
			+ "WHERE eliminada = 0 "
			+ "AND estado = 1 "
			+ "AND SQRT( POW( belleza.peso - ?, 2 ) + POW( hoteles.peso - ?, 2 ) + POW( restaurante.peso - ?, 2 ) ) <= ?"
			+ "AND vigenciaHasta >= ? "
			+ "ORDER BY fechaHoraRegistro ";
		
		Connection con = dao.openConBD();
		PreparedStatement stmt = con.prepareStatement(query);

		stmt.setDouble(1, usuario.getCategorias().obtenerCategoriaDeBelleza().getPeso());
		stmt.setDouble(2, usuario.getCategorias().obtenerCategoriaDeHotel().getPeso());
		stmt.setDouble(3, usuario.getCategorias().obtenerCategoriaDeRestaurante().getPeso());
		stmt.setDouble(4, distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas );
		stmt.setDate(5, new Date(Calendar.getInstance().getTimeInMillis()));
		
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
	
	public ArrayList<Oferta> filtrarOfertasPorComercioYCategoria(String  nombreComercio) throws ClassNotFoundException, SQLException
	{
		return filtrarOfertasPorComercioYCategoria(nombreComercio, 0);
	}
	
	public ArrayList<Oferta> filtrarOfertasPorComercioYCategoria(String nombreComercio, int desde) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ultimasOfertas = obtener(0, LIMITE);
		Map<Integer, List<Oferta>> valoresSimilitud = new HashMap<Integer, List<Oferta>>();
		int cantidadDeResultados = 0;
		int cantidadQueLleva = 0;
		
		for(Oferta ofertaActual : ultimasOfertas)
		{
			int levenshteinDistance = LevenshteinDistance.distance( nombreComercio, ofertaActual.getComercio());
			boolean contieneParteDelNombre = ofertaActual.getComercio().toLowerCase().contains(nombreComercio.toLowerCase());
			
			if ( levenshteinDistance < 6 || contieneParteDelNombre)
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
			
			if(cantidadDeResultados == CANTIDAD_PAGINACION_BO)
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
		int cantidadDePaginas = cantidadDeofertas / CANTIDAD_PAGINACION_BO;
	
		if(cantidadDeofertas % CANTIDAD_PAGINACION_BO != 0)
		{
			cantidadDePaginas++;
		}
		return cantidadDePaginas;
	}
	
	public int cantidadPaginas(int resultados) throws ClassNotFoundException, SQLException
	{
		int cantidadDePaginas = resultados / CANTIDAD_PAGINACION_BO;
		
		if(resultados % CANTIDAD_PAGINACION_BO != 0)
		{
			cantidadDePaginas++;
		}
		return cantidadDePaginas;
	}
	
	public int getCantidadPaginacion()
	{
		return CANTIDAD_PAGINACION_BO;
	}
}
