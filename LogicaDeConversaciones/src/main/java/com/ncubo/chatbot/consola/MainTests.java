package com.ncubo.chatbot.consola;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.realestate.Conversacion;
import com.ncubo.realestate.Coversaciones;
import com.ncubo.realestate.TemarioDeRealEstate;

public class MainTests {

	public MainTests(){}
	
	private void imprimirSalidas(ArrayList<Salida> salidas){
		
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
		
		Coversaciones misConversaciones = new Coversaciones();
		misConversaciones.agregarUnNuevoCliente("Ricky", "123456");
		
		String respuesta = "";
		
		ArrayList<Salida> salidasParaElCliente = misConversaciones.inicializarConversacionConElAgente("123456");
		main.imprimirSalidas(salidasParaElCliente);
		
		while(true){
			respuesta = main.leerTexto();
			
			salidasParaElCliente = misConversaciones.converzarConElAgente("123456", respuesta);
			main.imprimirSalidas(salidasParaElCliente);
		}
	}
	
}
