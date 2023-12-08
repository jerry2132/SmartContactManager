package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Controller
public class LoginController {
	
	@Autowired
    private UserService userService;
 	
 	@Autowired
 	private UserRepository userRepository;
 	
 	@RequestMapping("/login")
 	public String login(Model model) {
 		
 		model.addAttribute("user", new User());
 		return "login";
 	}

}
