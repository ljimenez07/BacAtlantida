package com.ncubo.logica;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import com.ncubo.conf.AgenteCognitivo;
import com.ncubo.conf.Usuario;
import com.ncubo.dao.OfertaDao;
import com.ncubo.data.Belleza;
import com.ncubo.data.Hotel;
import com.ncubo.data.Indice;
import com.ncubo.data.Oferta;
import com.ncubo.data.Restaurate;

@Component
@ConfigurationProperties("categorias")
public class OfertaService
{
	@Autowired
	private OfertaDao ofertaDao;
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	
	private double distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas;
	
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
	
	public ArrayList<Oferta> filtrarOferta(String nombreComercio, int desde) throws ClassNotFoundException, SQLException
	{
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
		if(nombreComercio.equals(""))
		{
			ofertas = ofertaDao.obtener();
		}
		else
		{
			ofertas = ofertaDao.filtrarOfertasPorComercioYCategoria(nombreComercio, desde);
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
	
	public void modificar(Oferta oferta) throws ClassNotFoundException, SQLException
	{
		//TODO transacciones
		ofertaDao.modificar(oferta);
		ofertaDao.insertarCategorias(oferta.getIdOferta(), oferta.getCategorias());
	}
	public List<Oferta> obtenerUltimasDiezOfertasParaMostrarDesde(Indice indiceInicial, Usuario usuario) throws Exception
	{
		if( usuario == null || ! usuario.getEstaLogueado() )
		{
			return ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
		}
		
		List<Oferta> ofertasFinales = new ArrayList<Oferta>();
		double hoteles =  serverCognitivo.obtenerValorDeGustosDeHoteles( usuario.getUsuarioId() );
		double belleza = serverCognitivo.obtenerValorDeGustosDeBelleza( usuario.getUsuarioId() );
		double restaurantes = serverCognitivo.obtenerValorDeGustosDeRestaurantes( usuario.getUsuarioId() );
		
		obtenerUltimasDiezOfertasParaMostrarDesde( 
				ofertasFinales, 
				indiceInicial, 
				usuario, 
				new Belleza(belleza), 
				new Hotel(hoteles), 
				new Restaurate(restaurantes) 
				);
		
		return ofertasFinales;
		
	}
	
	private void obtenerUltimasDiezOfertasParaMostrarDesde(List<Oferta> ofertasFinales, Indice indiceInicial, Usuario usuario, Belleza belleza, Hotel hoteles, Restaurate restaurante) throws Exception
	{
		List<Oferta> ofertas =  ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario.getUsuarioId());
		
		if( ofertas.size() == 0)
		{
			return;
		}
		
		for( Oferta oferta : ofertas )
		{
			double distanciaActualEntreAmbasCategorias = oferta.distanciaEuclidianaDeCategoria(
				belleza, 
				hoteles,
				restaurante );
			 
			if( distanciaActualEntreAmbasCategorias <= distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas )
			{
				ofertasFinales.add( oferta );
			}
			indiceInicial.agregarleDiez();
			obtenerUltimasDiezOfertasParaMostrarDesde( ofertasFinales, indiceInicial, usuario,  belleza, hoteles, restaurante);
		}		
	}

	public double getDistanciaMaximaEntreLasCategoriasDeUsuarioyOfertas()
	{
		return distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas;
	}

	public void setDistanciaMaximaEntreLasCategoriasDeUsuarioyOfertas( double distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas)
	{
		this.distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas = distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas;
	}
	
}
