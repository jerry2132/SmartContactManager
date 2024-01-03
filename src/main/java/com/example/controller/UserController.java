package com.example.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Contact;
import com.example.entity.User;
import com.example.repository.ContactRepository;
import com.example.service.ContactService;
import com.example.service.UserService;
import com.example.userdetails.CustomUserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
	@ModelAttribute
	public void commonDashboard(Model model,Principal principal) {
		
		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		model.addAttribute("userdetails", userDetails);
	}
	
	@RequestMapping("/dashboard")
	public String dashboard() {
		
		
		
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
//		UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//		model.addAttribute("userdetails", userDetails);
		 return "user/user_dashboard";
	}
	
	@RequestMapping("/contact-status")
	public String showContactStatus() {
		
		return "user/contact-status";
	}
	
	@GetMapping("/add-contacts")
	public String addContacts(Model model) {
		
		model.addAttribute("contact", new Contact());
		return "user/add_contacts_user";
	}
		
		@PostMapping("process-contact")
		public String saveContact(@ModelAttribute Contact contact,@RequestParam("imageFile") MultipartFile file,
				Principal principal
				,RedirectAttributes redirectAttributes) {
			
			try {	
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
			if(file.isEmpty()) {
				
				System.out.println("file is emptyt");
				
			}else {
				
				String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
	            String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
				
			contact.setImage(uniqueFilename);
			
			File saveFile = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + uniqueFilename);
			
			Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("upoaded");
			
			}
			
			contact.setUser(user);
			
			
				contactService.save(contact);
				redirectAttributes.addFlashAttribute("successMessage", "Contact saved succcessfully");
				
			}catch(Exception e){
				
				redirectAttributes.addFlashAttribute("successMessage", "error while saving contact,try again  "+ e.getMessage());
			}
			
			return "redirect:/user/contact-status";
		}
		
		@GetMapping("/view-contacts/{page}")
		public String viewContacts(@PathVariable("page") Integer page ,Model model,Principal principal) {
			
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
			Pageable pageable = PageRequest.of(page, 5);
			
			Page<Contact> allContact = contactRepository.findContactsByUser(user, pageable);
			
			model.addAttribute("contacts" , allContact);
			model.addAttribute("currentPage" , page);
			model.addAttribute("totalPages",allContact.getTotalPages());
			
//			Pageable pageable = PageRequest.of(0, 5);
//			Page<Contact> contactPage = contactRepository.findByUser(user, pageable);
			//List<Contact> contacts = user.getContacts();
//			contactRepository.findAll(PageRequest.of(0, 5));
//			model.addAttribute("contacts", contactPage.getContent());
//			model.addAttribute("curentPage" , page);
//			model.addAttribute("totalPages",contactPage.getTotalPages());
//			
			
			
			return "user/view-contacts";
		}
}
