package com.ncubo.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter 
{
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/", "/archivossubidos/**").permitAll()
		.antMatchers("/", "/conversacion/**").permitAll()
		.antMatchers("/", "/movil/login").permitAll()
		.antMatchers("/", "/movil/logout").permitAll()
		.antMatchers("/", "/movil/usuario").permitAll()
		.antMatchers("/", "/ofertas").permitAll()
		.antMatchers("/", "/ofertas/**").permitAll()
		.antMatchers("/", "/reaccion/oferta").permitAll()
		.antMatchers("/", "/resources/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
			.and()
		.logout()
			.permitAll()
			.and()
		.headers().frameOptions().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER");
	}
}