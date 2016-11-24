package com.ncubo.conf;

import static com.jayway.restassured.RestAssured.given;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.ncubo.data.Consulta;

public class ExtraerDatosWebService {
	protected ExtraerDatosWebService()
	{		
	}
	
	public String[] obtenerSaldoTarjetaCredito(String wsSaldo, String texto, String usuario){
		
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\"><soapenv:Header/> <soapenv:Body> <con:MT_ConsultaSaldo> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId>  <!--Optional:-->  <regionId>?</regionId>   <!--Optional:--> <canalId>?</canalId><!--Optional:-->  <version>?</version> <!--Optional:--> <llaveSesion></llaveSesion>  <!--Optional:--><usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:-->  <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:-->  <pi>?</pi><!--Optional:--><omniCanal>?</omniCanal>  <!--Optional:--> <recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion></identificadorColeccion> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem>  <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:-->    <valor>%s</valor></parametroAdicionalItem>  </parametroAdicionalColeccion> <!--Optional:--> <logColeccion><!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--><identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal>  <!--Optional:--><identificadorRecibo>?</identificadorRecibo> <!--Optional:--><numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:-->   <canalId>?</canalId> <!--Optional:-->  <ambienteId>?</ambienteId> <!--Optional:-->  <transaccionId>?</transaccionId> <!--Optional:--><accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora><!--Optional:--><auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2>  <!--Optional:--> <parametroAdicionalColeccion>  <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>?</tipoRegistro>  <!--Optional:-->   <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion>  <!--Optional:-->  <consultaSaldoColeccion> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <peticionId>?</peticionId> </consultaSaldoColeccion> </con:MT_ConsultaSaldo></soapenv:Body></soapenv:Envelope>";
		
		requestBody = String.format(requestBody, usuario, "4");
		System.out.println(requestBody);
		
		String responseXML = given().body(requestBody).post(wsSaldo).andReturn().asString();
		
		System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
		NodeImpl tarjetaColeccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("tarjetaColeccion");
		
		List<?> tarjetas = tarjetaColeccion.getList("tarjetaItem");
		
		
		String[] saldos = new String[tarjetas.size()];
		
		for(int s = 0; s < tarjetas.size(); s++)
		{
			NodeImpl nodoTarjeta = (NodeImpl) tarjetas.get(s);
			NodeImpl moneda = nodoTarjeta.get("moneda");
			NodeImpl saldo = null;
			System.out.println();
			System.out.println(moneda.toString());
			
			String nuevoTexto = texto;
			if(moneda.toString().equals("USD"))	{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Dólares");	 
				saldo = nodoTarjeta.getNode("saldoColeccion").get("saldoActualUsd");
			}
			if(moneda.toString().equals("EUR"))		
			{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Euros");	
				saldo = nodoTarjeta.getNode("saldoColeccion").get("saldoActualEur");
			}
			if(moneda.toString().equals("LPS"))		
			{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Lempiras");	
				saldo = nodoTarjeta.getNode("saldoColeccion").get("saldoActualLps");
			}
			nuevoTexto = nuevoTexto.replaceAll("%stc", saldo.toString());	
			NodeImpl alias = nodoTarjeta.get("alias");
			NodeImpl numeroTarjeta = nodoTarjeta.get("numeroTarjeta");
			if(!alias.toString().equals("Alias no ingresado") || !alias.toString().equals(""))
				nuevoTexto = nuevoTexto.replaceAll("%ntc", numeroTarjeta.toString()+"-"+alias.toString());
			else nuevoTexto = nuevoTexto.replaceAll("%ntc", numeroTarjeta.toString());	
			
			saldos[s] = nuevoTexto;
		}

		return saldos;
		
	}
	
	public String[] obtenerSaldoCuentaAhorros(String wsSaldo, String texto, String usuario){
		
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\"><soapenv:Header/> <soapenv:Body> <con:MT_ConsultaSaldo> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId>  <!--Optional:-->  <regionId>?</regionId>   <!--Optional:--> <canalId>?</canalId><!--Optional:-->  <version>?</version> <!--Optional:--> <llaveSesion></llaveSesion>  <!--Optional:--><usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:-->  <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:-->  <pi>?</pi><!--Optional:--><omniCanal>?</omniCanal>  <!--Optional:--> <recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion></identificadorColeccion> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem>  <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:-->    <valor>%s</valor></parametroAdicionalItem>  </parametroAdicionalColeccion> <!--Optional:--> <logColeccion><!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--><identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal>  <!--Optional:--><identificadorRecibo>?</identificadorRecibo> <!--Optional:--><numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:-->   <canalId>?</canalId> <!--Optional:-->  <ambienteId>?</ambienteId> <!--Optional:-->  <transaccionId>?</transaccionId> <!--Optional:--><accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora><!--Optional:--><auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2>  <!--Optional:--> <parametroAdicionalColeccion>  <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>?</tipoRegistro>  <!--Optional:-->   <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion>  <!--Optional:-->  <consultaSaldoColeccion> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <peticionId>?</peticionId> </consultaSaldoColeccion> </con:MT_ConsultaSaldo></soapenv:Body></soapenv:Envelope>";
		
		requestBody = String.format(requestBody, usuario, "2");
		System.out.println(requestBody);
		
		String responseXML = given().body(requestBody).post(wsSaldo).andReturn().asString();
		
		System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
		NodeImpl cuentaColeccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("cuentaColeccion");
		
		List<?> cuentas = cuentaColeccion.getList("cuentaItem");
		
		
		String[] saldos = new String[cuentas.size()];
		
		for(int s = 0; s < cuentas.size(); s++)
		{
			NodeImpl nodoTarjeta = (NodeImpl) cuentas.get(s);
			NodeImpl moneda = nodoTarjeta.get("moneda");
			NodeImpl saldo = null;
			System.out.println();
			System.out.println(moneda.toString());
			String nuevoTexto = texto;
			
			if(moneda.toString().equals("USD"))	{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Dólares");	 
				saldo = nodoTarjeta.getNode("saldoColeccion").get("disponibleUsd");
			}
			if(moneda.toString().equals("EUR"))		
			{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Euros");	
				saldo = nodoTarjeta.getNode("saldoColeccion").get("disponibleEur");
			}
			if(moneda.toString().equals("LPS"))		
			{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Lempiras");	
				saldo = nodoTarjeta.getNode("saldoColeccion").get("disponibleLps");
			}
			nuevoTexto = nuevoTexto.replaceAll("%cc", saldo.toString());	
			NodeImpl alias = nodoTarjeta.get("alias");
			NodeImpl numeroCuenta = nodoTarjeta.get("numeroCuenta");
			if(!alias.toString().equals("Alias no ingresado") || !alias.toString().equals(""))
				nuevoTexto = nuevoTexto.replaceAll("%ncc", numeroCuenta.toString()+"-"+alias.toString());
			else nuevoTexto = nuevoTexto.replaceAll("%ncc", numeroCuenta.toString());	
			
			saldos[s] = nuevoTexto;
		}

		return saldos;
		
	}
	
	public String[] obtenerMovimientos(String wsMovimientos, String texto, String usuario, String cuenta) throws ParseException{
		
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cor=\"http://corebancario.ext.srv.infatlan.hn/\"> <soapenv:Header/> <soapenv:Body> <cor:consultaMovimiento> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional><!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId><!--Optional:--><regionId>?</regionId><!--Optional:--><canalId>?</canalId><!--Optional:--><version>?</version><!--Optional:--><llaveSesion>?</llaveSesion><!--Optional:--><usuarioId>?</usuarioId><!--Optional:--><token>?</token><!--Optional:--><identificadorColeccion><!--Optional:--><was>?</was><!--Optional:--><pi>?</pi><!--Optional:--><omniCanal>?</omniCanal><!--Optional:--><recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion> </identificadorColeccion><!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem><linea>?</linea> <!--Optional:-->   <tipoRegistro>?</tipoRegistro>  <!--Optional:--><valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion>  <!--Optional:-->  <logColeccion>  <!--Zero or more repetitions:--> <logItem><identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--><identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--><aplicacionId>?</aplicacionId> <!--Optional:--><canalId>?</canalId>    <!--Optional:--> <ambienteId>?</ambienteId>  <!--Optional:-->  <transaccionId>?</transaccionId>  <!--Optional:--><accion>?</accion> <!--Optional:--><tipo>?</tipo><!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--><auxiliar2>?</auxiliar2> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:-->   <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:--> <valor>%s</valor></parametroAdicionalItem> </parametroAdicionalColeccion>   </logItem></logColeccion> <!--Optional:--> <consultaMovimientoColeccion>  <!--Optional:--> <tipoConsulta>E</tipoConsulta> <!--Optional:--> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <numeroCuenta>?</numeroCuenta> <!--Optional:--><periodo>01</periodo> <!--Optional:--> <fechaInicio>?</fechaInicio>   <!--Optional:--> <fechaFinal>?</fechaFinal><!--Optional:--><tipoMonto>?</tipoMonto> <!--Optional:--> <monto>?</monto> <!--Optional:--> <operacion>?</operacion> </consultaMovimientoColeccion></cor:consultaMovimiento></soapenv:Body></soapenv:Envelope>";
		
		String[] movimientos = new String[4];
		movimientos[0] = texto;
		requestBody = String.format(requestBody, usuario, cuenta);
		String responseXML = given().body(requestBody).post( wsMovimientos).andReturn().asString();
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl tasa = body.get(0).get("MT_ConsultaMovimientoResponse");
		List<?> codigo = tasa.getNode("Respuesta").getNode("movimientoCuentaTarjetaColeccion").get("movimientoCuentaTarjetaItem");
		
		int last = codigo.size()-1;
		for(int j = 1; j < 4 ; j++)
		{
			DateFormat formatoDeFechaInicial = new SimpleDateFormat("yyyyMMdd");
			DateFormat formatoDeFechaFinal = new SimpleDateFormat("dd/MM/yyyy");
			NodeImpl movimiento = (NodeImpl) codigo.get(last);
			String fecha = formatoDeFechaFinal.format(formatoDeFechaInicial.parse(movimiento.get("fecha").toString()));
			NodeImpl hora = movimiento.get("hora");
			NodeImpl codigoTransaccion = movimiento.get("codigoTransaccion");
			NodeImpl montoTransaccion = movimiento.get("montoTransaccion");
			NodeImpl moneda = movimiento.get("moneda");
			NodeImpl descripcion = movimiento.get("descripcion");
			String nombreMoneda = "";
			if(moneda.toString().equals("USD"))	{
				nombreMoneda = "Dólares";
			}
			if(moneda.toString().equals("EUR"))		
			{
				nombreMoneda = "Euros";
			}
			if(moneda.toString().equals("LPS"))		
			{
				nombreMoneda = "Lempiras";
			}
			if(codigoTransaccion.getValue().equals("CR"))
				movimientos[j] = "El día "+fecha+ " a las "+ hora + " se realizó un crédito por " + montoTransaccion + " " + nombreMoneda+" con el detalle "+descripcion+".";
			if(codigoTransaccion.getValue().equals("DB"))
				movimientos[j] = "El día "+fecha+ " a las "+ hora + " se realizó un débito por " + montoTransaccion + " " + nombreMoneda+" con el detalle "+descripcion+".";
			
			last--;
		}
		
		return movimientos;
	}
	
	public String[] obtenerDisponibleTarjetaCredito(String wsSaldo, String texto, String usuario){
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\"><soapenv:Header/> <soapenv:Body> <con:MT_ConsultaSaldo> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId>  <!--Optional:-->  <regionId>?</regionId>   <!--Optional:--> <canalId>?</canalId><!--Optional:-->  <version>?</version> <!--Optional:--> <llaveSesion></llaveSesion>  <!--Optional:--><usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:-->  <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:-->  <pi>?</pi><!--Optional:--><omniCanal>?</omniCanal>  <!--Optional:--> <recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion></identificadorColeccion> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem>  <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:-->    <valor>%s</valor></parametroAdicionalItem>  </parametroAdicionalColeccion> <!--Optional:--> <logColeccion><!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--><identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal>  <!--Optional:--><identificadorRecibo>?</identificadorRecibo> <!--Optional:--><numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:-->   <canalId>?</canalId> <!--Optional:-->  <ambienteId>?</ambienteId> <!--Optional:-->  <transaccionId>?</transaccionId> <!--Optional:--><accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora><!--Optional:--><auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2>  <!--Optional:--> <parametroAdicionalColeccion>  <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>?</tipoRegistro>  <!--Optional:-->   <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion>  <!--Optional:-->  <consultaSaldoColeccion> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <peticionId>?</peticionId> </consultaSaldoColeccion> </con:MT_ConsultaSaldo></soapenv:Body></soapenv:Envelope>";
		
		
		requestBody = String.format(requestBody, usuario, "4");
		System.out.println(requestBody);
		
		String responseXML = given().body(requestBody).post(wsSaldo).andReturn().asString();
		
		System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
		NodeImpl tarjetaColeccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("tarjetaColeccion");
		
		List<?> tarjetas = tarjetaColeccion.getList("tarjetaItem");
		
		
		String[] disponibles = new String[tarjetas.size()];
		
		for(int s = 0; s < tarjetas.size(); s++)
		{
			NodeImpl nodoTarjeta = (NodeImpl) tarjetas.get(s);
			NodeImpl moneda = nodoTarjeta.get("moneda");
			NodeImpl saldo = null;
			System.out.println();
			System.out.println(moneda.toString());
			
			String nuevoTexto = texto;
			if(moneda.toString().equals("USD"))	{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Dólares");	 
				saldo = nodoTarjeta.getNode("saldoColeccion").get("disponibleUsd");
			}
			if(moneda.toString().equals("EUR"))		
			{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Euros");	
				saldo = nodoTarjeta.getNode("saldoColeccion").get("disponibleEur");
			}
			if(moneda.toString().equals("LPS"))		
			{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "Lempiras");	
				saldo = nodoTarjeta.getNode("saldoColeccion").get("disponibleLps");
			}
			nuevoTexto = nuevoTexto.replaceAll("%stc", saldo.toString());	
			NodeImpl alias = nodoTarjeta.get("alias");
			NodeImpl numeroTarjeta = nodoTarjeta.get("numeroTarjeta");
			if(!alias.toString().equals("Alias no ingresado") || !alias.toString().equals(""))
				nuevoTexto = nuevoTexto.replaceAll("%ntc", numeroTarjeta.toString()+"-"+alias.toString());
			else nuevoTexto = nuevoTexto.replaceAll("%ntc", numeroTarjeta.toString());	
			
			disponibles[s] = nuevoTexto;
		}

		return disponibles;
		
	}
	
	
	public String obtenerTasaCambio( String wsTasaCambio, String texto){
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";
		
		String responseXML = given().body(requestBody).post(wsTasaCambio).andReturn().asString();
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl tasa = body.get(0).get("MT_TasaCambioResponse");
		List<?> codigo = tasa.getNode("Respuesta").getNode("tasaCambioColeccion").get("tasaCambioItem");
		
		for(int s = 0; s < codigo.size(); s++)
		{
			NodeImpl nodeTipoCambio1 = (NodeImpl) codigo.get(s);
			NodeImpl moneda = nodeTipoCambio1.get("moneda");
			NodeImpl compra = nodeTipoCambio1.get("compra");
			NodeImpl venta = nodeTipoCambio1.get("venta");
			if(moneda.getValue().equals(Entidad.DOLAR.toString())){
				texto = texto.replace("%dc", compra.toString()+" Lempiras ").replace("%dv", venta.toString()+" Lempiras ");
			}
			if(moneda.getValue().equals(Entidad.EURO.toString())){
				texto = texto.replace("%ec", compra.toString()+" Lempiras ").replace("%ev", venta.toString()+" Lempiras ");			
			}
		}
		return texto;
	}

	
}
