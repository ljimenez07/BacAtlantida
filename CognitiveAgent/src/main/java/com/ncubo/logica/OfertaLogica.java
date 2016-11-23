package com.ncubo.logica;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.ncubo.dao.OfertaDao;
import com.ncubo.data.Oferta;

@Component
public class OfertaLogica
{
	@Autowired
	private OfertaDao ofertaDao;
	
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

}
