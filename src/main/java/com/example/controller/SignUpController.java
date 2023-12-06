package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.User;
import com.example.service.UserService;

@Controller
public class SignUpController {

	 	@Autowired
	    private UserService userService;
	 
	 	@RequestMapping("/signup")
	 	public String signUp(Model model) {
	 		
	 		model.addAttribute("user", new User());
	 		return "signup";
	 	}
	 	
	 	@PostMapping("/signup")
	 	public String registerUser(@ModelAttribute("user") User user,Model model) {
	 		
	 		userService.save(user);
	 		return "redirect:/signup";
	 	}
//	 	@RequestMapping("/signup")
//		public String getRegistrationPage(Model model)
//		{
//	 		model.addAttribute("user", new User());
//			return "signup";
//			
//		}
}
