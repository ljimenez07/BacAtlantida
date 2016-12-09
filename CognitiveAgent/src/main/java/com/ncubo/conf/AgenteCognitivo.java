package com.ncubo.conf;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ncubo.chatbot.bitacora.HistoricosDeConversaciones;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.watson.TextToSpeechWatson;
import com.ncubo.dao.ConsultaDao;
import com.ncubo.dao.UsuarioDao;
import com.ncubo.data.Categorias;
import com.ncubo.data.Consulta;
import com.ncubo.logicaDeConversaciones.Conversaciones;
import com.ncubo.util.FTPServidor;

import groovyjarjarantlr.debug.GuessingEvent;

@Component
@ConfigurationProperties("servercognitivo")
public class AgenteCognitivo 
{
	private String user;
	private String password;
	private String workspaceDeChats;
	private String workspaceDeConocerte;
	private String userTextToSpeech;
	private String passwordTextToSpeech;
	private String voiceTextToSpeech;
	private String pathAudio;
	private String pathXML;
	private String urlPublicaAudios;

	@Autowired
	private ConsultaDao consultaDao;
	
	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private FTPServidor ftp;
	@Autowired
	private Conversaciones misConversaciones;
	private HistoricosDeConversaciones historicoDeConversaciones;
	@Autowired
	private ExtraerDatosWebService extraerDatos;

	@PostConstruct
    public void init(){
		misConversaciones.inicializar(getPathXML(), consultaDao);
		inicializarGeneradorDeAudiosSingleton();
		historicoDeConversaciones = new HistoricosDeConversaciones();
    }
	
	public String procesarMensajeChat(Usuario usuario, String mensaje, Date date) throws Exception
	{
		return procesarMensaje(usuario, mensaje, date, workspaceDeChats, false);
	}
	
	public String procesarMensajeConocerte(Usuario usuario, String mensaje, Date date) throws Exception
	{
		return procesarMensajeConocerte(usuario, mensaje, date, workspaceDeConocerte);
	}
	
	private String procesarMensaje(Usuario usuario, String mensaje, Date date, String workspace, boolean esParaConocerte) throws Exception
	{
		JSONObject respuesta = new JSONObject();
		/*ObjectMapper mapper = new ObjectMapper();
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
		
		myContext.put("nombre", nombre[0]);*/
		
		String[] textos = null;
		ArrayList<Salida> salida = misConversaciones.conversarConElAgente(usuario, mensaje, false);
		
		String texto = "";
		JSONArray arrayList = new JSONArray(); 
		
		System.out.println(salida.size());
		boolean seTerminoElChat = false;
		
		for(int i = 0; i < salida.size(); i++){
			
			texto = salida.get(i).getMiTexto();
			System.out.println();
			System.out.println("texto  "+texto);
			System.out.println();
			
			String idFrase = salida.get(i).getFraseActual().getIdFrase();
			texto = texto.replace("$", "");
			
			if((idFrase.equals("saldoCredito") ||  idFrase.equals("disponibleCredito") ) && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerSaldoTarjetaCredito( texto, usuario.getUsuarioId());
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
				textos = extraerDatos.obtenerSaldoCuentaAhorros(texto, usuario.getUsuarioId());
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}
			else if(idFrase.equals("tasaDolar")||idFrase.equals("tasaEuro")){
				
				texto = extraerDatos.obtenerTasaCambio(texto);
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", urlPublicaAudios+TextToSpeechWatson.getInstance().getAudioToURL(texto, true));	
				arrayList.put(jsonObject);
			}
			else if(idFrase.equals("movimientosTarjeta") || idFrase.equals("movimientosCuenta") && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerMovimientos(texto, usuario.getUsuarioId(), "");
				
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
				textos = extraerDatos.obtenerSaldoCuentaAhorros( texto, usuario.getUsuarioId());
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
			/*else if (idFrase.equals("reportarExtravio"))
			{
				
				String textoParaReproducir = "";
				textoParaReproducir = texto.replaceAll("<br/>", "");
				if (idFrase.endsWith("reportarExtravio"))
				{
					
					textoParaReproducir = texto.substring(texto.indexOf("%decir")+6, texto.indexOf("%terminaDecir"))+texto.substring(texto.indexOf("%despedida")+10, texto.indexOf("%terminaDespedida"));
					System.out.println(textoParaReproducir);
					texto = texto.replace("%decir", "");
					texto = texto.replace("%terminaDecir", "");
					texto = texto.replace("%despedida", "");
					texto = texto.replace("%terminaDespedida", "");
				}
				
				String textoTag = ""; 
				while(texto.contains("@@"))
				{
					textoTag = texto.substring(texto.indexOf("@@")+2, texto.indexOf("@@@"));
					texto = texto.replaceFirst("@@"+textoTag+"@@@", "<"+textoTag+">");
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio",urlPublicaAudios+TextToSpeechWatson.getInstance().getAudioToURL(textoParaReproducir, true));	
				
				arrayList.put(jsonObject);
			}*/
			else
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", salida.get(i).getMiSonido().url());	
				arrayList.put(jsonObject);
			}
			if(salida.get(i).seTerminoElChat() || idFrase.equals("despedida") || idFrase.equals("noQuiereHacerOtraConsulta"))
				seTerminoElChat = true;
		}
		
		respuesta.put("textos", arrayList);
		respuesta.put("seTerminoElChat", seTerminoElChat);
		System.out.println("Respuesta Chat: "+ respuesta.toString());
		String loQueElClienteDijo = "";
		try {
			loQueElClienteDijo = salida.get(0).obtenerLaRespuestaDeIBM().loQueElClienteDijoFue();
		}catch(Exception e){
			loQueElClienteDijo = "Hola!";
		}
		historicoDeConversaciones.agregarHistorialALaConversacion(usuario.getIdSesion(), loQueElClienteDijo, respuesta.toString());
		
		return respuesta.toString();
		
	}

	private String procesarMensajeConocerte(Usuario usuario, String mensaje, Date date, String workspace) throws Exception
	{
		JSONObject respuesta = new JSONObject();
		String texto = "";
		JSONArray arrayList = new JSONArray(); 
		ArrayList<Salida> salida = null;
		boolean seTerminoElChat = false;
		
		if(usuario.getEstaLogueado()){
			salida = misConversaciones.conversarConElAgente(usuario, mensaje, true);
		
			for(int i = 0; i < salida.size(); i++){
				texto = salida.get(i).getMiTexto();
				String idFrase = salida.get(i).getFraseActual().getIdFrase();
				
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("texto", texto);
				jsonObject.put("audio", salida.get(i).getMiSonido().url());
				arrayList.put(jsonObject);
				
				if(idFrase.equals("despedidaConocerte"))
				{
					seTerminoElChat = true;
					
					Categorias categorias = new Categorias(
							obtenerValorDeGustosDeRestaurantes(usuario.getUsuarioId()),
							obtenerValorDeGustosDeBelleza(usuario.getUsuarioId()),
							obtenerValorDeGustosDeHoteles(usuario.getUsuarioId())
							);
					
					usuarioDao.insertar(usuario.getUsuarioId(), categorias);
					
					usuario.setCategorias(categorias);
				}
			}
		}
		
		respuesta.put("textos", arrayList);
		respuesta.put("seTerminoElChat", seTerminoElChat);
		System.out.println("Respuesta Conocerte: "+ respuesta.toString());
		
		if(usuario.getEstaLogueado()){
			String loQueElClienteDijo = "";
			try {
				loQueElClienteDijo = salida.get(0).obtenerLaRespuestaDeIBM().loQueElClienteDijoFue();
			}catch(Exception e){
				loQueElClienteDijo = "Hola!";
			}
			historicoDeConversaciones.agregarHistorialALaConversacionEspecifica(usuario.getIdSesion(), loQueElClienteDijo, respuesta.toString());
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

	private void inicializarGeneradorDeAudiosSingleton(){
		TextToSpeechWatson.getInstance(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), 
				this.getVoiceTextToSpeech(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), this.getPathAudio(), this.geturlPublicaAudios());
	}
	
	public void generarTodosLosAudiosEstaticos(){
		System.out.println("El path xml es: "+getPathXML());
		misConversaciones.generarAudiosEstaticos(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
				this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), this.geturlPublicaAudios());
	}
	
	public void generarAudioEstatico(String id){
		System.out.println("El path xml es: "+getPathXML());
		int index = 0;
		try{
			index = Integer.parseInt(id);
			misConversaciones.generarAudiosEstaticosDeUnTema(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
					this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), index, this.geturlPublicaAudios());
		}catch(Exception e){
			e.getStackTrace();
		}
	}
	
	public String borrarUnaConversacion(String idSession){
		return misConversaciones.borrarUnaConversacion(idSession);
	}
	
	public String verTodasLasCoversacionesActivas(){
		return misConversaciones.verTodasLasCoversacionesActivas();
	}
	
	public String verTodosLosClientesActivos(){
		return misConversaciones.verTodosLosClientesActivos();
	}
	
	public String verLosIdsDeLasConversacionesActivasPorCliente(String idCliente){
		return misConversaciones.verLosIdsDeLasConversacionesActivasPorCliente(idCliente);
	}
	
	public String borrarTodasLasConversacionesDeUnCliente(String idCliente){
		historicoDeConversaciones.borrarElHistoricoDeUnaConversacion(misConversaciones.obtenerLosIdsDeSesionDeUnCliente(idCliente));
		return misConversaciones.borrarTodasLasConversacionesDeUnCliente(idCliente);
	}
	
	public String verMiTemario(){
		return misConversaciones.verMiTemario();
	}
	
	public String verElHistoricoDeLaConversacion(String idSesion){
		return historicoDeConversaciones.verElHistoricoDeUnaConversacion(idSesion);
	}
	
	public String verElHistoricoDeUnaConversacionEspecifica(String idSesion){
		return historicoDeConversaciones.verElHistoricoDeUnaConversacionEspecifica(idSesion);
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
	
	public String getPathAudio(){
		return pathAudio;
	}

	public void setPathAudio(String path){
		this.pathAudio = path;
	}
	
	public String geturlPublicaAudios(){
		return urlPublicaAudios;
	}

	public void seturlPublicaAudios(String path){
		this.urlPublicaAudios = path;
	}
	
	public String getPathXML() {
		return pathXML;
	}

	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}
	
	public double obtenerValorDeGustosDeHoteles(String idCliente) throws Exception
	{
		if( ! misConversaciones.existeElCliente(idCliente))
			Thread.sleep(4000);
		
		return misConversaciones.obtenerCliente(idCliente).obtenerValorDeGustosDeHoteles();
	}

	public double obtenerValorDeGustosDeRestaurantes(String idCliente) throws Exception
	{
		if( ! misConversaciones.existeElCliente(idCliente))
			Thread.sleep(4000);
		
		return misConversaciones.obtenerCliente(idCliente).obtenerValorDeGustosDeRestaurantes();
	}
	
	public double obtenerValorDeGustosDeBelleza(String idCliente) throws Exception
	{
		if( ! misConversaciones.existeElCliente(idCliente))
			Thread.sleep(4000);
		
		return misConversaciones.obtenerCliente(idCliente).obtenerValorDeGustosDeBelleza();
	}
	
}