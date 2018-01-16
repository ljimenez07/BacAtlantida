package com.ncubo.bambu.bl;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.ncubo.bambu.dao.BandejaDeMensajesDao;
import com.ncubo.bambu.dao.UsuarioBambuDao;
import com.ncubo.bambu.data.BandejaDeMensaje;
import com.ncubo.bambu.data.BandejaDeMensaje.Atributo;
import com.ncubo.bambu.util.Fecha;
import com.ncubo.bambu.data.UsuarioBambu;

@Component
public class BandejaDeMensajeBL
{
	
	public BandejaDeMensaje obtenerBandeja(int idBandejaMensaje)
	{
		return obtenerBandeja(idBandejaMensaje, 0);
	}
	
	public BandejaDeMensaje obtenerBandeja(int idBandejaMensaje, int idUsuarioEnSesion)
	{
		BandejaDeMensaje bandeja = new BandejaDeMensajesDao().obtenerBandeja(idBandejaMensaje);
		UsuarioBambu usuarioEnSesion = new UsuarioBambuDao().obtenerUsuario(idUsuarioEnSesion);
		bandeja.establecerUsuarioCreador(usuarioEnSesion);
		
		if (bandeja != null && bandeja.obtenerIdUsuarioEncargado() > 0)
		{
			UsuarioBambu usuarioEncargado = new UsuarioBambuDao().obtenerUsuario(bandeja.obtenerIdUsuarioEncargado());
			bandeja.establecerUsuarioCreador(usuarioEncargado);
		}
		return bandeja;
	}
	
	public ArrayList<BandejaDeMensaje> obtenerMensajes()
	{
		return new BandejaDeMensajesDao().obtenerMensajes();
	}
	
	public boolean actualizarEncargado(int idBandeja, int idUsuarioCreador, int idEncargado)
	{
		HashMap<Atributo, Object> atributos = new HashMap<>();
		atributos.put(Atributo.ID_ENCARGADO, idEncargado);
		
		HashMap<Atributo, Object> where = new HashMap<>();
		where.put(Atributo.ID_BANDEJA, idBandeja);
		where.put(Atributo.ID_CREADOR, idUsuarioCreador);
		return new BandejaDeMensajesDao().actualizar(atributos, where);
	}

	public boolean finalizarMensaje(int idBandejaMensaje, int idCreador, String justificacion, boolean esRequeridoEnviarCorreo)
	{
		HashMap<Atributo, Object> atributos = new HashMap<>();
		atributos.put(Atributo.PROCESADO, true);
		atributos.put(Atributo.MOTIVO_FINALIZACION, justificacion);
		atributos.put(Atributo.FECHA_MODIFICACION, Fecha.obtenerCadenaFechaActual());
		
		HashMap<Atributo, Object> where = new HashMap<>();
		where.put(Atributo.ID_BANDEJA, idBandejaMensaje);
		where.put(Atributo.ID_CREADOR, idCreador);
		
		return new BandejaDeMensajesDao().actualizar(atributos, where);
	}
}
