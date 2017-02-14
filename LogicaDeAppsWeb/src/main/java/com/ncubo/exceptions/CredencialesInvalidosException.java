package com.ncubo.exceptions;

public class CredencialesInvalidosException extends RuntimeException implements NoEmailException
{
	private static final long serialVersionUID = 1640172875368088756L;

	public CredencialesInvalidosException()
	{
		super("El nombre de usuario o la contraseña no son correctas.");
	}
	
}
