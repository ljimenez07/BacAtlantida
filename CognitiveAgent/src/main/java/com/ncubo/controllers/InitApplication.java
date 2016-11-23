package com.ncubo.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.ncubo"})
public class InitApplication extends SpringBootServletInitializer 
{

	public static void main(String[] args) throws Exception 
	{
		SpringApplication.run(InitApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(InitApplication.class);
	}

}
