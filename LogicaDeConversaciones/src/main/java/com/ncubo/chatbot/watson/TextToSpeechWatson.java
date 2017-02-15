package com.ncubo.chatbot.watson;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ncubo.caches.CacheDeAudios;
import com.ncubo.chatbot.configuracion.Constantes;
import com.ncubo.chatbot.exceptiones.ChatException;
import com.ncubo.ftp.FTPCliente;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.CustomFFMPEGLocator;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;

public class TextToSpeechWatson
{
	private TextToSpeech textService;
	private String voice;
	private static TextToSpeechWatson textToSpeechWatson = null;
	private String usuarioTTS = Constantes.WATSON_USER_TEXT_SPEECH;
	private String contrasenaTTS = Constantes.WATSON_PASS_TEXT_SPEECH;
	private String vozTTS = Constantes.WATSON_VOICE_TEXT_SPEECH;
	private FTPCliente ftp;
	private String pathAudios;
	private String urlPublicaAudios;
	
	private TextToSpeechWatson(String usuario, String contrasena, String voz, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, String carpeta, String path, String urlPublicaAudios)
	{
		this.usuarioTTS = usuario;
		this.contrasenaTTS = contrasena;
		this.vozTTS = voz;
		this.textService = new TextToSpeech();
		this.textService.setUsernameAndPassword(usuario, contrasena);
		this.voice = voz;
		this.pathAudios = path;
		this.urlPublicaAudios = urlPublicaAudios;
		this.ftp = new FTPCliente(usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, carpeta);
		
		System.out.println(String.format("Los datos del TTS  son: %s / %s / %s. Y los datos del FTP son: %s / %s / %s / %s", usuarioTTS, contrasenaTTS, vozTTS, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP));
	}
	
	public static TextToSpeechWatson getInstance(String usuario, String contrasena, String voz, String usuarioFTP, String contrasenaFTP, String hostFTP, int puetoFTP, String carpeta, String pathAudios, String urlPublicaAudios)
	{
		if(textToSpeechWatson == null)
		{
			textToSpeechWatson = new TextToSpeechWatson(usuario, contrasena, voz, usuarioFTP, contrasenaFTP, hostFTP, puetoFTP, carpeta, pathAudios, urlPublicaAudios);
		}
		return textToSpeechWatson;
	}
	
	public static TextToSpeechWatson getInstance(){
		
		if(textToSpeechWatson == null){
			throw new ChatException("No se a inicializado esta clase. Debe instanciar esta clase primero.");
		}
		
		return textToSpeechWatson;
	}

	public String getAudioToURL(String text, boolean esAudioDinamico){
		
		UUID idOne = UUID.randomUUID();
		String nombreDelArchivo = idOne + ".mp3";
		nombreDelArchivo = nombreDelArchivo.replace("-", "");
		
		String pathFinal = this.pathAudios + "/" + nombreDelArchivo;
		
		InputStream in = null;
		AudioFormat audioFormat = AudioFormat.WAV;
		File temp = new File(System.getProperty("user.home") + "/audios-de-watson");
		if(!temp.exists()){
			temp.mkdir();
			temp.deleteOnExit();
		}
		
		try{
			System.out.println("Generando audio al texto con Watson: " + text);
			
			in = generarAudioConWatson(text, audioFormat);
			if(in != null){
				InputStream mp3InputStream = transformarAMp3(in, audioFormat.name().toLowerCase(), temp);
				if(mp3InputStream != null){
					transferirAudiosAlFTP(esAudioDinamico, pathFinal, mp3InputStream);
				}else{
					System.out.println(String.format("No se pudo convertir el audio %s a mp3", nombreDelArchivo));
				}
			}else{
				System.out.println(String.format("No se pudo generar el audio %s con watson", nombreDelArchivo));
			}
		}catch(Exception e1){
			System.out.println(String.format("Error '%s' al generar y transferir audio del text '%s'. Se va a volver a intentar", e1.getStackTrace(), text));
			
			if(in != null)
				in = generarAudioConWatson(text, audioFormat);
			
			if(in != null){
				InputStream mp3InputStream = transformarAMp3(in, audioFormat.name().toLowerCase(), temp);
				if(mp3InputStream != null){
					try {
						transferirAudiosAlFTP(esAudioDinamico, pathFinal, mp3InputStream);
					} catch (IOException e) {
						System.out.println(String.format("Error al transferir audio %s del text '%s'.", nombreDelArchivo, text));
						e.printStackTrace();
					}
				}else{
					System.out.println(String.format("No se pudo convertir el audio %s a mp3", nombreDelArchivo));
				}
			}else{
				System.out.println(String.format("No se pudo generar el audio %s con watson", nombreDelArchivo));
			}
		}

		return nombreDelArchivo;
	}
	
	private InputStream generarAudioConWatson(String text, AudioFormat audioFormat){
		InputStream audio = null;
		
		audio = textService.synthesize(text, new Voice(voice, null, null), audioFormat).execute();
		if(audio == null){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Error al generar audio con watson. Se va a volver intentar");
			audio = textService.synthesize(text, new Voice(voice, null, null), audioFormat).execute();
			if(audio == null){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Error al generar audio con watson. Se va a volver intentar");
				audio = textService.synthesize(text, new Voice(voice, null, null), audioFormat).execute();
				if(audio == null){
					System.out.println("Error al generar audio con watson, usando el texto: "+text);
				}
			}
		}
		
		return audio;
	}
	
	public String getAudioToURL(String text, boolean esAudioDinamico, String pathToTransferirAlFTP, String fileName) // De las ofertas
	{
		String nombreDelArchivo = fileName + ".mp3";
		String pathFinal = pathToTransferirAlFTP + "/" + nombreDelArchivo;
		
		InputStream in = null;
		AudioFormat audioFormat = AudioFormat.WAV;
		File temp = new File(System.getProperty("user.home") + "/audios-de-watson");
		if(!temp.exists()){
			temp.mkdir();
			temp.deleteOnExit();
		}
		
		try{
			System.out.println("Generando audio al texto con Watson: " + text);
			in = generarAudioConWatson(text, audioFormat);
			if(in != null){
				InputStream mp3InputStream = transformarAMp3(in, audioFormat.name().toLowerCase(), temp);
				if(mp3InputStream != null){
					transferirAudiosAlFTP(esAudioDinamico, pathFinal, mp3InputStream);
				}else{
					System.out.println(String.format("No se pudo convertir el audio %s a mp3", nombreDelArchivo));
				}
			}else{
				System.out.println(String.format("No se pudo generar el audio %s con watson", nombreDelArchivo));
			}
		} catch(Exception e1){
			System.out.println(String.format("Error al generar y transferir audio del text '%s', se va a volver a intentar", text));
			in = generarAudioConWatson(text, audioFormat);
			if(in != null){
				InputStream mp3InputStream = transformarAMp3(in, audioFormat.name().toLowerCase(), temp);
				if(mp3InputStream != null){
					try {
						transferirAudiosAlFTP(esAudioDinamico, pathFinal, mp3InputStream);
					} catch (IOException e) {
						System.out.println(String.format("Error al transferir audio %s del text '%s'.", nombreDelArchivo, text));
					}
				}else{
					System.out.println(String.format("No se pudo convertir el audio %s a mp3", nombreDelArchivo));
				}
			}else{
				System.out.println(String.format("No se pudo generar el audio %s con watson", nombreDelArchivo));
			}
		}
		
		return nombreDelArchivo;
	}
	
	private InputStream transformarAMp3(InputStream in, String extensionOriginal, File parentDirectory)
	{
		File archivoMp3 = new File(parentDirectory, "Watson.mp3");
		try {
			archivoMp3.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		File archivoOriginal = transformarInputStreamAFile(in, extensionOriginal, parentDirectory);
		
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(128000));
		audio.setChannels(new Integer(2));
		audio.setSamplingRate(new Integer(44100));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder(new CustomFFMPEGLocator());
		
		try {
			encoder.encode(archivoOriginal, archivoMp3, attrs);
		} catch (IllegalArgumentException | EncoderException e) {
			e.printStackTrace();
		}
		
		return transformarFileAInputStream(archivoMp3);
	}
	
	private File transformarInputStreamAFile(InputStream inputStream, String extensionOriginal, File parentDirectory)
	{
		OutputStream outputStream = null;
		File file = new File(parentDirectory, "Watson." + extensionOriginal);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try
		{
			outputStream = new FileOutputStream(file);
			
			int read = 0;
			byte[] bytes = new byte[2048];
			
			while((read = inputStream.read(bytes)) != -1)
			{
				outputStream.write(bytes, 0, read);
			}
		} catch(IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if(inputStream != null)
			{
				try
				{
					inputStream.close();
				} catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(outputStream != null)
			{
				try
				{
					outputStream.close();
				} catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return file;
	}
	
	private InputStream transformarFileAInputStream(File archivo){
		
		InputStream inputStream = null;
		
		try{
			inputStream = new FileInputStream(archivo.getAbsolutePath());
		}catch(IOException e){
			System.out.println("Error al transformar file a Input Stream: "+e.getStackTrace().toString());
		}
		
		return inputStream;
	}
	
	private void transferirAudiosAlFTP(boolean esAudioDinamico, String pathFinal, InputStream in) throws IOException
	{
		if(in != null){
			if(esAudioDinamico){
				CacheDeAudios.agregar(pathFinal, IOUtils.toByteArray(in)); // Importantes xq el audio se esta subiendo por hilo (si se consulta y no esta en el ftp, por lo menos esta en cache)
				ftp.subirUnArchivoPorHilo(in, pathFinal);
			}
			else{
				ftp.subirUnArchivo(in, pathFinal);
				close(in);
			}
		}
	}
	
	public String obtenerUrlPublicaDeAudios()
	{
		return urlPublicaAudios;
	}
	
	private void close(Closeable closeable)
	{
		if(closeable != null){
			try{
				closeable.close();
			} catch(IOException e){}
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		
		/*
		 * InputStream in = null; FileOutputStream file = new FileOutputStream(
		 * "C:\\Users\\SergioAlberto\\Documents\\SergioGQ\\Watson\\Repo\\watson\\LogicaDeConversaciones\\src\\main\\resources\\test.wav"
		 * );
		 * 
		 * try { TextToSpeech textService = new TextToSpeech();
		 * textService.setUsernameAndPassword(Constantes.
		 * WATSON_USER_TEXT_SPEECH, Constantes.WATSON_PASS_TEXT_SPEECH); String
		 * voice = "es-ES_EnriqueVoice"; String text = "Hello Sergio!"; in =
		 * textService.synthesize(text, new Voice(voice, null, null),
		 * AudioFormat.OGG).execute();
		 * 
		 * byte[] buffer = new byte[2048]; int read; while ((read =
		 * in.read(buffer)) != -1) { file.write(buffer, 0, read); }
		 * System.out.println("Ready!"); } catch (Exception e) {
		 * System.out.println(e.getMessage()); }
		 */
	}
	
}
