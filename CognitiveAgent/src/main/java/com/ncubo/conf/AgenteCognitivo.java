package com.ncubo.conf;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.XmlPath;
@Component
@ConfigurationProperties("servercognitivo")
public class AgenteCognitivo 
{
	private String user;
	private String password;
	private String workspaceDeChats;
	private String workspaceDeConocerte;

	
	private HashMap<String, JSONObject> contextoPorUsuario = new HashMap<String, JSONObject>(); //TODO quitarlo de aquí y meterlo en la session
	
	public String procesarMensajeChat(String contexto, String mensaje, Date date, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException
	{
		return procesarMensaje(contexto,mensaje,date, workspaceDeChats, request);
	}
	
	public String procesarMensajeConocerte(String contexto, String mensaje, Date date, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException
	{
		return procesarMensaje(contexto,mensaje,date, workspaceDeConocerte, request);
	}
	
	
	private String procesarMensaje(String contexto, String mensaje, Date date, String workspace, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException
	{
		JSONObject respuesta = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		JSONObject contenidoDelContexto = contextoPorUsuario.get(contexto);
		if( contenidoDelContexto == null ) contenidoDelContexto = new JSONObject();
		
		Map<String, Object> myContext = mapper.readValue(contenidoDelContexto.toString(), new TypeReference<Map<String, Object>>(){});
		ConversationService service = new ConversationService(dateFormat.format(date));
		service.setUsernameAndPassword(user, password);
		
		MessageRequest newMessage = new MessageRequest.Builder()
				.inputText(mensaje)
				.context(myContext)
				.build();		
		MessageResponse response = service.message(workspace, newMessage).execute();
		
		respuesta.put("contexto", contexto);
		contextoPorUsuario.put(contexto, new JSONObject(response.toString()).getJSONObject("context"));
		
		String intent = getIntent(response);
		if(intent.equals(Intencion.SALDO.toString()))
		{
			String requestBody = "<cor:consultaSaldo><activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <transaccionId>100128</transaccionId> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <regionId>?</regionId> <canalId>102 </canalId> <version>?</version> <llaveSesion></llaveSesion> <usuarioId></usuarioId> <token>?</token> <parametroAdicionalColeccion> <parametroAdicionalItem> <linea>0</linea> <tipoRegistro>UAI</tipoRegistro> <valor>TSTBASAPI01</valor> </parametroAdicionalItem> <parametroAdicionalItem> <linea>1</linea> <tipoRegistro>TC</tipoRegistro> <valor>M</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <consultaSaldoColeccion> <tipoCuenta>4</tipoCuenta> <peticionId>?</peticionId> </consultaSaldoColeccion> </cor:consultaSaldo>";
			String responseXML = given().body(requestBody).post(getCurrentUrl(request) + "/Ecommerce/getOwnAccounts/").andReturn().asString();
			
			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Respuesta");
			NodeChildrenImpl productoColeccion = xmlPath.get("productoColeccion");
			NodeImpl cuentaColeccion = productoColeccion.get(0).get("cuentaColeccion");
			List<?> lista = cuentaColeccion.get("cuentaItem");
			NodeImpl saldoColecion = (NodeImpl) lista.get(0);
			NodeImpl saldoContable = saldoColecion.get("saldoColeccion");
			String texto = getText(response);
			respuesta.put("texto", texto + saldoContable.get("contable"));

		}
		else if(intent.equals(Intencion.TASA_DE_CAMBIO.toString()))
		{
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";
			String responseXML = given().body(requestBody).post( getCurrentUrl(request)+ "/Ecommerce/getConversionRates/").andReturn().asString();

			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
			NodeChildrenImpl body = xmlPath.get("Body");
			NodeImpl tasa = body.get(0).get("MT_TasaCambioResponse");
			List<?> codigo = tasa.getNode("Respuesta").getNode("tasaCambioColeccion").get("tasaCambioItem");
			NodeImpl nodeTipoCambio1 = (NodeImpl) codigo.get(0);
			NodeImpl moneda = nodeTipoCambio1.get("moneda");
			NodeImpl compra = nodeTipoCambio1.get("compra");
			NodeImpl venta = nodeTipoCambio1.get("venta");
			String tipoCambio1 = moneda +": " +  compra + " " + venta;
			NodeImpl nodeTipoCambio2 = (NodeImpl) codigo.get(1);
			moneda = nodeTipoCambio2.get("moneda");
			compra = nodeTipoCambio2.get("compra");
			venta = nodeTipoCambio2.get("venta");
			String tipoCambio2 = moneda +": " +  compra + " " + venta;

			
			String texto = getText(response);
			respuesta.put("texto", texto + tipoCambio1 + " " + tipoCambio2);
		}
		else
		{
			String texto = getText(response);
			respuesta.put("texto", texto);
		}
		return respuesta.toString();
		
	}

	public String getIntent(MessageResponse response)
	{
		List<Intent> intents = response.getIntents();
		float confidence = 0;
		String intent = "";
		for(int i = 0; i < intents.size(); i++ )
		{
			if(intents.get(i).getConfidence().floatValue()> confidence)
			{
				confidence = intents.get(i).getConfidence().floatValue();
				intent = intents.get(i).getIntent();
			}	
		}
		return intent;
	}
	
	public static String getCurrentUrl(HttpServletRequest request) throws URISyntaxException, MalformedURLException
	{
	    URL url = new URL(request.getRequestURL().toString());
	    String host  = url.getHost();
	    String userInfo = url.getUserInfo();
	    String scheme = url.getProtocol();
	    int port = url.getPort();
	    String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
	    String query = (String) request.getAttribute("javax.servlet.forward.query_string");

	    URI uri = new URI(scheme,userInfo,host,port,path,query,null);
	    System.out.println(uri);
	    return uri.toString();
	}
	
	public String getText(MessageResponse response)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("");
		
		List<String> mensajes = response.getText();
		for (String mensaje : mensajes) 
		{
			stringBuilder.append(mensaje);
		}

		return stringBuilder.toString();
	}
	
	
	public String getUser() 
	{
		return user;
	}
	public String getPassword() 
	{
		return password;
	}
	
	public void setUser(String user) 
	{
		this.user = user;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public String getWorkspaceDeChats() {
		return workspaceDeChats;
	}
	public void setWorkspaceDeChats(String workspaceDeChats) {
		this.workspaceDeChats = workspaceDeChats;
	}
	public String getWorkspaceDeConocerte() {
		return workspaceDeConocerte;
	}
	public void setWorkspaceDeConocerte(String workspaceDeConocerte) {
		this.workspaceDeConocerte = workspaceDeConocerte;
	}
	
	
	
	

}
