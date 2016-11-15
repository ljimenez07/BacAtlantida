package com.ncubo.exceptions;

public class NoSessionException extends RuntimeException implements NoEmailException
{
	public NoSessionException()
	{
		super("El usuario debe loguearse");
	}
	
}
