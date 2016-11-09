package com.ncubo.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.ncubo"})
public class InitApplication
{
	public static void main(String[] args) throws Exception 
	{
		SpringApplication.run(InitApplication.class, args);
	}
}
