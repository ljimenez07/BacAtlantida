package com.ncubo.chatbot.watson;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ncubo.chatbot.configuracion.Constantes;

public class TextToSpeechWatson {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private TextToSpeech textService;
	private String voice;
	
	public TextToSpeechWatson(){
		textService = new TextToSpeech();
		textService.setUsernameAndPassword(Constantes.WATSON_USER_TEXT_SPEECH, Constantes.WATSON_PASS_TEXT_SPEECH);
		voice = Constantes.WATSON_VOICE_TEXT_SPEECH;
	}
	
	public InputStream getAudio(String text){
		InputStream in = null;	
		try {
	         in = textService.synthesize(text, new Voice(voice, null, null), AudioFormat.OGG).execute();
		} catch (Exception e) {
			logger.info("Got error: " + e.getMessage());
		} 
		return in;
	}
	
	public String getAudioToURL(String text) throws IOException{
		
		UUID idOne = UUID.randomUUID();
		String path = Constantes.PATH_TO_SAVE+Constantes.FOLDER_TO_SAVE+idOne+".wav";
		String publicPath = Constantes.IP_SERVER+Constantes.FOLDER_TO_SAVE+idOne+".wav";
		
		InputStream in = null;
		File directory = new File(Constantes.PATH_TO_SAVE+Constantes.FOLDER_TO_SAVE);
        directory.mkdirs();
        
		File file = null;
		file = new File(path);
		
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
		try {
	         in = textService.synthesize(text, new Voice(voice, null, null), AudioFormat.WAV).execute();
	         
	         byte[] buffer = new byte[2048];
	         int read;
	         while ((read = in.read(buffer)) != -1) {
	        	stream.write(buffer, 0, read);
	         }   
		} catch (Exception e) {
			logger.info("Got error: " + e.getMessage());
		} finally {
		    close(in);
		    stream.close();
		}
		
		return publicPath;
	}
	
	private void close(Closeable closeable) {
	    if (closeable != null) {
	        try {
	            closeable.close();
	        } catch (IOException e) {
	        	logger.info("Got error: " + e.getMessage());
	        }
	    }	      	   
	}
	
	public static void main (String[] args) throws IOException{
		
		InputStream in = null;
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
		}
	}
	
}
