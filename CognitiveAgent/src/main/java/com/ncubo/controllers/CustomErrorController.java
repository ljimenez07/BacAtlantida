package com.ncubo.controllers;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomErrorController extends ResponseEntityExceptionHandler  {

	

	@ExceptionHandler(Exception.class)
	 @ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody Exception exception(Exception e) 
	{
	   if( e instanceof MultipartException)
	   {
		   return new RuntimeException("Limite excedido");
	   }
	   
		return e;
	}

	
}