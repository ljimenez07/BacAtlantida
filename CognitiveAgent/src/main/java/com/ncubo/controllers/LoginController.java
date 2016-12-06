package com.ncubo.controllers;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController
{
	@RequestMapping("/login")
	public String login( Model model) throws ClassNotFoundException, SQLException
	{
		return "login";
	}
	
	@RequestMapping("/logout")
	public String logout( Model model) throws ClassNotFoundException, SQLException
	{
		return "login";
	}
}
