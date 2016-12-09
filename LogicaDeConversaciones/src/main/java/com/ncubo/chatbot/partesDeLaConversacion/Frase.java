package com.ncubo.chatbot.partesDeLaConversacion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.chatbot.watson.TextToSpeechWatson;

public abstract class Frase
{
	private final String idFrase;
	
	private String[] textosDeLaFrase;
	private ArrayList<Sonido> sonidosDeLosTextosDeLaFrase = new ArrayList<Sonido>();
	private ArrayList<Vineta> vinetasDeLosTextosDeLaFrase = new ArrayList<Vineta>();
	
	private String[] textosImpertinetesDeLaFrase;
	private ArrayList<Sonido> sonidosDeLosTextosImpertinentesDeLaFrase = new ArrayList<Sonido>();
	
	private final CaracteristicaDeLaFrase[] caracteristicas;
	//private Intencion intencion;
	private boolean esEstatica = true;
	//private final Contenido contenido;
	
	private String pathAGuardarLosAudiosTTS;
	private String ipPublicaAMostrarLosAudioTTS;
	
	protected Frase (String idFrase, String[] textosDeLaFrase, String[] textosImpertinetesDeLaFrase, String[] vinetasDeLaFrase,
			CaracteristicaDeLaFrase... caracteristicas)
	{
		//this.contenido = contenido;
		this.caracteristicas = caracteristicas;
		this.idFrase = idFrase;
		this.textosDeLaFrase = textosDeLaFrase;
		this.textosImpertinetesDeLaFrase = textosImpertinetesDeLaFrase;
		cargarLaFrase();
		cargarVinetas(vinetasDeLaFrase);
		if(esEstatica()){
			System.out.println("Es estaticaaaaaaaa");
			// verificar el archivo de audio exite o devolver un ChatException
			// Asignar la url del archivo dependiendo del textos
		}
		if(esDinamica()){
			System.out.println("Es dinamicaaaaa");
		}
		
	}
	
	private void cargarVinetas(String[] vinetasDeLaFrase){
		if (vinetasDeLaFrase != null){
			for(int index = 0; index < vinetasDeLaFrase.length; index ++){
				String vineta = vinetasDeLaFrase[index];
				vineta = vineta.replace("@@", "<").replace("##", ">");
				vinetasDeLosTextosDeLaFrase.add(new Vineta(vineta));
			}
		}
	}
	
	public void agregarSonido(int index, Sonido sonido){
		sonidosDeLosTextosDeLaFrase.add(index, sonido);
	}
	
	public void agregarSonidoImpertinente(int index, Sonido sonido){
		sonidosDeLosTextosImpertinentesDeLaFrase.add(index, sonido);
	}
	
	private void verSiLaFraseTienePlaceHolders(){
		boolean tieneUnoOVariosPlaceHolders = false;
		for (String texto: textosDeLaFrase){
			//tieneUnoOVariosPlaceHolders = tieneUnoOVariosPlaceHolders || texto.indexOf("%s")  == -1;
			tieneUnoOVariosPlaceHolders = ! (texto.indexOf("$") == -1);
			if(tieneUnoOVariosPlaceHolders) 
				break;
		}
		esEstatica = ! tieneUnoOVariosPlaceHolders;
	}
	
	public String getIdFrase() {
		return idFrase;
	}
	
	public List<String> texto(){
		List<String> resultado = null;
		
		if(textosDeLaFrase.length > 0){
			resultado = new ArrayList<>();
			int unIndiceAlAzar = (int)Math.floor(Math.random()*textosDeLaFrase.length);
			resultado.add(unIndiceAlAzar+"");
			resultado.add(textosDeLaFrase[unIndiceAlAzar]);
		}
		
		return resultado;
	}
	
	public List<String> vineta(){
		List<String> resultado = null;
		
		if(vinetasDeLosTextosDeLaFrase.size() > 0){
			resultado = new ArrayList<>();
			int unIndiceAlAzar = (int)Math.floor(Math.random()*vinetasDeLosTextosDeLaFrase.size());
			resultado.add(unIndiceAlAzar+"");
			resultado.add(vinetasDeLosTextosDeLaFrase.get(unIndiceAlAzar).url());
		}
		
		return resultado;
	}
	
	public Sonido obtenerSonidoAUsar(int idDelSonidoAUsar){
		Sonido resultado = null;
		if(esEstatica() && sonidosDeLosTextosDeLaFrase.size() > 0){
			if (idDelSonidoAUsar == -1){
				idDelSonidoAUsar = (int)Math.floor(Math.random()*sonidosDeLosTextosDeLaFrase.size());
			}
			resultado = sonidosDeLosTextosDeLaFrase.get(idDelSonidoAUsar);
		}
		return resultado;
	}
	
	public List<String> textoImpertinete(){
		List<String> resultado = new ArrayList<>();
		if(textosImpertinetesDeLaFrase.length > 0){
			int unIndiceAlAzar = (int)Math.floor(Math.random()*textosImpertinetesDeLaFrase.length);
			resultado.add(unIndiceAlAzar+"");
			resultado.add(textosImpertinetesDeLaFrase[unIndiceAlAzar]);
			return resultado;
		}else{
			return texto();
		}
	}
	
	public Sonido obtenerSonidoImpertienteAUsar(int idDelSonidoImpertineteAUsar){
		Sonido resultado = null;
		if(hayTextosImpertinetes() && sonidosDeLosTextosImpertinentesDeLaFrase.size() > 0){
			if (idDelSonidoImpertineteAUsar == -1){
				idDelSonidoImpertineteAUsar = (int)Math.floor(Math.random()*sonidosDeLosTextosImpertinentesDeLaFrase.size());
			}resultado = sonidosDeLosTextosImpertinentesDeLaFrase.get(idDelSonidoImpertineteAUsar);
		}
		return resultado;
	}
	
	public boolean hayTextosImpertinetes(){
		try{
			return (textosImpertinetesDeLaFrase.length > 0);
		}catch(Exception e){
			return false;
		}
	}
	
	boolean esEstatica(){
		return esEstatica;
	}
	
	boolean esDinamica(){
		return ! esEstatica;
	}
	
	protected void cargarLaFrase() 
	{
		if (idFrase == null) 
			new ChatException("No has inicializado el id de la frase");
		
		// Validar que en el archivo o repositorio existe ese ID.
		//textoDeLaFrase = contenido.buscarLaFrase(this.idFrase);
		verSiLaFraseTienePlaceHolders();
		// "Validar inconsistencias como una frase no puede ser de saludo y despedida a la vez, pregunta y afirmativa a la vez"
		// Validar que ese ID al menos tenga un texto
		
	}
	
	public boolean buscarCaracteristica(CaracteristicaDeLaFrase caracteristica){
		boolean resultado = false;
		for (CaracteristicaDeLaFrase miCaracteristica: caracteristicas){
			if(miCaracteristica.equals(caracteristica)){
				resultado = true;
				break;
			}
		}
		return resultado;
	}
	
	public void generarAudiosEstaticos(String pathAGuardar, String ipPublica){
		this.pathAGuardarLosAudiosTTS = pathAGuardar;
		this.ipPublicaAMostrarLosAudioTTS = ipPublica;
		
		if (sePuedeDecirEnVozAlta()){
			if(esEstatica()){
				for(int index = 0; index < textosDeLaFrase.length; index ++){
					String texto = textosDeLaFrase[index];
					String textoParaReproducir = texto;
					String textoNumerosTelefonicos = "";
					String textoTag = ""; 
					while(texto.contains("@@"))
					{
						
						textoTag = texto.substring(texto.indexOf("@@")+2, texto.indexOf("@@@"));
						System.out.println(textoTag);
						if(textoTag.contains("#"))
						{
							textoNumerosTelefonicos = texto.substring(texto.indexOf("@@#@@@")+6, texto.indexOf("@@#/@@@"));
							textoParaReproducir = textoParaReproducir.replaceFirst("@@#@@@"+textoNumerosTelefonicos+"@@#/@@@", "");
							texto = texto.replaceFirst("@@#@@@", "");
							texto = texto.replaceFirst("@@#/@@@", "");
						}
						else
						{
							textoParaReproducir = textoParaReproducir.replaceFirst("@@"+textoTag+"@@@", "");
							texto = texto.replaceFirst("@@"+textoTag+"@@@", "<"+textoTag+">");

						}
					}
					String nombreDelArchivo = TextToSpeechWatson.getInstance().getAudioToURL(textoParaReproducir, false);
					String path = pathAGuardar+File.separator+nombreDelArchivo;
					String miIp = ipPublica+nombreDelArchivo;
					sonidosDeLosTextosDeLaFrase.add(new Sonido(miIp, path));
					
					textosDeLaFrase[index] = texto;
				}
			}
			
			if(hayTextosImpertinetes()){
				for(int index = 0; index < textosImpertinetesDeLaFrase.length; index ++){
					String texto = textosImpertinetesDeLaFrase[index];
					String nombreDelArchivo = TextToSpeechWatson.getInstance().getAudioToURL(texto, false);
					String path = pathAGuardar+File.separator+nombreDelArchivo;
					String miIp = ipPublica+nombreDelArchivo;
					sonidosDeLosTextosImpertinentesDeLaFrase.add(new Sonido(miIp, path));
				}
			}
		}
	}
	
	public boolean esUnaPregunta(){
		//boolean resultado = IntStream.of(caracteristicas).anyMatch(x -> x == CaracteristicaDeLaFrase.esUnaPregunta);
		return buscarCaracteristica(CaracteristicaDeLaFrase.esUnaPregunta);
	}

	public boolean esUnSaludo(){
		//boolean resultado = IntStream.of(caracteristicas).anyMatch(x -> x == CaracteristicaDeLaFrase.esUnSaludo);
		return buscarCaracteristica(CaracteristicaDeLaFrase.esUnSaludo);
	}
	
	public boolean esUnaOracionAfirmativa(){
		//boolean resultado = IntStream.of(caracteristicas).anyMatch(x -> x == CaracteristicaDeLaFrase.esUnaOracionAfirmativa);
		return buscarCaracteristica(CaracteristicaDeLaFrase.esUnaOracionAfirmativa);
	}
	
	public boolean esUnaDespedida(){
		//boolean resultado = IntStream.of(caracteristicas).anyMatch(x -> x == CaracteristicaDeLaFrase.esUnaDespedida);
		return buscarCaracteristica(CaracteristicaDeLaFrase.esUnaDespedida);
	}
	
	public boolean esMandatorio(){
		return buscarCaracteristica(CaracteristicaDeLaFrase.esUnaPreguntaMandatoria);
	}
	
	public boolean sePuedeDecirEnVozAlta(){
		return buscarCaracteristica(CaracteristicaDeLaFrase.sePuedeDecirEnVozAlta);
	}
	
	public String[] getTextosDeLaFrase() {
		return textosDeLaFrase;
	}

	public List<String> conjuncionParaRepreguntar(){
		/*String muletillas[] = new String[]{"I'm sorry, i didn't undertand.", "I didn't catch that."};
		
		int unIndiceAlAzar = (int)Math.floor(Math.random()*muletillas.length);
		return muletillas[unIndiceAlAzar];*/
		
		return Conjunciones.getInstance().obtenerUnaConjuncion().texto();
	}
	
	public void setTextosDeLaFrase(String[] textosDeLaFrase) {
		this.textosDeLaFrase = textosDeLaFrase;
	}
	
	public String getPathAGuardarLosAudiosTTS() {
		return pathAGuardarLosAudiosTTS;
	}

	public String getIpPublicaAMostrarLosAudioTTS() {
		return ipPublicaAMostrarLosAudioTTS;
	}
	
	/*public String muletilla(){
		// Podrian vernir de un archivo gigante, que no depende del real state
		
		// Las muletillas podrian estar 
		String muletillas[] = new String[]{"Well", "now"};
		
		int unIndiceAlAzar = (int)Math.floor(Math.random()*muletillas.length);
		return muletillas[unIndiceAlAzar];
	}*/
	
	public String optenerLaInformacionDeLaFrase(){
		String resultado = "";
		
		if(textosDeLaFrase.length > 0){
			resultado += "Frases: \n";
			for (int index = 0; index < textosDeLaFrase.length; index ++){
				resultado += "     - "+textosDeLaFrase[index]+"\n";
			}
		}
		
		if(hayTextosImpertinetes()){
			resultado += "Frases Impertinentes: \n";
			for (int index = 0; index < textosImpertinetesDeLaFrase.length; index ++){
				resultado += "     - "+textosImpertinetesDeLaFrase[index]+"\n";
			}
		}
		
		return resultado;
	}
}