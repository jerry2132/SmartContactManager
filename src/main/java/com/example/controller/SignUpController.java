package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Controller
public class SignUpController {

	 	@Autowired
	    private UserService userService;
	 	
	 	@Autowired
	 	private UserRepository userRepository;
	 
	 	@RequestMapping("/signup")
	 	public String signUp(Model model) {
	 		
	 		model.addAttribute("user", new User());
	 		return "signup";
	 	}
	 	
	 	@PostMapping("/signup")
	 	public String registerUser(@ModelAttribute("user") User user,RedirectAttributes redirectAttributes) {
	 		
	 		Optional<User> finduser = userRepository.findUserByEmail(user.getEmail());
	 		
	 		if(finduser.isPresent()) {
	 			
//	 			model.addAttribute("message", "user already present");
	 			
	 			redirectAttributes.addFlashAttribute("errorMessage", "User already present");
	 			
	 			return"redirect:/signup";
	 		}
	 		
	 		userService.save(user);
//	 		model.addAttribute("message", "user added successfully");
	 		redirectAttributes.addFlashAttribute("successMessage", "User saved succcessfully");
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
