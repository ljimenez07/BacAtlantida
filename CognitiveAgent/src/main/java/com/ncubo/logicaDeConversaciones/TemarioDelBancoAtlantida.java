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
			frase("saldo"),
			frase("noTengoSaldo"),
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
	
	@Override
	protected void cargarTemario(Temas temasDelDiscurso){
		
		System.out.println("Cargando temario ...");
		temasDelDiscurso.add(saludo());
		temasDelDiscurso.add(quiereSaldo());
		temasDelDiscurso.add(quiereTasaDeCambio());
		temasDelDiscurso.add(quiereMovimientos());
		temasDelDiscurso.add(fueraDeContexto());
		temasDelDiscurso.add(despedida());
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
	protected Contenido cargarContenido(){
		return new ContenidoDelBancoAtlantida();
	}

	@Override
	protected void cargarDependencias(Temas temasDelDiscurso){
		// temasDelDiscurso.get(2).dependeDe(temasDelDiscurso.get(1));

	}
	
	public static void main(String argv[]) {
		TemarioDelBancoAtlantida temario = new TemarioDelBancoAtlantida();
		temario.buscarTema(Constantes.FRASE_SALUDO);
	}
}
