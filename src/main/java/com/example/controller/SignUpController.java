package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.User;

@Controller
public class SignUpController {

	@RequestMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public String registerUser(@ModelAttribute("user") User user,@RequestParam(value = "acceptTerms" , defaultValue = "false") boolean acceptTerms, Model model) {
		
		
	System.out.println("Agreement " +acceptTerms);
	System.out.println("User "+user);
	return "signUp";
	
}
}
