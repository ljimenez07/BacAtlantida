package com.ncubo.bambu.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ncubo.bambu.data.LibroLlamada;

@Component
public class LibroLlamadoDao
{
	private final static String NOMBRE_TABLA = "librollamada";
	private final static String TABLA_ESTADO_CANDIDATO = "estadocandidato";
	
	private enum atributo
	{
		ID_LLAMADA("idLlamada"),
		DESCRIPCION("Descripción"),
		FECHA_LLAMADA("FechaLlamada"),
		ES_EMERGENCIA("EsEmergecia"),
		ID_CANDIDATO("idCandidato"),
		ID_ESTADOCANDIDATO("idEstadoCandidato"),
		ID_PENDIENTE("idPendiente"),
		ID_ASESOR("idAsesor"),
		CREADO_POR("CreadoPor"),
		MODIFICADO_POR("ModificadoPor"),
		FECHA_CREACION("FechaCreacion"),
		FECHA_MODIFICACION("FechaModificacion"),
		TIEMPO_DURACION("TiempoDuracion"),
		MOTIVO_PENDIENTE("MotivoPendiente"),
		FECHA_PENDIENTE("FechaPendiente"),
		NUMERO_ATENCION("NumeroAtencion"),
		REGISTRO_BORRADO("RegistroBorrado"),
		BORRADO_POR("BorradoPor"),
		FECHA_BORRADO("FechaBorrado"),
		LLAMADA_CONVERTIDA("LlamadaConvertida"),
		FECHA_CONVERSION("FechaConversion"),
		ID_EMPRESA("idEmpresa"),
		ID_DEPARTAMENTO("idDepartamento"),
		CODIGO_CHAT("codigoChat"),
		ID_CARRITO("idCarrito");
		
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
	
	public List<LibroLlamada> obtenerOportunidades(int idCandidato, boolean convertida)
	{
		List<LibroLlamada> oportunidades = new ArrayList<LibroLlamada>();
		return oportunidades;
	}
	
}
