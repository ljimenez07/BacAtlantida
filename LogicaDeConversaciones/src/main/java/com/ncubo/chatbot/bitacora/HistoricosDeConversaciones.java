package com.ncubo.chatbot.bitacora;

import java.util.Hashtable;

public class HistoricosDeConversaciones {

	private final static Hashtable<String, HistoricoDeLaConversacion> historicoDeMisConversaciones = new Hashtable<String, HistoricoDeLaConversacion>();
	private final static Hashtable<String, HistoricoDeLaConversacion> historicoDeMisConversacionesEspecificas = new Hashtable<String, HistoricoDeLaConversacion>();
	
	public HistoricosDeConversaciones(){}
	
	
	private boolean existeElHistoricoDeLaConversacion(String idSesion){
		return historicoDeMisConversaciones.contains(idSesion);
	}
	
	private void agregarUnHistoricoALaConversacion(String idSesion){
		synchronized(historicoDeMisConversaciones){
			historicoDeMisConversaciones.put(idSesion, new HistoricoDeLaConversacion());
		}
	}
	
	public void agregarHistorialALaConversacion(String idSesion, String loQueDijoElCliente, String loQueDijoElAgente){
		if(! existeElHistoricoDeLaConversacion(idSesion)){
			agregarUnHistoricoALaConversacion(idSesion);
		}
		historicoDeMisConversaciones.get(idSesion).agregarHistorico(loQueDijoElCliente, loQueDijoElAgente);
	}
	
	public String verElHistoricoDeUnaConversacion(String idSesion){
		String resultado = "El historico para la conversacion "+idSesion+" no existe";
		if(existeElHistoricoDeLaConversacion(idSesion)){
			resultado = historicoDeMisConversaciones.get(idSesion).verMiHistorico();
		}
		return resultado;
	}
}
