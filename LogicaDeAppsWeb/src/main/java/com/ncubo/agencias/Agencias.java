package com.ncubo.agencias;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.dao.AgenciasDao;

@Component
public class Agencias {

	@Autowired
	private AgenciasDao agencias;
	
	public ArrayList<Agencia> buscarAgenciasPorCuidadYDepartamento(String ciudad, String departamento){
		return agencias.buscarAgenciasPorCuidadYDepartamento(ciudad, departamento);
	}
	
	public ArrayList<Agencia> buscarAgenciasPorNombre(String nombre){
		return agencias.buscarAgenciasPorNombre(nombre);
	}
	
}
