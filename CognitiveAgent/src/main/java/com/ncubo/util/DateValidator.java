package com.ncubo.util;

import java.sql.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<com.ncubo.util.Date, Object>
{

	public void initialize(com.ncubo.util.Date annotation)
	{
		
	}

	public boolean isValid(final Object value, final ConstraintValidatorContext context)
	{
		if(value != null)
		{
			return value instanceof Date;
		}
		return true;
	}
	
}
