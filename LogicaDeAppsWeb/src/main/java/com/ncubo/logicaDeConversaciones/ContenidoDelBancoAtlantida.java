package com.ncubo.logicaDeConversaciones;

import java.io.File;
import com.ncubo.chatbot.partesDeLaConversacion.Contenido;

public class ContenidoDelBancoAtlantida extends Contenido{
	
	protected ContenidoDelBancoAtlantida(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected File archivoDeConfiguracion(String path){
		return new File(path);
	}
}
