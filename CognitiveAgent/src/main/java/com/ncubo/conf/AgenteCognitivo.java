package com.ncubo.conf;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import static com.jayway.restassured.RestAssured.given;

@Component
@ConfigurationProperties("servercognitivo")
public class AgenteCognitivo 
{
	private String user;
	private String password;
	private String workspaceDeChats;
	private String workspaceDeConocerte;
	
	private HashMap<String, JSONObject> contextoPorUsuario = new HashMap<String, JSONObject>(); //TODO quitarlo de aquí y meterlo en la session
	
	public String procesarMensajeChat(String contexto, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException, JDOMException
	{
		return procesarMensaje(contexto,mensaje,date, workspaceDeChats);
	}
	
	public String procesarMensajeConocerte(String contexto, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException, JDOMException
	{
		return procesarMensaje(contexto,mensaje,date, workspaceDeConocerte);
	}
	
	
	private String procesarMensaje(String contexto, String mensaje, Date date, String workspace) throws JsonParseException, JsonMappingException, IOException, JSONException, JDOMException
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
			String responseXML = given().body(requestBody).post("http://localhost:8080/Ecommerce/getOwnAccounts/").asString();
			writeXmlFile(responseXML, "saldo");
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File("src/main/resources/saldo.xml");
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			List<?> nodos = rootNode.getChildren("productoColeccion");
			Element productoColeccion = (Element) nodos.get(0);
			String saldoContable = productoColeccion.getChild("cuentaColeccion").getChild("cuentaItem").getChild("saldoColeccion").getChildText("contable");
			String texto = getText(response) + saldoContable;
			respuesta.put("texto", texto);

		}
		else if(intent.equals(Intencion.TASA_DE_CAMBIO.toString()))
		{
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";
			String responseXML = given().body(requestBody).post("http://localhost:8080/Ecommerce/getConversionRates/").asString();
			writeXmlFile(responseXML, "tasa_cambio");
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File("src/main/resources/tasa_cambio.xml");
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			
			System.out.println(rootNode.getChildren("Body"));
			List<?> nodos = rootNode.getChildren();
			Element nodoRepuesta = null;
			for(Object object : nodos)
			{
				Element nodo = (Element) object;
				for(Object nuevoNodo : nodo.getChildren())
				{
					Element nuevoElement = (Element) nuevoNodo;
					nodoRepuesta = (Element) nuevoElement.getChildren().get(0);
				}
				
			}
			Element tasaCambioColeccion = (Element) nodoRepuesta.getChildren().get(1);
			StringBuilder tasasDeCambio = new StringBuilder();
			for(Object tasaCambio : tasaCambioColeccion.getChildren())
			{
				Element tasaActual  = (Element) tasaCambio;
				tasasDeCambio.append(" Moneda ");
				tasasDeCambio.append(tasaActual.getChildText("moneda"));
				tasasDeCambio.append(" Compra: ");
				tasasDeCambio.append(tasaActual.getChildText("compra"));
				tasasDeCambio.append(" Venta: ");
				tasasDeCambio.append(tasaActual.getChildText("venta"));
			}
			
			String texto = getText(response);
			respuesta.put("texto", texto + tasasDeCambio);
		}
		else
		{
			String texto = getText(response);
			respuesta.put("texto", texto);
		}
		return respuesta.toString();
		
	}
	public void writeXmlFile(String responseXml, String name) throws IOException
	{
		PrintWriter writer = new PrintWriter("src/main/resources/" + name +".xml");
		String [] lines = responseXml.split("\n");
		for (String line : lines) 
		{
			writer.println(line);
		}
		writer.close();
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
