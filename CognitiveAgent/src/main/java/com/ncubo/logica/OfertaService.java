package com.ncubo.logica;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.JSONObject;
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
		if( usuario == null || 
			! usuario.getEstaLogueado() || 
			! usuarioDao.yaContestoElConocerteAlmenosUnaVez(usuario) )
		{
			return ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
		}
		
		Categorias  categoriasDelUsuario = usuarioDao.obtenerLasCategoriasDeUnUsuario(usuario);
		usuario.setCategorias(categoriasDelUsuario);
		
		List<Oferta> ofertasFinales = ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario, distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas);
		boolean noHayOfertasParaSusGustosPorLotantoListarLasMasRecientes =ofertasFinales.size() ==0 && indiceInicial.getPagina() == 1;
		
		if( noHayOfertasParaSusGustosPorLotantoListarLasMasRecientes )
		{
			return ofertaDao.obtenerUltimasDiezOfertasParaMostrarDesde(indiceInicial, usuario);
		}
		
		return ofertasFinales;
	}
		

	public double getDistanciaMaximaEntreLasCategoriasDeUsuarioyOfertas()
	{
		return distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas;
	}

	public void setDistanciaMaximaEntreLasCategoriasDeUsuarioyOfertas( double distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas)
	{
		this.distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas = distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas;
	}

	public int obtenerCantidadDeOfertasParaMostrar(Usuario usuario) throws ClassNotFoundException, SQLException {
		if( usuario == null || 
				! usuario.getEstaLogueado() || 
				! usuarioDao.yaContestoElConocerteAlmenosUnaVez(usuario) )
		{
			return ofertaDao.obtenerCantidadDeOfertasParaMostrar();
		}
		else
		{
			return ofertaDao.obtenerCantidadDeOfertasParaMostrar(usuario, distanciaMaximaEntreLasCategoriasDeUsuarioyOfertas);
		}
	}
	
}
