package com.ncubo.chatbot.participantes;

import java.util.ArrayList;
import com.ncubo.chatbot.contexto.AdministradorDeVariablesDeContexto;

public class Cliente extends Participante{

	private String miNombre;
	private String miId;
	private ArrayList<String> misIdsDeSesiones = new ArrayList<String>();
	private AdministradorDeVariablesDeContexto administradorDeVariablesDeContexto;
	
	public Cliente(){
		miNombre = "";
		miId = "";
		//administradorDeVariablesDeContexto = new AdministradorDeVariablesDeContexto();
		/*administradorDeVariablesDeContexto.agregarVariableDeContexto("leGustaLosHoteles", "0");
		administradorDeVariablesDeContexto.agregarVariableDeContexto("leGustaComerAfuera", "0");
		administradorDeVariablesDeContexto.agregarVariableDeContexto("sePreocupaPorLaSalud", "0");*/
	}
	
	public Cliente(String nombre, String id){
		miNombre = nombre;
		miId = id;
		administradorDeVariablesDeContexto = new AdministradorDeVariablesDeContexto();
	}
	
	public String getMiNombre() {
		return miNombre;
	}

	public void setMiNombre(String miNombre) {
		this.miNombre = miNombre;
	}

	public String getMiId() {
		return miId;
	}

	public void setMiId(String miId) {
		this.miId = miId;
	}
	
	public ArrayList<String> getMisIdsDeSesiones() {
		return misIdsDeSesiones;
	}

	public void borrarTodosLosIdsDeSesiones() {
		this.misIdsDeSesiones.clear();
	}
	
	public void agregarIdsDeSesiones(String idDeSesion) {
		if( ! contieneElIdSesion(idDeSesion)){
			this.misIdsDeSesiones.add(idDeSesion);
		}
	}
	
	private boolean contieneElIdSesion(String idSesion){
		return misIdsDeSesiones.contains(idSesion);
	}
	
	// Hoteles
	public void presionarLikeDeHoteles() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaLosHoteles = (leGustaLosHoteles + 1.1) / 2;");
	}
	
	public void presionarDisLikeDeHoteles() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaLosHoteles = (leGustaLosHoteles - 1.1) / 2;");
	}
	
	public void actualizarGustosDeHoteles(String valor) throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar(String.format("leGustaLosHoteles = %s; show leGustaLosHoteles;", valor));
	}
	
	public double obtenerValorDeGustosDeHoteles() throws Exception
	{
		return Double.parseDouble(administradorDeVariablesDeContexto.obtenerVariable("leGustaLosHoteles"));
	}
	
	// Restaurantes
	public void presionarLikeDeRestaurantes() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaComerAfuera = (leGustaComerAfuera + 1.1) / 2;");
	}
	
	public void presionarDisLikeDeRestaurantes() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("leGustaComerAfuera = (leGustaComerAfuera - 1.1) / 2;");
	}
	
	public void actualizarGustosDeRestaurantes(String valor) throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar(String.format("leGustaComerAfuera = %s; show leGustaComerAfuera;", valor));
	}
	
	public double obtenerValorDeGustosDeRestaurantes() throws Exception
	{
		return Double.parseDouble(administradorDeVariablesDeContexto.obtenerVariable("leGustaComerAfuera"));
	}
	
	// Belleza
	public void presionarLikeDeBelleza() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("sePreocupaPorLaSalud = (sePreocupaPorLaSalud + 1.1) / 2;");
	}
	
	public void presionarDisLikeDeBelleza() throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar("sePreocupaPorLaSalud = (sePreocupaPorLaSalud - 1.1) / 2;");
	}
	
	public void actualizarGustosDeBelleza(String valor) throws Exception
	{
		administradorDeVariablesDeContexto.ejecutar(String.format("sePreocupaPorLaSalud = %s; show sePreocupaPorLaSalud;", valor));
	}
	
	public double obtenerValorDeGustosDeBelleza() throws Exception
	{
		return Double.parseDouble(administradorDeVariablesDeContexto.obtenerVariable("sePreocupaPorLaSalud"));
	}
}

