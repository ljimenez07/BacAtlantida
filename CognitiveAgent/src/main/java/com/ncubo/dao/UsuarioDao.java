package com.ncubo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Categorias;

@Component
public class UsuarioDao {
	
	@Autowired
	private Persistencia dao;
	
	public void insertar( String idUsuarioenBA, Categorias categorias) throws ClassNotFoundException, SQLException
	{
		String query = "INSERT INTO categoria_usuario_peso"
				 + "(idUsuarioenBA, idCategoria, peso) VALUES (?,?,?);";

		Connection con = dao.openConBD();
		
		for( CategoriaOferta cat : categorias)
		{
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, idUsuarioenBA);
			stmt.setInt(2, cat.getId());
			stmt.setDouble(3, cat.getPeso());

			stmt.executeUpdate();
		}
		
		dao.closeConBD();
	}

}
