package com.ncubo.chatbot.ui;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controlador{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value="/" , method = RequestMethod.GET)
	public @ResponseBody String iniciar() throws IOException {
		return "Hola";
	}
	
}
