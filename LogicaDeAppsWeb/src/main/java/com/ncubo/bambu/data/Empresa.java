package com.ncubo.bambu.data;

public class Empresa
{
	private int idEmpresa;
	private String descripcion;
	private String color;
	private String storeFront;
	private String companiaNimbus;
	private String companiaChat;
	private String idsStoreFront;
	
	public int obtenerIdEmpresa()
	{
		return idEmpresa;
	}
	public void establecerIdEmpresa(int idEmpresa)
	{
		this.idEmpresa = idEmpresa;
	}
	public String obtenerDescripcion()
	{
		return descripcion;
	}
	public void establecerDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	public String obtenerColor()
	{
		return color;
	}
	public void establecerColor(String color)
	{
		this.color = color;
	}
	public String obtenerStoreFront()
	{
		return storeFront;
	}
	public void establecerStoreFront(String storeFront)
	{
		this.storeFront = storeFront;
	}
	public String obtenerCompaniaNimbus()
	{
		return companiaNimbus;
	}
	public void establecerCompaniaNimbus(String companiaNimbus)
	{
		this.companiaNimbus = companiaNimbus;
	}
	public String obtenerCompaniaChat()
	{
		return companiaChat;
	}
	public void establecerCompaniaChat(String companiaChat)
	{
		this.companiaChat = companiaChat;
	}
	public String obtenerIdsStoreFront()
	{
		return idsStoreFront;
	}
	public void establecerIdsStoreFront(String idsStoreFront)
	{
		this.idsStoreFront = idsStoreFront;
	}
}
