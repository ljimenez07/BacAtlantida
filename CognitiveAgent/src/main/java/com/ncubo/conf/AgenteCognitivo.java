package com.ncubo.conf;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.ncubo.dao.ConsultaDao;
import com.ncubo.data.Consulta;

@Component
@ConfigurationProperties("servercognitivo")

public class AgenteCognitivo 
{
	private String user;
	private String password;
	private String workspaceDeChats;
	private String workspaceDeConocerte;
	private String wsTasaCambio;
	private String wsSaldo;
	private String wsMovimientos;
	private String userTextToSpeech;
	private String passwordTextToSpeech;
	private String voiceTextToSpeech;

	@Autowired
	private ConsultaDao consultaDao;

	public String procesarMensajeChat(Usuario usuario, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException, ClassNotFoundException, SQLException, ParseException
	{
	
		return procesarMensaje(usuario,mensaje,date, workspaceDeChats, false);
	}
	
	public String procesarMensajeConocerte(Usuario usuario, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException, ClassNotFoundException, SQLException, ParseException
	{
	
		return procesarMensaje(usuario,mensaje,date, workspaceDeConocerte, true);
	}
	
	private String procesarMensaje(Usuario usuario, String mensaje, Date date, String workspace, boolean esParaConocerte) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException, ClassNotFoundException, SQLException, ParseException
	{
		JSONObject respuesta = new JSONObject();
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	//	String contexto= usuario.getContextoDeWatson();
		
		JSONObject contenidoDelContexto ;
		if( esParaConocerte )
		{
			System.out.println("contexto de watson cuando entra "+ usuario.getContextoDeWatsonParaConocerte());
			contenidoDelContexto = new JSONObject(usuario.getContextoDeWatsonParaConocerte());
		}
		else
		{
			System.out.println("contexto de watson cuando entra "+ usuario.getContextoDeWatsonParaChats());
			contenidoDelContexto = new JSONObject(usuario.getContextoDeWatsonParaChats());
		}
		
		
		
		if( contenidoDelContexto == null ) contenidoDelContexto = new JSONObject();
				
		Map<String, Object> myContext = mapper.readValue(contenidoDelContexto.toString(), new TypeReference<Map<String, Object>>(){});
		ConversationService service = new ConversationService(dateFormat.format(date));
		service.setUsernameAndPassword(user, password);
		
		myContext.put("logueado", usuario.estaLogueado());
		myContext.put("intentActual", "");
		String[] nombre = new String[4];
		if(usuario.getUsuarioNombre() != null)
			nombre = usuario.getUsuarioNombre().split(" ");
		
		myContext.put("nombre", nombre[0]);
		
		MessageRequest newMessage = new MessageRequest.Builder()
				.inputText(mensaje)
				.context(myContext)
				.alternateIntents(true)
				.build();		
		MessageResponse response = service.message(workspace, newMessage).execute();
		
		
		
		if( esParaConocerte )
		{
			usuario.setContextoDeWatsonParaConocerte(new JSONObject(response.toString()).getJSONObject("context").toString());
			System.out.println("contexto de watson cuando sale "+ usuario.getContextoDeWatsonParaConocerte());
		}
		else
		{
			usuario.setContextoDeWatsonParaChats(new JSONObject(response.toString()).getJSONObject("context").toString());
			System.out.println("contexto de watson cuando sale "+ usuario.getContextoDeWatsonParaChats());
		}
		
		
		
		
		String intent = getIntent(response);
		String texto = getText(response);
		String intentActual = getVariable(response,"intentActual");
		if(intentActual.equals(Intencion.SALDO.toString()) && usuario.estaLogueado())
		{
			Boolean variableResponder = Boolean.parseBoolean(getVariable(response, "responderSaldo"));
			
			if(variableResponder)
			{
			String requestBody = "<cor:consultaSaldo><activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <transaccionId>100128</transaccionId> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <regionId>?</regionId> <canalId>102 </canalId> <version>?</version> <llaveSesion></llaveSesion> <usuarioId></usuarioId> <token>?</token> <parametroAdicionalColeccion> <parametroAdicionalItem> <linea>0</linea> <tipoRegistro>UAI</tipoRegistro> <valor>TSTBASAPI01</valor> </parametroAdicionalItem> <parametroAdicionalItem> <linea>1</linea> <tipoRegistro>TC</tipoRegistro> <valor>M</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <consultaSaldoColeccion> <tipoCuenta>4</tipoCuenta> <peticionId>?</peticionId> </consultaSaldoColeccion> </cor:consultaSaldo>";	
			String responseXML = given().body(requestBody).post(wsSaldo).andReturn().asString();
			
				XmlPath xmlPath = new XmlPath(responseXML).setRoot("Respuesta");
				NodeChildrenImpl productoColeccion = xmlPath.get("productoColeccion");
				NodeImpl cuentaColeccion = productoColeccion.get(0).get("cuentaColeccion");
				List<?> lista = cuentaColeccion.get("cuentaItem");
				NodeImpl saldoColecion = (NodeImpl) lista.get(0);
				NodeImpl saldoContable = saldoColecion.get("saldoColeccion");
				NodeImpl saldo = saldoContable.get("contable");
				NodeImpl moneda = saldoColecion.get("moneda");
				
				texto = texto.replaceAll("%stc", saldo.toString());
				if(moneda.toString().equals("USD"))
					texto = texto.replaceAll("%nmm", "dólares");
				if(moneda.toString().equals("EUR"))
					texto = texto.replaceAll("%nmm", "euros");
				if(moneda.toString().equals("LPS"))
					texto = texto.replaceAll("%nmm", "lempiras");

				
				texto = texto.replaceAll("%cc", saldo.toString());
				
			}
			
			
			consultaDao.insertar(
					new Consulta(Intencion.SALDO.toString(), new Timestamp(new Date().getTime()), Intencion.SALDO_DESCRIPCION.toString() , 1));
			respuesta.put("texto", texto);
		}
	
		else if(intentActual.equals(Intencion.TASA_DE_CAMBIO.toString())){
			
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";

			String responseXML = given().body(requestBody).post(wsTasaCambio).andReturn().asString();
			
			System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
			
			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
			NodeChildrenImpl body = xmlPath.get("Body");
			NodeImpl tasa = body.get(0).get("MT_TasaCambioResponse");
			List<?> codigo = tasa.getNode("Respuesta").getNode("tasaCambioColeccion").get("tasaCambioItem");
			
			for(int i = 0; i < codigo.size(); i++)
			{
				NodeImpl nodeTipoCambio1 = (NodeImpl) codigo.get(i);
				NodeImpl moneda = nodeTipoCambio1.get("moneda");
				NodeImpl compra = nodeTipoCambio1.get("compra");
				NodeImpl venta = nodeTipoCambio1.get("venta");
				if(moneda.getValue().equals(Entidad.DOLAR.toString())){
					
				texto = texto.replaceAll("%dc", compra.toString()+" LPS");
				texto = texto.replaceAll("%dv", venta.toString()+" LPS");
							
				}
				if(moneda.getValue().equals(Entidad.EURO.toString())){

					texto = texto.replaceAll("%ec", compra.toString()+" LPS");
					texto = texto.replaceAll("%ev", venta.toString()+" LPS");			
				}
			}
			respuesta.put("texto", texto);
		}
		else if(intent.equals(Intencion.MOVIMIENTOS.toString()) && usuario.estaLogueado())
		{
			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";
			String responseXML = given().body(requestBody).post( wsMovimientos).andReturn().asString();

			System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
			
			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Envelope");
			NodeChildrenImpl body = xmlPath.get("Body");
			NodeImpl tasa = body.get(0).get("MT_ConsultaMovimientoResponse");
			List<?> codigo = tasa.getNode("Respuesta").getNode("movimientoCuentaTarjetaColeccion").get("movimientoCuentaTarjetaItem");
			
			int last = codigo.size()-1;
			String movimientos = "";
			for(int i = 0; i < 3 ; i++)
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

				if(codigoTransaccion.getValue().equals("CR"))
					movimientos = movimientos +"<br> El día "+fecha+ " a las "+ hora + " se realizó un crédito por " + montoTransaccion + " " + moneda+" con el detalle "+descripcion+".";
				if(codigoTransaccion.getValue().equals("DB"))
					movimientos = movimientos +"<br> El día "+fecha+ " a las "+ hora + " se realizó un débito por " + montoTransaccion + " " + moneda+" con el detalle "+descripcion+".";
				
				last--;
				consultaDao.insertar(
						new Consulta(Intencion.MOVIMIENTOS.toString(), new Timestamp(new Date().getTime()), Intencion.MOVIMIENTOS_DESCRIPCION.toString() , 1));
			}
			respuesta.put("texto", texto + movimientos );
			
		}
		else if(intentActual.equals(Intencion.DISPONIBLE.toString()) && usuario.estaLogueado())
		{
			Boolean variableResponder = Boolean.parseBoolean(getVariable(response, "responderDisponible"));
			
			if(variableResponder)
			{
			String requestBody = "<cor:consultaSaldo><activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <transaccionId>100128</transaccionId> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <regionId>?</regionId> <canalId>102 </canalId> <version>?</version> <llaveSesion></llaveSesion> <usuarioId></usuarioId> <token>?</token> <parametroAdicionalColeccion> <parametroAdicionalItem> <linea>0</linea> <tipoRegistro>UAI</tipoRegistro> <valor>TSTBASAPI01</valor> </parametroAdicionalItem> <parametroAdicionalItem> <linea>1</linea> <tipoRegistro>TC</tipoRegistro> <valor>M</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <consultaSaldoColeccion> <tipoCuenta>4</tipoCuenta> <peticionId>?</peticionId> </consultaSaldoColeccion> </cor:consultaSaldo>";	
			String responseXML = given().body(requestBody).post(wsSaldo).andReturn().asString();
			

			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Respuesta");
			NodeChildrenImpl productoColeccion = xmlPath.get("productoColeccion");
			NodeImpl cuentaColeccion = productoColeccion.get(0).get("cuentaColeccion");
			List<?> lista = cuentaColeccion.get("cuentaItem");
			NodeImpl saldoColecion = (NodeImpl) lista.get(0);
			NodeImpl saldoContable = saldoColecion.get("saldoColeccion");
			NodeImpl saldo = saldoContable.get("contable");
			NodeImpl moneda = saldoColecion.get("moneda");
			
			texto = texto.replaceAll("%stc", saldo.toString());
			if(moneda.toString().equals("USD"))
				texto = texto.replaceAll("%nmm", "dólares");
			if(moneda.toString().equals("EUR"))
				texto = texto.replaceAll("%nmm", "euros");
			if(moneda.toString().equals("LPS"))
				texto = texto.replaceAll("%nmm", "lempiras");

				
				texto = texto.replaceAll("%cc", saldo.toString());
				
			}

			if(texto.contains("%pp"))
			{
				texto = texto.replaceAll("%pp", "200");
			}
			respuesta.put("texto", texto);
		}
		else
		{
			respuesta.put("texto", texto);
		}
		return respuesta.toString();
		
	}

	public String getWsMovimientos() {
		return wsMovimientos;
	}


	public void setWsMovimientos(String wsMovimientos) {
		this.wsMovimientos = wsMovimientos;
	}


	public String getIntent(MessageResponse response)
	{
		List<Intent> intents = response.getIntents();
		float confidence = 0;
		String intent = "";
		for(int i = 0; i < intents.size(); i++ )
		{
			System.out.println("intent  "+ intents.get(i).getIntent()+"   "+intents.get(i).getConfidence().floatValue());
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
	
	public String[] getEntities(MessageResponse response)
	{
		List<Entity> entities = response.getEntities();
		String[] entitiesArray = new String [entities.size()];
		for(int i = 0; i < entities.size(); i++ )
		{
			entitiesArray[i]= entities.get(i).getEntity();
		}
		return entitiesArray;
	}
	
	public String getVariable(MessageResponse response, String variable) throws JSONException
	{
		String value = "";
		
		JSONObject json = new JSONObject(response.toString());
		
		json = new JSONObject(json.get("context").toString());
		value = json.get(variable).toString();
		
		return value;
	}
	
	public String getWsTasaCambio() {
		return wsTasaCambio;
	}

	public void setWsTasaCambio(String wsTasaCambio) {
		this.wsTasaCambio = wsTasaCambio;
	}

	public String getWsSaldo() {
		return wsSaldo;
	}

	public void setWsSaldo(String wsSaldo) {
		this.wsSaldo = wsSaldo;
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

	public String getUserTextToSpeech() {
		return userTextToSpeech;
	}

	public void setUserTextToSpeech(String userTextToSpeech) {
		this.userTextToSpeech = userTextToSpeech;
	}

	public String getPasswordTextToSpeech() {
		return passwordTextToSpeech;
	}

	public void setPasswordTextToSpeech(String passwordTextToSpeech) {
		this.passwordTextToSpeech = passwordTextToSpeech;
	}

	public String getVoiceTextToSpeech() {
		return voiceTextToSpeech;
	}

	public void setVoiceTextToSpeech(String voiceTextToSpeech) {
		this.voiceTextToSpeech = voiceTextToSpeech;
	}

	public ConsultaDao getConsultaDao() {
		return consultaDao;
	}

	public void setConsultaDao(ConsultaDao consultaDao) {
		this.consultaDao = consultaDao;
	}
	
}
