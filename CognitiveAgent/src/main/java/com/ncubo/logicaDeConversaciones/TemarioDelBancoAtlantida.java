package com.ncubo.logicaDeConversaciones;

import java.util.List;

import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.partesDeLaConversacion.Contenido;
import com.ncubo.chatbot.partesDeLaConversacion.Tema;
import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.chatbot.partesDeLaConversacion.Temas;
import com.ncubo.chatbot.watson.Entidades;
import com.ncubo.chatbot.watson.Intenciones;

//TODO: aunque ya veo 1/2 claro... me huele mal que parezca plantilla.
public class TemarioDelBancoAtlantida extends Temario
{
	
	protected TemarioDelBancoAtlantida(String pathXML) {
		super(pathXML);
		// TODO Auto-generated constructor stub
	}

	private Tema saludo()
	{
		Tema resultado = new Tema
		(
			"saludo",
			"BAIntents",
			true,
			"BAIntents",
			frase("saludoGeneral"),
			frase("saludar"),
			frase("saludoPreguntar")
		);
		return resultado;
	}
	
	private Tema quiereSaldo()
	{
		Tema resultado = new Tema
		(
			"quiereSaldo",
			"BATemas",
			true,
			"saldo",
			frase("quiereSaldoTarjetaCredito"),
			frase("saldoCredito"),
			frase("saldoCuentaAhorros"),
			frase("noTengoSaldo"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereDisponible()
	{
		Tema resultado = new Tema
		(
			"quiereDisponible",
			"BATemas",
			true,
			"disponible",
			frase("quiereDisponibleTarjetaCredito"),
			frase("disponibleCredito"),
			frase("disponibleCuentaAhorros"),
			frase("disponiblePuntos"),
			frase("disponibleMillas"),
			frase("noTengoDisponible"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereTasaDeCambio()
	{
		Tema resultado = new Tema
		(
			"quiereTasaDeCambio",
			"BATemas",
			true,
			"tasa_cambio",
			frase("tasaDolar"),
			frase("tasaEuro"),
			frase("tasaCambio"),
			frase("tasaOtras"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereMovimientos()
	{
		Tema resultado = new Tema
		(
			"quiereMovimientos",
			"BATemas",
			true,
			"movimientos",
			frase("movimientos"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereAyudaComo()
	{
		Tema resultado = new Tema
		(
			"quiereAyudaComo",
			"BATemas",
			true,
			"ayuda_como",
			frase("ayudaLogin"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema fueraDeContexto()
	{
		Tema resultado = new Tema
		(
			"fueraDeContexto",
			"BAIntents",
			true,
			"out_of_scope",
			frase("fueraDeContextoGeneral")
		);
		return resultado;
	}
	
	private Tema despedida()
	{
		Tema resultado = new Tema
		(
			"despedida",
			"BAIntents",
			true,
			"despedidas",
			frase("despedida")
		);
		return resultado;
	}		
	
	private Tema saludarConocerte()
	{
		Tema resultado = new Tema
		(
			"saludarConocerte",
			"ConocerteGeneral",
			false,
			"conocerte",
			frase("saludarConocerte"),
			frase("preguntarPorTiempoLibre")
		);
		return resultado;
	}
	
	private Tema preguntarPorFinesDeSemana()
	{
		Tema resultado = new Tema
		(
			"preguntarPorFinesDeSemana",
			"ConocerteGeneral",
			false,
			"conocerte",
			frase("preguntarPorFinesDeSemana")
		);
		return resultado;
	}
	
	private Tema despedidaConocerte()
	{
		Tema resultado = new Tema
		(
			"despedidaConocerte",
			"ConocerteGeneral",
			false,
			"conocerte",
			frase("despedidaConocerte")
		);
		return resultado;
	}
	
private Tema quiereAbrirCuenta()
	{
		Tema resultado = new Tema
		(
			"quiereAbrirCuenta",
			"BAFAQ",
			true,
			"abrir_cuenta",
			frase("requisitosCuentaAhorros"),
			frase("cualProducto"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereMontoInicial()
	{
		Tema resultado = new Tema
		(
			"quiereMontoInicial",
			"BAFAQ",
			true,
			"precio",
			frase("montoInicialCuentaAhorros"),
			frase("cualProducto"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereSaldoMinimo()
	{
		Tema resultado = new Tema
		(
			"quiereSaldoMinimo",
			"BAFAQ",
			true,
			"saldo_minimo",
			frase("saldoMinimoCuentaAhorros"),
			frase("cualProducto"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	
	private Tema quiereRequisitos()
	{
		Tema resultado = new Tema
		(
			"quiereRequisitos",
			"BAFAQ",
			true,
			"requisitos",
			frase("requisitosCuentaAhorros"),			
			frase("cualProducto"),
			frase("preguntarPorOtraConsulta"),
			frase("quiereHacerOtraConsulta"),
			frase("noQuiereHacerOtraConsulta")
		);
		return resultado;
	}
	@Override
	protected void cargarTemario(Temas temasDelDiscurso){
		
		System.out.println("Cargando temario ...");
		temasDelDiscurso.add(saludo());
		temasDelDiscurso.add(quiereSaldo());
		temasDelDiscurso.add(quiereDisponible());
		temasDelDiscurso.add(quiereTasaDeCambio());
		temasDelDiscurso.add(quiereMovimientos());
		temasDelDiscurso.add(fueraDeContexto());
		temasDelDiscurso.add(quiereAyudaComo());
		temasDelDiscurso.add(despedida());
		temasDelDiscurso.add(quiereAbrirCuenta());
		temasDelDiscurso.add(quiereMontoInicial());
		temasDelDiscurso.add(quiereSaldoMinimo());
		temasDelDiscurso.add(quiereRequisitos());
		
		temasDelDiscurso.add(saludarConocerte());
		temasDelDiscurso.add(preguntarPorFinesDeSemana());
		temasDelDiscurso.add(despedidaConocerte());
		
	}
	
	public void cargarIntenciones(List<Intenciones> intenciones)
	{
		//intenciones.add (Intencion.get("WANT_HOUSE"));
		//intenciones.add (Intencion.get("GOODBYE"));
	}

	@Override
	protected void cargarEntidades(List<Entidades> entidades) {
		// TODO Auto-generated method stub
		//entidades.add (Entidad.get("INTEREST"));
		//entidades.add (Entidad.get("AFFIRMATIONS"));
		//entidades.add (Entidad.get("COUNTRIES"));
		//entidades.add (Entidad.get("LOCATIONS"));
		//entidades.add (Entidad.get("PLEASURES"));
	}
	
	@Override
	protected Contenido cargarContenido(String path){
		return new ContenidoDelBancoAtlantida(path);
	}

	@Override
	protected void cargarDependencias(Temas temasDelDiscurso){
		temasDelDiscurso.get(9).dependeDe(temasDelDiscurso.get(8));
		temasDelDiscurso.get(10).dependeDe(temasDelDiscurso.get(9));
		
	}
	
	public static void main(String argv[]) {
		TemarioDelBancoAtlantida temario = new TemarioDelBancoAtlantida(Constantes.PATH_ARCHIVO_DE_CONFIGURACION_BA);
		temario.buscarTema(Constantes.FRASE_SALUDO);
	}
}
