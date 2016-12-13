package com.ncubo.chatbot.bitacora;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import com.ncubo.db.BitacoraDao;
import com.ncubo.db.ConexionALaDB;

public class HistoricosDeConversaciones {

	private final static Hashtable<String, HistoricoDeLaConversacion> historicoDeMisConversaciones = new Hashtable<String, HistoricoDeLaConversacion>();
	private final static Hashtable<String, HistoricoDeLaConversacion> historicoDeMisConversacionesEspecificas = new Hashtable<String, HistoricoDeLaConversacion>();
	private BitacoraDao miBitacora;
	
	public HistoricosDeConversaciones(){
		miBitacora = new BitacoraDao();
	}
	
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
		String resultado = "";
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
	
	public void borrarElHistoricoDeUnaConversacionPorCliente(ArrayList<String> idsSesiones, String idCliente){
		for (String idSesion: idsSesiones){
			borrarElHistoricoDeUnaConversacion(idSesion, idCliente);
		}
	}
	
	public void borrarElHistoricoDeUnaConversacion(String idSesion, String idCliente){
		
		if(! idSesion.isEmpty()){
			String miHistorico = verElHistoricoDeUnaConversacion(idSesion);
			if(! miHistorico.isEmpty()){
				if(guardarUnaConversacionEnLaDB(idSesion, idCliente, miHistorico, 0))
					borrarElHistoricoDeUnaConversacionGeneral(idSesion);
			}
			
			String miHistoricoEspecifica = verElHistoricoDeUnaConversacionEspecifica(idSesion);
			if(! miHistoricoEspecifica.isEmpty()){
				if(guardarUnaConversacionEnLaDB(idSesion, idCliente, miHistoricoEspecifica, 1))
					borrarElHistoricoDeUnaConversacionEspecifica(idSesion);
			}
			
		}
		
	}
	
	private boolean guardarUnaConversacionEnLaDB(String idSesion, String idCliente, String miHistorico, int esConversacionEspecifica){
		try {
			miBitacora.insertar(idSesion, idCliente, miHistorico, esConversacionEspecifica);
			return true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
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
		String resultado = "";
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
	
	public BitacoraDao obtenerMiBitacoraDeBD(){
		return miBitacora;
	}
	
}
