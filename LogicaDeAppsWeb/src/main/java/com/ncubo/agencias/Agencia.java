package com.ncubo.agencias;

public class Agencia {

	private String codigo;
	private final String nombre;
	private final String telefono;
	private final String departamento;
	private final String ciudad;
	private String direccion;
	private final HorariosDeAgencia horarios;
	private final TipoDeAgencia tipoDeAgencia;
	
	public Agencia(String nombre, String telefono, String 
			departamento, String ciudad, String tipo, HorariosDeAgencia horarios){
		
		this.codigo = "";
		this.direccion = "";
		this.nombre = nombre;
		this.telefono = telefono;
		this.departamento = departamento;
		this.ciudad = ciudad;
		this.horarios = horarios;
		
		if(tipo.contains("Agencia")){
			tipoDeAgencia = TipoDeAgencia.AGENCIA;
		}else{
			tipoDeAgencia = TipoDeAgencia.AUTOBANCO;
		}
		
	}
	
	private enum TipoDeAgencia {
		AGENCIA, AUTOBANCO
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getDepartamento() {
		return departamento;
	}

	public String getCiudad() {
		return ciudad;
	}

	public HorariosDeAgencia getHorarios() {
		return horarios;
	}

	public TipoDeAgencia getTipoDeAgencia() {
		return tipoDeAgencia;
	}
	
}
