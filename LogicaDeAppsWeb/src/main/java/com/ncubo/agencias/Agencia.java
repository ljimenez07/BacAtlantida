package com.ncubo.agencias;

public class Agencia {

	private String codigo;
	private final String nombre;
	private final String telefono;
	private final String departamento;
	private final String ciudad;
	private final String direccion;
	private final HorariosDeAgencia horarios;
	private final TipoDeAgencia tipoDeAgencia;
	
	public Agencia(String nombre, String telefono, String 
			departamento, String ciudad, String tipo, HorariosDeAgencia horarios, String direccion){
		
		this.codigo = "";
		
		if(direccion.equals("null")){
			this.direccion = "";
		}else{
			this.direccion = direccion;
		}
		
		if(nombre.equals("null")){
			this.nombre = "";
		}else{
			this.nombre = nombre;
		}
		
		if(telefono.equals("null")){
			this.telefono = "";
		}else{
			this.telefono = telefono;
		}
		
		if(departamento.equals("null")){
			this.departamento = "";
		}else{
			this.departamento = departamento;
		}
		
		if(ciudad.equals("null")){
			this.ciudad = "";
		}else{
			this.ciudad = ciudad;
		}
		
		this.horarios = horarios;
		
		if(tipo.contains("Agencia")){
			tipoDeAgencia = TipoDeAgencia.AGENCIA;
		}else if(tipo.contains("Ventanilla")){
			tipoDeAgencia = TipoDeAgencia.VENTANILLA;
		}
		else{
			tipoDeAgencia = TipoDeAgencia.AUTOBANCO;
		}
		
	}
	
	public enum TipoDeAgencia {
		AGENCIA, AUTOBANCO,VENTANILLA
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
