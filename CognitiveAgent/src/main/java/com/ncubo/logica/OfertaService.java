package com.ncubo.logica;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncubo.dao.OfertaDao;
import com.ncubo.data.Oferta;
import com.ncubo.util.GestorDeArchivos;

@Service
public class OfertaService
{
	
	@Autowired
	private OfertaDao ofertaDao;
	
	@Autowired
	GestorDeArchivos gestorDeArchivos;
	
	
	
	public void insertar(Oferta oferta) throws ClassNotFoundException, SQLException, IOException 
	{
		ofertaDao.insertar(oferta);
		gestorDeArchivos.textoAAudio( ""+oferta.getIdOferta(), oferta.getDescripcion() );
	}

	public List<Oferta> ultimasDiezOfertasDesde(int indiceInicial, String idUsuario) throws ClassNotFoundException, SQLException {
		return ofertaDao.ultimasDiezOfertasDesde(indiceInicial, idUsuario);
	}

	public Oferta obtener(int idOferta2, String idUsuario) throws ClassNotFoundException, SQLException
	{
		return ofertaDao.obtener(idOferta2, idUsuario);
	}

	public int consultarCantidadDeOfertasNOEliminadas() throws ClassNotFoundException, SQLException 
	{
		return ofertaDao.obtenerCantidadDeOfertasParaMostrar();
	}

	public void modificar(Oferta oferta) throws ClassNotFoundException, SQLException, IOException 
	{
		ofertaDao.modificar(oferta);
		if( oferta.cambioLaDescripcion() )
		{
			gestorDeArchivos.textoAAudio( ""+oferta.getIdOferta(),oferta. getDescripcion() );
		}
	}

	public void eliminar(int idOferta) throws ClassNotFoundException, SQLException 
	{
		ofertaDao.eliminar(idOferta);
	}

	public List<Oferta> obtenerUltimasDiezOfertasParaMostrarDesde(int indiceInicial, String idUsuario) throws ClassNotFoundException, SQLException {
		return ofertaDao.ultimasDiezOfertasDesde( indiceInicial, idUsuario);
	}

	public int obtenerCantidadDeOfertasParaMostrar() throws ClassNotFoundException, SQLException {
		return ofertaDao.obtenerCantidadDeOfertasParaMostrar();
	}
	
}
