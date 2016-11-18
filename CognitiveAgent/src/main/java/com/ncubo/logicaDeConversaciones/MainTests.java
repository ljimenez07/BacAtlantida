package com.ncubo.logicaDeConversaciones;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//import com.ncubo.realestate.Coversaciones;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.conf.Usuario;

public class MainTests {

	public MainTests(){}
	
	private void imprimirSalidas(ArrayList<Salida> salidas){
		
		for(Salida salida: salidas){
			try{
				System.out.println("Contexto: "+salida.obtenerLaRespuestaDeIBM().messageResponse().getContext());
			}catch(Exception e){
				
			}
		}
		
		System.out.println("");
		for(Salida salida: salidas){
			System.out.println("- "+salida.getMiTexto());
		}
	}
	
	private String leerTexto(){
		String input = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("R/: ");
		try{
			input = br.readLine();
        }catch(IOException nfe){
            System.err.println("Invalid Format!");
        }
		return input;
	}
	
	public static void main(String argv[]) {
		MainTests main = new MainTests();
		
		Conversaciones misConversaciones = new Conversaciones();

		String respuesta = "";
		
		ArrayList<Salida> salidasParaElCliente = null;
		
		/*Usuario usuasio = new Usuario();
		usuasio.setIdSesion("123456");
		
		while(true){
			respuesta = main.leerTexto();
			
			salidasParaElCliente = misConversaciones.conversarConElAgente(usuasio, respuesta);
			main.imprimirSalidas(salidasParaElCliente);
		}*/
	}
	
}
