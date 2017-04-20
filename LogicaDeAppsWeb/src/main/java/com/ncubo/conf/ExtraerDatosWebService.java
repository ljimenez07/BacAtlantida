package com.ncubo.conf;

import static com.jayway.restassured.RestAssured.given;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.path.xml.element.Node;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

import scala.util.Random;

@Component
@ConfigurationProperties("webservicesBancoAtlantida")
public class ExtraerDatosWebService {
	
	private String tipoCambio;
	private String saldo;
	private String movimientos;
	private String login;
	private String preLogin;
	private String logout;
	private String usuario;
	private String password;
	
	
	public ExtraerDatosWebService(){}	
	
	public String[] obtenerSaldoTarjetaCredito( String texto, String llaveSesion, String user){
		String[] saldos = null;
		try{
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\">"+
					"<soapenv:Header/>"+
						"<soapenv:Body>"+
							"<con:MT_ConsultaSaldo>"+
								"<activarMultipleEntrada>?</activarMultipleEntrada>"+
						         "<activarParametroAdicional>?</activarParametroAdicional>"+
						         "<!--Optional:-->"+
						         "<transaccionId>100128</transaccionId>"+
						         "<!--Optional:-->"+
						         "<aplicacionId>001</aplicacionId>"+
						         "<paisId>HN</paisId>"+
						         "<empresaId>?</empresaId>"+
						         "<!--Optional:-->"+
						         "<regionId>?</regionId>"+
						         "<!--Optional:-->"+
						         "<canalId>104</canalId>"+
						         "<!--Optional:-->"+
						         "<version>?</version>"+
						         "<!--Optional:-->"+
						         "<llaveSesion>%s</llaveSesion>"+
						         "<!--Optional:-->"+
						         "<usuarioId>?</usuarioId>"+
						         "<!--Optional:-->"+
						         "<token>?</token>"+
						         "<!--Zero or more repetitions:-->"+
						         "<identificadorColeccion>"+
						         "	<!--Optional:-->"+
						         "   <was>?</was>"+
						         "   <!--Optional:-->"+
						         "   <pi>?</pi>"+
						         "   <!--Optional:-->"+
						         "   <omniCanal>?</omniCanal>"+
						         "   <!--Optional:-->"+
						         "   <recibo>?</recibo>"+
						         "   <!--Optional:-->"+
						         "   <numeroTransaccion>?</numeroTransaccion>"+
						         "</identificadorColeccion>"+
						         "<!--Optional:-->"+
						         "<parametroAdicionalColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "    <parametroAdicionalItem>"+
						         " 			<!--You may enter the following 3 items in any order-->"+
						         " 			<linea>0</linea>"+
						         " 			<!--Optional:-->"+
						         "			<tipoRegistro>UAI</tipoRegistro>"+
						         " 			<!--Optional:-->"+
						         " 			<valor>%s</valor>"+
						         " 		</parametroAdicionalItem>"+
						         "   <parametroAdicionalItem>"+
						         "     <linea>1</linea>"+
						         "      <!--Optional:-->"+
						         "      <tipoRegistro></tipoRegistro>"+
						         "      <!--Optional:--> "+
						         "      <valor>A</valor>"+
						         "   </parametroAdicionalItem>"+
						         "</parametroAdicionalColeccion>"+
						         "<!--Optional:-->"+
						         "<logColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "   <logItem>"+
						         "      <identificadorWas>?</identificadorWas>"+
						         "      <!--Optional:-->"+
						         "      <identificadorPi>?</identificadorPi>"+
						         "     <!--Optional:-->"+
						         "      <identificadorOmniCanal>?</identificadorOmniCanal>"+
						         "      <!--Optional:-->"+
						         "      <identificadorRecibo>?</identificadorRecibo>"+
						         "      <!--Optional:-->"+
						         "      <numeroPeticion>?</numeroPeticion>"+
						         "      <!--Optional:-->"+
						         "      <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion>"+
						         "      <!--Optional:-->"+
						         "      <aplicacionId>?</aplicacionId>"+
						         "      <!--Optional:-->"+
						         "      <canalId>?</canalId>"+
						         "      <!--Optional:-->"+
						         "      <ambienteId>?</ambienteId>"+
						         "      <!--Optional:-->"+
						         "      <transaccionId>?</transaccionId>"+
						         "      <!--Optional:-->"+
						         "      <accion>?</accion>"+
						         "     <!--Optional:-->"+
						         "      <tipo>?</tipo>"+
						         "      <!--Optional:-->"+
						         "      <fecha>?</fecha>"+
						         "      <!--Optional:-->"+
						         "      <hora>?</hora>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar1>?</auxiliar1>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar2>?</auxiliar2>"+
						         "     <!--Optional:-->"+
						         "      <parametroAdicionalColeccion>"+
						         "        <!--Zero or more repetitions:-->"+
						         "         <parametroAdicionalItem>"+
						         "            <linea>?</linea>"+
						         "            <!--Optional:-->"+
						         "            <tipoRegistro>?</tipoRegistro>"+
						         "            <!--Optional:-->"+
						         "            <valor>?</valor>"+
						         "         </parametroAdicionalItem>"+
						         "      </parametroAdicionalColeccion>"+
						         "   </logItem>"+
						         "</logColeccion>"+
						         "<!--Optional:-->"+
						         "<consultaSaldoColeccion>"+
						         "   <tipoCuenta>4</tipoCuenta>"+
						         "   <!--Optional:-->"+
						         "   <peticionId>?</peticionId>"+
						         "</consultaSaldoColeccion>"+
					         "</con:MT_ConsultaSaldo>"+
				         "</soapenv:Body>"+
			         "</soapenv:Envelope>";
		requestBody = String.format(requestBody, llaveSesion, user);
		
		String responseXML = given().
				header("Content-Type", "text/xml;charset=UTF-8").
				auth().
				basic(usuario, password).
				body(requestBody).
				post(saldo).
				andReturn().
				asString();
		
		System.out.println(saldo+" \n\t  "+requestBody+"	\n\t"+responseXML);
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
		NodeImpl tarjetaColeccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("tarjetaColeccion");
		
		List<?> tarjetas = tarjetaColeccion.getList("tarjetaItem");
		
		
		saldos = new String[tarjetas.size()];
		
		for(int s = 0; s < tarjetas.size(); s++)
		{
			NodeImpl nodoTarjeta = (NodeImpl) tarjetas.get(s);
			NodeImpl moneda = nodoTarjeta.get("moneda");
			NodeImpl saldo = null;
			
			String nuevoTexto = texto;
			if(moneda.toString().equals("USD"))	{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "DÃ³lares");	 
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
			String ultimosDigitosTarjeta = ultimosDigitos(numeroTarjeta.toString());
			if(saldo.equals("0.0") && (!alias.toString().equals("Alias no ingresado") && !alias.toString().equals("N/A")))
				nuevoTexto = "No tienes saldo en la tarjeta de crÃ©dito que termina en " +ultimosDigitosTarjeta+"-"+alias.toString();
			else if(saldo.equals("0.0") && (alias.toString().equals("Alias no ingresado") || alias.toString().equals("N/A")))
				nuevoTexto = "No tienes saldo en la tarjeta de crÃ©dito que termina en " +ultimosDigitosTarjeta;
			else if(! saldo.equals("0.0") &&  !alias.toString().equals("Alias no ingresado") && !alias.toString().equals("N/A"))
				nuevoTexto = nuevoTexto.replaceAll("%ntc", ultimosDigitosTarjeta+"-"+alias.toString());
			else nuevoTexto = nuevoTexto.replaceAll("%ntc", ultimosDigitosTarjeta);	

				
			saldos[s] = nuevoTexto;
		}
	}catch (Exception e) {
		// TODO: handle exception
		saldos = new String [1];
		saldos[0] = MensajesErrorConWebServices.ERROR_CONSULTA_SALDO;
	}

		return saldos;
		
	}
	
	public String[] obtenerSaldoCuentaAhorros(String texto, String llaveSesion, String user){
		String[] saldos = null;
		try{
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\">"+
					"<soapenv:Header/>"+
						"<soapenv:Body>"+
							"<con:MT_ConsultaSaldo>"+
								"<activarMultipleEntrada>?</activarMultipleEntrada>"+
						         "<activarParametroAdicional>?</activarParametroAdicional>"+
						         "<!--Optional:-->"+
						         "<transaccionId>100128</transaccionId>"+
						         "<!--Optional:-->"+
						         "<aplicacionId>001</aplicacionId>"+
						         "<paisId>HN</paisId>"+
						         "<empresaId>?</empresaId>"+
						         "<!--Optional:-->"+
						         "<regionId>?</regionId>"+
						         "<!--Optional:-->"+
						         "<canalId>104</canalId>"+
						         "<!--Optional:-->"+
						         "<version>?</version>"+
						         "<!--Optional:-->"+
						         "<llaveSesion>%s</llaveSesion>"+
						         "<!--Optional:-->"+
						         "<usuarioId>?</usuarioId>"+
						         "<!--Optional:-->"+
						         "<token>?</token>"+
						         "<!--Zero or more repetitions:-->"+
						         "<identificadorColeccion>"+
						         "	<!--Optional:-->"+
						         "   <was>?</was>"+
						         "   <!--Optional:-->"+
						         "   <pi>?</pi>"+
						         "   <!--Optional:-->"+
						         "   <omniCanal>?</omniCanal>"+
						         "   <!--Optional:-->"+
						         "   <recibo>?</recibo>"+
						         "   <!--Optional:-->"+
						         "   <numeroTransaccion>?</numeroTransaccion>"+
						         "</identificadorColeccion>"+
						         "<!--Optional:-->"+
						         "<parametroAdicionalColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "    <parametroAdicionalItem>"+
						         " 			<!--You may enter the following 3 items in any order-->"+
						         " 			<linea>0</linea>"+
						         " 		 <!--Optional:-->"+
						         " 		<tipoRegistro>UAI</tipoRegistro>"+
						         " 		 <!--Optional:-->"+
						         " 		 <valor>%s</valor>"+
						         "   </parametroAdicionalItem>"+
						         "   <parametroAdicionalItem>"+
						         "     <linea>1</linea>"+
						         "      <!--Optional:-->"+
						         "      <tipoRegistro></tipoRegistro>"+
						         "      <!--Optional:--> "+
						         "      <valor>A</valor>"+
						         "   </parametroAdicionalItem>"+
						         "</parametroAdicionalColeccion>"+
						         "<!--Optional:-->"+
						         "<logColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "   <logItem>"+
						         "      <identificadorWas>?</identificadorWas>"+
						         "      <!--Optional:-->"+
						         "      <identificadorPi>?</identificadorPi>"+
						         "     <!--Optional:-->"+
						         "      <identificadorOmniCanal>?</identificadorOmniCanal>"+
						         "      <!--Optional:-->"+
						         "      <identificadorRecibo>?</identificadorRecibo>"+
						         "      <!--Optional:-->"+
						         "      <numeroPeticion>?</numeroPeticion>"+
						         "      <!--Optional:-->"+
						         "      <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion>"+
						         "      <!--Optional:-->"+
						         "      <aplicacionId>?</aplicacionId>"+
						         "      <!--Optional:-->"+
						         "      <canalId>?</canalId>"+
						         "      <!--Optional:-->"+
						         "      <ambienteId>?</ambienteId>"+
						         "      <!--Optional:-->"+
						         "      <transaccionId>?</transaccionId>"+
						         "      <!--Optional:-->"+
						         "      <accion>?</accion>"+
						         "     <!--Optional:-->"+
						         "      <tipo>?</tipo>"+
						         "      <!--Optional:-->"+
						         "      <fecha>?</fecha>"+
						         "      <!--Optional:-->"+
						         "      <hora>?</hora>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar1>?</auxiliar1>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar2>?</auxiliar2>"+
						         "     <!--Optional:-->"+
						         "      <parametroAdicionalColeccion>"+
						         "        <!--Zero or more repetitions:-->"+
						         "         <parametroAdicionalItem>"+
						         "            <linea>?</linea>"+
						         "            <!--Optional:-->"+
						         "            <tipoRegistro>?</tipoRegistro>"+
						         "            <!--Optional:-->"+
						         "            <valor>?</valor>"+
						         "         </parametroAdicionalItem>"+
						         "      </parametroAdicionalColeccion>"+
						         "   </logItem>"+
						         "</logColeccion>"+
						         "<!--Optional:-->"+
						         "<consultaSaldoColeccion>"+
						         "   <tipoCuenta>2</tipoCuenta>"+
						         "   <!--Optional:-->"+
						         "   <peticionId>?</peticionId>"+
						         "</consultaSaldoColeccion>"+
					         "</con:MT_ConsultaSaldo>"+
				         "</soapenv:Body>"+
			         "</soapenv:Envelope>";
					requestBody = String.format(requestBody, llaveSesion, user);
				String responseXML =
				given(). 
				header("Content-Type", "text/xml;charset=UTF-8").
				auth().
				basic(usuario, password).
				body(requestBody).
				post(saldo).
				andReturn().
				asString();
		
		System.out.println(saldo+" \n\t  "+requestBody+"	\n\t"+responseXML);
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
		NodeImpl cuentaColeccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("cuentaColeccion");
		
		List<?> cuentas = cuentaColeccion.getList("cuentaItem");
		
		
		saldos = new String[cuentas.size()];
		
		for(int s = 0; s < cuentas.size(); s++)
		{
			NodeImpl nodoTarjeta = (NodeImpl) cuentas.get(s);
			NodeImpl moneda = nodoTarjeta.get("moneda");
			NodeImpl saldo = null;
			String nuevoTexto = texto;
			
			if(moneda.toString().equals("USD"))	{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "DÃ³lares");	 
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
			String ultimosDigitosCuenta = ultimosDigitos(numeroCuenta.toString());
			
			if(saldo.toString().equals("0.0") && (!alias.toString().equals("Alias no ingresado") && !alias.toString().equals("N/A")))
				nuevoTexto = "No tienes saldo en la cuenta de ahorros que termina en " +ultimosDigitosCuenta+"-"+alias.toString();
			else if(saldo.toString().equals("0.0") && (alias.toString().equals("Alias no ingresado") || alias.toString().equals("N/A")))
				nuevoTexto = "No tienes saldo en la cuenta de ahorros que termina en " +ultimosDigitosCuenta;
			else if(!saldo.toString().equals("0.0") &&  !alias.toString().equals("Alias no ingresado") && !alias.toString().equals("N/A"))
				nuevoTexto = nuevoTexto.replaceAll("%ncc", ultimosDigitosCuenta+"-"+alias.toString());
			else nuevoTexto = nuevoTexto.replaceAll("%ncc", ultimosDigitosCuenta);	
			
			saldos[s] = nuevoTexto;
		}

	}catch (Exception e) {
		// TODO: handle exception
		saldos = new String [1];
		saldos[0] = MensajesErrorConWebServices.ERROR_CONSULTA_SALDO;
	}
		return saldos;
		
	}
	
	public String[] obtenerMovimientos(String texto, String llaveSesion, String user, String tipoCuenta) throws ParseException{
		String[] arregloMovimientos = null;
		try{
			
			String requestBodySaldo = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\">"+
					"<soapenv:Header/>"+
						"<soapenv:Body>"+
							"<con:MT_ConsultaSaldo>"+
								"<activarMultipleEntrada>?</activarMultipleEntrada>"+
						         "<activarParametroAdicional>?</activarParametroAdicional>"+
						         "<!--Optional:-->"+
						         "<transaccionId>100128</transaccionId>"+
						         "<!--Optional:-->"+
						         "<aplicacionId>001</aplicacionId>"+
						         "<paisId>HN</paisId>"+
						         "<empresaId>?</empresaId>"+
						         "<!--Optional:-->"+
						         "<regionId>?</regionId>"+
						         "<!--Optional:-->"+
						         "<canalId>104</canalId>"+
						         "<!--Optional:-->"+
						         "<version>?</version>"+
						         "<!--Optional:-->"+
						         "<llaveSesion>%s</llaveSesion>"+
						         "<!--Optional:-->"+
						         "<usuarioId>?</usuarioId>"+
						         "<!--Optional:-->"+
						         "<token>?</token>"+
						         "<!--Zero or more repetitions:-->"+
						         "<identificadorColeccion>"+
						         "	<!--Optional:-->"+
						         "   <was>?</was>"+
						         "   <!--Optional:-->"+
						         "   <pi>?</pi>"+
						         "   <!--Optional:-->"+
						         "   <omniCanal>?</omniCanal>"+
						         "   <!--Optional:-->"+
						         "   <recibo>?</recibo>"+
						         "   <!--Optional:-->"+
						         "   <numeroTransaccion>?</numeroTransaccion>"+
						         "</identificadorColeccion>"+
						         "<!--Optional:-->"+
						         "<parametroAdicionalColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "    <parametroAdicionalItem>"+
						         " 		<!--You may enter the following 3 items in any order-->"+
						         " 			 <linea>0</linea>"+
						         " 	 <!--Optional:-->"+
						         " 	 <tipoRegistro>UAI</tipoRegistro>"+
						         " 	 <!--Optional:-->"+
						         " 	 <valor>%s</valor>"+
						         " 	  </parametroAdicionalItem>"+
						         "   <parametroAdicionalItem>"+
						         "     <linea>1</linea>"+
						         "      <!--Optional:-->"+
						         "      <tipoRegistro></tipoRegistro>"+
						         "      <!--Optional:--> "+
						         "      <valor>A</valor>"+
						         "   </parametroAdicionalItem>"+
						         "</parametroAdicionalColeccion>"+
						         "<!--Optional:-->"+
						         "<logColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "   <logItem>"+
						         "      <identificadorWas>?</identificadorWas>"+
						         "      <!--Optional:-->"+
						         "      <identificadorPi>?</identificadorPi>"+
						         "     <!--Optional:-->"+
						         "      <identificadorOmniCanal>?</identificadorOmniCanal>"+
						         "      <!--Optional:-->"+
						         "      <identificadorRecibo>?</identificadorRecibo>"+
						         "      <!--Optional:-->"+
						         "      <numeroPeticion>?</numeroPeticion>"+
						         "      <!--Optional:-->"+
						         "      <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion>"+
						         "      <!--Optional:-->"+
						         "      <aplicacionId>?</aplicacionId>"+
						         "      <!--Optional:-->"+
						         "      <canalId>?</canalId>"+
						         "      <!--Optional:-->"+
						         "      <ambienteId>?</ambienteId>"+
						         "      <!--Optional:-->"+
						         "      <transaccionId>?</transaccionId>"+
						         "      <!--Optional:-->"+
						         "      <accion>?</accion>"+
						         "     <!--Optional:-->"+
						         "      <tipo>?</tipo>"+
						         "      <!--Optional:-->"+
						         "      <fecha>?</fecha>"+
						         "      <!--Optional:-->"+
						         "      <hora>?</hora>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar1>?</auxiliar1>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar2>?</auxiliar2>"+
						         "     <!--Optional:-->"+
						         "      <parametroAdicionalColeccion>"+
						         "        <!--Zero or more repetitions:-->"+
						         "         <parametroAdicionalItem>"+
						         "            <linea>?</linea>"+
						         "            <!--Optional:-->"+
						         "            <tipoRegistro>?</tipoRegistro>"+
						         "            <!--Optional:-->"+
						         "            <valor>?</valor>"+
						         "         </parametroAdicionalItem>"+
						         "      </parametroAdicionalColeccion>"+
						         "   </logItem>"+
						         "</logColeccion>"+
						         "<!--Optional:-->"+
						         "<consultaSaldoColeccion>"+
						         "   <tipoCuenta>%s</tipoCuenta>"+
						         "   <!--Optional:-->"+
						         "   <peticionId>?</peticionId>"+
						         "</consultaSaldoColeccion>"+
					         "</con:MT_ConsultaSaldo>"+
				         "</soapenv:Body>"+
			         "</soapenv:Envelope>";
			
			requestBodySaldo = String.format(requestBodySaldo, llaveSesion, user, tipoCuenta);
			
			String responseXML =
					given(). 
					header("Content-Type", "text/xml;charset=UTF-8").
					auth().
					basic(usuario, password).
					body(requestBodySaldo).
					post(saldo).
					andReturn().
					asString();
			
			System.out.println(saldo+" \n\t  "+requestBodySaldo+"	\n\t"+responseXML);
			
			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
			NodeChildrenImpl body = xmlPath.get("Body");
			NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
			NodeImpl coleccion;
			List<?> cuentas = null;
			if(tipoCuenta.equals("2"))
			{
				coleccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("cuentaColeccion");
				cuentas = coleccion.getList("cuentaItem");
			}
			
			if(tipoCuenta.equals("4"))
			{
				coleccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("tarjetaColeccion");
				cuentas = coleccion.getList("tarjetaItem");
			}
			
			
			
			int tam = cuentas.size()*4+1;
			
			
			arregloMovimientos = new String[tam];
			
			arregloMovimientos[0] = texto;
			
			int i = 1;
			
			for(int s = 0; s < cuentas.size(); s++)
			{
				NodeImpl nodoTarjeta = (NodeImpl) cuentas.get(s);
				String cuenta = "";
				if(tipoCuenta.equals("2"))
				{
					cuenta = nodoTarjeta.get("numeroCuenta").toString();
					String ultimosDigitosCuenta = ultimosDigitos(cuenta);
					String alias = nodoTarjeta.get("alias").toString();
					if(!alias.equals("Alias no ingresado") && !alias.equals("N/A"))
						arregloMovimientos[i] = "Para la cuenta que termina en "+ultimosDigitosCuenta+"-"+alias+":";
					else arregloMovimientos[i] = "Para la cuenta que termina en "+ultimosDigitosCuenta+":";
				}
				if(tipoCuenta.equals("4"))
				{
					cuenta = nodoTarjeta.get("numeroTarjeta").toString();
					String ultimosDigitosTarjeta = ultimosDigitos(cuenta);
					String alias = nodoTarjeta.get("alias").toString();
					if(!alias.equals("Alias no ingresado") && !alias.equals("N/A"))
						arregloMovimientos[i] = "Para la tarjeta que termina en "+ultimosDigitosTarjeta+"-"+alias+":";
					else arregloMovimientos[i] = "Para la tarjeta que termina en "+ultimosDigitosTarjeta+":";
				}
				
				i++;
					String requestBody = ""+
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD078/out/ConsultaMovimiento\">"+
					"<soapenv:Header/>"+
					"<soapenv:Body>"+
						"<con:MT_ConsultaMovimiento>"+
						"<activarMultipleEntrada>1</activarMultipleEntrada>	"+
						"<activarParametroAdicional/>"+
						"    <transaccionId>100143</transaccionId>	"+
						"<aplicacionId>001</aplicacionId>"+
				         "<paisId>HN</paisId>"+
				         "<empresaId>?</empresaId>"+
				         "<!--Optional:-->"+
				         "<regionId>?</regionId>"+
				         "<!--Optional:-->"+
				         "<canalId>104</canalId>"+
						"     <llaveSesion>%s</llaveSesion>"+
						"      <usuarioId>?</usuarioId>"+
						"    <parametroAdicionalColeccion>	"+
						"            <parametroAdicionalItem>"+
						"                    <linea>0</linea>"+
						"                    <tipoRegistro>UAI</tipoRegistro>	"+
						"                    <valor>%s</valor>	"+
						"             </parametroAdicionalItem>		"+
						"     </parametroAdicionalColeccion>"+
						"    <consultaMovimientoColeccion>	"+
						"            <tipoConsulta>E</tipoConsulta>	"+
						"            <tipoCuenta>%s</tipoCuenta>"+
						"            <numeroCuenta>%s</numeroCuenta>"+
						"           <periodo>00</periodo>	"+
						"            <fechaInicio></fechaInicio>	"+
						"            <fechaFinal></fechaFinal>	"+
						"            <tipoMonto></tipoMonto>	"+
						"           <monto>0</monto>		"+
						"           <operacion></operacion>	"+
						"    </consultaMovimientoColeccion>	"+
				      "</con:MT_ConsultaMovimiento>"+
					"</soapenv:Body>"+
				"</soapenv:Envelope>";
				
				
				requestBody = String.format(requestBody, llaveSesion, user, tipoCuenta, cuenta);
				String responseXMLMovimientos = 
						given().
						header("Content-Type", "text/xml;charset=UTF-8").
						auth().
						basic(usuario, password).
						body(requestBody).
						post(movimientos).
						andReturn().
						asString();
				
				System.out.println(movimientos+" \n\t  "+requestBody+"	\n\t"+responseXMLMovimientos);
				
				
				XmlPath xmlPathMovimientos = new XmlPath(responseXMLMovimientos).setRoot("Envelope");
				NodeChildrenImpl bodyMovimientos = xmlPathMovimientos.get("Body");
				NodeImpl movimientoResponse = bodyMovimientos.get(0).get("MT_ConsultaMovimientoResponse");
				List<?> codigo = movimientoResponse.getNode("Respuesta").getNode("movimientoCuentaTarjetaColeccion").get("movimientoCuentaTarjetaItem");
				
				int last = codigo.size()-1;
				int max = 4;
				if(codigo.size() < 3)
				{
					max = codigo.size()+1;
				}
				for(int j = 1; j < max ; j++)
				{
					DateFormat formatoDeFechaInicial = new SimpleDateFormat("yyyyMMdd");
					DateFormat formatoDeFechaFinal = new SimpleDateFormat("dd/MM/yyyy");
					NodeImpl movimiento = (NodeImpl) codigo.get(last);
					String fecha = formatoDeFechaFinal.format(formatoDeFechaInicial.parse(movimiento.get("fecha").toString()));
					NodeImpl hora = movimiento.get("hora");
					NodeImpl codigoTransaccion = movimiento.get("tipoTransaccion");
					NodeImpl montoTransaccion = movimiento.get("montoTransaccion");
					NodeImpl moneda = movimiento.get("moneda");
					NodeImpl descripcion = movimiento.get("descripcion");
					String nombreMoneda = "";
					if(moneda.toString().equals("USD"))	{
						nombreMoneda = "DÃ³lares";
					}
					if(moneda.toString().equals("EUR"))		
					{
						nombreMoneda = "Euros";
					}
					if(moneda.toString().equals("LPS"))		
					{
						nombreMoneda = "Lempiras";
					}
					if(codigoTransaccion.getValue().equals("5"))
						arregloMovimientos[i] = "El dÃ­a "+fecha+ " a las "+ hora + " se realizÃ³ un crÃ©dito por " + montoTransaccion + " " + nombreMoneda+" con el detalle "+descripcion+".";
					if(codigoTransaccion.getValue().equals("0"))
						arregloMovimientos[i] = "El dÃ­a "+fecha+ " a las "+ hora + " se realizÃ³ un dÃ©bito por " + montoTransaccion + " " + nombreMoneda+" con el detalle "+descripcion+".";
					else
						arregloMovimientos[i] = "El dÃ­a "+fecha+ " a las "+ hora + " se realizÃ³ un movimiento por " + montoTransaccion + " " + nombreMoneda+" con el detalle "+descripcion+".";
					last--;
					i++;
				}
				
			}
	}catch (Exception e) {
		// TODO: handle exception
		arregloMovimientos = new String [1];
		arregloMovimientos[0] = MensajesErrorConWebServices.ERROR_CONSULTA_MOVIMIENTOS;
	}
		return arregloMovimientos;
	}
	
	public String[] obtenerDisponibleTarjetaCredito(String texto, String llaveSesion, String user){
		String[] disponibles = null;
		try
		{
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\">"+
					"<soapenv:Header/>"+
						"<soapenv:Body>"+
							"<con:MT_ConsultaSaldo>"+
								"<activarMultipleEntrada>?</activarMultipleEntrada>"+
						         "<activarParametroAdicional>?</activarParametroAdicional>"+
						         "<!--Optional:-->"+
						         "<transaccionId>100128</transaccionId>"+
						         "<!--Optional:-->"+
						         "<aplicacionId>001</aplicacionId>"+
						         "<paisId>HN</paisId>"+
						         "<empresaId>?</empresaId>"+
						         "<!--Optional:-->"+
						         "<regionId>?</regionId>"+
						         "<!--Optional:-->"+
						         "<canalId>104</canalId>"+
						         "<!--Optional:-->"+
						         "<version>?</version>"+
						         "<!--Optional:-->"+
						         "<llaveSesion>%s</llaveSesion>"+
						         "<!--Optional:-->"+
						         "<usuarioId>?</usuarioId>"+
						         "<!--Optional:-->"+
						         "<token>?</token>"+
						         "<!--Zero or more repetitions:-->"+
						         "<identificadorColeccion>"+
						         "	<!--Optional:-->"+
						         "   <was>?</was>"+
						         "   <!--Optional:-->"+
						         "   <pi>?</pi>"+
						         "   <!--Optional:-->"+
						         "   <omniCanal>?</omniCanal>"+
						         "   <!--Optional:-->"+
						         "   <recibo>?</recibo>"+
						         "   <!--Optional:-->"+
						         "   <numeroTransaccion>?</numeroTransaccion>"+
						         "</identificadorColeccion>"+
						         "<!--Optional:-->"+
						         "<parametroAdicionalColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "    <parametroAdicionalItem>"+
						         " 	 <!--You may enter the following 3 items in any order-->"+
						         " 	<linea>0</linea>"+
						         " 	<!--Optional:-->"+
						         " 	 <tipoRegistro>UAI</tipoRegistro>"+
						         " 	<!--Optional:-->"+
						         " 	 <valor>%s</valor>"+
						         " 	 </parametroAdicionalItem>"+
						         "   <parametroAdicionalItem>"+
						         "     <linea>1</linea>"+
						         "      <!--Optional:-->"+
						         "      <tipoRegistro></tipoRegistro>"+
						         "      <!--Optional:--> "+
						         "      <valor>A</valor>"+
						         "   </parametroAdicionalItem>"+
						         "</parametroAdicionalColeccion>"+
						         "<!--Optional:-->"+
						         "<logColeccion>"+
						         "   <!--Zero or more repetitions:-->"+
						         "   <logItem>"+
						         "      <identificadorWas>?</identificadorWas>"+
						         "      <!--Optional:-->"+
						         "      <identificadorPi>?</identificadorPi>"+
						         "     <!--Optional:-->"+
						         "      <identificadorOmniCanal>?</identificadorOmniCanal>"+
						         "      <!--Optional:-->"+
						         "      <identificadorRecibo>?</identificadorRecibo>"+
						         "      <!--Optional:-->"+
						         "      <numeroPeticion>?</numeroPeticion>"+
						         "      <!--Optional:-->"+
						         "      <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion>"+
						         "      <!--Optional:-->"+
						         "      <aplicacionId>?</aplicacionId>"+
						         "      <!--Optional:-->"+
						         "      <canalId>?</canalId>"+
						         "      <!--Optional:-->"+
						         "      <ambienteId>?</ambienteId>"+
						         "      <!--Optional:-->"+
						         "      <transaccionId>?</transaccionId>"+
						         "      <!--Optional:-->"+
						         "      <accion>?</accion>"+
						         "     <!--Optional:-->"+
						         "      <tipo>?</tipo>"+
						         "      <!--Optional:-->"+
						         "      <fecha>?</fecha>"+
						         "      <!--Optional:-->"+
						         "      <hora>?</hora>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar1>?</auxiliar1>"+
						         "      <!--Optional:-->"+
						         "      <auxiliar2>?</auxiliar2>"+
						         "     <!--Optional:-->"+
						         "      <parametroAdicionalColeccion>"+
						         "        <!--Zero or more repetitions:-->"+
						         "         <parametroAdicionalItem>"+
						         "            <linea>?</linea>"+
						         "            <!--Optional:-->"+
						         "            <tipoRegistro>?</tipoRegistro>"+
						         "            <!--Optional:-->"+
						         "            <valor>?</valor>"+
						         "         </parametroAdicionalItem>"+
						         "      </parametroAdicionalColeccion>"+
						         "   </logItem>"+
						         "</logColeccion>"+
						         "<!--Optional:-->"+
						         "<consultaSaldoColeccion>"+
						         "   <tipoCuenta>4</tipoCuenta>"+
						         "   <!--Optional:-->"+
						         "   <peticionId>?</peticionId>"+
						         "</consultaSaldoColeccion>"+
					         "</con:MT_ConsultaSaldo>"+
				         "</soapenv:Body>"+
			         "</soapenv:Envelope>";
		requestBody = String.format(requestBody, llaveSesion, user);
		
		String responseXML =
				given().
				header("Content-Type", "text/xml;charset=UTF-8").
				auth().
				basic(usuario, password).
				body(requestBody).
				post(saldo).
				andReturn().
				asString();
		
		System.out.println(saldo+" \n\t  "+requestBody+"	\n\t"+responseXML);
		
		XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
		NodeChildrenImpl body = xmlPath.get("Body");
		NodeImpl consultaSaldo = body.get(0).get("MT_ConsultaSaldoResponse");
		NodeImpl tarjetaColeccion = consultaSaldo.getNode("Respuesta").getNode("productoColeccion").get("tarjetaColeccion");
		
		List<?> tarjetas = tarjetaColeccion.getList("tarjetaItem");
		
		
		disponibles = new String[tarjetas.size()];
		
		for(int s = 0; s < tarjetas.size(); s++)
		{
			NodeImpl nodoTarjeta = (NodeImpl) tarjetas.get(s);
			NodeImpl moneda = nodoTarjeta.get("moneda");
			NodeImpl saldo = null;
			
			String nuevoTexto = texto;
			if(moneda.toString().equals("USD"))	{
				nuevoTexto = nuevoTexto.replaceAll("%nmm", "DÃ³lares");	 
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
			String ultimosDigitosTarjeta = ultimosDigitos(numeroTarjeta.toString());
			if(saldo.equals("0.0") && (!alias.toString().equals("Alias no ingresado") && !alias.toString().equals("N/A")))
				nuevoTexto = "No tienes disponible en la tarjeta de crÃ©dito que termina en " +ultimosDigitosTarjeta+"-"+alias.toString();
			else if(saldo.equals("0.0") && (alias.toString().equals("Alias no ingresado") || alias.toString().equals("N/A")))
				nuevoTexto = "No tienes disponible en la tarjeta de crÃ©dito que termina en " + ultimosDigitosTarjeta;
			else if(! saldo.equals("0.0") &&  !alias.toString().equals("Alias no ingresado") && !alias.toString().equals("N/A"))
				nuevoTexto = nuevoTexto.replaceAll("%ntc", ultimosDigitosTarjeta +"-"+alias.toString());
			else nuevoTexto = nuevoTexto.replaceAll("%ntc", ultimosDigitosTarjeta);	
			
			disponibles[s] = nuevoTexto;
		}

	}catch (Exception e) {
		// TODO: handle exception
		disponibles = new String [1];
		disponibles[0] = MensajesErrorConWebServices.ERROR_CONSULTA_DISPONIBLE;
	}
		return disponibles;
		
	}
	
	public String obtenerTasaCambio( String texto )
	{
	
		try{
		String requestBody =
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\">" +
			"<soapenv:Header/>" +
			"<soapenv:Body>" +
				"<tas:MT_TasaCambio>" +
					"<activarMultipleEntrada>?</activarMultipleEntrada>" +
					"<activarParametroAdicional>?</activarParametroAdicional>" +
					"<!--Optional:-->" +
					"<transaccionId>100054</transaccionId>" +
					"<!--Optional:-->" +
					"<aplicacionId>001</aplicacionId>"+
			         "<paisId>HN</paisId>"+
			         "<empresaId>?</empresaId>"+
			         "<!--Optional:-->"+
			         "<regionId>?</regionId>"+
			         "<!--Optional:-->"+
			         "<canalId>104</canalId>"+
					"<!--Optional:-->" +
					"<version>?</version>" +
					"<!--Optional:-->" +
					"<llaveSesion>?</llaveSesion>" +
					"<!--Optional:-->" +
					"<usuarioId>?</usuarioId>" +
					"<!--Optional:-->" +
					"<token>?</token>" +
					"<!--Zero or more repetitions:-->" +
					"<identificadorColeccion>" +
						"<!--Optional:-->" +
						"<was>?</was>" +
						"<!--Optional:-->" +
						"<pi>?</pi>" +
						"<!--Optional:-->" +
						"<omniCanal>?</omniCanal>" +
						"<!--Optional:-->" +
						"<recibo>?</recibo>" +
						"<!--Optional:-->" +
						"<numeroTransaccion>?</numeroTransaccion>" +
					"</identificadorColeccion>" +
					"<!--Optional:-->" +
					"<parametroAdicionalColeccion>" +
						"<!--Zero or more repetitions:-->" +
						"<parametroAdicionalItem>" +
							"<linea>?</linea>" +
							"<!--Optional:-->" +
							"<tipoRegistro>?</tipoRegistro>" +
							"<!--Optional:-->" +
							"<valor>?</valor>" +
						"</parametroAdicionalItem>" +
					"</parametroAdicionalColeccion>" +
				"</tas:MT_TasaCambio>" +
			"</soapenv:Body>" +
		"</soapenv:Envelope>";
		
		String responseXML = 
				given().
				header("Content-Type", "text/xml;charset=UTF-8").
				auth().
				basic(usuario, password).
				body(requestBody).
				post(tipoCambio).
				andReturn().
				asString();
		System.out.println("body "+requestBody);
		
		System.out.println(responseXML);
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
			if(compra.getValue().equals("0.0") || venta.getValue().equals("0.0"))
				texto = MensajesErrorConWebServices.ERROR_CONSULTA_TASA_CAMBIO;
			else{
				if(moneda.getValue().equals(Entidad.DOLAR.toString())){
					texto = texto.replace("%dc", compra.toString()+" Lempiras ").replace("%dv", venta.toString()+" Lempiras ");
				}
				if(moneda.getValue().equals(Entidad.EURO.toString())){
					texto = texto.replace("%ec", compra.toString()+" Lempiras ").replace("%ev", venta.toString()+" Lempiras ");			
				}
			}
				
		}
		}catch (Exception e) {
			// TODO: handle exception
			texto = MensajesErrorConWebServices.ERROR_CONSULTA_TASA_CAMBIO;
		}
		return texto;
	}
	
	public String[] preLogin(String name){
		String[] response = new String [4];
		response[0] = MensajesErrorConWebServices.ERROR_COMUNICACION;
		
		try {
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:val=\"http://hn.infatlan.och/ws/IA003/out/ValidaPreLogin\">"+
						  "<soapenv:Header/>"+
						   "<soapenv:Body>"+
						   "  <val:MT_ValidaPreLogin>"+
						   "      <activarMultipleEntrada>?</activarMultipleEntrada>"+
						   "      <activarParametroAdicional>?</activarParametroAdicional>"+
						   "       <!--Optional:-->"+
						   "       <transaccionId>100126</transaccionId>"+
						   "     <!--Optional:-->"+
						   "<aplicacionId>001</aplicacionId>"+
					         "<paisId>HN</paisId>"+
					         "<empresaId>?</empresaId>"+
					         "<!--Optional:-->"+
					         "<regionId>?</regionId>"+
					         "<!--Optional:-->"+
					         "<canalId>104</canalId>"+
						   "     <!--Optional:-->"+
						   "       <version>?</version>"+
						   "     <!--Optional:-->"+
						   "      <llaveSesion></llaveSesion>"+
						   "      <!--Optional:-->"+
						   "      <usuarioId>tsbasapi01</usuarioId>"+
						   "      <!--Optional:-->"+
						   "    <token>?</token>"+
						   "     <!--Zero or more repetitions:-->"+
						   "      <identificadorColeccion>"+
						   "        <!--Optional:-->"+
						   "          <was>?</was>"+
						   "         <!--Optional:-->"+
						   "         <pi>?</pi>"+
						   "        <!--Optional:-->"+
						   "         <omniCanal>?</omniCanal>"+
						   "         <!--Optional:-->"+
						   "        <recibo>?</recibo>"+
						   "         <!--Optional:-->"+
						   "         <numeroTransaccion>?</numeroTransaccion>"+
						   "      </identificadorColeccion>"+
						   "      <!--Optional:-->"+
						   "       <parametroAdicionalColeccion>"+
						   "       <!--Zero or more repetitions:-->"+
						   "         <parametroAdicionalItem>"+
						   "            <linea>1</linea>"+
						   "             <!--Optional:-->"+
						   "            <tipoRegistro>UAI</tipoRegistro>"+
						   "             <!--Optional:-->"+
						   "            <valor>%s</valor>"+
						   "         </parametroAdicionalItem>"+
						   "     </parametroAdicionalColeccion>"+
						   "      <!--Optional:-->"+
						   "  <logColeccion>"+
						   "    <!--Zero or more repetitions:-->"+
						   "        <logItem>"+
						   "            <identificadorWas>?</identificadorWas>"+
						   "            <!--Optional:-->"+
						   "           <identificadorPi>?</identificadorPi>"+
						   "          <!--Optional:-->"+
						   "       <identificadorOmniCanal>?</identificadorOmniCanal>"+
						   "           <!--Optional:-->"+
						   "            <identificadorRecibo>?</identificadorRecibo>"+
						   "            <!--Optional:-->"+
						   "            <numeroPeticion>?</numeroPeticion>"+
						   "            <!--Optional:-->"+
						   "            <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion>"+
						   "            <!--Optional:-->"+
						   "            <aplicacionId>?</aplicacionId>"+
						   "            <!--Optional:-->"+
						   "           <canalId>?</canalId>"+
						   "            <!--Optional:-->"+
						   "            <ambienteId>?</ambienteId>"+
						   "           <!--Optional:-->"+
						   "            <transaccionId>?</transaccionId>"+
						   "            <!--Optional:-->"+
						   "           <accion>?</accion>"+
						   "            <!--Optional:-->"+
						   "            <tipo>?</tipo>"+
						   "            <!--Optional:-->"+
						   "            <fecha>?</fecha>"+
						   "            <!--Optional:-->"+
						   "            <hora>?</hora>"+
						   "            <!--Optional:-->"+
						   "            <auxiliar1>?</auxiliar1>"+
						   "            <!--Optional:-->"+
						   "            <auxiliar2>?</auxiliar2>"+
						   "            <!--Optional:-->"+
						   "            <parametroAdicionalColeccion>"+
						   "               <!--Zero or more repetitions:-->"+
						   "               <parametroAdicionalItem>"+
						   "                  <linea>?</linea>"+
						   "                   <!--Optional:-->"+
						   "                  <tipoRegistro>?</tipoRegistro>"+
						   "                 <!--Optional:-->"+
						   "                  <valor>?</valor>"+
						   "               </parametroAdicionalItem>"+
						   "            </parametroAdicionalColeccion>"+
						   "         </logItem>"+
						   "      </logColeccion>"+
						   "      <!--Optional:-->"+
						   "      <validaPreLoginParametroColeccion>"+
						   "         <!--You may enter the following 7 items in any order-->"+
						   "         <!--Optional:-->"+
						   "         <pinIngresado></pinIngresado>"+
						   "         <!--Optional:-->"+
						   "         <tokenIngresado>8819110P</tokenIngresado>"+
						   "         <!--Optional:-->"+
						   "         <direccionIp>150.150.7.12</direccionIp>"+
						   "         <!--Optional:-->"+
						   "         <captchaObtenido>1</captchaObtenido>"+
						   "         <!--Optional:-->"+
						   "          <captchaIngresado>3Y5DP</captchaIngresado>"+
						   "         <contadorPeticion>?</contadorPeticion>"+
						   "         <tipoAutorizacion>1</tipoAutorizacion>"+
						   "     </validaPreLoginParametroColeccion>"+
						   "   </val:MT_ValidaPreLogin>"+
						   "</soapenv:Body>"+
						   "</soapenv:Envelope>";
					
			requestBody = String.format(requestBody, name);
			
			String responseXML = 
					given().
					header("Content-Type", "text/xml;charset=UTF-8").
					auth().
					basic(usuario, password).
					body(requestBody).
					post(preLogin).
					andReturn().
					asString();
			
			System.out.println(responseXML);
			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
			NodeChildrenImpl body = xmlPath.get("Body");
			if(!body.toString().contains("Error")){
				NodeImpl prelogin = body.get(0).get("MT_ValidaPreLoginResponse");
				Node estado = prelogin.getNode("Respuesta").get("estado");
				
				String tipo = estado.getNode("tipo").toString();
				if(tipo.equals("S1"))
				{
					Node validaPreLoginColeccion = prelogin.getNode("Respuesta").getNode("validaPreLoginColeccion");
					response[0] = validaPreLoginColeccion.getNode("valido").toString();
					response[1] = validaPreLoginColeccion.getNode("llaveSesion").toString();
					response[2] = validaPreLoginColeccion.getNode("usuarioId").toString();
					response[3] = validaPreLoginColeccion.getNode("usuarioNombre").toString();
				}
				else{
					if(!estado.getNode("descripcion").toString().equals("") && !estado.getNode("descripcion").toString().equals("Error desconocido"))
						response[0] = estado.getNode("descripcion").toString();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response[0] = MensajesErrorConWebServices.ERROR_COMUNICACION;
		}
		return response;
	}

	public boolean[] tieneCuentas(String user, String llaveSesion){
		
		boolean[] cuentas = new boolean[2]; 
		String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\">"+
			"<soapenv:Header/>"+
				"<soapenv:Body>"+
					"<con:MT_ConsultaSaldo>"+
						"<activarMultipleEntrada>?</activarMultipleEntrada>"+
				         "<activarParametroAdicional>?</activarParametroAdicional>"+
				         "<!--Optional:-->"+
				         "<transaccionId>100128</transaccionId>"+
				         "<!--Optional:-->"+
				         "<aplicacionId>001</aplicacionId>"+
				         "<paisId>HN</paisId>"+
				         "<empresaId>?</empresaId>"+
				         "<!--Optional:-->"+
				         "<regionId>?</regionId>"+
				         "<!--Optional:-->"+
				         "<canalId>104</canalId>"+
				         "<!--Optional:-->"+
				         "<version>?</version>"+
				         "<!--Optional:-->"+
				         "<llaveSesion>%s</llaveSesion>"+
				         "<!--Optional:-->"+
				         "<usuarioId>?</usuarioId>"+
				         "<!--Optional:-->"+
				         "<token>?</token>"+
				         "<!--Zero or more repetitions:-->"+
				         "<identificadorColeccion>"+
				         "	<!--Optional:-->"+
				         "   <was>?</was>"+
				         "   <!--Optional:-->"+
				         "   <pi>?</pi>"+
				         "   <!--Optional:-->"+
				         "   <omniCanal>?</omniCanal>"+
				         "   <!--Optional:-->"+
				         "   <recibo>?</recibo>"+
				         "   <!--Optional:-->"+
				         "   <numeroTransaccion>?</numeroTransaccion>"+
				         "</identificadorColeccion>"+
				         "<!--Optional:-->"+
				         "<parametroAdicionalColeccion>"+
				         "   <!--Zero or more repetitions:-->"+
				         "    <parametroAdicionalItem>"+
				         " 	 <!--You may enter the following 3 items in any order-->"+
				         "	 <linea>0</linea>"+
				         " 	<!--Optional:-->"+
				         "	<tipoRegistro>UAI</tipoRegistro>"+
				         "	<!--Optional:-->"+
				         " 	<valor>%s</valor>"+
				         " 	</parametroAdicionalItem>"+
				         "   <parametroAdicionalItem>"+
				         "     <linea>1</linea>"+
				         "      <!--Optional:-->"+
				         "      <tipoRegistro></tipoRegistro>"+
				         "      <!--Optional:--> "+
				         "      <valor>A</valor>"+
				         "   </parametroAdicionalItem>"+
				         "</parametroAdicionalColeccion>"+
				         "<!--Optional:-->"+
				         "<logColeccion>"+
				         "   <!--Zero or more repetitions:-->"+
				         "   <logItem>"+
				         "      <identificadorWas>?</identificadorWas>"+
				         "      <!--Optional:-->"+
				         "      <identificadorPi>?</identificadorPi>"+
				         "     <!--Optional:-->"+
				         "      <identificadorOmniCanal>?</identificadorOmniCanal>"+
				         "      <!--Optional:-->"+
				         "      <identificadorRecibo>?</identificadorRecibo>"+
				         "      <!--Optional:-->"+
				         "      <numeroPeticion>?</numeroPeticion>"+
				         "      <!--Optional:-->"+
				         "      <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion>"+
				         "      <!--Optional:-->"+
				         "      <aplicacionId>?</aplicacionId>"+
				         "      <!--Optional:-->"+
				         "      <canalId>?</canalId>"+
				         "      <!--Optional:-->"+
				         "      <ambienteId>?</ambienteId>"+
				         "      <!--Optional:-->"+
				         "      <transaccionId>?</transaccionId>"+
				         "      <!--Optional:-->"+
				         "      <accion>?</accion>"+
				         "     <!--Optional:-->"+
				         "      <tipo>?</tipo>"+
				         "      <!--Optional:-->"+
				         "      <fecha>?</fecha>"+
				         "      <!--Optional:-->"+
				         "      <hora>?</hora>"+
				         "      <!--Optional:-->"+
				         "      <auxiliar1>?</auxiliar1>"+
				         "      <!--Optional:-->"+
				         "      <auxiliar2>?</auxiliar2>"+
				         "     <!--Optional:-->"+
				         "      <parametroAdicionalColeccion>"+
				         "        <!--Zero or more repetitions:-->"+
				         "         <parametroAdicionalItem>"+
				         "            <linea>?</linea>"+
				         "            <!--Optional:-->"+
				         "            <tipoRegistro>?</tipoRegistro>"+
				         "            <!--Optional:-->"+
				         "            <valor>?</valor>"+
				         "         </parametroAdicionalItem>"+
				         "      </parametroAdicionalColeccion>"+
				         "   </logItem>"+
				         "</logColeccion>"+
				         "<!--Optional:-->"+
				         "<consultaSaldoColeccion>"+
				         "   <tipoCuenta>?</tipoCuenta>"+
				         "   <!--Optional:-->"+
				         "   <peticionId>?</peticionId>"+
				         "</consultaSaldoColeccion>"+
			         "</con:MT_ConsultaSaldo>"+
		         "</soapenv:Body>"+
	         "</soapenv:Envelope>";
		
		requestBody = String.format(requestBody, llaveSesion, user);
		System.out.println(requestBody);
		
		try {
			String responseXML =
					given().
					header("Content-Type", "text/xml;charset=UTF-8").
					auth().
					basic(usuario, password).
					body(requestBody).
					post(saldo).
					andReturn().
					asString();
			System.out.println(responseXML);
			cuentas[0] = false;
			cuentas[1] = false;
			if(responseXML.contains("tarjetaItem"))
				cuentas[0] = true;
			if(responseXML.contains("cuentaItem") && responseXML.contains("<tipo>2</tipo>"))
				cuentas[1] = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			cuentas[0] = false;
			cuentas[1] = false;
		}
		    return cuentas;
	}
	
	
	public String[] login(String name, String clave){
		String[] resultados = new String[4];
		resultados[0] = MensajesErrorConWebServices.ERROR_COMUNICACION;
		try {
			String requestBody = "{"+
						   "\"Id\": \"\","+
						   "\"UserName\": \"%s\","+
						   "\"AppType\": \"Consumers\","+
						   "\"ChannelType\": \"Mobile\","+
						   "\"UserType\": 1,"+
						   "\"Password\": \"%s\"}";
					
			requestBody = String.format(requestBody, name,clave);
			
			Response response = 
					given().
					header("Content-Type", "application/json").
					header("Accept", "application/json").
					header("channelType", "Mobile").
					body(requestBody).
					post(login).andReturn();
			
			String responseXML = response.asString();
			Headers headers = response.getHeaders();
			
			
			System.out.println(responseXML);
			
			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject) parser.parse(responseXML);

			if(!json.containsKey("error"))
			{
				if(headers.hasHeaderWithName("TokenKey") && headers.hasHeaderWithName("x-csrf-token"))
				{
					resultados[0] = "true";
					resultados[1] = headers.getValue("TokenKey");
					resultados[2] = headers.getValue("x-csrf-token");
					resultados[3] = json.get("d").toString();
					
					System.out.println(headers.getValue("TokenKey"));
					System.out.println(headers.getValue("x-csrf-token"));
					System.out.println(json.get("d").toString());
				}
			}
			else{
				JSONObject error =  (JSONObject) json.get("error");
				if(error.get("message").toString().contains(MensajesErrorConWebServices.CODIGO_CREDENCIALES_INCORRECTOS))
					resultados[0] = MensajesErrorConWebServices.MENSAJE_CREDENCIALES_INCORRECTOS;
				if(error.get("message").toString().contains(MensajesErrorConWebServices.CODIGO_SESION_YA_INICIADA))
					resultados[0] = MensajesErrorConWebServices.MENSAJE_SESION_YA_INICIADA;
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resultados[0] = MensajesErrorConWebServices.ERROR_COMUNICACION;
		}
		return resultados;
	}
	
	public boolean logout(String tokenKey, String token, String requestBody){
		boolean logueado = false;
		try {
				
			requestBody = requestBody.replace("\\", "");
			System.out.println(requestBody);
			String response = 
					given().
					header("Content-Type", "application/json").
					header("Accept", "application/json").
					header("TokenKey", tokenKey).
					header("x-csrf-token", token).
					body(requestBody).
					post(logout).
					andReturn().
					asString();
			
	
			System.out.println(response);
			
			JSONParser parser = new JSONParser(); 
			JSONObject json = (JSONObject) parser.parse(response);

			if(!json.containsKey("error"))
				return true;
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		return logueado;
	}

	
	public String ultimosDigitos(String numero){
		int longitudMascara = numero.length()-4;
		return numero.substring(longitudMascara, numero.length());
	}
	
	public String getTipoCambio() 
	{
		return tipoCambio;
	}
	public void setTipoCambio(String tipoCambio) 
	{
		this.tipoCambio = tipoCambio;
	}

	public String getSaldo() {
		return saldo;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(String movimientos) {
		this.movimientos = movimientos;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPreLogin() {
		return preLogin;
	}

	public void setPreLogin(String preLogin) {
		this.preLogin = preLogin;
	}
	
	public String getLogout() {
		return logout;
	}

	public void setLogout(String logout) {
		this.logout = logout;
	}
	
}
 