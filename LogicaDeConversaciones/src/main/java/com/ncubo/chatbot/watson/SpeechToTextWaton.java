package com.ncubo.chatbot.watson;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ncubo.chatbot.configuracion.Constantes;

import java.util.Iterator;
import java.util.UUID;

public class SpeechToTextWaton {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SpeechToText service;
	
	public SpeechToTextWaton(){
		service = new SpeechToText();
		service.setUsernameAndPassword(Constantes.WATSON_USER_SPEECH_TEXT, Constantes.WATSON_PASS_SPEECH_TEXT);
	}
	
	public String getText(MultipartHttpServletRequest request) throws Exception{
		
		Iterator<String> itrator = request.getFileNames();
        MultipartFile multiFile = request.getFile(itrator.next());
        File file = null;
        String resultado = "";
        
        try { // just to show that we have actually received the file
        	
        	logger.info("File Length:" + multiFile.getBytes().length);
        	logger.info("File Type:" + multiFile.getContentType());
            String fileName = multiFile.getOriginalFilename();
            logger.info("File Name:" +fileName);
            String path = request.getServletContext().getRealPath("/");
                    
            // making directories for our required path.
            byte[] bytes = multiFile.getBytes();
            File directory=    new File(path+ "/uploads");
            directory.mkdirs();
            // saving the file
            file = new File(directory.getAbsolutePath()+System.getProperty("file.separator")+UUID.randomUUID()+".wav");
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(bytes);
            stream.close();
            
            resultado = enviarAudio(file);
       
        } catch (Exception e) {
            resultado = "ERROR";
            throw new Exception("Error while loading the file");
        }
        
        return resultado;
	}
	
	public String enviarAudio(File inputstream) throws IOException, InterruptedException{
		
		String resultado = "";
		try{
			RecognizeOptions options = new RecognizeOptions.Builder()
					.continuous(true)
					/*.maxAlternatives(3)
					.inactivityTimeout(40)
					.keywords("lote", "propiedad","casa", "terreno")
					.keywordsThreshold(0.4)*/
					.contentType(HttpMediaType.AUDIO_WAV)
					.model(Constantes.WATSON_MODEL_SPEECH_TEXT) // http://www.ibm.com/watson/developercloud/doc/speech-to-text/input.shtml
					.build();
			
			SpeechResults results = service.recognize(inputstream, options).execute();
			resultado = results.getResults().get(0).getAlternatives().get(0).getTranscript();
			
			logger.info("Watson Speech Response: "+ resultado);
		}catch (Exception e) {
			logger.info(e.getMessage());
			resultado = "ERROR";
		}
		
		return resultado;
	}
	
	public String toJson(Object data)
    {
        ObjectMapper mapper=new ObjectMapper();
        StringBuilder builder=new StringBuilder();
        try {
            builder.append(mapper.writeValueAsString(data));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return builder.toString();
    }
	
	/*public static void main (String[] args) throws IOException{
		
		try{
			SpeechToTextWaton test = new SpeechToTextWaton();
			InputStream inputstream = new FileInputStream("C:\\Users\\SergioAlberto\\Documents\\Watson\\Repo\\watson\\Demos\\VentaPropiedadesSpeechToText\\src\\main\\webapp\\uploads\\test.wav");
			test.enviarAudio(inputstream);
		}catch(Exception e){
			System.out.println(""+e.getMessage());
		}
	}*/
}
