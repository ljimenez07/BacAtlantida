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
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.jayway.restassured.internal.path.xml.NodeChildrenImpl;
import com.jayway.restassured.internal.path.xml.NodeImpl;
import com.jayway.restassured.path.xml.XmlPath;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.dao.ConsultaDao;
import com.ncubo.data.Consulta;
import com.ncubo.util.FTPServidor;
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
	private String pathAudio;
	private String pathXML;
	private String urlPublicaAudios;

	@Autowired
	private ConsultaDao consultaDao;

	@Autowired
	private FTPServidor ftp;
	
	private Conversaciones miConversaciones;

	@PostConstruct
    public void init(){
        // start your monitoring in here
		miConversaciones = new Conversaciones(getPathXML());
    }
	
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
		
		ArrayList<Salida> salida = miConversaciones.conversarConElAgente(usuario, mensaje);
		
		String intent = "";
		String texto = "";
		for(int i = 0; i < salida.size(); i++){
			texto = texto + " " + salida.get(i).getMiTexto();
			if( ! salida.get(i).getMiSonido().url().equals("")){
				System.out.println("La url del audio es: "+salida.get(i).getMiSonido().url());
			}
			if( salida.get(i).obtenerLaRespuestaDeIBM() == null)
			{
				continue;
			}
			intent = salida.get(i).obtenerLaRespuestaDeIBM().obtenerLaIntencionDeLaRespuesta().getNombre();
			System.out.println("Contexto Watson: "+salida.get(i).obtenerLaRespuestaDeIBM().messageResponse().getContext());
		
		}
		texto = texto.replace("$", "");
		//usuario.setContextoDeWatson(new JSONObject(response.toString()).getJSONObject("context").toString());
		
		
	/*	if( esParaConocerte )
		{
			usuario.setContextoDeWatsonParaConocerte(new JSONObject(response.toString()).getJSONObject("context").toString());
			System.out.println("contexto de watson cuando sale "+ usuario.getContextoDeWatsonParaConocerte());
		}
		else
		{
			usuario.setContextoDeWatsonParaChats(new JSONObject(response.toString()).getJSONObject("context").toString());
			System.out.println("contexto de watson cuando sale "+ usuario.getContextoDeWatsonParaChats());
		}*/
		
		if(intent.equals(Intencion.SALDO.toString()) && usuario.estaLogueado() || texto.contains("%stc") || texto.contains("%cc"))
		{

			String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:con=\"http://hn.infatlan.och/ws/ACD004/out/ConsultaSaldo\"><soapenv:Header/> <soapenv:Body> <con:MT_ConsultaSaldo> <activarMultipleEntrada>?</activarMultipleEntrada><activarParametroAdicional>?</activarParametroAdicional> <!--Optional:--><transaccionId>?</transaccionId><!--Optional:--><aplicacionId>?</aplicacionId><paisId>?</paisId><empresaId>?</empresaId>  <!--Optional:-->  <regionId>?</regionId>   <!--Optional:--> <canalId>?</canalId><!--Optional:-->  <version>?</version> <!--Optional:--> <llaveSesion></llaveSesion>  <!--Optional:--><usuarioId>?</usuarioId> <!--Optional:--> <token>?</token> <!--Zero or more repetitions:-->  <identificadorColeccion> <!--Optional:--> <was>?</was> <!--Optional:-->  <pi>?</pi><!--Optional:--><omniCanal>?</omniCanal>  <!--Optional:--> <recibo>?</recibo> <!--Optional:--><numeroTransaccion>?</numeroTransaccion></identificadorColeccion> <!--Optional:-->  <parametroAdicionalColeccion> <!--Zero or more repetitions:--> <parametroAdicionalItem>  <linea>?</linea>  <!--Optional:-->  <tipoRegistro>UAI</tipoRegistro> <!--Optional:-->    <valor>%s</valor></parametroAdicionalItem>  </parametroAdicionalColeccion> <!--Optional:--> <logColeccion><!--Zero or more repetitions:--> <logItem> <identificadorWas>?</identificadorWas> <!--Optional:--><identificadorPi>?</identificadorPi> <!--Optional:--> <identificadorOmniCanal>?</identificadorOmniCanal>  <!--Optional:--><identificadorRecibo>?</identificadorRecibo> <!--Optional:--><numeroPeticion>?</numeroPeticion> <!--Optional:--> <identificadorNumeroTransaccion>?</identificadorNumeroTransaccion> <!--Optional:--> <aplicacionId>?</aplicacionId> <!--Optional:-->   <canalId>?</canalId> <!--Optional:-->  <ambienteId>?</ambienteId> <!--Optional:-->  <transaccionId>?</transaccionId> <!--Optional:--><accion>?</accion> <!--Optional:--> <tipo>?</tipo> <!--Optional:--> <fecha>?</fecha> <!--Optional:--> <hora>?</hora><!--Optional:--><auxiliar1>?</auxiliar1> <!--Optional:--> <auxiliar2>?</auxiliar2>  <!--Optional:--> <parametroAdicionalColeccion>  <!--Zero or more repetitions:--> <parametroAdicionalItem> <linea>?</linea>  <!--Optional:-->  <tipoRegistro>?</tipoRegistro>  <!--Optional:-->   <valor>?</valor> </parametroAdicionalItem> </parametroAdicionalColeccion> </logItem> </logColeccion>  <!--Optional:-->  <consultaSaldoColeccion> <tipoCuenta>?</tipoCuenta> <!--Optional:--> <peticionId>?</peticionId> </consultaSaldoColeccion> </con:MT_ConsultaSaldo></soapenv:Body></soapenv:Envelope>";
			requestBody = String.format(requestBody, usuario.getUsuarioId());
			System.out.println(requestBody);
		
			String responseXML = given().body(requestBody).post(wsSaldo).andReturn().asString();
			
			System.out.println(wsSaldo+" \n\t  "+requestBody+"   \n\t"+responseXML);
			
			XmlPath xmlPath = new XmlPath(responseXML).setRoot("Respuesta");
			NodeChildrenImpl productoColeccion = xmlPath.get("productoColeccion");
			NodeImpl cuentaColeccion = productoColeccion.get(0).get("cuentaColeccion");
			List<?> lista = cuentaColeccion.get("cuentaItem");
			NodeImpl saldoColecion = (NodeImpl) lista.get(0);
			NodeImpl saldoContable = saldoColecion.get("saldoColeccion");
			NodeImpl moneda = saldoColecion.get("moneda");
			NodeImpl saldo = saldoContable.get("contable");
			
			if(moneda.toString().equals("USD"))	{
				texto = texto.replaceAll("%nmm", "dólares");	 
			}
			if(moneda.toString().equals("EUR"))		
				texto = texto.replaceAll("%nmm", "euros");		
			if(moneda.toString().equals("LPS"))		
				texto = texto.replaceAll("%nmm", "lempiras");		
			
			texto = texto.replaceAll("%stc", saldo.toString());		
			texto = texto.replaceAll("%cc", saldo.toString());		
			
			respuesta.put("texto", texto);
			
			consultaDao.insertar(
					new Consulta(Intencion.SALDO.toString(), new Timestamp(new Date().getTime()), Intencion.SALDO_DESCRIPCION.toString() , 1));
		}
		else if(( intent.equals(Intencion.SALDO.toString()) || intent.equals(Intencion.MOVIMIENTOS.toString())) && ! usuario.estaLogueado())
		{
			respuesta.put("texto", "Debe iniciar sesión para que conozcas tu disponible.");
		}
		else if(intent.equals(Intencion.TASA_DE_CAMBIO.toString()) || texto.contains("%dc") || texto.contains("%dv") || texto.contains("%ec") || texto.contains("%ev")){
			
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
					texto = texto.replace("%dc", compra.toString()+" LPS ").replace("%dv", venta.toString()+" LPS ");
				}
				if(moneda.getValue().equals(Entidad.EURO.toString())){
					texto = texto.replace("%ec", compra.toString()+" LPS ").replace("%ev", venta.toString()+" LPS ");			
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
		else if(intent.equals(Intencion.DISPONIBLE.toString()) && usuario.estaLogueado())
		{
			
			if(texto.contains("%pp"))
			{
				texto = texto.replaceAll("%pp", "200");
				respuesta.put("texto", texto);
			}
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
	
	public void generarTodosLosAudiosEstaticos(){
		System.out.println("El path xml es: "+getPathXML());
		miConversaciones.generarAudiosEstaticos(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
				this.getPathAudio(), this.getUrlPublicaAudios(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto());
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
	
	public String getPathAudio(){
		return pathAudio;
	}

	public void setPathAudio(String path){
		this.pathAudio = path;
	}
	
	public String getUrlPublicaAudios() {
		return urlPublicaAudios;
	}

	public void setUrlPublicaAudios(String urlPublicaAudios) {
		this.urlPublicaAudios = urlPublicaAudios;
	}
	
	public String getPathXML() {
		return pathXML;
	}

	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}
}