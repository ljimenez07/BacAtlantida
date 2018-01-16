package com.ncubo.bambu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.Cotizacion;
import com.ncubo.bambu.data.EEtapaCotizacion;
import com.ncubo.db.ConexionALaDB;

@Component
public class CotizacionDao
{
	private final static String NOMBRE_TABLA = "cotizacion";
	
	private enum atributo
	{
		ID_COTIZACION("idCotizacion"),
		ID_LLAMADA("idLlamada"),
		NOMBRE_OPORTUNIDAD("NombreOportunidad"),
		FECHA_COTIZACION("FechaCotizacion"),
		ID_COMPANIA("idCompania"),
		DESCRIPCION("Descripcion"),
		FECHA_CIERRE("FechaCierre"),
		MANTENIMIENTO("Mantenimiento"),
		MES_CIERRE("MesCierre"),
		MONTO("Monto"),
		PROBABILIDAD("Probabilidad"),
		ID_TIPO_COTIZACION("idTipoCotizacion"),
		NO_FACTURA("NoFactura"),
		ID_DIVISA("idDivisa"),
		CENTRO_COSTOS("CentroCostos"),
		MESES_GARANTIA("MesesGarantia"),
		STERIL_AIRE("SterilAire"),
		MONTO_STERIL_AIRE("MontoSterilAire"),
		ID_ZONA("idZona"),
		ID_ETAPA_COTIZACION("idEtapaCotizacion"),
		ID_ASESOR("idAsesor"),
		ID_TIPO_PRODUCTO("idTipoProducto"),
		ID_TIPO_APROBACION("idTipoAprobacion"),
		ID_TIPO_DESCUENTO("idTipoDescuento"),
		CREADO_POR("CreadoPor"),
		MODIFICADO_POR("ModificadoPor"),
		FECHA_CREACION("FechaCreacion"),
		FECHA_MODIFICACION("FechaModificacion"),
		ID_PROPIETARIO("idPropietario"),
		NUMERO_REFERANCIA("NumeroReferencia"),
		ORDEN_COMPRA("OrdenCompra"),
		ID_DENEGADO_PORQUE("idDenegadoPorQue"),
		ID_DENEGADO_CONTRA_QUIEN("idDenegadoContraQuien"),
		ID_DENEGADO_QUE_MARCA("idDenegadoQueMarca"),
		REGISTRO_BORRADO("RegistroBorrado"),
		BORRADO_POR("BorradoPor"),
		FECHA_BORRADO("FechaBorrado"),
		NUMERO_VERSION("NumeroVersion"),
		FECHA_ENVIO("FechaEnvio"),
		FECHA_COMPROMISO_CLIENTE("FechaCompromisoCliente"),
		ID_SEGMENTO("idSegmento"),
		ID_DEPARTAMENTO("idDepartamento"),
		FECHA_FIN("FechaFin"),
		DURACION_EN_MILLISEGUNDOS("DuracionEnMilisegundos"),
		ID_PERSONA("idPersona"),
		NUMERO_PEDIDO("NumeroPedido"),
		CODIGO_CHAT("codigoChat"),
		FECHA_DE_APROVACION("fechaDeAprobacion");
		
		private String nombreAtributo;
		
		private atributo(String nombreAtributo)
		{
			this.nombreAtributo = nombreAtributo;
		}
		
		public String toString()
		{
			return nombreAtributo;
		}
	}
	
	public List<Cotizacion> obtenerCotizaciones(int idClientePersona, EEtapaCotizacion etapa) throws SQLException, ClassNotFoundException
	{
		List<Cotizacion> cotizaciones = new ArrayList<Cotizacion>();
		String query = "SELECT * FROM " + NOMBRE_TABLA + " WHERE "
				+ atributo.ID_PERSONA.toString() + " = ? AND "
				+ atributo.REGISTRO_BORRADO.toString() + " != true AND "
				+ atributo.ID_ETAPA_COTIZACION.toString() + " = ?";
		
		final ConexionALaDB conALaDb = new ConexionALaDB("localhost:3306", "bambu", "root", "root");
		final Connection con = conALaDb.openConBD();
		final PreparedStatement ps = con.prepareStatement(query);
		ps.setInt(1, idClientePersona);
		ps.setInt(2, etapa.getId());
		final ResultSet rs = ps.executeQuery();
		
		while(rs.next())
		{
			Cotizacion cotizacion = new Cotizacion();
			cotizacion.establecerBorradoPor(rs.getInt(atributo.BORRADO_POR.toString()));
			cotizacion.establecerCentroCostos(rs.getString(atributo.CENTRO_COSTOS.toString()));
			cotizacion.establecerCodigoChat(rs.getString(atributo.CODIGO_CHAT.toString()));
			cotizacion.establecerCreadoPor(rs.getInt(atributo.CREADO_POR.toString()));
			cotizacion.establecerDescripcion(rs.getString(atributo.DESCRIPCION.toString()));
			cotizacion.establecerDuracionEnMillisegundos(rs.getFloat(atributo.DURACION_EN_MILLISEGUNDOS.toString()));
			cotizacion.establecerFechaBorrado(rs.getString(atributo.FECHA_BORRADO.toString()));
			cotizacion.establecerFechaCierre(rs.getString(atributo.FECHA_CIERRE.toString()));
			cotizacion.establecerFechaCompromisoCliente(rs.getString(atributo.FECHA_COMPROMISO_CLIENTE.toString()));
			cotizacion.establecerFechaCotizacion(rs.getString(atributo.FECHA_COTIZACION.toString()));
			cotizacion.establecerFechaCreacion(rs.getString(atributo.FECHA_CREACION.toString()));
			cotizacion.establecerFechaDeAprovacion(rs.getString(atributo.FECHA_DE_APROVACION.toString()));
			cotizacion.establecerFechaEnvio(rs.getString(atributo.FECHA_ENVIO.toString()));
			cotizacion.establecerFechaFin(rs.getString(atributo.FECHA_FIN.toString()));
			cotizacion.establecerFechaModificacion(rs.getString(atributo.FECHA_MODIFICACION.toString()));
			cotizacion.establecerIdAsesor(rs.getInt(atributo.ID_ASESOR.toString()));
			cotizacion.establecerIdCompania(rs.getInt(atributo.ID_COMPANIA.toString()));
			cotizacion.establecerIdCotizacion(rs.getInt(atributo.ID_COTIZACION.toString()));
			cotizacion.establecerIdDenegadoContraQuien(rs.getInt(atributo.ID_DENEGADO_CONTRA_QUIEN.toString()));
			cotizacion.establecerIdDenegadoPorQue(rs.getInt(atributo.ID_DENEGADO_PORQUE.toString()));
			cotizacion.establecerIdDenegadoQueMarca(rs.getInt(atributo.ID_DENEGADO_QUE_MARCA.toString()));
			cotizacion.establecerIdDepartamento(rs.getInt(atributo.ID_DEPARTAMENTO.toString()));
			cotizacion.establecerIdDivisa(rs.getInt(atributo.ID_DIVISA.toString()));
			cotizacion.establecerIdEtapaCotizacion(rs.getInt(atributo.ID_ETAPA_COTIZACION.toString()));
			cotizacion.establecerIdLlamada(rs.getInt(atributo.ID_LLAMADA.toString()));
			cotizacion.establecerIdPersona(rs.getInt(atributo.ID_PERSONA.toString()));
			cotizacion.establecerIdPropietario(rs.getInt(atributo.ID_PROPIETARIO.toString()));
			cotizacion.establecerIdSegmento(rs.getInt(atributo.ID_SEGMENTO.toString()));
			cotizacion.establecerIdTipoAprobacion(rs.getInt(atributo.ID_TIPO_APROBACION.toString()));
			cotizacion.establecerIdTipoCotizacion(rs.getInt(atributo.ID_TIPO_COTIZACION.toString()));
			cotizacion.establecerIdTipoDescuento(rs.getInt(atributo.ID_TIPO_DESCUENTO.toString()));
			cotizacion.establecerIdTipoProducto(rs.getInt(atributo.ID_TIPO_PRODUCTO.toString()));
			cotizacion.establecerIdZona(rs.getInt(atributo.ID_ZONA.toString()));
			cotizacion.establecerMantenimiento(rs.getBoolean(atributo.MANTENIMIENTO.toString()));
			cotizacion.establecerMesCierre(rs.getString(atributo.MES_CIERRE.toString()));
			cotizacion.establecerMesesGarantia(rs.getFloat(atributo.MESES_GARANTIA.toString()));
			cotizacion.establecerModificadoPor(rs.getInt(atributo.MODIFICADO_POR.toString()));
			cotizacion.establecerMonto(rs.getFloat(atributo.MONTO.toString()));
			cotizacion.establecerMontoSterilAire(rs.getFloat(atributo.MONTO_STERIL_AIRE.toString()));
			cotizacion.establecerNoFactura(rs.getString(atributo.NO_FACTURA.toString()));
			cotizacion.establecerNombreOportunidad(rs.getString(atributo.NOMBRE_OPORTUNIDAD.toString()));
			cotizacion.establecerNumeroPedido(rs.getString(atributo.NUMERO_PEDIDO.toString()));
			cotizacion.establecerNumeroReferancia(rs.getString(atributo.NUMERO_REFERANCIA.toString()));
			cotizacion.establecerNumeroVersion(rs.getInt(atributo.NUMERO_VERSION.toString()));
			cotizacion.establecerOrdenCompra(rs.getString(atributo.ORDEN_COMPRA.toString()));
			cotizacion.establecerProbabilidad(rs.getInt(atributo.PROBABILIDAD.toString()));
			cotizacion.establecerRegistroBorrado(rs.getBoolean(atributo.REGISTRO_BORRADO.toString()));
			cotizacion.establecerSterilAire(rs.getBoolean(atributo.STERIL_AIRE.toString()));
			
			cotizaciones.add(cotizacion);
		}

		conALaDb.closeConBD();
		return cotizaciones;
	}
}
