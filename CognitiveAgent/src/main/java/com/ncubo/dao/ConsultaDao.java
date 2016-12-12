package com.ncubo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ncubo.chatbot.partesDeLaConversacion.Temario;
import com.ncubo.data.Consulta;

@Component("consultaDao")
public class ConsultaDao
{
	@Autowired
	private Persistencia dao;
	private final String NOMBRE_TABLA = "estadistica_tema";
	private Temario temario;
	
	private enum atributo
	{
		ID_TEMA("idTema"),
		FECHA("fecha"),
		TEMA("nombreTema"),
		VECES_CONSULTADO("vecesConsultado"),
		TOTAL_CONSULTADO("TotalConsultado");
		
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
	
	public ArrayList<Consulta> obtener(String fechaDesde, String fechaHasta) throws ClassNotFoundException, SQLException
	{
		ArrayList<Consulta> consultas = new ArrayList<Consulta>();
		String query = "SELECT " 
				+ NOMBRE_TABLA + "." + atributo.ID_TEMA + ", "
				+ "SUM(" + atributo.VECES_CONSULTADO + ") as '" + atributo.TOTAL_CONSULTADO + "'"
				+ " FROM " + NOMBRE_TABLA
				+ " WHERE " + atributo.FECHA + " BETWEEN '" + fechaDesde + "' AND '" + fechaHasta + "'" 
				+ " group by " + atributo.ID_TEMA + " ;";

		Connection con = dao.openConBD();
		ResultSet rs = con.createStatement().executeQuery(query);
		
		while (rs.next())
		{
			consultas.add(new Consulta(
					temario.buscarTema(rs.getString(atributo.ID_TEMA.toString())),
					null,
					rs.getInt(atributo.TOTAL_CONSULTADO.toString())
				));
		}
		
		dao.closeConBD();
		return consultas;
	}
	
	public void insertar(Consulta consulta) throws ClassNotFoundException, SQLException
	{
		String queryDatos = "'" + consulta.getTema().obtenerIdTema()+ "'"
				+ ",'" + consulta.getFecha() + "'"
				+ ",'" + consulta.getVecesConsultado() + "'";
		String query = "INSERT INTO " + NOMBRE_TABLA
				 + "(" + atributo.ID_TEMA + ","
				 + atributo.FECHA + ","
				 + atributo.VECES_CONSULTADO + ")"
				 + " VALUES (" + queryDatos + ") "
				 + " ON DUPLICATE KEY UPDATE " + atributo.VECES_CONSULTADO + " = " +atributo.VECES_CONSULTADO + " + " + consulta.getVecesConsultado();
		
		Connection con = dao.openConBD();
		con.createStatement().execute(query);
		dao.closeConBD();
	}

	public void establecerTemario(Temario temario) {
		this.temario = temario;
	}
	
}