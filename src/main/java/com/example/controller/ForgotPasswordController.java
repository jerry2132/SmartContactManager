package com.example.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.ForgotPassword;
import com.example.entity.User;
import com.example.service.ForgotPasswordService;
import com.example.service.UserService;

import jakarta.mail.MessagingException;

@Controller
public class ForgotPasswordController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private User user;
	
	@Autowired
	private ForgotPassword forgotPassword;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@GetMapping("/forgotPassword")
	public String showForgotPassword() {
		
		return "forgotPassword";
	}
	
	
	@PostMapping("/process-forgotPassword")
	public String processOtp(@RequestParam("email")String email,Model model) {
		
		User user = userService.findByEmail(email);
		
		if(user==null) {
			
			model.addAttribute("error", "email is not registered");
			return "forgotPassword";
		}
		
		
		forgotPassword.setExpireTime(forgotPasswordService.expireTimeRange());
		forgotPassword.setToken(forgotPasswordService.generateToken());
		forgotPassword.setUser(user);
		forgotPassword.setOtp(forgotPasswordService.generateOtp());
		forgotPassword.setUsed(false);
		
		try {
	        // Send OTP instead of email link
	        forgotPasswordService.sendOtp(user.getEmail(), "Password Reset OTP", forgotPassword.getOtp());
	    } catch (UnsupportedEncodingException | MessagingException e) {
	        model.addAttribute("error", "Error while sending OTP");
	        e.printStackTrace();
	        return "forgotPassword";
	    }
		
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
