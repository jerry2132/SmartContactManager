package com.example.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.ForgotPassword;
import com.example.entity.User;
import com.example.entity.ForgotPassword;
import com.example.repository.ForgotPasswordRepository;
import com.example.repository.UserRepository;
import com.example.service.ForgotPasswordService;
import com.example.service.UserService;
import com.example.userdetails.CustomUserDetailsServiceImpl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
	  
	  @Autowired
	  private PasswordEncoder passwordEncoder;
	
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
		
		
		 //String emailLink = "http://localhost:8080/verify-Otp?email=" + email + "&token=" + forgotPassword.getToken();
		  
		
		try {
	        // Send OTP instead of email link
	        forgotPasswordService.sendOtp(user.getEmail(), "Password Reset OTP", forgotPassword.getOtp());
	    } catch (UnsupportedEncodingException | MessagingException e) {
	        model.addAttribute("error", "Error while sending OTP");
	        e.printStackTrace();
	        return "forgotPassword";
	    }
		
//		model.addAttribute("email", email);
		return "redirect:/Otp-page?success&email=" + email;
	}
	
	
	
	@GetMapping("/Otp-page")
	public String showOtpPage(@RequestParam("email")String email, Model model) {
		
//		System.out.println("otp email  " + email);
		model.addAttribute("email", email);
		return "otpPage";
	}
//	
//	@RequestMapping(value = "/verify-Otp", method = {RequestMethod.GET, RequestMethod.POST})
	
	
	@PostMapping("/verify-Otp")
	public String verifyOtp(@RequestParam("otp")String enteredOtp,@RequestParam("email")String email,
			Model model) {
		
			
			
//			UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//			//User user = userService.findByEmail(userDetails.getUsername());*******,
//		@RequestParam("email")String email
//			String email = userDetails.getUsername();
//			System.out.println("verify-otp mail"+email);
//			model.addAttribute("email", email);
////			model.addAttribute("user", user);
			
		
//		 User user = (User) model.getAttribute("user");
		 

			 if (forgotPasswordService.verifyOtp(email, enteredOtp)) {
		            return "redirect:/show-change-password?success&token=" + forgotPassword.getToken();
		        }else {
		            model.addAttribute("error", "Invalid Otp ");
		            return "otpPage";
		        }

			
		}
		
		
	@GetMapping("/show-change-password")	
	public String showChangePassword(@RequestParam("token")String token,Model model,HttpSession session) {
		
		session.setAttribute("token", token);
		
		forgotPassword = forgotPasswordRepository.findByToken(token);
		return forgotPasswordService.checkValidity(forgotPassword, model);
	}
	
	
	@PostMapping("/change-password")
	public String changePassword(HttpServletRequest request, HttpSession session, Model model) {
		
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		System.out.println("new pass  "+newPassword);
		System.out.println("confrim pass "+confirmPassword);
		
		if (!newPassword.equals(confirmPassword)) {
	        model.addAttribute("error", "Passwords do not match");
	        
	        return "newPassword";
	    }
		
	
		
		String token = (String)session.getAttribute("token");
		
		forgotPassword = forgotPasswordRepository.findByToken(token);
		User user = forgotPassword.getUser();
		System.out.println("password emial  " + user.getEmail());
		
		user.setPassword(newPassword);
		
		System.out.println("password "+user.getPassword());
		forgotPassword.setUsed(true);
		userService.save(user);
		forgotPasswordRepository.save(forgotPassword);
		
		model.addAttribute("error", "password changed successfully");
		
		
		return "login";
		
	}
	
}
