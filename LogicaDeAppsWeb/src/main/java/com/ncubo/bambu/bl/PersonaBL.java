package com.ncubo.bambu.bl;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.ncubo.bambu.dao.PersonaDao;
import com.ncubo.bambu.data.Persona.Atributo;

@Component
public class PersonaBL
{
	public int agregar(HashMap<Atributo, Object> atributos)
	{
		int idPersona = new PersonaDao().agregar(atributos);
		return idPersona;
	}
}
