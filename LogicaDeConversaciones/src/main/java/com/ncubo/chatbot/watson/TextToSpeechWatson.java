package com.ncubo.chatbot.watson;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.ftp.FTPServidor;

public class TextToSpeechWatson {
	
	//private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private TextToSpeech textService;
	private String voice;
	private static TextToSpeechWatson textToSpeechWatson = null;
	private static String usuarioTTS = Constantes.WATSON_USER_TEXT_SPEECH;
	private static String contrasenaTTS = Constantes.WATSON_PASS_TEXT_SPEECH;
	private static String vozTTS = Constantes.WATSON_VOICE_TEXT_SPEECH;
	private static FTPServidor ftp;
	
	private TextToSpeechWatson(String usuario, String contrasena, String voz, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP){
		usuarioTTS = usuario;
		contrasenaTTS = contrasena;
		vozTTS = voz;
		textService = new TextToSpeech();
		textService.setUsernameAndPassword(usuario, contrasena);
		voice = voz;
		ftp = new FTPServidor(usuarioFTP, contrasenaFTP, hostFTP, puetoFTP);
	}
	
	public static TextToSpeechWatson getInstance(String usuario, String contrasena, String voz, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP){
		if(textToSpeechWatson == null){
			textToSpeechWatson = new TextToSpeechWatson(usuario, contrasena, voz, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP);
		}
		return textToSpeechWatson;
	}
	
	public static TextToSpeechWatson getInstance(){
		if(textToSpeechWatson == null){
			textToSpeechWatson = new TextToSpeechWatson(usuarioTTS, contrasenaTTS, vozTTS, ftp.getUsuario(), ftp.getPassword(), ftp.getHost(), ftp.getPuerto());
		}
		return textToSpeechWatson;
	}
	
	public InputStream getAudio(String text){
		InputStream in = null;	
		try {
	         in = textService.synthesize(text, new Voice(voice, null, null), AudioFormat.OGG).execute();
		} catch (Exception e) {
			//logger.info("Got error: " + e.getMessage());
		} 
		return in;
	}
	
	public String getAudioToURL(String text, String pathAGuardar){
		
		UUID idOne = UUID.randomUUID();
		String nombreDelArchivo = idOne+".wav";
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
		
		return nombreDelArchivo;
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
		System.out.println(""+(0b001 & 0b010));
		System.out.println(""+(0b010 & 0b010));
		System.out.println(""+(0b011 & 0b010));
		System.out.println(""+(0b100 & 0b010));
		System.out.println(""+(0b101 & 0b010));
	}
	
}
