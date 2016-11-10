package com.ncubo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Oferta;

@Component
public class OfertaDao
{
	private final String NOMBRE_TABLA = "oferta";
	private final String NOMBRE_TABLA_CATEGORIA_OFERTA = "categoriaoferta";
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
		FECHA_HORA_REGISTRO("fechaHoraRegistro");
		
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
				+ " WHERE " + atributo.CATEGORIA + " = " + atributo.ID_CATEGORIA + ";";

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
					rs.getTimestamp(atributo.FECHA_HORA_REGISTRO.toString())
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
							+ ",'" + oferta.getVigenciahasta() + "'"
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
}
