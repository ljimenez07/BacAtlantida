package com.ncubo.bambu.bl;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.ncubo.bambu.dao.PersonaContactoDao;
import com.ncubo.bambu.data.NuevoContacto;
import com.ncubo.bambu.data.Persona;
import com.ncubo.bambu.data.PersonaContacto;
import com.ncubo.bambu.data.UsuarioBambu;
import com.ncubo.bambu.util.Fecha;

@Component
public class PersonaContactoBL
{
	
	public int crearContacto(NuevoContacto usuarioCandidato, int idUsuarioEnSesion)
	{
		String fechaActual = Fecha.obtenerCadenaFechaActual();
		HashMap<Persona.Atributo, Object> atributosPersona = new HashMap<>();
		atributosPersona.put(Persona.Atributo.CREADO_POR, idUsuarioEnSesion);
		atributosPersona.put(Persona.Atributo.FECHA_CREACION, fechaActual);
		atributosPersona.put(Persona.Atributo.REGISTRO_BORRADO, false);
		int idPersona = new PersonaBL().agregar(atributosPersona);
		
		fechaActual = Fecha.obtenerCadenaFechaActual();
		UsuarioBambu usuarioEnSesion = new UsuarioBambuBL().recuperar(idUsuarioEnSesion);
		HashMap<PersonaContacto.atributo, Object> atributosContacto = new HashMap<>();
		atributosContacto.put(PersonaContacto.atributo.NOMBRE, usuarioCandidato.obtenerNombre());
		atributosContacto.put(PersonaContacto.atributo.APELLIDOS, usuarioCandidato.obtenerApellidos());
		atributosContacto.put(PersonaContacto.atributo.TELEFONO, usuarioCandidato.obtenerTelefono());
		atributosContacto.put(PersonaContacto.atributo.CELULAR, usuarioCandidato.obtenerCelular());
		atributosContacto.put(PersonaContacto.atributo.EMAIL, usuarioCandidato.obtenerCorreoElectronico());
		atributosContacto.put(PersonaContacto.atributo.DIRECCION_ENTREGA, usuarioCandidato.obtenerDireccion());
		atributosContacto.put(PersonaContacto.atributo.CREADO_POR, idUsuarioEnSesion);
		atributosContacto.put(PersonaContacto.atributo.FECHA_CREACION, fechaActual);
		atributosContacto.put(PersonaContacto.atributo.ID_EMPRESA, usuarioEnSesion.obtenerIdEmpresaPrincipal());
		atributosContacto.put(PersonaContacto.atributo.ID_PERSONA, idPersona);
		atributosContacto.put(PersonaContacto.atributo.REGISTRO_BORRADO, false);
		int idContactoPersona = new PersonaContactoDao().agregar(atributosContacto);
		return idContactoPersona;
	}

}
