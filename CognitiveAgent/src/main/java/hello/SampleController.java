package hello;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
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

@Controller
@EnableAutoConfiguration
@EnableConfigurationProperties(AgenteCognitivo.class)
public class SampleController {
	
	@Autowired
	private AgenteCognitivo serverCognitivo;
	private static int contadorDeContextos = 0;//TODO quitarlo de aquí y meterlo en la session
	
	@RequestMapping("/")
	@ResponseBody String home() 
	{
		return "Hello World!";
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/", method = RequestMethod.POST)
	@ResponseBody String convesacionSinContexto(@RequestBody String mensaje, HttpServletRequest request) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		contadorDeContextos = contadorDeContextos +1;
		return convesacion( mensaje, ""+contadorDeContextos );
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/conversacion/{contexto}", method = RequestMethod.POST)
	@ResponseBody String convesacion(@RequestBody String mensaje, @PathVariable String contexto) throws JSONException, JsonParseException, JsonMappingException, IOException 
	{
		
		return serverCognitivo.procesarMensaje(
				contexto, 
				mensaje, 
				new Date() );
	}
	
	public static void main(String[] args) throws Exception 
	{
		SpringApplication.run(SampleController.class, args);
	}
}