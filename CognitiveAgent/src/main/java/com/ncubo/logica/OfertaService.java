package com.ncubo.logica;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.ncubo.conf.Usuario;
import com.ncubo.dao.OfertaDao;
import com.ncubo.dao.UsuarioDao;
import com.ncubo.data.Belleza;
import com.ncubo.data.Categorias;
import com.ncubo.data.Hotel;
import com.ncubo.data.Indice;
import com.ncubo.data.Oferta;
import com.ncubo.data.Restaurate;
import com.ncubo.util.GestorDeArchivos;

@Component
@ConfigurationProperties("categorias")
public class OfertaService
{
	@Autowired
	private OfertaDao ofertaDao;
	
	@Autowired
	private UsuarioDao usuarioDao;
	private double distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas;
	
	@Autowired
	private GestorDeArchivos gestorDeArchivos;
	
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
			return null;
		}
		else
		{
			ofertas = ofertaDao.filtrarOfertasPorComercioYCategoria(nombreComercio, desde);
		}
		return ofertas;
	}

	@Transactional
	public void insertar(Oferta oferta) throws ClassNotFoundException, SQLException, IOException 
	{
		//TODO transacciones
		ofertaDao.insertar(oferta);
		ofertaDao.insertarCategorias(oferta.getIdOferta(), oferta.getCategorias());
		gestorDeArchivos.textoAAudio( ""+oferta.getIdOferta(), oferta.getDescripcion() );
		
		ArrayList<Usuario>usuarios = usuarioDao.usuarios();
		for(Usuario usuario : usuarios)
		{
			double distanciaActualEntreAmbasCategorias = oferta.distanciaEuclidianaDeCategoria(
					usuario.getCategorias().obtenerCategoriaDeBelleza(), 
					usuario.getCategorias().obtenerCategoriaDeHotel(), 
					usuario.getCategorias().obtenerCategoriaDeRestaurante() );
			
			System.out.println(
				String.format("El usuario %s tiene una distancia %s, con recpecto a la oferta %s.",
				usuario.getIdSesion(),
				distanciaActualEntreAmbasCategorias,
				oferta.getIdOferta())
			);
			
			if( distanciaActualEntreAmbasCategorias <= distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas )
			{
				System.out.println(
					String.format("El usuario %s tendrá una notificación por la oferta %s.",
					usuario.getIdSesion(),
					oferta.getIdOferta())
				);
				
				usuarioDao.marcarComoNoVistoElPopupDeNuevasOfertas( usuario.getIdSesion() );
			}
		}
		
	}
	
	public void modificar(Oferta oferta) throws ClassNotFoundException, SQLException, IOException
	{
		//TODO transacciones
		ofertaDao.modificar(oferta);
		ofertaDao.insertarCategorias(oferta.getIdOferta(), oferta.getCategorias());
		if( oferta.cambioLaDescripcion() )
		{
			gestorDeArchivos.textoAAudio( ""+oferta.getIdOferta(), oferta.getDescripcion() );
		}
	}
	public List<Oferta> obtenerUltimasDiezOfertasParaMostrarDesde(Indice indiceInicial, Usuario usuario) throws Exception
	{

		if( usuario == null || ! usuario.getEstaLogueado() )
		{
			return ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
		}
		
		Categorias  categoriasDelUsuario = usuarioDao.obtenerLasCategoriasDeUnUsuario(usuario);
		usuario.setCategorias(categoriasDelUsuario);
		
		List<Oferta> ofertasFinales = new ArrayList<Oferta>();
				
		obtenerUltimasDiezOfertasParaMostrarDesde( 
				ofertasFinales, 
				indiceInicial, 
				usuario, 
				categoriasDelUsuario.obtenerCategoriaDeBelleza(), 
				categoriasDelUsuario.obtenerCategoriaDeHotel(), 
				categoriasDelUsuario.obtenerCategoriaDeRestaurante()
				);
		
		return ofertasFinales;
		
	}
	
	private void obtenerUltimasDiezOfertasParaMostrarDesde(List<Oferta> ofertasFinales, Indice indiceInicial, Usuario usuario, Belleza belleza, Hotel hoteles, Restaurate restaurante) throws Exception
	{
		List<Oferta> ofertas =  ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
		
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
		}	
		
		if( ofertasFinales.size() < 10)
		{
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
