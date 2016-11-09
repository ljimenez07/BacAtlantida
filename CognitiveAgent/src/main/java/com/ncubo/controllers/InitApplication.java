package com.ncubo.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.ncubo.conf.AgenteCognitivo;

@SpringBootApplication(scanBasePackages = {"com.ncubo"})
public class InitApplication
{
	public static void main(String[] args) throws Exception 
	{
		SpringApplication.run(InitApplication.class, args);
	}
}
