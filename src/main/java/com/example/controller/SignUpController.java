package com.example.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import jakarta.validation.Valid;

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
	 	public String registerUser(@Valid @ModelAttribute("user") User user,@RequestParam("imageUrl")MultipartFile file,
	 			BindingResult bindingresult
	 			,RedirectAttributes redirectAttributes) {
	 		
	 	try {
	 		
	 			User finduser = userRepository.findUserByEmail(user.getEmail());
	 		
	 		if(bindingresult.hasErrors()) {
	 			
	 			return "signup";
	 		}
	 		
	 		if(finduser != null) {
	 			
//	 			model.addAttribute("message", "user already present");
	 			
	 			redirectAttributes.addFlashAttribute("errorMessage", "User already present");
	 			
	 			return "redirect:/signup";
	 		}
	 		
	 		if(file.isEmpty()) {
	 			
	 			System.out.println("File is empty");
	 		}else {
	 			
	 			String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
	            String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
				
	            user.setImgUrl(uniqueFilename);
	            
	            File saveFile = new ClassPathResource("static/registeredImage").getFile();
	            
	            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + uniqueFilename);
	            
	            Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
	            
	            System.out.println("File uploaded");
	 		}
	 		
	 		
	 		userService.save(user);
//	 		model.addAttribute("message", "user added successfully");
	 		redirectAttributes.addFlashAttribute("successMessage", "User saved succcessfully");
	 		
	 	}catch(Exception e) {
	 		
	 		redirectAttributes.addFlashAttribute("errorMessage", "User saved succcessfully"+e.getMessage());
	 	}
	 		
	 		
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
