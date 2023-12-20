package com.example.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.userdetails.CustomUserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model,Principal principal) {
		
		
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		 if (auth != null) {
//         Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
//         
//         if (roles.contains("ROLE_USER")) 
//         return "redirect:/user_dashboard";
//         
//	
//       }
		
		//String userName = principal.getName();
		//System.out.println(userName);
		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		model.addAttribute("userdetails", userDetails);
		 return "user_dashboard";
	}
	
		
}
