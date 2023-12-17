package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//import java.util.Set;
//
//import  org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.AuthorityUtils;
//import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping("/dashboard")
	public String dashboard() {
		
//		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		 if (auth != null) {
//          Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());
//          
//          if (roles.contains("ROLE_ADMIN")) 
//          return "redirect:/admin_dashboard";
//          
//        	 // return "redirect:/default_dashboard";
//		 }
		 return "admin_dashboard";
	}
}
