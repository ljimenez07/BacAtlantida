package com.ncubo.bambu.data;

public enum EEtapaCotizacion
{
    PENDIENTE_ENVIAR(1),
    ABIERTA_ENVIADA(2),
    ABIERTA_PRE_APROVADA(3),
    APROVADA(4),
    DENEGADA(5),
    CONGELADA(6),
    PROYECCION_ABIERTA_80(7);
	
	private int idEtapaCotizacion;
	
	public int getId()
	{
		return idEtapaCotizacion;
	}
	
	private EEtapaCotizacion(int idEtapaCotizacion)
	{
		this.idEtapaCotizacion = idEtapaCotizacion;
	}
}
