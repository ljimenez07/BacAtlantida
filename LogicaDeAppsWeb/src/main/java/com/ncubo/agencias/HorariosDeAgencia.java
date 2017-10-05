package com.ncubo.agencias;

public class HorariosDeAgencia {

	private final String horarioLV;
	private final String horarioSabados;
	private final String horarioDomingo;
	
	public HorariosDeAgencia(String horarioLV, String horarioSabados, String horarioDomingo){
		this.horarioLV = horarioLV;
		this.horarioSabados = horarioSabados;
		this.horarioDomingo = horarioDomingo;
	}
	
	public String getHorarioLV() {
		return horarioLV;
	}

	public String getHorarioSabados() {
		return horarioSabados;
	}

	public String getHorarioDomingo() {
		return horarioDomingo;
	}
	
}
