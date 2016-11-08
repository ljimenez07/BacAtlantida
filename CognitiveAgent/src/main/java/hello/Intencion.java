package hello;

public enum Intencion 
{
	SALDO("saldo"),
	TASA_DE_CAMBIO("tasa_cambio");
	
	private String nombre;
	Intencion(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String toString()
	{
		return nombre;
	}
}
