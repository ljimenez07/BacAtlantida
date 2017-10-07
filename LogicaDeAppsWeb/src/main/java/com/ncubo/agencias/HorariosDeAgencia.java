package com.ncubo.agencias;

public class HorariosDeAgencia {

	private String horarioLV = "";
	private String horarioSabados = "";
	private String horarioDomingo = "";
	
	public HorariosDeAgencia(String horarioLV, String horarioSabados, String horarioDomingo){
		
		if(horarioLV.equals("null")){
			this.horarioLV = "";
		}else{
			this.horarioLV = horarioLV;
		}
		
		if(horarioSabados.equals("null")){
			this.horarioSabados = "";
		}else{
			this.horarioSabados = horarioSabados;
		}
		
		if(horarioDomingo.equals("null")){
			this.horarioDomingo = "";
		}else{
			this.horarioDomingo = horarioDomingo;
		}
	
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
