package com.example.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Contact;
import com.example.entity.User;
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
		public String saveContact(@ModelAttribute Contact contact, Principal principal
				,RedirectAttributes redirectAttributes) {
			
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
			contact.setUser(user);
			
			try {
				contactService.save(contact);
				redirectAttributes.addFlashAttribute("successMessage", "Contact saved succcessfully");
				
			}catch(Exception e){
				
				redirectAttributes.addFlashAttribute("successMessage", "error while saving contact,try again  "+ e.getMessage());
			}
			
			return "redirect:/user/contact-status";
		}
}
