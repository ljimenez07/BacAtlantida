import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONObject;

import com.jayway.restassured.response.Cookie;
import com.jayway.restassured.response.Response;
public class Watson 
{
	public static void main(String args[]) throws IOException
	{
		String folderDeCasos = args[0];
		String urlTarget = args[1];
		
		File f = new File(folderDeCasos);
		File[] ficheros = f.listFiles();
		
		String session="";

		for (int x=0; x<ficheros.length; x++)
		{
			FileInputStream fstream = new FileInputStream(ficheros[x]);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			
			String strLine;
			
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)
			{
				String text = strLine.split("|")[0].trim();
				String expected = strLine.split("|")[1].trim();
				
				Response salida = (session.isEmpty()) ?
						given().body(text).post(urlTarget):
						given().cookie("SESSION", session).body(text).post(urlTarget);
				
				if( ! expected.equals( new JSONObject(salida.asString()).getString("texto")  ))
				{
					System.err.println("A '"+text+"' se esperaba '"+expected+"' y se recibió '"+salida.asString()+"'");
				}
				
				if( session.isEmpty() )
				{
					session = salida.getCookie("SESSION");
				}
				
			}
			
			session= "";
			
			br.close();
		}
	}
}
