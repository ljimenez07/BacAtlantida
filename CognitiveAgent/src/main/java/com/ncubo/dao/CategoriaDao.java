package com.ncubo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.CategoriaOferta;

@Component
public class CategoriaDao
{
	private final String CATEGORIA_OFERTA_TABLA = "categoriadeoferta";
	@Autowired
	private Persistencia dao;
	
	public enum atributo
	{
		ID_CATEGORIA("id"),
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

		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			categoriaOferta.add(new CategoriaOferta(
					rs.getInt(atributo.ID_CATEGORIA.toString()),
					rs.getString(atributo.NOMBRE_CATEGORIA.toString()),
					0
				));
		}
		
		dao.closeConBD();
		return categoriaOferta;
	}
}
