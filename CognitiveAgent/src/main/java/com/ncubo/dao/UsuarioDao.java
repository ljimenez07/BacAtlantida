package com.ncubo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.conf.Usuario;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Categorias;

@Component
public class UsuarioDao {
	
	@Autowired
	private Persistencia dao;
	
	public void insertar( String idUsuarioenBA, Categorias categorias) throws ClassNotFoundException, SQLException
	{
		String query = "INSERT INTO categoria_usuario_peso"
				 + "(idUsuarioenBA, idCategoria, peso) VALUES (?,?,?) ON DUPLICATE KEY UPDATE peso = ?;";

		Connection con = dao.openConBD();
		
		for( CategoriaOferta cat : categorias)
		{
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, idUsuarioenBA);
			stmt.setInt(2, cat.getId());
			stmt.setDouble(3, cat.getPeso());
			stmt.setDouble(4, cat.getPeso());
			
			stmt.executeUpdate();
		}
		
		dao.closeConBD();
	}

	
	public Categorias obtenerLasCategoriasDeUnUsuario(Usuario usuario) throws ClassNotFoundException, SQLException
	{
		Categorias result = new Categorias();
		
		Connection con = dao.openConBD();
		String query = 
			"SELECT idUsuarioenBA, idCategoria, peso "
			+ "FROM categoria_usuario_peso "
			+ "INNER JOIN categoriadeoferta on categoria_usuario_peso.idCategoria = categoriadeoferta.id "
			+ "WHERE idUsuarioenBA = ? ";
				
		PreparedStatement stmt = con.prepareStatement(query);
		stmt.setString(1, usuario.getIdSesion() );
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next())
		{
			CategoriaOferta categoriaOferta = new CategoriaOferta(
				rs.getInt( "idCategoria" ),
				rs.getString( "nombre" ),
				rs.getDouble( "peso" )
				);
			
			result.agregar( categoriaOferta );
		}

		return result;
	}
	
}
