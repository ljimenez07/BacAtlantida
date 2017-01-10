package com.ncubo.exceptions;

public class NoSessionException extends RuntimeException implements NoEmailException
{
	private static final long serialVersionUID = -4807855934794731462L;

	public NoSessionException()
	{
		super("El usuario debe loguearse");
	}
	
}
