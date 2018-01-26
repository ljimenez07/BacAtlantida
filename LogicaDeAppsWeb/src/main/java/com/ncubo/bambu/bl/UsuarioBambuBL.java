package com.ncubo.bambu.bl;

import com.ncubo.bambu.dao.UsuarioBambuDao;
import com.ncubo.bambu.data.UsuarioBambu;

public class UsuarioBambuBL
{
	public UsuarioBambu recuperar(int idUsuario)
	{
		return new UsuarioBambuDao().obtenerUsuario(idUsuario);
	}
}
