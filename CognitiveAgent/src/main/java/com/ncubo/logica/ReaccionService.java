package com.ncubo.logica;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.conf.Usuario;
import com.ncubo.dao.OfertaDao;
import com.ncubo.dao.ReaccionDao;
import com.ncubo.dao.UsuarioDao;
import com.ncubo.data.CategoriaOferta;
import com.ncubo.data.Categorias;
import com.ncubo.data.Oferta;
import com.ncubo.data.Reaccion;

@Component
public class ReaccionService
{
	@Autowired
	private ReaccionDao reaccionDao;
	@Autowired
	private UsuarioDao usuarioDao;
	@Autowired
	private OfertaDao ofertaDao;

	public void eliminar(Reaccion objReaccion, Usuario usuario) throws ClassNotFoundException, SQLException
	{
		Oferta oferta = ofertaDao.obtener(objReaccion.getIdOferta(), usuario.getUsuarioId());
		Categorias categoriasOferta = oferta.getCategorias() ;
		Categorias categoriasUsuario = usuario.getCategorias() ;
		
		for( int i = 0; i < categoriasUsuario.size(); i++ )
		{
			CategoriaOferta categoriaDelUsuario = categoriasUsuario.get(i);
			
			int indice = categoriasOferta.indexOf( categoriaDelUsuario );
			CategoriaOferta categoriaDelaOferta = categoriasOferta.get( indice );
			
			double peso = categoriaDelUsuario.getPeso() - categoriaDelaOferta.getPeso();
			peso =  peso / 2 ;
			
			usuario.getCategorias().get(i).setPeso(peso);
		}
		
		reaccionDao.eliminar(objReaccion);
		usuarioDao.insertar(usuario.getUsuarioId(), usuario.getCategorias() );
	}

	public void guardar(Reaccion objReaccion, Usuario usuario) throws ClassNotFoundException, SQLException 
	{
		Oferta oferta = ofertaDao.obtener(objReaccion.getIdOferta(), usuario.getUsuarioId());
		Categorias categoriasOferta = oferta.getCategorias() ;
		Categorias categoriasUsuario = usuario.getCategorias() ;
		
		for( int i = 0; i < categoriasUsuario.size(); i++ )
		{
			CategoriaOferta categoriaDelUsuario = categoriasUsuario.get(i);
			
			int indice = categoriasOferta.indexOf( categoriaDelUsuario );
			CategoriaOferta categoriaDelaOferta = categoriasOferta.get( indice );
			
			double peso = categoriaDelUsuario.getPeso() + categoriaDelaOferta.getPeso();
			peso =  peso / 2 ;
			
			usuario.getCategorias().get(i).setPeso(peso);
		}
		
		reaccionDao.guardar(objReaccion);
		usuarioDao.insertar(usuario.getUsuarioId(), usuario.getCategorias() );
	}
	
	
	
}
