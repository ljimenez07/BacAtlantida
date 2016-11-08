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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

@Controller
@EnableAutoConfiguration
@EnableConfigurationProperties(ServerCognitivo.class)
public class SampleController {
	
	@Autowired
	private ServerCognitivo serverCognitivo;
	private static int contadorDeContextos = 0;//TODO quitarlo de aquí y meterlo en la session
	private HashMap<String, JSONObject> contextoPorUsuario = new HashMap<String, JSONObject>(); 
	
	
	@RequestMapping("/")
	@ResponseBody String home() 
	{
		return "Hello World!";
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/", method = RequestMethod.POST)
	@ResponseBody String convesacionSinContexto(@RequestBody String mensaje) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		contadorDeContextos = contadorDeContextos ++;
		String nuevoContador = ""+contadorDeContextos;
		contextoPorUsuario.put(nuevoContador, new JSONObject());
		return convesacion(mensaje , nuevoContador);
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/{contexto}", method = RequestMethod.POST)
	@ResponseBody String convesacion(@RequestBody String mensaje, @PathVariable String contexto) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		JsonObject respuesta = new JsonObject();
		ObjectMapper mapper = new ObjectMapper();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		Map<String, Object> myContext = mapper.readValue(contextoPorUsuario.get(contexto).toString(), new TypeReference<Map<String, Object>>(){});
		ConversationService service = new ConversationService(dateFormat.format(date));
		service.setUsernameAndPassword(serverCognitivo.getUser(),serverCognitivo.getPassword());
		
		MessageRequest newMessage = new MessageRequest.Builder()
				.inputText(mensaje)
				.context(myContext)
				.build();		
		MessageResponse response = service.message(serverCognitivo.getWorkspace(), newMessage).execute();
		
		respuesta.addProperty("contexto", contexto);
		contextoPorUsuario.put(contexto, new JSONObject(response.getContext().toString()));
		
		String intent = getIntent(response);
		if(intent.equals(Intencion.SALDO))
		{
			//se llama al web service de saldo
			
			
			//falta concatenar el saldo
			String texto = getText(response)+ "  80000";
			respuesta.addProperty("texto", texto);

		}
		if(intent.equals(Intencion.TASA_DE_CAMBIO))
		{
			String respuestaWS = "{\"codigo\":\"abc\",\"descripcion\":\"bla bla\",\"detalleTecnico\":\"11\",\"tipo\":\"S1\",\"fecha\":\"0000-00-00\",\"tasaCambioItemUSD\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"},\"tasaCambioEUR\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"}}";
			//se llama al web service de saldo
			
			
			String texto = getText(response);
			respuesta.addProperty("texto", texto);
		}
		else
		{
			String texto = getText(response);
			respuesta.addProperty("texto", texto);
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
		String text = "";
		List<String> msjs = response.getText();
		for(int i = 0; i < msjs.size(); i++ )
		{
			text = text +" "+ msjs.get(i);
		}
		
		return text;
	}
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}