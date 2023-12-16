package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	@RequestMapping("/admin/admin_dasboard")
	public String dashboard() {
		
		return "admin_dashboard";
	}
}
