package com.ncubo.exceptions;

public class CredencialesInvalidosException extends RuntimeException implements NoEmailException
{
	public CredencialesInvalidosException()
	{
		super("El usuaio y password no corresponden a un usuario válido");
	}
	
}
