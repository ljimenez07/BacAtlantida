package com.ncubo.chatbot.partesDeLaConversacion;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.chatbot.parser.Operador;
import com.ncubo.chatbot.parser.Operador.TipoDeOperador;
import com.ncubo.chatbot.watson.Entidad;
import com.ncubo.chatbot.watson.Entidades;
import com.ncubo.chatbot.watson.Intencion;
import com.ncubo.chatbot.watson.Intenciones;
import com.ncubo.chatbot.watson.WorkSpace;
import com.ncubo.realestate.ContenidoDeRealEstate;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public abstract class Contenido 
{
	private ArrayList<Frase> frases = new ArrayList<Frase>();
	//private ArrayList<Intencion> intenciones = new ArrayList<Intencion>();
	private ArrayList<WorkSpace> miWorkSpaces = new ArrayList<WorkSpace>();
	
	protected Contenido(){
		File archivoDeConfiguracion = archivoDeConfiguracion();
		// TODO Ver que el archivo existe, sino return error
		cargarPreguntasYRespuestasDelArchivoDeConfiguracion(archivoDeConfiguracion);
	}
	
	protected abstract File archivoDeConfiguracion();

	public Frase frase(String idDeLaFrase){
		
		for(Frase frase: frases){
			if(frase.getIdFrase().equalsIgnoreCase(idDeLaFrase)){
				return frase;
			}
		}
		
		throw new ChatException(
			String.format("El el archivo de contenido '%s' no se esta ninguna frase cuyo id sea '%s'", archivoDeConfiguracion().getAbsoluteFile(), idDeLaFrase)
		);
	}
	
	public void generarAudioEstatico(){
		System.out.println("Cargar audios estaticos ...");
		// Para cada frase en el xml 
		for (Frase frase: frases){
			if(frase.esEstatica()){
				System.out.println("Es estatica ...");
				for (int indice = 0; indice < frase.getTextosDeLaFrase().length; indice++){
					System.out.println("Frase: "+frase.getTextosDeLaFrase()[indice]);
				}
			}
		}
	}
	
	private Contenido agregarFrase(Frase unaFrase){
		System.out.println("Agregando frase: " +unaFrase.getIdFrase());
		frases.add(unaFrase);
		return this;
	}
	
	/*protected Contenido add(Intencion unaIntencion)
	{
		
	}*/
	
	protected Contenido end()
	{
		//Warning si hay cosas en el XML que no se estan usando en el contenido
		// Validar que solo exista un saludo y una despedida en el xml
		// Validar que un id no se repita
		// Validar que al menos haya una pregunta o frase en el xml
		// Validar si una frase afirmativa tiene entidades o intenciones REVENTAR
		// Validar que la preguntas tengan por lo menos una intencion o entidad
		return this;
	}
	
	@SuppressWarnings("null")
	private void cargarPreguntasYRespuestasDelArchivoDeConfiguracion(File file){
		
		try {
			Frase miFrase = null;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);

			doc.getDocumentElement().normalize();

			/*// Entidades
			NodeList lasEntidades = doc.getElementsByTagName("entidades");
			
			Entidades misEntidades = new Entidades();
			try{
				Element entidades = (Element) eElement.getElementsByTagName("entidades").item(0);
				NodeList entidad = entidades.getElementsByTagName("entidad");
				for (int temp1 = 0; temp1 < entidad.getLength(); temp1++) {
					String nombre = entidades.getElementsByTagName("entidad").item(temp1).getTextContent();
					Entidad miEntidad = Entidad.newInstance(nombre, valores);
					misEntidades.agregar(miEntidad);
					System.out.println("Entidad: " +miEntidad.getNombre());
				}
			}catch(Exception e){
				
			}*/
			
			// WorkSpaces
			try{
				System.out.println("\nCargando los workspaces ...\n");
				NodeList workspaces = doc.getElementsByTagName("workspaces");
				Node workspacesNode = workspaces.item(0);
				Element workspacesElement = (Element) workspacesNode;
				String user = workspacesElement.getAttribute("user");
				String pass = workspacesElement.getAttribute("pass");
				NodeList workspace = workspacesElement.getElementsByTagName("workspace");
				for (int temp = 0; temp < workspace.getLength(); temp++) {
					Node nNode = workspace.item(temp);
					Element eElement = (Element) nNode;
					String tipo = eElement.getAttribute("tipo");
					String idIBM = eElement.getAttribute("idIBM");
					String nombre = nNode.getTextContent();
					System.out.println("NOMBRE :" + nombre);
					System.out.println("Tipo :" + tipo);
					//System.out.println("User :" + user);
					//System.out.println("idIBM :" + idIBM);
					miWorkSpaces.add(new WorkSpace(user, pass, idIBM, tipo, nombre));
				}
			}catch(Exception e){
				throw new ChatException("Error cargando los workspaces "+e.getMessage());
			}
			
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList conversaciones = doc.getElementsByTagName("conversacion");
			System.out.println("\nCargando las frases ...\n");

			for (int temp = 0; temp < conversaciones.getLength(); temp++) {

				Node nNode = conversaciones.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					CaracteristicaDeLaFrase[] caracteristicasDeLaFrase = new CaracteristicaDeLaFrase[3];
					
					String idDeLaFrase = eElement.getAttribute("id");
					System.out.println("Conversacion Id : " + idDeLaFrase);
					
					String elTipoEs = eElement.getElementsByTagName("tipo").item(0).getTextContent();
					System.out.println("Tipo : " + elTipoEs);
					
					String esMandatorio = eElement.getElementsByTagName("mandatoria").item(0).getTextContent();
					System.out.println("Mandatorio : " + esMandatorio);
					if(esMandatorio.equals("true")){
						caracteristicasDeLaFrase[0] = CaracteristicaDeLaFrase.esUnaPreguntaMandatoria;
					}else{
						caracteristicasDeLaFrase[0] = CaracteristicaDeLaFrase.noUnaPreguntaMandatoria;
					}
					
					String enVozAlta = eElement.getElementsByTagName("enVozAlta").item(0).getTextContent();
					System.out.println("enVozAlta : " + enVozAlta);
					if(enVozAlta.equals("false")){
						caracteristicasDeLaFrase[1] = CaracteristicaDeLaFrase.noPuedeDecirEnVozAlta;
					}else{
						caracteristicasDeLaFrase[1] = CaracteristicaDeLaFrase.sePuedeDecirEnVozAlta;
					}
					
					Element frases = (Element) eElement.getElementsByTagName("frases").item(0);
					
					String tipoDeFraseACargar = "frase";
					if(elTipoEs.equals("pregunta")){
						tipoDeFraseACargar = "curioso";
					}
					
					String[] textosDeLaFrase = obtenerFrasesPorTipo(frases, tipoDeFraseACargar);
					
					if(elTipoEs.equals("saludo")){
						miFrase = new Saludo(idDeLaFrase, textosDeLaFrase);
					}else if(elTipoEs.equals("pregunta")){
						caracteristicasDeLaFrase[2] = CaracteristicaDeLaFrase.esUnaPregunta;
						miFrase = new Pregunta(idDeLaFrase, textosDeLaFrase, obtenerFrasesPorTipo(frases, "impertinente"), 
								caracteristicasDeLaFrase, 
								obtenerEntidades((Element) eElement.getElementsByTagName("when").item(0)), 
								obtenerIntenciones((Element) eElement.getElementsByTagName("when").item(0)));
					}else if(elTipoEs.equals("afirmativa")){
						miFrase = new Afirmacion(idDeLaFrase, textosDeLaFrase);
					}else if(elTipoEs.equals("despedida")){
						miFrase = new Despedida(idDeLaFrase, textosDeLaFrase);
					}
					agregarFrase(miFrase);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		end();
	}
	
	private Entidades obtenerEntidades(Element condition){
		Entidades misEntidades = new Entidades();
		Hashtable<String, Operador> valores;
		
		try{
			NodeList frase = condition.getElementsByTagName("condition");
			
			for (int temp = 0; temp < frase.getLength(); temp++) {
				Node nNode = frase.item(temp);
				Element eElement = (Element) nNode;
				String tipo = eElement.getAttribute("tipo");
				valores = new Hashtable<String, Operador>();
				
				if(tipo.equals("entidad")){
					String operador = eElement.getAttribute("operador");
					String entidadValor = condition.getElementsByTagName("condition").item(temp).getTextContent();
					String entidadValores[] = entidadValor.split("@");
					String entidad = "";
					String valor = "";
					if(entidadValores.length > 1){
						entidad = entidadValores[0];
						valor = entidadValores[1];
					}else{
						entidad = entidadValores[0];
					}
					valores.put(valor, new Operador(obtenerTipoDeOperador(tipo)));
					System.out.println("Entidad : " + entidad);
					//System.out.println("Entidad tipo: " + tipo);
					//System.out.println("Entidad operador: " + operador);
					misEntidades.agregar(new Entidad(entidad, valores));
				}
			}
		}catch(Exception e){
			
		}
		return misEntidades;
	}
	
	private Intenciones obtenerIntenciones(Element condition){
		Intenciones misIntenciones = new Intenciones();
		Hashtable<String, Operador> valores;
		
		try{
			NodeList frase = condition.getElementsByTagName("condition");
			
			for (int temp = 0; temp < frase.getLength(); temp++) {
				Node nNode = frase.item(temp);
				Element eElement = (Element) nNode;
				String tipo = eElement.getAttribute("tipo");
				valores = new Hashtable<String, Operador>();
				
				if(tipo.equals("intencion")){
					String operador = eElement.getAttribute("operador");
					String intencion = condition.getElementsByTagName("condition").item(temp).getTextContent();
					valores.put(intencion, new Operador(obtenerTipoDeOperador(tipo)));
					System.out.println("Intencion : " + intencion);
					System.out.println("Entidad tipo: " + tipo);
					System.out.println("Entidad operador: " + operador);
					misIntenciones.agregar(new Intencion(intencion, new Operador(obtenerTipoDeOperador(operador))));
				}
			}
		}catch(Exception e){
			
		}
		return misIntenciones;
	}
	
	private TipoDeOperador obtenerTipoDeOperador(String tipo){
		if(tipo.toUpperCase().equals("AND")) return TipoDeOperador.AND;
		else return TipoDeOperador.OR;
	}
	
	private String[] obtenerFrasesPorTipo(Element frases, String tipoDeFraseACargar){
		NodeList frase = frases.getElementsByTagName(tipoDeFraseACargar);
		String[] textosDeLaFrase = new String[frase.getLength()];
		for (int temp1 = 0; temp1 < frase.getLength(); temp1++) {
			textosDeLaFrase[temp1] = frases.getElementsByTagName(tipoDeFraseACargar).item(temp1).getTextContent();
			System.out.println("Frase : " + textosDeLaFrase[temp1]);
		}
		return textosDeLaFrase;
	}
	
	public ArrayList<WorkSpace> getMiWorkSpaces() {
		return miWorkSpaces;
	}
	
	public static void main(String argv[]) {
		Contenido contenido = new Contenido(){
		@Override
		protected File archivoDeConfiguracion() {
			// TODO Auto-generated method stub
			return new File("C:\\Users\\SergioAlberto\\Documents\\SergioGQ\\Watson\\Repo\\watson\\LogicaDeConversaciones\\src\\main\\resources\\conversaciones.xml");
		}};
		contenido.generarAudioEstatico();
	}
}