package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.UsuarioBambu;
import com.ncubo.db.ConexionALaDB;

@Component
public class UsuarioBambuDao
{
	private final String NOMBRE_TABLA = "usuario";
	private final static Logger LOG;
	
	static
	{
		LOG = Logger.getLogger(UsuarioBambuDao.class);
	}

	public enum atributos
	{
		ID_USUARIO("idUsuario"),
		NOMBRE("Nombre"),
		APELLIDOS("Apellidos"),
		ID_DEPARTAMENTO("idDepartamento"),
		ID_FUNCION("idFuncion"),
		EMAIL("Email"),
		TELEFONO("Telefono"),
		EXTENSION("Extension"),
		ID_PERFIL("idPerfil"),
		LOGIN("Login"),
		PASSWORD("Password"),
		ACTIVO("Activo"),
		IMAGEN("Imagen"),
		CREADO_POR("CreadoPor"),
		MODIFICADO_POR("ModificadoPor"),
		FECHA_CREACION("FechaCreacion"),
		FECHA_MODIFICACION("FechaModificacion"),
		ID_EMPRESA_PRINCIPAL("idEmpresaPrincipal"),
		CARGO("Cargo"),
		RECIEN_CREADO("RecienCreado"),
		REGISTRO_BORRADO("RegistroBorrado"),
		BORRADO_POR("BorradoPor"),
		FECHA_BORRADO("FechaBorrado"),
		ID_MARCADOR("idMarcador"),
		SESION_ACTIVA("sessionActiva"),
		ID_VENDEDOR("IdVendedor"),
		EMAIL_FACEBOOK("emailFacebook"),
		PRIMER_NOMBRE_FACEBOOK("primerNombreFacebook"),
		SEGUNDO_NOMBRE_FACEBOOK("segundoNombreFacebook"),
		GENERO_FACEBOOK("generoFacebook"),
		ID_FACEBOOK("idFacebook"),
		APELLIDOS_FACEBOOK("apellidosFacebook"),
		LINK_FACEBOOK("linkFacebook"),
		LUGAR_FACEBOOK("lugarFacebook"),
		NOMBRE_FACEBOOK("nombreFacebook"),
		ZONA_HORARIA_FACEBOOK("zonaHorariaFacebook"),
		HORA_ACTUALIZADA_FACEBOOK("horaActualizadaFacebook"),
		VERIFICADO_FACEBOOK("verificadoFacebook"),
		ID_SIBU("idSibu"),
		ID_COMPANIA_SIBU("idCompaniaSibu"),
		CEDULA("cedula"),
		ID_COMPANIA_NIMBUS("idCompaniaNimbus"),
		FECHA_DEL_ULTIMO_LOGIN("FechaDeUltimoLogin"),
		ID_USUARIO_NIMBUS("idUsuarioNimbus");
		
		private String nombre;
		
		private atributos(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public UsuarioBambu obtenerUsuario(int idUsuario)
	{
		String query = "SELECT * FROM " + NOMBRE_TABLA
					+ " WHERE " + atributos.ID_USUARIO.toString()  + " = " + idUsuario; 
		ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		UsuarioBambu usuario = null;
		try
		{
			Connection con = conALaDb.openConBD();
			ResultSet rs = con.createStatement().executeQuery(query);
			
			if(rs.next())
			{
				usuario = new UsuarioBambu();
				usuario.establecerActivo(rs.getBoolean(atributos.ACTIVO.toString()));
				usuario.establecerApellidos(rs.getString(atributos.APELLIDOS.toString()));
				usuario.establecerApellidosFacebook(rs.getString(atributos.APELLIDOS_FACEBOOK.toString()));
				usuario.establecerBorradoPor(rs.getInt(atributos.BORRADO_POR.toString()));
				usuario.establecerCargo(rs.getString(atributos.CARGO.toString()));
				usuario.establecerCedula(rs.getString(atributos.CEDULA.toString()));
				usuario.establecerCreadorPor(rs.getInt(atributos.CREADO_POR.toString()));
				usuario.establecerEmail(rs.getString(atributos.EMAIL.toString()));
				usuario.establecerEmailFacebook(rs.getString(atributos.EMAIL_FACEBOOK.toString()));
				usuario.establecerExtension(rs.getString(atributos.EXTENSION.toString()));
				usuario.establecerFechaBorrado(rs.getString(atributos.FECHA_BORRADO.toString()));
				usuario.establecerFechaCreacion(rs.getString(atributos.FECHA_CREACION.toString()));
				usuario.establecerFechaModificacion(rs.getString(atributos.FECHA_MODIFICACION.toString()));
				usuario.establecerFechaUltimoLogin(rs.getString(atributos.FECHA_DEL_ULTIMO_LOGIN.toString()));
				usuario.establecerGeneroFacebook(rs.getString(atributos.GENERO_FACEBOOK.toString()));
				usuario.establecerHoraActualizadaFacebook(rs.getString(atributos.HORA_ACTUALIZADA_FACEBOOK.toString()));
				usuario.establecerIdCompaniaNimbus(rs.getString(atributos.ID_COMPANIA_NIMBUS.toString()));
				usuario.establecerIdCompaniaSibu(rs.getInt(atributos.ID_COMPANIA_SIBU.toString()));
				usuario.establecerIdDepartamento(rs.getInt(atributos.ID_DEPARTAMENTO.toString()));
				usuario.establecerIdEmpresaPrincipal(rs.getInt(atributos.ID_EMPRESA_PRINCIPAL.toString()));
				usuario.establecerIdFacebook(rs.getInt(atributos.ID_FACEBOOK.toString()));
				usuario.establecerIdFuncion(rs.getInt(atributos.ID_FUNCION.toString()));
				usuario.establecerIdMarcador(rs.getInt(atributos.ID_MARCADOR.toString()));
				usuario.establecerIdPerfil(rs.getInt(atributos.ID_PERFIL.toString()));
				usuario.establecerIdSibu(rs.getInt(atributos.ID_SIBU.toString()));
				usuario.establecerIdUsuario(rs.getInt(atributos.ID_USUARIO.toString()));
				usuario.establecerIdVendedor(rs.getInt(atributos.ID_VENDEDOR.toString()));
				usuario.establecerImagen(rs.getString(atributos.IMAGEN.toString()));
				usuario.establecerLogin(rs.getString(atributos.LOGIN.toString()));
				usuario.establecerRecienCreado(rs.getBoolean(atributos.RECIEN_CREADO.toString()));
				usuario.establecerSesionActiva(rs.getBoolean(atributos.SESION_ACTIVA.toString()));
				usuario.establecerIdUsuarioNimbus(rs.getInt(atributos.ID_USUARIO_NIMBUS.toString()));
				usuario.establecerLinkFacebook(rs.getString(atributos.LINK_FACEBOOK.toString()));
				usuario.establecerLugarFacebook(rs.getString(atributos.LUGAR_FACEBOOK.toString()));
				usuario.establecerModificadoPor(rs.getInt(atributos.MODIFICADO_POR.toString()));
				usuario.establecerNombre(rs.getString(atributos.NOMBRE.toString()));
				usuario.establecerNombreFacebook(rs.getString(atributos.NOMBRE_FACEBOOK.toString()));
				usuario.establecerPrimerNombreFacebook(rs.getString(atributos.PRIMER_NOMBRE_FACEBOOK.toString()));
				usuario.establecerRegistroBorrado(rs.getBoolean(atributos.REGISTRO_BORRADO.toString()));
				usuario.establecerSegundoNombreFacebook(rs.getString(atributos.SEGUNDO_NOMBRE_FACEBOOK.toString()));
				usuario.establecerTelefono(rs.getString(atributos.TELEFONO.toString()));
				usuario.establecerVerificadoFacebook(rs.getString(atributos.VERIFICADO_FACEBOOK.toString()));
				usuario.establecerZoneHorariaFacebook(rs.getString(atributos.ZONA_HORARIA_FACEBOOK.toString()));
			}
			
		}
		catch(SQLException | ClassNotFoundException e)
		{
			LOG.error("Error", e);
		}
		finally
		{
			try
			{
				conALaDb.closeConBD();
			} 
			catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return usuario;
	}
	
	public ArrayList<UsuarioBambu> listaDeUsuarioSegunFuncion(int idFuncion)
	{
		ArrayList<UsuarioBambu> usuarios = new ArrayList<UsuarioBambu>();
		String query = "SELECT * FROM " + NOMBRE_TABLA
				+ " where " + atributos.ID_FUNCION.toString() + " = " + idFuncion; 
		
		ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		Connection con;
		try
		{
			con = conALaDb.openConBD();
			ResultSet rs = con.createStatement().executeQuery(query);

			while(rs.next())
			{
				UsuarioBambu usuario = new UsuarioBambu();
				usuario.establecerActivo(rs.getBoolean(atributos.ACTIVO.toString()));
				usuario.establecerApellidos(rs.getString(atributos.APELLIDOS.toString()));
				usuario.establecerApellidosFacebook(rs.getString(atributos.APELLIDOS_FACEBOOK.toString()));
				usuario.establecerBorradoPor(rs.getInt(atributos.BORRADO_POR.toString()));
				usuario.establecerCargo(rs.getString(atributos.CARGO.toString()));
				usuario.establecerCedula(rs.getString(atributos.CEDULA.toString()));
				usuario.establecerCreadorPor(rs.getInt(atributos.CREADO_POR.toString()));
				usuario.establecerEmail(rs.getString(atributos.EMAIL.toString()));
				usuario.establecerEmailFacebook(rs.getString(atributos.EMAIL_FACEBOOK.toString()));
				usuario.establecerExtension(rs.getString(atributos.EXTENSION.toString()));
				usuario.establecerFechaBorrado(rs.getString(atributos.FECHA_BORRADO.toString()));
				usuario.establecerFechaCreacion(rs.getString(atributos.FECHA_CREACION.toString()));
				usuario.establecerFechaModificacion(rs.getString(atributos.FECHA_MODIFICACION.toString()));
				usuario.establecerFechaUltimoLogin(rs.getString(atributos.FECHA_DEL_ULTIMO_LOGIN.toString()));
				usuario.establecerGeneroFacebook(rs.getString(atributos.GENERO_FACEBOOK.toString()));
				usuario.establecerHoraActualizadaFacebook(rs.getString(atributos.HORA_ACTUALIZADA_FACEBOOK.toString()));
				usuario.establecerIdCompaniaNimbus(rs.getString(atributos.ID_COMPANIA_NIMBUS.toString()));
				usuario.establecerIdCompaniaSibu(rs.getInt(atributos.ID_COMPANIA_SIBU.toString()));
				usuario.establecerIdDepartamento(rs.getInt(atributos.ID_DEPARTAMENTO.toString()));
				usuario.establecerIdEmpresaPrincipal(rs.getInt(atributos.ID_EMPRESA_PRINCIPAL.toString()));
				usuario.establecerIdFacebook(rs.getInt(atributos.ID_FACEBOOK.toString()));
				usuario.establecerIdFuncion(rs.getInt(atributos.ID_FUNCION.toString()));
				usuario.establecerIdMarcador(rs.getInt(atributos.ID_MARCADOR.toString()));
				usuario.establecerIdPerfil(rs.getInt(atributos.ID_PERFIL.toString()));
				usuario.establecerIdSibu(rs.getInt(atributos.ID_SIBU.toString()));
				usuario.establecerIdUsuario(rs.getInt(atributos.ID_USUARIO.toString()));
				usuario.establecerIdVendedor(rs.getInt(atributos.ID_VENDEDOR.toString()));
				usuario.establecerImagen(rs.getString(atributos.IMAGEN.toString()));
				usuario.establecerLogin(rs.getString(atributos.LOGIN.toString()));
				usuario.establecerRecienCreado(rs.getBoolean(atributos.RECIEN_CREADO.toString()));
				usuario.establecerSesionActiva(rs.getBoolean(atributos.SESION_ACTIVA.toString()));
				usuario.establecerIdUsuarioNimbus(rs.getInt(atributos.ID_USUARIO_NIMBUS.toString()));
				usuario.establecerLinkFacebook(rs.getString(atributos.LINK_FACEBOOK.toString()));
				usuario.establecerLugarFacebook(rs.getString(atributos.LUGAR_FACEBOOK.toString()));
				usuario.establecerModificadoPor(rs.getInt(atributos.MODIFICADO_POR.toString()));
				usuario.establecerNombre(rs.getString(atributos.NOMBRE.toString()));
				usuario.establecerNombreFacebook(rs.getString(atributos.NOMBRE_FACEBOOK.toString()));
				usuario.establecerPrimerNombreFacebook(rs.getString(atributos.PRIMER_NOMBRE_FACEBOOK.toString()));
				usuario.establecerRegistroBorrado(rs.getBoolean(atributos.REGISTRO_BORRADO.toString()));
				usuario.establecerSegundoNombreFacebook(rs.getString(atributos.SEGUNDO_NOMBRE_FACEBOOK.toString()));
				usuario.establecerTelefono(rs.getString(atributos.TELEFONO.toString()));
				usuario.establecerVerificadoFacebook(rs.getString(atributos.VERIFICADO_FACEBOOK.toString()));
				usuario.establecerZoneHorariaFacebook(rs.getString(atributos.ZONA_HORARIA_FACEBOOK.toString()));
				
				usuarios.add(usuario);
			}
		} 
		catch(SQLException | ClassNotFoundException e)
		{
			LOG.error("Error", e);
		}
		finally
		{
			try
			{
				conALaDb.closeConBD();
			} 
			catch (SQLException e)
			{
				LOG.error("Error", e);
			}
		}
		return usuarios;
	}
}
