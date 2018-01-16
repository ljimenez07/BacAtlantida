package com.ncubo.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.ncubo.cas.JwtAuthFilter;
//import com.ncubo.cas.JwtAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter 
{
//    @Autowired
//    private JwtAuthFilter jwtAuthFilter;
//    
//	@Autowired
//    private JwtAuthenticationProvider jwtAuthenticationProvider;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
		.csrf().disable() 
		.authorizeRequests()
		.antMatchers("/", "/archivossubidos/**").permitAll()
		.antMatchers("/", "/BackOffice/**").hasRole("USER")
		.antMatchers("/", "/activarToken/**").permitAll()
		.antMatchers("/", "/index.html").permitAll()
		.antMatchers("/", "/movil/**").permitAll()
		.antMatchers("/", "/conversacion/**").permitAll()
		.antMatchers("/", "/ofertas/**").permitAll()
		.antMatchers("/", "/ofertas").permitAll()
		.antMatchers("/", "/reaccion/**").permitAll()
		.antMatchers("/","/css/**").permitAll()
		.antMatchers("/","/bambu/**").permitAll()
		.antMatchers("/","/js/**").permitAll()
		.antMatchers("/","/img/**").permitAll()
		.antMatchers("/","/imagenes/**").permitAll()
		.antMatchers("/","/archivos/**").permitAll()
		.antMatchers("/","/fonts/**").permitAll()
		.anyRequest().authenticated()
		.and()
		//.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
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
//		auth.authenticationProvider(jwtAuthenticationProvider);
//		
		auth.inMemoryAuthentication()
				.withUser("user")
				.password("password")
				.roles("USER");
	}
}