package com.ncubo.chatbot.partesDeLaConversacion;

import com.ncubo.chatbot.exceptiones.ChatException;

public abstract class Frase
{
	private final String idFrase;
	
	private String[] textosDeLaFrase;
	private String[] textosImpertinetesDeLaFrase;
	private final CaracteristicaDeLaFrase[] caracteristicas;
	//private Intencion intencion;
	private boolean esEstatica = true;
	//private final Contenido contenido;
	
	protected Frase (String idFrase, String[] textosDeLaFrase, String[] textosImpertinetesDeLaFrase, 
			CaracteristicaDeLaFrase... caracteristicas)
	{
		//this.contenido = contenido;
		this.caracteristicas = caracteristicas;
		this.idFrase = idFrase;
		this.textosDeLaFrase = textosDeLaFrase;
		this.textosImpertinetesDeLaFrase = textosImpertinetesDeLaFrase;
		cargarLaFrase();
		if(esEstatica()){
			System.out.println("Es estaticaaaaaaaa");
			// verificar el archivo de audio exite o devolver un ChatException
			// Asignar la url del archivo dependiendo del textos
		}
		if(esDinamica()){
			System.out.println("Es dinamicaaaaa");
		}
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
	
	public Sonido sonido(){
		return new Sonido("");
	}
	
	public Vineta vineta(){
		return new Vineta("");
	}
	
	public String getIdFrase() {
		return idFrase;
	}
	
	public String texto(){
		int unIndiceAlAzar = (int)Math.floor(Math.random()*textosDeLaFrase.length);
		return textosDeLaFrase[unIndiceAlAzar];
	}
	
	public String textoImpertinete(){
		if(textosImpertinetesDeLaFrase.length > 0){
			int unIndiceAlAzar = (int)Math.floor(Math.random()*textosImpertinetesDeLaFrase.length);
			return textosImpertinetesDeLaFrase[unIndiceAlAzar];
		}else{
			return texto();
		}
	}
	
	public boolean hayTextosImpertinetes(){
		return (textosImpertinetesDeLaFrase.length > 0);
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
	
	public String[] getTextosDeLaFrase() {
		return textosDeLaFrase;
	}

	public String conjuncionParaRepreguntar(){
		/*String muletillas[] = new String[]{"I'm sorry, i didn't undertand.", "I didn't catch that."};
		
		int unIndiceAlAzar = (int)Math.floor(Math.random()*muletillas.length);
		return muletillas[unIndiceAlAzar];*/
		
		return Conjunciones.getInstance().obtenerUnaConjuncion().texto();
	}
	
	public void setTextosDeLaFrase(String[] textosDeLaFrase) {
		this.textosDeLaFrase = textosDeLaFrase;
	}
	
	/*public String muletilla(){
		// Podrian vernir de un archivo gigante, que no depende del real state
		
		// Las muletillas podrian estar 
		String muletillas[] = new String[]{"Well", "now"};
		
		int unIndiceAlAzar = (int)Math.floor(Math.random()*muletillas.length);
		return muletillas[unIndiceAlAzar];
	}*/
	
}