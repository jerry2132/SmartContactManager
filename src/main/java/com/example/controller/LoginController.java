package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 	
	
 	@Autowired
 	private UserRepository userRepository;
 	
 	@RequestMapping("/login")
 	public String login(Model model, @RequestParam(name = "error", required=false) String error) {
 		
 		model.addAttribute("user", new User());
// 		if (error != null && !error.isEmpty()) {
//            model.addAttribute("error", "Invalid username or password");
//        }
 		return "login";
 	}
 	
 	@RequestMapping("/default_dashboard")
 	public String default1() {
 		
 		return "default_dashboard";
 	}

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
