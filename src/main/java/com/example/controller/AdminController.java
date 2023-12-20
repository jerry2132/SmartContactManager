package com.example.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.userdetails.CustomUserDetailsServiceImpl;

//import java.util.Set;
//
//import  org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.AuthorityUtils;
//import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@RequestMapping("/dashboard")
	public String dashboard(Model model, Principal principal) {
		
//		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		 if (auth != null) {
//          Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
//          
//          if (roles.contains("ROLE_ADMIN")) 
//          return "redirect:/admin_dashboard";
//          
//        	 // return "redirect:/default_dashboard";
//		 }
		
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		
		model.addAttribute("userdetails", userDetails);
		 return "admin_dashboard";
	}
}
