package com.ncubo.chatbot.bitacora;

import java.util.ArrayList;
import java.util.Hashtable;

public class HistoricosDeConversaciones {

	private final static Hashtable<String, HistoricoDeLaConversacion> historicoDeMisConversaciones = new Hashtable<String, HistoricoDeLaConversacion>();
	private final static Hashtable<String, HistoricoDeLaConversacion> historicoDeMisConversacionesEspecificas = new Hashtable<String, HistoricoDeLaConversacion>();
	
	public HistoricosDeConversaciones(){}
	
	private boolean existeElHistoricoDeLaConversacion(String idSesion){
		return historicoDeMisConversaciones.containsKey(idSesion);
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
	
	private String borrarElHistoricoDeUnaConversacionGeneral(String idSesion){
		String resultado = "El historico para la conversacion "+idSesion+" no existe";
		if(existeElHistoricoDeLaConversacion(idSesion)){
			synchronized(historicoDeMisConversaciones){
				historicoDeMisConversaciones.remove(idSesion);
				resultado = "Se borro exitosamente el historico de la conversacion: "+idSesion;
			}
		}
		return resultado;
	}
	
	public void borrarElHistoricoDeUnaConversacion(ArrayList<String> idsSesiones){
		for (String idSesion: idsSesiones){
			borrarElHistoricoDeUnaConversacionEspecifica(idSesion);
			borrarElHistoricoDeUnaConversacionGeneral(idSesion);
		}
	}
	
	// Conversacion Especifica
	private boolean existeElHistoricoDeLaConversacionEspecifica(String idSesion){
		return historicoDeMisConversacionesEspecificas.containsKey(idSesion);
	}
	
	private void agregarUnHistoricoALaConversacionEspecifica(String idSesion){
		synchronized(historicoDeMisConversacionesEspecificas){
			historicoDeMisConversacionesEspecificas.put(idSesion, new HistoricoDeLaConversacion());
		}
	}
	
	public void agregarHistorialALaConversacionEspecifica(String idSesion, String loQueDijoElCliente, String loQueDijoElAgente){
		if(! existeElHistoricoDeLaConversacionEspecifica(idSesion)){
			agregarUnHistoricoALaConversacionEspecifica(idSesion);
		}
		historicoDeMisConversacionesEspecificas.get(idSesion).agregarHistorico(loQueDijoElCliente, loQueDijoElAgente);
	}
	
	public String verElHistoricoDeUnaConversacionEspecifica(String idSesion){
		String resultado = "El historico para la conversacion especifica "+idSesion+" no existe";
		if(existeElHistoricoDeLaConversacionEspecifica(idSesion)){
			resultado = historicoDeMisConversacionesEspecificas.get(idSesion).verMiHistorico();
		}
		return resultado;
	}
	
	private String borrarElHistoricoDeUnaConversacionEspecifica(String idSesion){
		String resultado = "El historico para la conversacion especifica "+idSesion+" no existe";
		if(existeElHistoricoDeLaConversacionEspecifica(idSesion)){
			synchronized(historicoDeMisConversacionesEspecificas){
				historicoDeMisConversacionesEspecificas.remove(idSesion);
				resultado = "Se borro exitosamente el historico de la conversacion especifica: "+idSesion;
			}
		}
		return resultado;
	}
}
