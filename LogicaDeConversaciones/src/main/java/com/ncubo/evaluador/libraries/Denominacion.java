package com.ncubo.evaluador.libraries;

import com.ncubo.evaluador.interprete.libraries.LanguageException;

public class Denominacion extends Moneda 
{
	
	public Denominacion(Decimal monto,Monedas tipoDeMoneda) 
	{
		super(monto, tipoDeMoneda);
	}

	public Denominacion(Numero numero, Monedas tipoDeMoneda) 
	{
		super(numero, tipoDeMoneda);
	}

	@Override
	String simboloMonetario() 
	{
		return tipoDeMoneda.name();
	}

	@Override
	boolean estaEnLaMismaMonedaQue(Moneda otroMonto) 
	{
		return this.tipoDeMoneda == otroMonto.tipoDeMoneda;
	}

	@Override
	public String nombreDeLaMoneda() 
	{
		return tipoDeMoneda.nombre();
	}

	@Override
	public Moneda ceroEnEstaMoneda() 
	{
		return new Denominacion(new Decimal(0), tipoDeMoneda);
	}

	@Override
	public Moneda obtenerNuevaMonedaEnLaMismaEconomia(double cantidadDePlata) 
	{
		double cantidadRedondeada = Math.rint( cantidadDePlata * 100.0 ) / 100.0;
		return new Denominacion(new Decimal( cantidadRedondeada ) ,tipoDeMoneda);
	}

	@Override
	public double convertirADouble() 
	{		
		return monto.getValor();
	}

	@Override
	public Objeto sinDecimales() 
	{
		return new Denominacion( new Numero( (int) this.monto.getValor()),tipoDeMoneda);
	}
	
	@Override
	public boolean esIgualQue(Objeto objeto) 
	{
		Denominacion otraMoneda = null;
		try
		{
			otraMoneda = (Denominacion)objeto;				
		}
		catch(ClassCastException p)
		{
			throw new LanguageException(
					String.format("En la comparación se esperaba el valor de tipo [%s] pero se encontro un valor de tipo [%s]", Denominacion.class.getSimpleName(), objeto.getClass().getSimpleName())
					);
		}
		return convertirADouble() == otraMoneda.convertirADouble() && tipoDeMoneda == otraMoneda.tipoDeMoneda;
	}
	
	@Override
	public boolean noEsIgualQue(Objeto objeto) 
	{
		return ! esIgualQue(objeto);
	}
}
