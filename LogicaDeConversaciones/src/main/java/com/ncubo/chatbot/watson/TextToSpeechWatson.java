package com.ncubo.chatbot.watson;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.ftp.FTPCliente;

public class TextToSpeechWatson {
	
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private TextToSpeech textService;
	private String voice;
	private static TextToSpeechWatson textToSpeechWatson = null;
	private static String usuarioTTS = Constantes.WATSON_USER_TEXT_SPEECH;
	private static String contrasenaTTS = Constantes.WATSON_PASS_TEXT_SPEECH;
	private static String vozTTS = Constantes.WATSON_VOICE_TEXT_SPEECH;
	private static FTPCliente ftp;
	private static String pathAudios;
	
	private TextToSpeechWatson(String usuario, String contrasena, String voz, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, String path){
		usuarioTTS = usuario;
		contrasenaTTS = contrasena;
		vozTTS = voz;
		textService = new TextToSpeech();
		textService.setUsernameAndPassword(usuario, contrasena);
		voice = voz;
		pathAudios = path;
		ftp = new FTPCliente(usuarioFTP, contrasenaFTP, hostFTP, puetoFTP);
		
		try {
			FileUtils.deleteDirectory(new File(pathAudios));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(String.format("Los datos del TTS  son: %s / %s / %s. Y los datos del FTP son: %s / %s / %s / %s", usuarioTTS, contrasenaTTS, vozTTS, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP));
	}
	
	public static TextToSpeechWatson getInstance(String usuario, String contrasena, String voz, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, String pathAudios){
		if(textToSpeechWatson == null){
			textToSpeechWatson = new TextToSpeechWatson(usuario, contrasena, voz, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, pathAudios);
		}
		return textToSpeechWatson;
	}
	
	public static TextToSpeechWatson getInstance(){
		if(textToSpeechWatson == null){
			//textToSpeechWatson = new TextToSpeechWatson(usuarioTTS, contrasenaTTS, vozTTS, ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto(), pathAudios);
			throw new ChatException("No se a inicializado esta clase. Debe instanciar esta clase primero.");
		}
		return textToSpeechWatson;
	}
	
	public InputStream getAudio(String text){
		InputStream in = null;	
		try {
	         in = textService.synthesize(text, new Voice(voice, null, null), AudioFormat.WAV).execute();
		} catch (Exception e) {
			//logger.info("Got error: " + e.getMessage());
		} 
		return in;
	}
	
	public String getAudioToURL(String text, String pathAGuardar){
		
		UUID idOne = UUID.randomUUID();
		String nombreDelArchivo = idOne+".wav";
		nombreDelArchivo = nombreDelArchivo.replace("-", "");
		String path = pathAGuardar+File.separator+nombreDelArchivo;

		InputStream in = null;
		File directory = new File(pathAGuardar);
        directory.mkdirs();
        
		File file = null;
		file = new File(path);

		BufferedOutputStream stream = null;
		try {
			stream = new BufferedOutputStream(new FileOutputStream(file));
			try {
				in = textService.synthesize(text, new Voice(voice, null, null), AudioFormat.WAV).execute();
		        byte[] buffer = new byte[2048];
		        int read;
		        while ((read = in.read(buffer)) != -1) {
		        	stream.write(buffer, 0, read);
		        }
		        stream.close();
			} catch (Exception e) {
				throw new ChatException(String.format("Error debido a: %s", e.getMessage()));
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			throw new ChatException(String.format("Error debido a: %s", e1.getMessage()));
		}
		finally {
		    close(in);
		}
		try {
			ftp.subirArchivo(pathAGuardar);
			borrarDirectorio(pathAGuardar);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nombreDelArchivo;
	}
	
	public void borrarDirectorio(String direccionABorrar) throws IOException{
		FileUtils.forceDelete(new File(direccionABorrar));
	}
	
	private void close(Closeable closeable) {
	    if (closeable != null) {
	        try {
	            closeable.close();
	        } catch (IOException e) {
	        	//logger.info("Got error: " + e.getMessage());
	        }
	    }	      	   
	}
	
	public static void main (String[] args) throws IOException{
		
		/*InputStream in = null;
		FileOutputStream file = new FileOutputStream("C:\\Users\\SergioAlberto\\Documents\\SergioGQ\\Watson\\Repo\\watson\\LogicaDeConversaciones\\src\\main\\resources\\test.ogg");

		try {
	         TextToSpeech textService = new TextToSpeech();
	         textService.setUsernameAndPassword(Constantes.WATSON_USER_TEXT_SPEECH, Constantes.WATSON_PASS_TEXT_SPEECH);
	         String voice = "es-ES_EnriqueVoice";
	         String text = "Hello Sergio!";
	         in = textService.synthesize(text, new Voice(voice, null, null), AudioFormat.OGG).execute();
	         
	         byte[] buffer = new byte[2048];
	         int read;
	         while ((read = in.read(buffer)) != -1) {
	        	 file.write(buffer, 0, read);
	         }
	         System.out.println("Ready!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}*/
	}
	
}
