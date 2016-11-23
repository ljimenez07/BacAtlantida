package com.ncubo.conf;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.cglib.core.EmitUtils.ArrayDelimiters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.mysql.fabric.xmlrpc.base.Array;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Sonido;
import com.ncubo.dao.ConsultaDao;
import com.ncubo.data.Consulta;
import com.ncubo.logicaDeConversaciones.Conversaciones;

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

	private Conversaciones miConversaciones = new Conversaciones();

	private ExtraerDatosWebService extraerDatos = new ExtraerDatosWebService();
	
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
		
		String[] nombre = new String[4];
		if(usuario.getUsuarioNombre() != null)
			nombre = usuario.getUsuarioNombre().split(" ");
		
		myContext.put("nombre", nombre[0]);
		
		String[] textos = null;
		ArrayList<Salida> salida = miConversaciones.conversarConElAgente(usuario, mensaje);
		
		String intent = "";
		String texto = "";
		String audio = "";
		JSONArray arrayList = new JSONArray(); 
		
		System.out.println(salida.size());
		
		for(int i = 0; i < salida.size(); i++){
			
			texto = salida.get(i).getMiTexto();
			System.out.println();
			System.out.println("texto  "+texto);
			System.out.println();
			
			if( salida.get(i).obtenerLaRespuestaDeIBM() == null)
			{
				continue;
			}
			intent = salida.get(i).obtenerLaRespuestaDeIBM().obtenerLaIntencionDeLaRespuesta().getNombre();
			System.out.println("Contexto Watson: "+salida.get(i).obtenerLaRespuestaDeIBM().messageResponse().getContext());
			
			texto = texto.replace("$", "");
			
		
			if(intent.equals(Intencion.SALDO.toString()) && usuario.estaLogueado())
			{
				String quiereSaldo = getVariable(salida.get(i).obtenerLaRespuestaDeIBM().messageResponse().getContext().toString(), "quiereSaldo");
	
				String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\"><soapenv:Header/> <soapenv:Body> <con:MT_ConsultaSaldo> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId>  <!--Optional:-->  <regionId>?</regionId>   <!--Optional:--> <canalId>?</canalId><!--Optional:-->  <version>?</version> <!--Optional:--> <llaveSesion></llaveSesion>  <!--Optional:--><usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:-->  <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:-->  <pi>?</pi><!--Optional:--><omniCanal>?</omniCanal>  <!--Optional:--> <recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion></identificadorColeccion> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem>  <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:-->    <valor>%s</valor></parametroAdicionalItem>  </parametroAdicionalColeccion> <!--Optional:--> <logColeccion><!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--><identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal>  <!--Optional:--><identificadorRecibo>?</identificadorRecibo> <!--Optional:--><numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:-->   <canalId>?</canalId> <!--Optional:-->  <ambienteId>?</ambienteId> <!--Optional:-->  <transaccionId>?</transaccionId> <!--Optional:--><accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora><!--Optional:--><auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2>  <!--Optional:--> <parametroAdicionalColeccion>  <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>?</tipoRegistro>  <!--Optional:-->   <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion>  <!--Optional:-->  <consultaSaldoColeccion> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <peticionId>?</peticionId> </consultaSaldoColeccion> </con:MT_ConsultaSaldo></soapenv:Body></soapenv:Envelope>";
				if(quiereSaldo.equals("credito"))
				{
					textos = extraerDatos.obtenerSaldoTarjetaCredito(requestBody, wsSaldo, texto, usuario.getUsuarioId());
				}
				if(quiereSaldo.equals("ahorro"))
				{
					textos = extraerDatos.obtenerSaldoCuentaAhorros(requestBody, wsSaldo, texto, usuario.getUsuarioId());
				}			
				
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.add(jsonObject);
				}
				
				consultaDao.insertar(
						new Consulta(Intencion.SALDO.toString(), new Timestamp(new Date().getTime()), Intencion.SALDO_DESCRIPCION.toString() , 1));
			}
			else if(( intent.equals(Intencion.SALDO.toString()) || intent.equals(Intencion.MOVIMIENTOS.toString())) && ! usuario.estaLogueado())
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", "Debe iniciar sesión para que conozcas tu disponible.");
				jsonObject.put("audio", "");	
				arrayList.add(jsonObject);
			}
			else if(intent.equals(Intencion.TASA_DE_CAMBIO.toString()) || texto.contains("%dc") || texto.contains("%dv") || texto.contains("%ec") || texto.contains("%ev")){
				
				String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tas=\"http://hn.infatlan.och/ws/ACD082/out/TasaCambio\"> <soapenv:Header/> <soapenv:Body> <tas:MT_TasaCambio> <activarMultipleEntrada>?</activarMultipleEntrada> <activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--> <transaccionId>100054</transaccionId> <!--Optional:--> <aplicacionId>?</aplicacionId> <paisId>?</paisId> <empresaId>?</empresaId> <!--Optional:--> <regionId>?</regionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <version>?</version> <!--Optional:--> <llaveSesion>?</llaveSesion> <!--Optional:--> <usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:--> <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:--> <pi>?</pi> <!--Optional:--> <omniCanal>?</omniCanal> <!--Optional:--> <recibo>?</recibo> <!--Optional:--> <numeroTransaccion>?</numeroTransaccion> </identificadorColeccion> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> <!--Optional:--> <logColeccion> <!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:--> <canalId>?</canalId> <!--Optional:--> <ambienteId>?</ambienteId> <!--Optional:--> <transaccionId>?</transaccionId> <!--Optional:--> <accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2> <!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea> <!--Optional:--> <tipoRegistro>?</tipoRegistro> <!--Optional:--> <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion> </tas:MT_TasaCambio> </soapenv:Body> </soapenv:Envelope>";
	
				String responseXML = given().body(requestBody).post(wsTasaCambio).andReturn().asString();
				
				System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
				
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
						texto = texto.replace("%dc", compra.toString()+" LPS ").replace("%dv", venta.toString()+" LPS ");
					}
					if(moneda.getValue().equals(Entidad.EURO.toString())){
						texto = texto.replace("%ec", compra.toString()+" LPS ").replace("%ev", venta.toString()+" LPS ");			
					}
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", "");	
				arrayList.add(jsonObject);
			}
			else if(intent.equals(Intencion.MOVIMIENTOS.toString()) && usuario.estaLogueado())
			{
				String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:cor=\"http://corebancario.ext.srv.infatlan.hn/\"> <soapenv:Header/> <soapenv:Body> <cor:consultaMovimiento> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional><!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId><!--Optional:--><regionId>?</regionId><!--Optional:--><canalId>?</canalId><!--Optional:--><version>?</version><!--Optional:--><llaveSesion>?</llaveSesion><!--Optional:--><usuarioId>?</usuarioId><!--Optional:--><token>?</token><!--Optional:--><identificadorColeccion><!--Optional:--><was>?</was><!--Optional:--><pi>?</pi><!--Optional:--><omniCanal>?</omniCanal><!--Optional:--><recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion> </identificadorColeccion><!--Optional:--> <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem><linea>?</linea> <!--Optional:-->   <tipoRegistro>?</tipoRegistro>  <!--Optional:--><valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion>  <!--Optional:-->  <logColeccion>  <!--Zero or more repetitions:--> <logItem><identificadorWas>?</identificadorWas> <!--Optional:--> <identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal> <!--Optional:--> <identificadorRecibo>?</identificadorRecibo> <!--Optional:--> <numeroPeticion>?</numeroPeticion> <!--Optional:--><identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--><aplicacionId>?</aplicacionId> <!--Optional:--><canalId>?</canalId>    <!--Optional:--> <ambienteId>?</ambienteId>  <!--Optional:-->  <transaccionId>?</transaccionId>  <!--Optional:--><accion>?</accion> <!--Optional:--><tipo>?</tipo><!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora> <!--Optional:--> <auxiliar1>?</auxiliar1> <!--Optional:--><auxiliar2>?</auxiliar2> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:-->   <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:--> <valor>%s</valor></parametroAdicionalItem> </parametroAdicionalColeccion>   </logItem></logColeccion> <!--Optional:--> <consultaMovimientoColeccion>  <!--Optional:--> <tipoConsulta>E</tipoConsulta> <!--Optional:--> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <numeroCuenta>?</numeroCuenta> <!--Optional:--><periodo>01</periodo> <!--Optional:--> <fechaInicio>?</fechaInicio>   <!--Optional:--> <fechaFinal>?</fechaFinal><!--Optional:--><tipoMonto>?</tipoMonto> <!--Optional:--> <monto>?</monto> <!--Optional:--> <operacion>?</operacion> </consultaMovimientoColeccion></cor:consultaMovimiento></soapenv:Body></soapenv:Envelope>";
				textos = extraerDatos.obtenerMovimientos(requestBody, wsMovimientos, texto, usuario.getUsuarioId(), "");
				
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.add(jsonObject);
				}
				
				consultaDao.insertar(
						new Consulta(Intencion.MOVIMIENTOS.toString(), new Timestamp(new Date().getTime()), Intencion.MOVIMIENTOS_DESCRIPCION.toString() , 1));
			
				
			}
			else if(intent.equals(Intencion.DISPONIBLE.toString()) && usuario.estaLogueado())
			{
				String quiereDisponible = getVariable(salida.get(i).obtenerLaRespuestaDeIBM().messageResponse().getContext().toString(), "quiereSaldo");
				String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\"><soapenv:Header/> <soapenv:Body> <con:MT_ConsultaSaldo> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId>  <!--Optional:-->  <regionId>?</regionId>   <!--Optional:--> <canalId>?</canalId><!--Optional:-->  <version>?</version> <!--Optional:--> <llaveSesion></llaveSesion>  <!--Optional:--><usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:-->  <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:-->  <pi>?</pi><!--Optional:--><omniCanal>?</omniCanal>  <!--Optional:--> <recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion></identificadorColeccion> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem>  <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:-->    <valor>%s</valor></parametroAdicionalItem>  </parametroAdicionalColeccion> <!--Optional:--> <logColeccion><!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--><identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal>  <!--Optional:--><identificadorRecibo>?</identificadorRecibo> <!--Optional:--><numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:-->   <canalId>?</canalId> <!--Optional:-->  <ambienteId>?</ambienteId> <!--Optional:-->  <transaccionId>?</transaccionId> <!--Optional:--><accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora><!--Optional:--><auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2>  <!--Optional:--> <parametroAdicionalColeccion>  <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>?</tipoRegistro>  <!--Optional:-->   <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion>  <!--Optional:-->  <consultaSaldoColeccion> <tipoCuenta>%s</tipoCuenta> <!--Optional:--> <peticionId>?</peticionId> </consultaSaldoColeccion> </con:MT_ConsultaSaldo></soapenv:Body></soapenv:Envelope>";
				
				if(quiereDisponible.equals("credito"))
				{
					textos = extraerDatos.obtenerDisponibleTarjetaCredito(requestBody, wsSaldo, texto, usuario.getUsuarioId());
				}
				if(quiereDisponible.equals("ahorro"))
				{
					textos = extraerDatos.obtenerSaldoCuentaAhorros(requestBody, wsSaldo, texto, usuario.getUsuarioId());
				}			
				if(quiereDisponible.equals("puntos"))
				{
					texto = texto.replaceAll("%pp", "200");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", texto);
					jsonObject.put("audio", "");	
					arrayList.add(jsonObject);
				}
				else{}
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", "");	
				arrayList.add(jsonObject);
			}
		}
		respuesta.put("textos", arrayList);
		System.out.println(respuesta.toString());
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
	
	public String getVariable(String context, String variable) throws JSONException
	{
		String value = "";
				
		JSONObject json = new JSONObject(context);
		if(json.has(variable))
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