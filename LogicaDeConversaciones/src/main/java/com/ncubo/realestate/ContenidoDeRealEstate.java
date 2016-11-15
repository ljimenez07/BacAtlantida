package com.ncubo.realestate;

import java.io.File;

import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.partesDeLaConversacion.Contenido;

public class ContenidoDeRealEstate extends Contenido{
	
	@Override
	protected File archivoDeConfiguracion(){
		return new File(Constantes.PATH_ARCHIVO_DE_CONFIGURACION);
	}
}
