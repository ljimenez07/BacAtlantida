package com.ncubo.logicaDeConversaciones;

import java.util.ArrayList;
import java.util.List;

import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.partesDeLaConversacion.Contenido;
import com.ncubo.chatbot.partesDeLaConversacion.Tema;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.partesDeLaConversacion.Temas;
import com.ncubo.chatbot.watson.Entidades;
import com.ncubo.chatbot.watson.Intenciones;

//TODO: aunque ya veo 1/2 claro... me huele mal que parezca plantilla.
public class TemarioDelBancoAtlantida extends Temario{
	
	protected TemarioDelBancoAtlantida(String pathXML) {
		super(pathXML);
		// TODO Auto-generated constructor stub
	}

	private Tema saludo()
	{
		Tema resultado = new Tema
		(
			"saludo",
			"Saludo",
			"BAIntents",
			true,
			"BAIntents",
			frase("saludoGeneral"),
			frase("saludar"),
			frase("saludarConNombre"),
			frase("saludoPreguntar")
		);
		return resultado;
	}
	
	private Tema quiereSaldo()
	{
		List<String> variables = new ArrayList<>();
		variables.add("estaLogueado");
		
		Tema resultado = new Tema
		(
			"quiereSaldo",
			"Saldo",
			"BATemas",
			true,
			"saldo",
			variables,
			frase("quiereSaldoTarjetaCredito"),
			frase("saldoCredito"),
			frase("saldoCuentaAhorros"),
			frase("noTengoSaldo"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("necesitaLogin")
		);
		return resultado;
	}
	
	private Tema quiereDisponible()
	{
		List<String> variables = new ArrayList<>();
		variables.add("estaLogueado");
		
		Tema resultado = new Tema
		(
			"quiereDisponible",
			"Disponible",
			"BATemas",
			true,
			"disponible",
			variables,
			frase("quiereDisponibleTarjetaCredito"),
			frase("disponibleCredito"),
			frase("disponibleCuentaAhorros"),
			frase("disponiblePuntos"),
			frase("disponibleMillas"),
			frase("noTengoDisponible"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("necesitaLogin")
		);
		return resultado;
	}
	
	private Tema quiereTasaDeCambio()
	{
		Tema resultado = new Tema
		(
			"quiereTasaDeCambio",
			"Tasa de cambio",
			"BATemas",
			true,
			"tasa_cambio",
			frase("tasaDolar"),
			frase("tasaEuro"),
			frase("tasaCambio"),
			frase("tasaOtras"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereMovimientos()
	{
		List<String> variables = new ArrayList<>();
		variables.add("estaLogueado");
		
		Tema resultado = new Tema
		(
			"quiereMovimientos",
			"Movimientos",
			"BATemas",
			true,
			"movimientos",
			variables,
			frase("quiereMovimiento"),
			frase("movimientosTarjeta"),
			frase("movimientosCuenta"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("necesitaLogin")
		);
		return resultado;
	}
	
	private Tema quiereAyudaComo()
	{
		Tema resultado = new Tema
		(
			"quiereAyudaComo",
			"Ayuda",
			"BAFAQ",
			true,
			"ayuda_como",
			frase("ayudaLogin"),
			frase("cualProducto"),
			frase("cualProductoAhorro"),
			frase("cualMonedaAhorros"),
			frase("cualMonedaAhorrosKids"),
			frase("requisitosCuentaAhorrosLempiras"),
			frase("requisitosCuentaAhorrosEuros"),
			frase("requisitosCuentaAhorrosDolares"),
			frase("requisitosCuentaAhorrosKidsLempiras"),
			frase("requisitosCuentaAhorrosKidsDolares"),
			frase("requisitosAOL"),
			frase("requisitosPrestamoPersonal"),
			frase("requisitosPrestamoVivienda"),
			frase("requisitosPrestamoVehiculo"),
			frase("cualProductoPrestamo"),
			frase("cualAntecedentes"),
			frase("cualTipoAntecedentesPenales"),
			frase("precioAntecendentesPenalesTrabajo"),
			frase("precioAntecendentesPenalesMatrimonio"),
			frase("precioAntecendentesPenalesEstudio"),
			frase("precioAntecendentesPenalesVarios"),
			frase("precioAntecendentesPoliciales"),
			frase("requisitosMensajitos"),
			frase("requisitosTarjetaDebito"),
			frase("requisitosTarjetaCredito"),
			frase("requisitosChequesPersona"),
			frase("requisitosChequesEmpresas"),
			frase("requisitosChequesGubernamentales"),
			frase("requisitosChequesNoGubernamentales"),
			frase("cualCheques"),
			frase("cualCuenta"),
			frase("cualTarjeta"),
			frase("requisitosEmbajada"),
			frase("requisitosPasaporte"),
			frase("requisitosLicencia"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema fueraDeContexto()
	{
		Tema resultado = new Tema
		(
			"fueraDeContexto",
			"Fuera de contexto",
			"BAIntents",
			true,
			"out_of_scope",
			frase("fueraDeContextoGeneral")
		);
		return resultado;
	}
	
	private Tema noEntendi()
	{
		Tema resultado = new Tema
		(
			"noEntendi",
			"No entiendo",
			"BAIntents",
			true,
			"out_of_scope",
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema despedida()
	{
		Tema resultado = new Tema
		(
			"despedida",
			"Despedida",
			"BAIntents",
			true,
			"despedidas",
			frase("despedida")
		);
		return resultado;
	}		
	
	private Tema saludarConocerte()
	{
		List<String> variables = new ArrayList<>();
		variables.add("leGustaLosHoteles");
		variables.add("leGustaComerAfuera");
		variables.add("sePreocupaPorLaSalud");
		
		Tema resultado = new Tema
		(
			"saludarConocerte",
			"Conocerte",
			"ConocerteGeneral",
			false,
			"conocerte",
			variables,
			frase("saludarConocerte"),
			frase("preguntarPorRestaurantes"),
			frase("preguntarPorRestaurantesSiNo"),
			frase("fueraContextoConocerteDelBanco"),
			frase("fueraContextoConocerteGeneral")
		);
		return resultado;
	}	
	
	private Tema preguntarPorHospedaje()
	{
		List<String> variables = new ArrayList<>();
		variables.add("leGustaLosHoteles");
		variables.add("leGustaComerAfuera");
		variables.add("sePreocupaPorLaSalud");
		
		Tema resultado = new Tema
		(
			"preguntarPorHospedaje",
			"Hospedaje",
			"ConocerteGeneral",
			false,
			"conocerte",
			variables,
			frase("preguntarPorHospedaje"),
			frase("preguntarPorHospedajeSiNo"),
			frase("fueraContextoConocerteDelBanco"),
			frase("fueraContextoConocerteGeneral")
		);
		return resultado;
	}
	
	private Tema preguntarPorBelleza()
	{
		List<String> variables = new ArrayList<>();
		variables.add("leGustaLosHoteles");
		variables.add("leGustaComerAfuera");
		variables.add("sePreocupaPorLaSalud");
		
		Tema resultado = new Tema
		(
			"preguntarPorBelleza",
			"Belleza",
			"ConocerteGeneral",
			false,
			"conocerte",
			variables,
			frase("preguntarPorBelleza"),
			frase("fueraContextoConocerteDelBanco"),
			frase("fueraContextoConocerteGeneral")
		);
		return resultado;
	}
	
	private Tema despedidaConocerte()
	{
		List<String> variables = new ArrayList<>();
		variables.add("leGustaLosHoteles");
		variables.add("leGustaComerAfuera");
		variables.add("sePreocupaPorLaSalud");
		
		Tema resultado = new Tema
		(
			"despedidaConocerte",
			"Despedida del conocerte",
			"ConocerteGeneral",
			false,
			"conocerte",
			variables,
			frase("despedidaConocerte"),
			frase("fueraContextoConocerteDelBanco"),
			frase("fueraContextoConocerteGeneral")
		);
		return resultado;
	}
	
	private Tema quiereAbrirCuenta()
	{
		Tema resultado = new Tema
		(
			"quiereAbrirCuenta",
			"Abrir cuenta",
			"BAFAQ",
			true,
			"abrir_cuenta",			
			frase("cualProductoAhorro"),
			frase("cualMonedaAhorros"),
			frase("cualMonedaAhorrosKids"),
			frase("requisitosCuentaAhorrosLempiras"),
			frase("requisitosCuentaAhorrosEuros"),
			frase("requisitosCuentaAhorrosDolares"),
			frase("requisitosCuentaAhorrosKidsLempiras"),
			frase("requisitosCuentaAhorrosKidsDolares"),
			frase("cualMonedaAhorrosRemesas"),
			frase("requisitosCuentaAhorrosRemesasLempiras"),
			frase("requisitosCuentaAhorrosRemesasDolares"),
			frase("requisitosCuentaAhorrosVirtualRemesasDolares"),
			frase("requisitosChequesPersona"),
			frase("requisitosChequesEmpresas"),
			frase("requisitosChequesGubernamentales"),
			frase("requisitosChequesNoGubernamentales"),
			frase("cualCheques"),
			frase("cualCuenta"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema quiereMontoInicial()
	{
		Tema resultado = new Tema
		(
			"quiereMontoInicial",
			"Precio/costo de productos",
			"BAFAQ",
			true,
			"precio",
			frase("montoInicialCuentaAhorrosLempiras"),
			frase("montoInicialCuentaAhorrosDolares"),
			frase("montoInicialCuentaAhorrosEuros"),
			frase("montoInicialCuentaAhorrosKidsLempiras"),
			frase("montoInicialCuentaAhorrosKidsDolares"),
			frase("montoInicialCuentaAhorrosRemesasLempiras"),
			frase("montoInicialCuentaAhorrosRemesasDolares"),
			frase("montoInicialCuentaAhorrosVirtualRemesasDolares"),
			frase("cualProducto"),
			frase("cualProductoAhorro"),
			frase("cualMonedaAhorros"),
			frase("cualMonedaAhorrosKids"),
			frase("cualAntecedentes"),
			frase("cualTipoAntecedentesPenales"),
			frase("precioAntecendentesPenalesTrabajo"),
			frase("precioAntecendentesPenalesMatrimonio"),
			frase("precioAntecendentesPenalesEstudio"),
			frase("precioAntecendentesPenalesVarios"),
			frase("precioAntecendentesPoliciales"),
			frase("precioPasaporte"),
			frase("precioLicencia"),
			frase("precioEmbajada"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema quiereSaldoMinimo()
	{
		Tema resultado = new Tema
		(
			"quiereSaldoMinimo",
			"Saldo mínimo en cuentas",
			"BAFAQ",
			true,
			"saldo_minimo",
			frase("saldoMinimoCuentaAhorrosLempiras"),
			frase("saldoMinimoCuentaAhorrosDolares"),
			frase("saldoMinimoCuentaAhorrosEuros"),
			frase("saldoMinimoCuentaAhorrosKidsLempiras"),
			frase("saldoMinimoCuentaAhorrosKidsDolares"),
			frase("saldoMinimoCuentaAhorrosRemesasLempiras"),
			frase("saldoMinimoCuentaAhorrosRemesasDolares"),
			frase("saldoMinimoCuentaAhorrosVirtualRemesasDolares"),
			frase("cualProducto"),
			frase("cualProductoAhorro"),
			frase("cualMonedaAhorros"),
			frase("cualMonedaAhorrosKids"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema quiereRequisitos()
	{
		Tema resultado = new Tema
		(
			"quiereRequisitos",
			"Requisitos",
			"BAFAQ",
			true,
			"requisitos",
			frase("cualProducto"),
			frase("cualProductoAhorro"),
			frase("cualMonedaAhorros"),
			frase("cualMonedaAhorrosKids"),
			frase("requisitosCuentaAhorrosLempiras"),
			frase("requisitosCuentaAhorrosEuros"),
			frase("requisitosCuentaAhorrosDolares"),
			frase("requisitosCuentaAhorrosKidsLempiras"),
			frase("requisitosCuentaAhorrosKidsDolares"),
			frase("requisitosAOL"),
			frase("requisitosPrestamoPersonal"),
			frase("requisitosPrestamoVivienda"),
			frase("requisitosPrestamoVehiculo"),
			frase("cualProductoPrestamo"),
			frase("cualAntecedentes"),
			frase("cualTipoAntecedentesPenales"),
			frase("precioAntecendentesPenalesTrabajo"),
			frase("precioAntecendentesPenalesMatrimonio"),
			frase("precioAntecendentesPenalesEstudio"),
			frase("precioAntecendentesPenalesVarios"),
			frase("precioAntecendentesPoliciales"),
			frase("requisitosMensajitos"),
			frase("requisitosTarjetaDebito"),
			frase("requisitosTarjetaCredito"),
			frase("requisitosChequesPersona"),
			frase("requisitosChequesEmpresas"),
			frase("requisitosChequesGubernamentales"),
			frase("requisitosChequesNoGubernamentales"),
			frase("cualCheques"),
			frase("cualCuenta"),
			frase("cualTarjeta"),
			frase("requisitosEmbajada"),
			frase("requisitosPasaporte"),
			frase("requisitosLicencia"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema quiereReportarExtravio()
	{
		Tema resultado = new Tema
		(
			"quiereReportarExtravio",
			"Reportar extravío",
			"BAFAQ",
			true,
			"reportar_extravio",
			frase("reportarExtravio"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema quiereAgradecer()
	{
		Tema resultado = new Tema
		(
			"quiereAgradecer",
			"Agradecer",
			"BAFAQ",
			true,
			"agradecimiento",
			frase("responderAgradecimiento"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	private Tema quiereSaberTelefonos()
	{
		Tema resultado = new Tema
		(
			"quiereSaberTelefonos",
			"Conocer números telefónicos",
			"BAFAQ",
			true,
			"saber_numeros",
			frase("numerosCallCenter"),
			frase("numerosAgencias"),
			frase("cualTelefono"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta"),
			frase("noEntendi")
		);
		return resultado;
	}
	
	@Override
	protected void cargarTemario(Temas temasDelDiscurso){
		
		System.out.println("Cargando temario ...");
		// Conocerte
		temasDelDiscurso.add(saludarConocerte());
		temasDelDiscurso.add(preguntarPorHospedaje());
		temasDelDiscurso.add(preguntarPorBelleza());
		temasDelDiscurso.add(despedidaConocerte());
		
		temasDelDiscurso.add(saludo());
		temasDelDiscurso.add(quiereSaldo());
		temasDelDiscurso.add(quiereDisponible());
		temasDelDiscurso.add(quiereTasaDeCambio());
		temasDelDiscurso.add(quiereMovimientos());
		temasDelDiscurso.add(fueraDeContexto());
		temasDelDiscurso.add(quiereAyudaComo());
		temasDelDiscurso.add(despedida());
		temasDelDiscurso.add(quiereAbrirCuenta());
		temasDelDiscurso.add(quiereMontoInicial());
		temasDelDiscurso.add(quiereSaldoMinimo());
		temasDelDiscurso.add(quiereRequisitos());
		temasDelDiscurso.add(quiereReportarExtravio());
		temasDelDiscurso.add(noEntendi());
		temasDelDiscurso.add(quiereAgradecer());
		temasDelDiscurso.add(quiereSaberTelefonos());
	}
	
	
	
	public void cargarIntenciones(List<Intenciones> intenciones)
	{
		//intenciones.add (Intencion.get("WANT_HOUSE"));
		//intenciones.add (Intencion.get("GOODBYE"));
	}

	@Override
	protected void cargarEntidades(List<Entidades> entidades) {
		// TODO Auto-generated method stub
		//entidades.add (Entidad.get("INTEREST"));
		//entidades.add (Entidad.get("AFFIRMATIONS"));
		//entidades.add (Entidad.get("COUNTRIES"));
		//entidades.add (Entidad.get("LOCATIONS"));
		//entidades.add (Entidad.get("PLEASURES"));
	}
	
	@Override
	protected Contenido cargarContenido(String path){
		return new ContenidoDelBancoAtlantida(path);
	}

	@Override
	protected void cargarDependencias(Temas temasDelDiscurso){
		temasDelDiscurso.get(1).dependeDe(temasDelDiscurso.get(0));
		temasDelDiscurso.get(2).dependeDe(temasDelDiscurso.get(0));
		temasDelDiscurso.get(3).dependeDe(temasDelDiscurso.get(1)).dependeDe(temasDelDiscurso.get(2));
	}
	
	public static void main(String argv[]) {
		TemarioDelBancoAtlantida temario = new TemarioDelBancoAtlantida(Constantes.PATH_ARCHIVO_DE_CONFIGURACION_BA);
		temario.buscarTema(Constantes.FRASE_SALUDO);
	}
}
