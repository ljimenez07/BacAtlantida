package com.ncubo.conf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.ibm.watson.developer_cloud.conversation.v1.model.Entity;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ncubo.agencias.Agencia;
import com.ncubo.agencias.Agencias;
import com.ncubo.agencias.FraseDeLaAgencia;
import com.ncubo.chatbot.audiosXML.AudiosXML;
import com.ncubo.chatbot.bitacora.HistoricosDeConversaciones;
import com.ncubo.chatbot.partesDeLaConversacion.Frase;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Sonido;
import com.ncubo.chatbot.watson.TextToSpeechWatson;
import com.ncubo.dao.UsuarioDao;
import com.ncubo.data.Categorias;
import com.ncubo.data.Configuracion;
import com.ncubo.db.ConexionALaDB;
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
	private String userTextToSpeech;
	private String passwordTextToSpeech;
	private String voiceTextToSpeech;
	private String pathAudio;
	private String pathXML;
	private String urlPublicaAudios;
	private String pathXMLAudios;
	
	@Autowired
	private Agencias agencias;
	
	@Autowired
	private UsuarioDao usuarioDao;
	
	@Autowired
	private FTPServidor ftp;
	
	@Autowired
	private Conversaciones misConversaciones;
	private HistoricosDeConversaciones historicoDeConversaciones;
	
	@Autowired
	private ExtraerDatosWebService extraerDatos;

	@Autowired
	private Configuracion config;
	
	@PostConstruct
    public void init(){
		misConversaciones.inicializar(getPathXML());
		inicializarGeneradorDeAudiosSingleton();
		inicializadorDeLaBD();
		historicoDeConversaciones = new HistoricosDeConversaciones();
		generarTodosLosAudiosEstaticosInternamente();
    }
	
	public String procesarMensajeChat(Usuario usuario, String mensaje, Date date) throws Exception
	{
		return procesarMensaje(usuario, mensaje, date, workspaceDeChats, false);
	}
	
	public String procesarMensajeConocerte(Usuario usuario, String mensaje, Date date) throws Exception
	{
		return procesarMensajeConocerte(usuario, mensaje, date, workspaceDeConocerte);
	}
	
	private ArrayList<Salida> seleccionarAgenciasADecir(Salida respuestaDeWatson){
		ArrayList<Salida> resupuesta = new ArrayList<Salida>();
		boolean hayAutobancos = false;
		boolean hayAgencias = true;
		
		if(respuestaDeWatson != null){
			if(respuestaDeWatson.obtenerLaRespuestaDeIBM().messageResponse() != null){
				if(respuestaDeWatson.obtenerLaRespuestaDeIBM().messageResponse().getContext() != null){
					String departamento = "";
					String ciudad = "";
					String nombreDeAgencia = "";
					
					for(Map.Entry<String, Object> contexto : respuestaDeWatson.obtenerLaRespuestaDeIBM().messageResponse().getContext().entrySet()) {
						if(contexto.getKey().equals("departamento")){
							System.out.println("Contexto: "+contexto.getKey()+", valor: "+contexto.getValue()); 
							departamento = contexto.getValue().toString();
							if(departamento.equals("nada")){
								departamento = "";	
							}
						}else if(contexto.getKey().equals("ciudad")){
							System.out.println("Contexto: "+contexto.getKey()+", valor: "+contexto.getValue()); 
							ciudad = contexto.getValue().toString();
							if(ciudad.equals("nada")){
								ciudad = "";
							}
						}else if(contexto.getKey().equals("nombreDeAgencia")){
							System.out.println("Contexto: "+contexto.getKey()+", valor: "+contexto.getValue()); 
							nombreDeAgencia = contexto.getValue().toString();
							if(nombreDeAgencia.equals("nada")){
								nombreDeAgencia = "";
							}
						}
					}
					Salida miAgencia = new Salida();
					Frase fraseDeLaAgencia = null;
					FraseDeLaAgencia respuesta = null;
					
					if( ! nombreDeAgencia.isEmpty() ){
						respuesta = agencias.buscarAgenciasPorNombre(nombreDeAgencia);
					}else{
						respuesta = agencias.buscarAgenciasPorCuidadYDepartamento(ciudad, departamento);
					}
					
					fraseDeLaAgencia = respuesta.getFrase();
					hayAutobancos = respuesta.isHayAutobancos();
					hayAgencias = respuesta.isHayAgencias();
					
					if(fraseDeLaAgencia != null){
						miAgencia.escribir(fraseDeLaAgencia.texto().get(1),  respuestaDeWatson.obtenerLaRespuestaDeIBM(), respuestaDeWatson.getTemaActual(), fraseDeLaAgencia);
					}else{
						fraseDeLaAgencia = respuestaDeWatson.getTemaActual().buscarUnaFrase("noHayAgenciasParaMostrar");
						miAgencia.escribir(fraseDeLaAgencia.texto().get(1),  respuestaDeWatson.obtenerLaRespuestaDeIBM(), respuestaDeWatson.getTemaActual(), fraseDeLaAgencia);
					}
					
					if(hayAgencias){
						if(hayAutobancos){
							resupuesta.add(respuestaDeWatson);
						}else{
							Salida sinAutobancos = new Salida();
							Frase fraseDeSinAutobanco = respuestaDeWatson.getTemaActual().buscarUnaFrase("mostrarSinAutobancos");
							int idDelSonidoAUsar = Integer.valueOf(fraseDeSinAutobanco.texto().get(0));
							Sonido sonido = fraseDeSinAutobanco.obtenerSonidoAUsar(idDelSonidoAUsar);
							sinAutobancos.escribir(fraseDeSinAutobanco.texto().get(1),  respuestaDeWatson.obtenerLaRespuestaDeIBM(), respuestaDeWatson.getTemaActual(), fraseDeSinAutobanco);
							sinAutobancos.escribir(sonido,  respuestaDeWatson.obtenerLaRespuestaDeIBM(), respuestaDeWatson.getTemaActual(), fraseDeSinAutobanco);

							resupuesta.add(sinAutobancos);
						}
						resupuesta.add(miAgencia);
					}else{
						resupuesta.add(miAgencia);
					}
			
				}
			}
		}
		return resupuesta;
	}
	
	private String procesarMensaje(Usuario usuario, String mensaje, Date date, String workspace, boolean esParaConocerte) throws Exception
	{
		JSONObject respuesta = new JSONObject();
		
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
			
			if(idFrase.equals("saldoCredito") && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerSaldoTarjetaCredito( texto, usuario.getLlaveSession(), usuario.getUsuarioId());
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}
			else if(idFrase.equals("disponibleCredito") && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerDisponibleTarjetaCredito(texto, usuario.getLlaveSession(), usuario.getUsuarioId());
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}
			else if((idFrase.equals("saldoCuentaAhorros") || idFrase.equals("disponibleCuentaAhorros")) && usuario.getEstaLogueado())
			{
				textos = extraerDatos.obtenerSaldoCuentaAhorros(texto, usuario.getLlaveSession(), usuario.getUsuarioId());
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
				
				if(!arrayList.toString().contains("\"texto\":\""+texto+"\""))
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", texto);
					jsonObject.put("audio", urlPublicaAudios+TextToSpeechWatson.getInstance().getAudioToURL(texto, true));	
					arrayList.put(jsonObject);
				}
			}
			else if(idFrase.equals("movimientosTarjeta") || idFrase.equals("movimientosCuenta") && usuario.getEstaLogueado())
			{
				if(idFrase.equals("movimientosTarjeta"))
					textos = extraerDatos.obtenerMovimientos(texto, usuario.getLlaveSession(), usuario.getUsuarioId(), "4");
				if(idFrase.equals("movimientosCuenta"))
					textos = extraerDatos.obtenerMovimientos(texto, usuario.getLlaveSession(), usuario.getUsuarioId(), "2");
				
				for(int j = 0; j < textos.length; j++)
				{
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", textos[j]);
					jsonObject.put("audio", "");	
					arrayList.put(jsonObject);
				}
			}else if(idFrase.equals("mostrarAgencias")){
				ArrayList<Salida> agencias = seleccionarAgenciasADecir(salida.get(i));
				
				for(Salida agencia: agencias){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("texto", agencia.getMiTexto());
					jsonObject.put("audio", agencia.getMiSonido().url());	
					arrayList.put(jsonObject);
				}
				
			}
			else{
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
				this.getVoiceTextToSpeech(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), ftp.getCarpeta(), this.getPathAudio(), this.geturlPublicaAudios());
	}
	
	private void inicializadorDeLaBD(){
		ConexionALaDB.getInstance(config.getUrl(), config.getNombreBase(), config.getUsuario(), config.getClave());
	}
	
	public void generarTodosLosAudiosEstaticos(){
		System.out.println("El path xml es: "+getPathXML());
		misConversaciones.generarAudiosEstaticos(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
				this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), ftp.getCarpeta(), this.geturlPublicaAudios(), this.getPathXMLAudios());
	}
	
	public void generarTodosLosAudiosEstaticosInternamente(){
		if (AudiosXML.getInstance().exiteElArchivoXMLDeAudios(this.getPathXMLAudios())){
			misConversaciones.generarAudiosEstaticos(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
					this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), ftp.getCarpeta(), this.geturlPublicaAudios(), this.getPathXMLAudios());
		}
	}
	
	public void generarAudioEstatico(String id){
		System.out.println("El path xml es: "+getPathXML());
		int index = 0;
		try{
			index = Integer.parseInt(id);
			misConversaciones.generarAudiosEstaticosDeUnTema(this.getUserTextToSpeech(), this.getPasswordTextToSpeech(), this.getVoiceTextToSpeech(), 
					this.getPathAudio(), ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), ftp.getCarpeta(), index, this.geturlPublicaAudios());
		}catch(Exception e){
			System.out.println("Error al generar el audio estatico: "+e.getMessage());
		}
	}
	
	public void cargarElNombreDeUnSonidoEstaticoEnMemoria(int indexTema, int indexFrase, String idNombreTema, String nombreDelArchivo){
		System.out.println("El path xml es: "+getPathXML());
		try{
			misConversaciones.cargarElNombreDeUnSonidoEstaticoEnMemoria(this.getPathAudio(), this.geturlPublicaAudios(), indexTema, indexFrase, idNombreTema, nombreDelArchivo);
		}catch(Exception e){
			System.out.println("Error al generar el audio estatico: "+e.getMessage());
		}
	}
	
	public String borrarUnaConversacion(String idSesion){
		historicoDeConversaciones.borrarElHistoricoDeUnaConversacion(idSesion, misConversaciones.buscarUnClienteApartirDeLaSesion(idSesion));
		return misConversaciones.borrarUnaConversacion(idSesion);
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
		historicoDeConversaciones.borrarElHistoricoDeUnaConversacionPorCliente(misConversaciones.obtenerLosIdsDeSesionDeUnCliente(idCliente), idCliente);
		return misConversaciones.borrarTodasLasConversacionesDeUnCliente(idCliente);
	}
	
	public String verMiTemario(){
		return misConversaciones.verMiTemario();
	}
	
	public String verElHistoricoDeLaConversacion(String idSesion, String fecha){
		String miHistorico = "";
		
		try {
			miHistorico = historicoDeConversaciones.obtenerMiBitacoraDeBD().buscarUnaConversacion(idSesion, fecha);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(historicoDeConversaciones.existeElHistoricoDeLaConversacion(idSesion) && miHistorico.isEmpty()){
			miHistorico = historicoDeConversaciones.verElHistoricoDeUnaConversacion(idSesion);
		}
			
		return miHistorico;
	}

	public String buscarConversacionesQueNoHanSidoVerificadasPorTema(String idTema) throws ClassNotFoundException, SQLException{
		return historicoDeConversaciones.buscarConversacionesQueNoHanSidoVerificadasPorTema(idTema);
	}
	
	public String cambiarDeEstadoAVerificadoDeLaConversacion(String idCliente, String idSesion, String fecha) throws ClassNotFoundException, SQLException{
		return historicoDeConversaciones.cambiarDeEstadoAVerificadoDeLaConversacion(idCliente, idSesion, fecha);
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
		return misConversaciones.obtenerCliente(idCliente).obtenerValorDeGustosDeHoteles();
	}

	public double obtenerValorDeGustosDeRestaurantes(String idCliente) throws Exception
	{
		return misConversaciones.obtenerCliente(idCliente).obtenerValorDeGustosDeRestaurantes();
	}
	
	public double obtenerValorDeGustosDeBelleza(String idCliente) throws Exception
	{
		return misConversaciones.obtenerCliente(idCliente).obtenerValorDeGustosDeBelleza();
	}
	
	public void guardarSiTieneTarjetaCredito(String idCliente, boolean valor) throws Exception
	{
		misConversaciones.obtenerCliente(idCliente).guardarSiTieneTarjetaCredito(valor);
	}
	
	public Boolean obtenerSiTieneTarjetaCredito(String idCliente) throws Exception
	{
		return misConversaciones.obtenerCliente(idCliente).obtenerSiTieneTarjetaCredito();
	}
	
	public void guardarSiTieneCuentaAhorros(String idCliente, boolean valor) throws Exception
	{
		misConversaciones.obtenerCliente(idCliente).guardarSiTieneCuentaAhorros(valor);
	}
	
	public Boolean obtenerSiTieneCuentaAhorros(String idCliente) throws Exception
	{
		return misConversaciones.obtenerCliente(idCliente).obtenerSiTieneCuentaAhorros();
	}
	
	public String getPathXMLAudios() {
		return pathXMLAudios;
	}

	public void setPathXMLAudios(String pathXMLAudios) {
		this.pathXMLAudios = pathXMLAudios;
	}
}