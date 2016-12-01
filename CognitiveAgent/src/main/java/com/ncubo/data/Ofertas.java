package com.ncubo.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.dao.OfertaDao;

@Component
public class Ofertas
{
	
	@Autowired
	private OfertaDao ofertaDao;
	
	public Ofertas()
	{

	}
	
	public ArrayList<Oferta> filtrarOferta(String nombreComercio) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		if(nombreComercio.isEmpty())
		{
			ofertas = ofertaDao.obtenerUltimas50Ofertas();
		}
		else
		{
			ofertas = ofertaDao.filtrarOfertasPorComercioYCategoria(nombreComercio);
		}
		return ofertas;
	}

	public ArrayList<Oferta> obtener() throws ClassNotFoundException, SQLException 
	{
		return filtrarOferta("");
	}

	public List<Oferta> ultimasDiezOfertasDesde(int indiceInicial, String idUsuario) throws ClassNotFoundException, SQLException {
		return ofertaDao.ultimasDiezOfertasDesde(indiceInicial, idUsuario);
	}

}
