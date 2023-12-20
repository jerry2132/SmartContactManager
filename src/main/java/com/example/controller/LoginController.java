package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
//import java.security.Principal;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
	
	@Autowired
    private UserService userService;
 	
//	@Autowired
//    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
//	
// 	@Autowired
// 	private UserRepository userRepository;
 	
 	@RequestMapping("/login")
 	public String login(Model model, @RequestParam(name = "error", required=false) String error) {		
		
 		model.addAttribute("user", new User());
 		if (error != null) {
            if (error.equals("error")) {
                model.addAttribute("error", "Username doesnot exist or password is incorrect");
           
        }
 		}
 		System.out.println("Error parameter value: " + error);
 		
 		return "login";
 	}
 	
 	@RequestMapping("/default_dashboard")
 	public String default1() {
 		
 		return "default_dashboard";
 	}
 	
// 	@ExceptionHandler(UsernameNotFoundException.class)
//    public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model) {
//        model.addAttribute("error", ex.getMessage());
//        return "login";
//    }

// 	@GetMapping("/default")
//    public String defaultAfterLogin(HttpServletRequest request) {
//        if (request.isUserInRole("USER")) {
//            return "redirect:/user/user_dashboard";
//        } else if (request.isUserInRole("ADMIN")) {
//            return "redirect:/admin/admin_dashboard";
//        } else {
//            // Handle other roles or situations
//            return "redirect:/default_dashboard";
//        }
//    }
}
