package com.ncubo.bambu.bl;

import java.util.ArrayList;

import com.ncubo.bambu.dao.OrigenDao;
import com.ncubo.bambu.data.Origen;

public class OrigenBL
{
	public ArrayList<Origen> obtenerOrigenes()
	{
		return new OrigenDao().obtenerOrigenes();
	}
}
