package com.ncubo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Stub 
{
	@RequestMapping(value = "/Ecommerce/getOwnAccounts", method = RequestMethod.POST)
	public @ResponseBody String getOwnAccounts(@RequestBody String requestXML)
	{
		String respuestaJson = "<?xml version=\"1.0\"?><Respuesta> <estado> <codigo>0000</codigo> <descripcion>Satisfactorio</descripcion> <detalleTecnico/> <tipo>S1</tipo> <fecha>20160503</fecha> </estado> <productoColeccion> <cuentaColeccion> <cuentaItem> <codigo/> <banco/> <sucursal/> <agencia/> <moneda>LPS</moneda> <aplicacion/> <tipo>2</tipo> <saldoColeccion> <contable>30344.62</contable> <diferido>0.0</diferido> <disponibleLps>30344.62</disponibleLps> <disponibleUsd>0.0</disponibleUsd> <disponibleEur>0.0</disponibleEur> <retenido>0.0</retenido> <saldoActualLps>0.0</saldoActualLps> <saldoMoraLps>0.0</saldoMoraLps> <saldoMoraUsd>0.0</saldoMoraUsd> <saldoAnteriorLps>0.0</saldoAnteriorLps> <saldoAnteriorUsd>0.0</saldoAnteriorUsd> <saldoAlCorteLps>0.0</saldoAlCorteLps> <saldoAlCorteUsd>0.0</saldoAlCorteUsd> </saldoColeccion> <alias>CUENTA AHORRO REGULAR M/N EMPRESAS</alias> <fecha1>20160503</fecha1> <fecha2>20160503</fecha2> <fecha3>20160503</fecha3> <mensaje>F</mensaje> <valido>S</valido> <numeroCuenta>1204337420</numeroCuenta> <periodoColeccion> <periodoItem> <periodo>00</periodo> <descripcion>Periodo Finalizando el 31/12/16</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>1204337420|00|Periodo Finalizando el 31/12/16|</mensaje> <valido>S</valido> </periodoItem> <periodoItem> <periodo>01</periodo> <descripcion>Periodo Finalizando el 31/12/15</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>1204337420|01|Periodo Finalizando el 31/12/15|</mensaje> <valido>S</valido> </periodoItem> <periodoItem> <periodo>90</periodo> <descripcion>Transacciones del Dia 19/04/16</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>1204337420|90|Transacciones del Dia 19/04/16|</mensaje> <valido>S</valido> </periodoItem> </periodoColeccion> <contableMayor/> </cuentaItem> <cuentaItem> <codigo/> <banco/> <sucursal/> <agencia/> <moneda>USD</moneda> <aplicacion/> <tipo>2</tipo> <saldoColeccion> <contable>418.97</contable> <diferido>0.0</diferido> <disponibleLps>0.0</disponibleLps> <disponibleUsd>418.97</disponibleUsd> <disponibleEur>0.0</disponibleEur> <retenido>0.0</retenido> <saldoActualLps>0.0</saldoActualLps> <saldoMoraLps>0.0</saldoMoraLps> <saldoMoraUsd>0.0</saldoMoraUsd> <saldoAnteriorLps>0.0</saldoAnteriorLps> <saldoAnteriorUsd>0.0</saldoAnteriorUsd> <saldoAlCorteLps>0.0</saldoAlCorteLps> <saldoAlCorteUsd>0.0</saldoAlCorteUsd> </saldoColeccion> <alias>CUENTA CHEQUES LPS</alias> <fecha1>20160505</fecha1> <fecha2>20160505</fecha2> <fecha3>20160505</fecha3> <mensaje>T</mensaje> <valido>I</valido> <numeroCuenta>1180000026</numeroCuenta> <periodoColeccion> <periodoItem> <periodo>00</periodo> <descripcion>Periodo Finalizando el 31/05/16</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>1180000026|00|Periodo Finalizando el 31/05/16|</mensaje> <valido>S</valido> </periodoItem> <periodoItem> <periodo>90</periodo> <descripcion>Transacciones del Dia 03/05/16</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>1180000026|90|Transacciones del Dia 03/05/16|</mensaje> <valido>S</valido> </periodoItem> <periodoItem> <periodo>91</periodo> <descripcion>Transacciones del Dia 03/03/16</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>1180000026|91|Transacciones del Dia 03/03/16|</mensaje> <valido>S</valido> </periodoItem> </periodoColeccion> <contableMayor/> </cuentaItem> </cuentaColeccion> <tarjetaColeccion> <tarjetaItem> <codigo/> <banco/> <agencia/> <moneda>LPS</moneda> <aplicacion/> <tipo>4</tipo> <saldoColeccion> <contable>0.0</contable> <diferido>0.0</diferido> <disponibleLps>195.38</disponibleLps> <disponibleUsd>0.0</disponibleUsd> <disponibleEur>0.0</disponibleEur> <retenido>0.0</retenido> <saldoActualLps>3195.38</saldoActualLps> <saldoMoraLps>0.0</saldoMoraLps> <saldoMoraUsd>0.0</saldoMoraUsd> <saldoAnteriorLps>3103.86</saldoAnteriorLps> <saldoAnteriorUsd>0.0</saldoAnteriorUsd> <saldoAlCorteLps>0.0</saldoAlCorteLps> <saldoAlCorteUsd>0.0</saldoAlCorteUsd> </saldoColeccion> <alias>PRUEBA DE PCI</alias> <mensaje>F</mensaje> <valido>S</valido> <numeroTarjeta>528040020025</numeroTarjeta> <periodoColeccion> <periodoItem> <periodo>00</periodo> <descripcion>Estado de Cuenta al 15/07/15</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>528040020025|00|Estado de Cuenta al 15/07/15|</mensaje> <valido>S</valido> </periodoItem> <periodoItem> <periodo>90</periodo> <descripcion>Movimientos al dia 19/04/16</descripcion> <fechaInicio/> <fechaFinal/> <mensaje>528040020025|90|Movimientos al dia 19/04/16|</mensaje> <valido>S</valido> </periodoItem> </periodoColeccion> <fechaCorte>20130808</fechaCorte> <fechaPago>20130902</fechaPago> <fechaTrama/> <pagoContadoLps>3195.38</pagoContadoLps> <cuotaMesLps>370.00</cuotaMesLps> <limiteLps>0.00</limiteLps> <cargoMesLps>0.00</cargoMesLps> <limiteUsd>0.00</limiteUsd> <pagoContadoUsd>0.00</pagoContadoUsd> <cuotaMesUsd>0.00</cuotaMesUsd> <cargoMesUsd>0.00</cargoMesUsd> <referenciaTarjeta>0000528040020025</referenciaTarjeta> <fechaConsumo/> <secuencia>0</secuencia> <descripcion>4,F</descripcion> <simbolo>LPS</simbolo> <cargoLps>PRUEBA DE PCI</cargoLps> <cargoUsd>0.00</cargoUsd> <fechaUltimoCorte/> </tarjetaItem> </tarjetaColeccion> </productoColeccion> </Respuesta>";
		return respuestaJson;
	}
	
	@RequestMapping(value = "/Ecommerce/getConversionRates", method = RequestMethod.POST)
	public @ResponseBody String prelogin(@RequestBody String requestXML)
	{
		String respuestaJson = "<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"> <SOAP:Header/> <SOAP:Body> <ns1:MT_TasaCambioResponse xmlns:ns1=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <Respuesta> <estado> <codigo>0000</codigo> <descripcion>Satisfactorio</descripcion> <detalleTecnico/> <tipo>S1</tipo> <fecha>20150909</fecha> </estado> <tasaCambioColeccion> <tasaCambioItem> <moneda>USD</moneda> <compra>22.0606</compra> <venta>21.9072</venta> </tasaCambioItem> <tasaCambioItem> <moneda>EUR</moneda> <compra>25.6314</compra> <venta>23.5502</venta> </tasaCambioItem> </tasaCambioColeccion> </Respuesta> </ns1:MT_TasaCambioResponse> </SOAP:Body> </SOAP:Envelope>";
		return respuestaJson;
	}
	
	@RequestMapping(value = "/Ecommerce/login", method = RequestMethod.POST)
	public @ResponseBody String login(@RequestBody String requestXML)
	{
		String respuestaJson = 
				"<SOAP:Envelope xmlns:SOAP=\"http://schemas.xmlsoap.org/soap/envelope/\"> "
					+ "<SOAP:Header/> "
					+ "<SOAP:Body> "
						+ "<Respuesta> "
							+ "<estado>"
								+ "	<codigo>0000</codigo>"
								+ "<descripcion>Satisfactorio</descripcion> "
								+ "<detalleTecnico/> "
								+ "<tipo>S1</tipo> "
								+ "<fecha>20150909</fecha>"
							+ "</estado> "
							+ "<validaPreLoginColeccion>"
								+"<valido>S</valido>"
								+ "<usuarioId>OPAGOAG025</usuarioId>"
								+ "<usuarioNombre>Oscar Orlando Pagoaca Argueta</usuarioNombre>"
								+ "<llaveSession>sdfaerf34tgergvrevrev</llaveSession>"
							+ "</validaPreLoginColeccion>"
						+ "</Respuesta> "
					+ "</SOAP:Body>"
				+ "</SOAP:Envelope>";
		return respuestaJson;
	}
}
