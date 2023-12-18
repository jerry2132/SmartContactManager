package com.example.component;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	 @Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException{
		
		String targetUrl = determineErrorMessage(exception);
		System.out.println("targe  = "+targetUrl);
		setDefaultFailureUrl(targetUrl);
		onAuthenticationFailure(request, response, exception);
		
	}

	private String determineErrorMessage(AuthenticationException exception) {
		// TODO Auto-generated method 

		if(exception instanceof BadCredentialsException) 
			return "/login?error=invalidCredentials";
		else 
			return "/login?error=notRegistered";
			
		//return "unknownError";
	}


}
