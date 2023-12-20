package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class BaseController {

	@RequestMapping("/")
	public String basePage() {
		
		return "base";
	}
	
	@RequestMapping("/base")
	public String base() {
		
		return "base";
	}
	
}
