package com.example.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.example.entity.Contact;
import com.example.entity.User;
import com.example.service.ContactService;
import com.example.service.UserService;
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
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
//	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
//	String formattedDateTime = dateFormat.format(new Date());
	
	
	
	@ModelAttribute
	public void commonDashboard(Model model, Principal principal) {
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		
		model.addAttribute("userdetails", userDetails);
		
	}
	
	
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
		
//		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//		
//		model.addAttribute("userdetails", userDetails);
		 return "admin/admin_dashboard";
	}
	
	@GetMapping("/add-contacts")
	public String addContacts(Model model) {
		
		model.addAttribute("contact", new Contact());
		return "admin/add_contacts_admin";
	}
	
	@GetMapping("/contact-status")
	public String showContactStatus() {
	    return "admin/contact-status";
	}

	
	@PostMapping("/process-contact")
	public String saveContact(@ModelAttribute Contact contact, @RequestParam("imageFile") MultipartFile file,
			Principal principal
			,RedirectAttributes redirectAttributes) {
		
		try {
			UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
	
			User user = userService.findByEmail(userDetails.getUsername());
			
			if(file.isEmpty()) {
				
				System.out.println("file is emptyt");
				
			}else {
				
				String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
	            String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
				
			contact.setImage(uniqueFilename);
				
//				 String directoryPath = "static" + File.separator + "img";
				 
				 
				
				File saveFile = new ClassPathResource("static/img").getFile();
				
//				 File saveFile = new File(directoryPath, uniqueFilename);
				 
//				 if (!saveFile.getParentFile().exists()) {
//		                saveFile.getParentFile().mkdirs();
//		            } 
				
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + uniqueFilename);
				
//				Files.createDirectories(path.getParent());
				
//				Files.createDirectories(saveFile.toPath().getParent());
				
				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				
//				file.transferTo(saveFile);
				
				 //contact.setImage(uniqueFilename);
				
				System.out.println("uploded");
			}
		
		contact.setUser(user);
		
	
			
			contactService.save(contact);
			redirectAttributes.addFlashAttribute("successMessage", "Contact saved succcessfully");
		}catch(Exception e) {
			
			 redirectAttributes.addFlashAttribute("errorMessage", "Error saving contact: " + e.getMessage());
		}
		
		//redirectAttributes.addFlashAttribute("successMessage", "Contact saved succcessfully");
		//System.out.println(contact);
		return "redirect:/admin/contact-status";
	}
}
