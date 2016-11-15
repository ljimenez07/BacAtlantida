package com.ncubo.chatbot.watson;

public class WorkSpace {

	private String usuarioIBM;
	private String contrasenaIBM;
	private String idIBM;
	private String tipo;
	private String nombre;
	
	public WorkSpace(String usuarioIBM, String contrasenaIBM, String idIBM, String tipo, String nombre) {
		this.usuarioIBM = usuarioIBM;
		this.contrasenaIBM = contrasenaIBM;
		this.idIBM = idIBM;
		this.tipo = tipo;
		this.nombre = nombre;
	}

	public String getUsuarioIBM() {
		return usuarioIBM;
	}

	public void setUsuarioIBM(String usuarioIBM) {
		this.usuarioIBM = usuarioIBM;
	}

	public String getContrasenaIBM() {
		return contrasenaIBM;
	}

	public void setContrasenaIBM(String contrasenaIBM) {
		this.contrasenaIBM = contrasenaIBM;
	}

	public String getIdIBM() {
		return idIBM;
	}

	public void setIdIBM(String idIBM) {
		this.idIBM = idIBM;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
