package hello;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.Intent;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

@Controller
@EnableAutoConfiguration
public class SampleController {
	
    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
    
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/convesacion/mensaje", method = RequestMethod.POST)
	@ResponseBody String convesacion(@RequestBody String mensaje) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		String respuestaJson = "";
		JSONObject obj;
		obj = new JSONObject(mensaje);
		String question = obj.getString("Question");
		obj = new JSONObject(obj.get("MyContext").toString());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> myContext = mapper.readValue(obj.toString(), new TypeReference<Map<String, Object>>(){});
		
		System.out.println(question);
		System.out.println(myContext);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//get current date time with Date()
		Date date = new Date();
		ConversationService service = new ConversationService(dateFormat.format(date));
		service.setUsernameAndPassword("189548d8-2956-41a2-9f88-6c40e9a93c8e","XW6rPcUzIFQl");
		
		MessageResponse response = null;
		
		MessageRequest newMessage = new MessageRequest.Builder()
				.inputText(question)
				.context(myContext)
				.build();
		
		response = service.message("892588f4-c009-458e-b196-02bb8945df17", newMessage).execute();
		
		String intent = getIntent(response);
		
		if(intent.equals("saldo"))
		{
			//se llama al web service de saldo
			String respuestaDelWebService = "{\"saldoColeccion\":{\"contable\":\"30344.62\", \"diferido\":\"0.0\",\"disponibleLps\":\"30344.62\",\"disponibleUsd\":\"0.0\",\"disponibleEur\":\"0.0\", \"retenido\":\"0.0\", \"saldoActualLps\":\"0.0\", \"saldoMoraLps\":\"0.0\", \"saldoMoraUsd\":\"0.0\", \"saldoAnteriorLps\":\"0.0\", \"saldoAnteriorUsd\":\"0.0\", \"saldoAlCorteLps\":\"0.0\", \"saldoAlCorteUsd\":\"0.0\"}}";
			JSONObject objetoRespuestaDelWS = new JSONObject(respuestaDelWebService);
			Object saldo = objetoRespuestaDelWS.getJSONObject("saldoColeccion").get("contable");

			//falta concatenar el saldo
			String texto = getText(response);
			texto += saldo;
			System.out.println("sald"+ texto);
			JSONObject objResponse = new JSONObject().put("Text",texto).put("MyContext", response.getContext().toString());
			
			respuestaJson = objResponse.toString();
		}
		if(intent.equals("tasa_cambio"))
		{
			String respuestaWS = "{\"codigo\":\"abc\",\"descripcion\":\"bla bla\",\"detalleTecnico\":\"11\",\"tipo\":\"S1\",\"fecha\":\"0000-00-00\",\"tasaCambioItemUSD\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"},\"tasaCambioEUR\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"}}";
			JSONObject jsonRespuesta = new JSONObject(respuestaWS);
			Object tasaDeCambio = jsonRespuesta.get("tasaCambioItemUSD");
			//concatenar tasa de cambio
			JSONObject objResponse = new JSONObject().put("Text",getText(response)+ tasaDeCambio.toString()).put("MyContext", response.getContext().toString());
			respuestaJson = objResponse.toString();
			
		}
		else
			{
				JSONObject objResponse = new JSONObject().put("Text",getText(response)+"").put("MyContext", response.getContext().toString());
				respuestaJson = objResponse.toString();
			}
		return respuestaJson;
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
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}