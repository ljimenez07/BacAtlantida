import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.json.JSONObject;

import com.jayway.restassured.response.Response;
public class Watson 
{

	static String session="";
	static String urlTarget="";
	static Properties prop = new Properties();
	static Response ultimaRespuesta =null;
	public static void main(String args[]) throws IOException
	{		
		InputStream input = new FileInputStream(args[0]);
		prop.load(input);
		
		String folderDeCasos = args[1];
		urlTarget = args[2];
		
		File f = new File(folderDeCasos);
		File[] ficheros = f.listFiles();

		for (int x=0; x<ficheros.length; x++)
		{
			if( ! ficheros[x].getName().endsWith(".txt") )
			{
				continue;
			}
			
			FileInputStream fstream = new FileInputStream(ficheros[x]);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			String strLine;
			
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{			
				if ( esComando(strLine) ) 
				{
					procesarComando(strLine);
				}
				else if( esGlobal(strLine) )
				{
					procesarGlobal(strLine);
				}
				else
				{
					procesarTexto(strLine);
				}	
				
				if( session.isEmpty() )
				{
					session = ultimaRespuesta.getCookie("SESSION");
				}
			}
			
			session= "";
			
			br.close();
		}
	}

	static void procesarTexto(String strLine)
	{
		String text = strLine.split("/")[0].trim();
		String expected = strLine.split("/")[1].trim();
		
		ultimaRespuesta = (session.isEmpty()) ?
				given().body(text).post(urlTarget+"/conversacion/chat/"):
				given().cookie("SESSION", session).body(text).post(urlTarget+"/conversacion/chat/");
		
		if( ! expected.equals( new JSONObject(ultimaRespuesta.asString()).getString("texto")  ))
		{
			System.err.println("Con "+session+" A '"+text+"' \n\t se esperaba '"+expected+"' \n\t  y se recibió '"+new JSONObject(ultimaRespuesta.asString()).getString("texto")+"'");
		}
		else
		{
			System.out.println("Con "+session+" A '"+text+"' \n\t  se encontró '"+expected);
		}		
	}
	
	static void procesarGlobal(String strLine)
	{
		String text = strLine.split("/")[0].trim();
		String expected = strLine.split("/")[1].trim();
		
		ultimaRespuesta = (session.isEmpty()) ?
				given().body(text).post(urlTarget+"/conversacion/chat/"):
				given().cookie("SESSION", session).body(text).post(urlTarget+"/conversacion/chat/");
				
		String global = prop.getProperty(expected.substring(1));
		String valores[] = global.split("/");
		
		for ( String valor : valores)
		{
			if( valor.trim().equals( new JSONObject(ultimaRespuesta.asString()).getString("texto")  ))
			{
				System.out.println("Con "+session+" A '"+text+"' \n\t  se encontró '"+valor);
				return;
			}
		}
		System.err.println("Con "+session+" A '"+text+"' \n\t  se esperaba algunas de estas respuestas '"+global+"' \n\t y se recibió '"+new JSONObject(ultimaRespuesta.asString()).getString("texto")+"'");
		
	}
	
	static void procesarComando(String strLine)
	{
		String text = strLine.substring(1).trim();
		
		if( text.equals("IniciarSesion"))
		{
			String path = urlTarget+"/movil/login";
			ultimaRespuesta = (session.isEmpty()) ?
				given().contentType("application/x-www-form-urlencoded").param("name", "cris").param("password", "pass").post(path):
				given().cookie("SESSION", session).contentType("application/x-www-form-urlencoded").param("name", "cris").param("password", "pass").post(path);
				
			System.err.println(ultimaRespuesta.andReturn().asString());
		}
		
	}
	
	
	static boolean esGlobal(String text)
	{
		return text.contains("#");
	}
	
	static boolean esComando(String texto)
	{
		return texto.contains("@");
	}

}
