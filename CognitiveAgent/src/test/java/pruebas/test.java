package pruebas;
import org.junit.Test;

import com.ncubo.data.Belleza;
import com.ncubo.data.Categorias;
import com.ncubo.data.Hotel;
import com.ncubo.data.Oferta;
import com.ncubo.data.Restaurate;

public class test {
	
	@Test
	public void pruebaUno()
	{
	Categorias categorias = new Categorias();	
	
	Oferta oferta = new Oferta();
	oferta.setCategorias(categorias);
	
	double distanciaActualEntreAmbasCategorias = oferta.distanciaEuclidianaDeCategoria(
		new Belleza(0), 
		new Hotel(0),
		new Restaurate(0));
	System.out.println( distanciaActualEntreAmbasCategorias );
	
	distanciaActualEntreAmbasCategorias = oferta.distanciaEuclidianaDeCategoria(
			new Belleza(1), 
			new Hotel(1),
			new Restaurate(1));
	System.out.println( distanciaActualEntreAmbasCategorias );
	
	distanciaActualEntreAmbasCategorias = oferta.distanciaEuclidianaDeCategoria(
			new Belleza(2), 
			new Hotel(2),
			new Restaurate(2));
	System.out.println( distanciaActualEntreAmbasCategorias );
	
	distanciaActualEntreAmbasCategorias = oferta.distanciaEuclidianaDeCategoria(
			new Belleza(1.3), 
			new Hotel(1.7),
			new Restaurate(1.4));
	System.out.println( distanciaActualEntreAmbasCategorias );
	}

}
