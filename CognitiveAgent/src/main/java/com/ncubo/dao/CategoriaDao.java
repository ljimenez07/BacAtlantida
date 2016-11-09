package com.ncubo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ncubo.data.CategoriaOferta;

public class CategoriaDao
{
private final String CATEGORIA_OFERTA_TABLA = "categoriaoferta";
	
	public enum atributo
	{
		ID_CATEGORIA("idCategoriaOferta"),
		NOMBRE_CATEGORIA("nombre");
		
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
	
	public ArrayList<CategoriaOferta> obtener() throws ClassNotFoundException, SQLException
	{
		ArrayList<CategoriaOferta> categoriaOferta = new ArrayList<CategoriaOferta>();
		String query = "SELECT " 
				+ atributo.ID_CATEGORIA + ", "
				+ atributo.NOMBRE_CATEGORIA
				+ " FROM " + CATEGORIA_OFERTA_TABLA +  ";";

		Persistencia dao = new Persistencia();
		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			categoriaOferta.add(new CategoriaOferta(
					rs.getInt(atributo.ID_CATEGORIA.toString()),
					rs.getString(atributo.NOMBRE_CATEGORIA.toString())
				));
		}
		
		dao.closeConBD();
		return categoriaOferta;
	}
}