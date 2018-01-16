package com.ncubo.cas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthFilter implements Filter
{

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    		throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String tokenArg = servletRequest.getParameter("token");
        if (tokenArg != null) {
            JwtAuthToken token = new JwtAuthToken(tokenArg);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        chain.doFilter(request, response);
    }

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// TODO Auto-generated method stub
		
	}
	
}
