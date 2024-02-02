package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ForgotPasswordController {

	
	@GetMapping("/forgotPassword")
	public String showForgotPassword() {
		
		return "forgotPassword";
	}
	
	
	@PostMapping("/process-forgotPassword")
	public String processOtp() {
		
		
		
		return "otpPage";
	}
	
	@PostMapping("/verify-Otp")
	public String verifyOtp() {
		
		
		return "newPassword";
	}
	
	
	@PostMapping("/change-password")
	public String changePassword() {
		
		return "login";
		
	}
	
}
