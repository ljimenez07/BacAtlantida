package hello;

import java.io.IOException;
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

@Controller
@EnableAutoConfiguration
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
    @RequestMapping(value = "/Ecommerce/GetCustomerAvailableAmount", method = RequestMethod.POST)
	public @ResponseBody String obtenerRespuestaPost(@RequestBody String json) throws JSONException, JsonParseException, JsonMappingException, IOException
	{
		JSONObject obj;
		obj = new JSONObject(json);
		String question = obj.getString("Question");
		obj = new JSONObject(obj.get("MyContext").toString());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> myContext = mapper.readValue(obj.toString(), new TypeReference<Map<String, Object>>(){});
		
		System.out.println(question);
		System.out.println(myContext);
		String respuestaJson = "{\"codigo\":\"abc\",\"descripcion\":\"bla bla\",\"detalleTecnico\":\"11\",\"tipo\":\"S1\",\"fecha\":\"0000-00-00\",\"tasaCambioItemUSD\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"},\"tasaCambioEUR\":{\"moneda\":\"usd\",\"compra\":\"560.0\",\"venta\":\"530.0\"}}";
		return respuestaJson;
	}
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class, args);
    }
}