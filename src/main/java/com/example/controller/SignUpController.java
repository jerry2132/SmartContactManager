package com.example.controller;

import java.io.File;
import java.io.IOException;
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
	 			user.setImgUrl("contact.png");
	 		}else {
	 			
	 			String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
	            String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
				
	            user.setImgUrl(uniqueFilename);
	            
//	            File saveFile = new ClassPathResource("static/registeredImage").getFile();
//	            
//	            String uploadDirectory = "/src/main/resources/static/registeredImage/";
//	            Path path = Paths.get(uploadDirectory,uniqueFilename);
//	            
//	            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + uniqueFilename);
	            
//	            Files.createDirectories(path.getParent());
	            
//	            Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
//	            
//	            System.out.println(path);
//	 		}
	 		
	            String uploadDirectory;
				try {
					
					if (System.getProperty("user.dir").contains("Intellij Projects")) {
					    uploadDirectory = "src/main/resources/static/registeredImage/";
					}
					else {
						ClassPathResource classPathResource = new ClassPathResource("static/img/");
						uploadDirectory = classPathResource.getFile().getAbsolutePath();
					}
					
					Path path = Paths.get(uploadDirectory, uniqueFilename);
					Files.createDirectories(path.getParent());
					System.out.println(path.toAbsolutePath());
					Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				}
	 		
	 		userService.save(user);
//	 		model.addAttribute("message", "user added successfully");
	 		redirectAttributes.addFlashAttribute("successMessage", "User saved succcessfully");
	 		
	 	}catch(Exception e) {
	 		
	 		redirectAttributes.addFlashAttribute("errorMessage", "Error saving User "+e.getMessage());
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
