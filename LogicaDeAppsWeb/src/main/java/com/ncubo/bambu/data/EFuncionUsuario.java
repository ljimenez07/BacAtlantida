package com.ncubo.bambu.data;

public enum EFuncionUsuario
{
	ADMINISTRATOR(1),
	ASESORES(2),
	SUPERVISOR(3),
	TECNICO(4),
	MENSAJERO(5),
	VISITANTE(6),
	USUARIO_CALL_CENTER(7);
	
	public int idFuncion;
	
	public int obtenerId()
	{
		return idFuncion;
	}
	
	EFuncionUsuario(int idFuncion)
	{
		this.idFuncion = idFuncion;
	}
}
