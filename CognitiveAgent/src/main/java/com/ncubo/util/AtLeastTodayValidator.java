package com.ncubo.util;

import java.sql.Date;
import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastTodayValidator implements ConstraintValidator<AtLeastToday, Date>
{

	public void initialize(AtLeastToday annotation)
	{
		
	}

	public boolean isValid(final Date value, final ConstraintValidatorContext context)
	{
		if(value != null)
		{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -1);
			return value.after(c.getTime());
		}
		return true;
	}
	
}
