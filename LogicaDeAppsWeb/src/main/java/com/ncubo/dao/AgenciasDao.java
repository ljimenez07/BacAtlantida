package com.ncubo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.agencias.Agencia;
import com.ncubo.agencias.HorariosDeAgencia;

@Component
public class AgenciasDao {

	private final String AGENCIAS_AUTOBANCOS = "contenedor_info_agencias_autobancos";
	
	@Autowired
	private Persistencia dao;
	
	public enum atributo
	{
		ID("idContenedorDeInfoDeAgenciasYAutobancos"),
		CODIGO("codigo"),
		TIPO("tipo"),
		NOMBRE("nombre"),
		TELEFONO("telefono"),
		DEPARTAMENTO("departamento"),
		CIUDAD("ciudad"),
		DIRECCION("direccion"),
		HORARIO_LV("horarioLV"),
		HORARIO_SABADO("horarioSabado"),
		HORARIO_DOMINGO("horarioDomingo");
		
		private String nombre;
		atributo(String nombre)
		{
			this.nombre = nombre;
		}
		
		public String toString()
		{
			return this.nombre;
		}
	}
	
	public ArrayList<Agencia> buscarAgenciasPorCuidadYDepartamento(String ciudad, String departamento, String operadorLogicoABuscar){
		
		ArrayList<Agencia> respuesta = new ArrayList<>();
		
		String query = "SELECT * FROM " + AGENCIAS_AUTOBANCOS+" where ";

		if(ciudad.isEmpty() && departamento.isEmpty()){
			return respuesta;
		}else if(! ciudad.isEmpty() && departamento.isEmpty()){
			query += atributo.CIUDAD+" like '%"+ ciudad +"%';";
		}else if (ciudad.isEmpty() && ! departamento.isEmpty()){
			query += atributo.DEPARTAMENTO+" like '%"+ departamento +"%';";
		}else{
			query += atributo.CIUDAD+" like '%"+ ciudad +"%' "+operadorLogicoABuscar+" " + atributo.DEPARTAMENTO+" like '%"+ departamento +"%';";
		}
		
		try {
			
			Connection con = dao.openConBD();
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()){
				
				HorariosDeAgencia horario = new HorariosDeAgencia(rs.getString(atributo.HORARIO_LV.toString())+"",
						rs.getString(atributo.HORARIO_SABADO.toString())+"", rs.getString(atributo.HORARIO_DOMINGO.toString())+"");
				
				Agencia agencia = new Agencia(rs.getString(atributo.NOMBRE.toString())+"", rs.getString(atributo.TELEFONO.toString())+"", 
						rs.getString(atributo.DEPARTAMENTO.toString())+"", rs.getString(atributo.CIUDAD.toString())+"", 
						rs.getString(atributo.TIPO.toString())+"", horario, rs.getString(atributo.DIRECCION.toString())+"");
				
				respuesta.add(agencia);
			}
			
			dao.closeConBD();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return respuesta;
	}
	
	public ArrayList<Agencia> buscarAgenciasPorNombre(String nombre){
		
		ArrayList<Agencia> respuesta = new ArrayList<>();
		
		String query = "SELECT * FROM " + AGENCIAS_AUTOBANCOS+" where ";

		if(nombre.isEmpty()){
			return respuesta;
		}else{
			query += atributo.NOMBRE+" like '%"+ nombre +"%';";
		}
		
		try {
			Connection con = dao.openConBD();
			ResultSet rs = con.createStatement().executeQuery(query);
			
			while (rs.next()){
				
				HorariosDeAgencia horario = new HorariosDeAgencia(rs.getString(atributo.HORARIO_LV.toString())+"",
						rs.getString(atributo.HORARIO_SABADO.toString())+"", rs.getString(atributo.HORARIO_DOMINGO.toString())+"");
				
				Agencia agencia = new Agencia(rs.getString(atributo.NOMBRE.toString())+"", rs.getString(atributo.TELEFONO.toString())+"", 
						rs.getString(atributo.DEPARTAMENTO.toString())+"", rs.getString(atributo.CIUDAD.toString())+"", 
						rs.getString(atributo.TIPO.toString())+"", horario, rs.getString(atributo.DIRECCION.toString())+"");
				
				respuesta.add(agencia);
			}
			
			dao.closeConBD();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return respuesta;
	}

}
