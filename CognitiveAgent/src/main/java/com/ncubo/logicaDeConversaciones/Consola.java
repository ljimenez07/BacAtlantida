package com.ncubo.logicaDeConversaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.partesDeLaConversacion.Salida;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.participantes.Cliente;
import com.ncubo.realestate.TemarioDeRealEstate;

public class Consola {

	public Consola(){}
	
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
	
	public static void main(String argv[]) throws Exception {
		
		Consola consola = new Consola();
		
		Temario temarioDelBancoAtlantida = new TemarioDelBancoAtlantida(Constantes.PATH_ARCHIVO_DE_CONFIGURACION_BA);
		Cliente cliente = new Cliente("Ricky", "123456");
		Conversacion miConversacion = new Conversacion(temarioDelBancoAtlantida, cliente);
		String respuesta = "";
		
		ArrayList<Salida> salidasParaElCliente = miConversacion.analizarLaRespuestaConWatsonEnUnWorkspaceEspecifico("Hola!", "ConocerteGeneral", "conocerte");
		consola.imprimirSalidas(salidasParaElCliente);
		
		while(true){
			respuesta = consola.leerTexto();
			
			salidasParaElCliente = miConversacion.analizarLaRespuestaConWatsonEnUnWorkspaceEspecifico(respuesta, "ConocerteGeneral", "conocerte");
			consola.imprimirSalidas(salidasParaElCliente);
		}
	}
}
