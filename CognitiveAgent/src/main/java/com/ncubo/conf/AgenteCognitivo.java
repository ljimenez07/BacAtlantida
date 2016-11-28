package com.ncubo.conf;

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
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.watson.TextToSpeechWatson;
import com.ncubo.chatbot.partesDeLaConversacion.Tema;
import com.ncubo.dao.ConsultaDao;
import com.ncubo.data.Consulta;
import com.ncubo.logicaDeConversaciones.Conversaciones;
import com.ncubo.util.FTPServidor;

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
	
	private Conversaciones misConversaciones;
	private ExtraerDatosWebService extraerDatos = new ExtraerDatosWebService();

	@PostConstruct
    public void init(){
        // start your monitoring in here
		misConversaciones = new Conversaciones(getPathXML());
    }
	
	public String procesarMensajeChat(Usuario usuario, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException, ClassNotFoundException, SQLException, ParseException
	{
	
		return procesarMensaje(usuario,mensaje,date, workspaceDeChats, false);
	}
	
	public String procesarMensajeConocerte(Usuario usuario, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException, URISyntaxException, ClassNotFoundException, SQLException, ParseException
	{
		return procesarMensajeConocerte(usuario, mensaje, date, workspaceDeConocerte);
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
		
		Map<String, Object> myContext = mapper.readValue(contenidoDelContexto.toString(), new TypeReference<Map<String, Object>>(){});
		ConversationService service = new ConversationService(dateFormat.format(date));
		service.setUsernameAndPassword(user, password);
		
		myContext.put("logueado", usuario.getEstaLogueado());
		
		String[] nombre = new String[4];
		if(usuario.getUsuarioNombre() != null)
			nombre = usuario.getUsuarioNombre().split(" ");
		
		myContext.put("nombre", nombre[0]);
		
		String[] textos = null;
		ArrayList<Salida> salida = misConversaciones.conversarConElAgente(usuario, mensaje, false);
		
		String texto = "";
		JSONArray arrayList = new JSONArray(); 
		
		System.out.println(salida.size());
		boolean estaditicaSeDebeGuardar = true;
		Tema tema = null;
		ArrayList<Tema> temasTratados = new ArrayList<>();
		
		for(int i = 0; i < salida.size(); i++){
			
			texto = salida.get(i).getMiTexto();
			System.out.println();
			System.out.println("texto  "+texto);
			System.out.println();
			
			String idFrase = salida.get(i).getFraseActual().getIdFrase();
			texto = texto.replace("$", "");
			
			if((idFrase.equals("saldoCredito") ||  idFrase.equals("disponibleCredito") ) && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerSaldoTarjetaCredito(wsSaldo, texto, usuario.getUsuarioId());
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}
			else if(idFrase.equals("saldoCuentaAhorros") && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerSaldoCuentaAhorros(wsSaldo, texto, usuario.getUsuarioId());
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}
			else if((idFrase.equals("saldoCuentaAhorros") || idFrase.equals("saldoCredito")|| idFrase.equals("quiereSaldoTarjetaCredito")) && ! usuario.getEstaLogueado())
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", "Disculpa, no puedo mostrarte esa información a menos que inicies una sesión!.");
				jsonObject.put("audio", "");	
				arrayList.put(jsonObject);
			}
			else if((idFrase.equals("disponibleCredito") || idFrase.equals("disponibleCuentaAhorros")|| idFrase.equals("disponiblePuntos") || idFrase.equals("quiereDisponibleTarjetaCredito")) && ! usuario.getEstaLogueado())
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", "Disculpa, no puedo mostrarte esa información a menos que inicies una sesión!.");
				jsonObject.put("audio", "");	
				arrayList.put(jsonObject);
			}
			else if(idFrase.equals("movimientos") && ! usuario.getEstaLogueado())
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", "Disculpa, no puedo mostrarte esa información a menos que inicies una sesión!.");
				jsonObject.put("audio", "");	
				arrayList.put(jsonObject);
			}
			else if(idFrase.equals("tasaDolar")||idFrase.equals("tasaEuro")){
				
				texto = extraerDatos.obtenerTasaCambio(wsTasaCambio, texto);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", "");	
				arrayList.put(jsonObject);
			}
			else if(idFrase.equals("movimientos")&& usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerMovimientos(wsMovimientos, texto, usuario.getUsuarioId(), "");
				
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}
			else if(idFrase.equals("disponibleCuentaAhorros") && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerSaldoCuentaAhorros(wsSaldo, texto, usuario.getUsuarioId());
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
				
			}
			else if(idFrase.equals("disponiblePuntos") && usuario.getEstaLogueado())	
			{			
				texto = texto.replaceAll("%pp", "200");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", texto);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
			}
			else if(idFrase.equals("disponibleMillas")){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", salida.get(i).getMiSonido().url());	
				arrayList.put(jsonObject);
			}
			else if(texto.contains("%br"))
			{
				texto = texto.replaceAll("%br", "<br/>");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio",urlPublicaAudios+TextToSpeechWatson.getInstance().getAudioToURL(texto, pathAudio));	
				
				arrayList.put(jsonObject);
			}
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", salida.get(i).getMiSonido().url());	
				arrayList.put(jsonObject);
				
				estaditicaSeDebeGuardar = false;
			}
			
			tema = salida.get(i).getTemaActual();
			if ( estaditicaSeDebeGuardar && ! temasTratados.contains(tema) )
			{
				temasTratados.add(tema);
				System.out.println(tema);
			}
		}
		
		for(Tema temaActual : temasTratados)
		{
			consultaDao.insertar( new Consulta(temaActual, new Timestamp(new Date().getTime())) );
		}
			//System.out.println(tema);
			//consultaDao.insertar(
			//		new Consulta(Intencion.MOVIMIENTOS.toString(), new Timestamp(new Date().getTime()), Intencion.MOVIMIENTOS_DESCRIPCION.toString() , 1));
		
		respuesta.put("textos", arrayList);
		System.out.println(respuesta.toString());
		return respuesta.toString();
		
	}

	private String procesarMensajeConocerte(Usuario usuario, String mensaje, Date date, String workspace) throws JSONException
	{
		JSONObject respuesta = new JSONObject();
		String texto = "";
		JSONArray arrayList = new JSONArray(); 
		
		ArrayList<Salida> salida = misConversaciones.conversarConElAgente(usuario, mensaje, true);
		
		for(int i = 0; i < salida.size(); i++){	
			texto = salida.get(i).getMiTexto();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("texto", texto);
			jsonObject.put("audio", salida.get(i).getMiSonido().url());
			arrayList.put(jsonObject);
		}
		
		/*if(usuario.estaLogueado()){
			ArrayList<Salida> salida = miConversaciones.conversarConElAgente(usuario, mensaje, true);
			
			for(int i = 0; i < salida.size(); i++){	
				texto = salida.get(i).getMiTexto();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", salida.get(i).getMiSonido().url());
				arrayList.put(jsonObject);
			}
		}else{
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("texto", "Debe iniciar sesión.");
			jsonObject.put("audio", "");
			arrayList.put(jsonObject);
		}*/
		
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

	public void generarTodosLosAudiosEstaticos(){
		System.out.println("El path xml es: "+getPathXML());
		misConversaciones.generarAudiosEstaticos(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
				this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto());
	}
	
	public void generarAudioEstatico(String id){
		System.out.println("El path xml es: "+getPathXML());
		int index = 0;
		try{
			index = Integer.parseInt(id);
			misConversaciones.generarAudiosEstaticosDeUnTema(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
					this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), index);
		}catch(Exception e){
			e.getStackTrace();
		}
		
	}
	
	public String verMiTemario(){
		return misConversaciones.verMiTemario();
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
	
	public String getPathXML() {
		return pathXML;
	}

	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}
}