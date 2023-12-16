package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller

public class UserController {

	@RequestMapping("/user/user_dashboard")
	public String dashboard() {
		
		
		return "user_dashboard";
	}
	
	
}
