package com.example.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.ForgotPassword;
import com.example.entity.User;
import com.example.repository.ForgotPasswordRepository;
import com.example.repository.UserRepository;
import com.example.service.ForgotPasswordService;
import com.example.service.UserService;
import com.example.userdetails.CustomUserDetailsServiceImpl;

import jakarta.mail.MessagingException;

@Controller
public class ForgotPasswordController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private User user;
	
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ForgotPassword forgotPassword;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	  @Autowired
	  private ForgotPasswordRepository forgotPasswordRepository;
	
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
		
		System.out.println("email " + user.getEmail());
		forgotPassword.setExpireTime(forgotPasswordService.expireTimeRange());
		System.out.println("Expire Time "+forgotPassword.getExpireTime());
		forgotPassword.setToken(forgotPasswordService.generateToken());
		System.out.println("Token = "+forgotPassword.getToken());
		forgotPassword.setUser(user);
		forgotPassword.setOtp(forgotPasswordService.generateOtp());
		forgotPassword.setUsed(false);
		
		forgotPasswordRepository.save(forgotPassword);
		
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
	public String verifyOtp(@RequestParam("email")String email, @RequestParam("otp")String entredOtp, Model model,
			Principal principal) {
//		
//		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//		User user = userService.findByEmail(userDetails.getUsername());
//		
//		model.addAttribute("user", user);
		
		if(forgotPasswordService.verifyOtp(email, entredOtp)) {
			return "newPassword";
		}else {
			
			model.addAttribute("error", "Invalid Otp");
			return "otpPage";
		}
		
	}
	
	
	@PostMapping("/change-password")
	public String changePassword() {
		
		return "login";
		
	}
	
}
