package hello;

import java.io.IOException;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

@ConfigurationProperties("servercognitivo")
public class AgenteCognitivo 
{
	private String user;
	private String password;
	private String workspace;
	private HashMap<String, JSONObject> contextoPorUsuario = new HashMap<String, JSONObject>(); //TODO quitarlo de aquí y meterlo en la session
	
	public String procesarMensaje(String contexto, String mensaje, Date date) throws JsonParseException, JsonMappingException, IOException, JSONException
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
		contextoPorUsuario.put(contexto, new JSONObject(response.getContext().toString()));
		
		String intent = getIntent(response);
		if(intent.equals(Intencion.SALDO.toString()))
		{
			//se llama al web service de saldo
			String respuestaDelWebService = "{\"saldoColeccion\":{\"contable\":\"30344.62\", \"diferido\":\"0.0\",\"disponibleLps\":\"30344.62\",\"disponibleUsd\":\"0.0\",\"disponibleEur\":\"0.0\", \"retenido\":\"0.0\", \"saldoActualLps\":\"0.0\", \"saldoMoraLps\":\"0.0\", \"saldoMoraUsd\":\"0.0\", \"saldoAnteriorLps\":\"0.0\", \"saldoAnteriorUsd\":\"0.0\", \"saldoAlCorteLps\":\"0.0\", \"saldoAlCorteUsd\":\"0.0\"}}";
			JSONObject objetoRespuestaDelWS = new JSONObject(respuestaDelWebService);
			Object saldo = objetoRespuestaDelWS.getJSONObject("saldoColeccion").get("contable");

			//falta concatenar el saldo
			String texto = getText(response)+ "  80000";
			respuesta.put("texto", texto);

		}
		if(intent.equals(Intencion.TASA_DE_CAMBIO.toString()))
		{
			String respuestaWS = "{\"codigo\":\"abc\",\"descripcion\":\"bla bla\",\"detalleTecnico\":\"11\",\"tipo\":\"S1\",\"fecha\":\"0000-00-00\",\"tasaCambioItemUSD\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"},\"tasaCambioEUR\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"}}";
			//se llama al web service de saldo
			
			
			String texto = getText(response);
			respuesta.put("texto", texto);
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
	public String getWorkspace() 
	{
		return workspace;
	}
	public void setUser(String user) 
	{
		this.user = user;
	}
	public void setPassword(String password) 
	{
		this.password = password;
	}
	public void setWorkspace(String workspace) 
	{
		this.workspace = workspace;
	}
	
	
	

}
