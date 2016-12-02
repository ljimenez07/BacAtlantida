package com.ncubo.logica;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.ncubo.chatbot.participantes.Gustos;
import com.ncubo.conf.AgenteCognitivo;
import com.ncubo.conf.Usuario;
import com.ncubo.dao.OfertaDao;
import com.ncubo.data.Oferta;

@Component
public class OfertaService
{
	@Autowired
	private OfertaDao ofertaDao;
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	
	public BindingResult validarCampos(BindingResult bindingResult, Oferta oferta) throws ParseException
	{
		if( ! bindingResult.hasFieldErrors("vigenciaHasta") && ! bindingResult.hasFieldErrors("vigenciaDesde"))
		{
			if( ! oferta.fechaHastaMayorAFechaDesde())
			{
				bindingResult.rejectValue("vigenciaHasta", "1", "*Fechas incorrectas");
			}
		}
		return bindingResult;
	}
	
	public ArrayList<Oferta> filtrarOferta(String nombreComercio) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		if(nombreComercio.equals(""))
		{
			ofertas = ofertaDao.obtener();
		}
		else
		{
			ofertas = ofertaDao.filtrarOfertasPorComercioYCategoria(nombreComercio);
		}
		return ofertas;
	}

	@Transactional
	public void insertar(Oferta oferta) throws ClassNotFoundException, SQLException 
	{
		//TODO transacciones
		ofertaDao.insertar(oferta);
		ofertaDao.insertarCategorias(oferta.getIdOferta(), oferta.getCategorias());
	}
	
	public Oferta obtener(int idOferta, Usuario usuario) throws ClassNotFoundException, SQLException
	{
		Gustos gusto = serverCognitivo.obtenerGustosDelCliente( usuario.getUsuarioId() );
		
		boolean tieneAlMenosUnaCategoriaEspecificadaEnSusGustos = 0 < gusto.getLeGustaComerAfuera() + gusto.getLeGustaLosHoteles() + gusto.getLeGustaComerAfuera();
		
		if( usuario.getEstaLogueado() && tieneAlMenosUnaCategoriaEspecificadaEnSusGustos)
		{
			
			
		}
		
		return ofertaDao.obtener(idOferta, idUsuario);
	}

}
