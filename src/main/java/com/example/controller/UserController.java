package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import com.example.repository.UserRepository;
import com.example.service.ContactService;
import com.example.service.UserService;
import com.example.userdetails.CustomUserDetailsServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
	
	@Autowired
	private UserRepository userRepository;
	
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
		model.addAttribute("addContact", true);
		return "user/add_contacts_user";
	}
		
		@PostMapping("process-contact")
		public String saveContact(@ModelAttribute Contact contact,@RequestParam("imageFile") MultipartFile file,
				Principal principal
				,RedirectAttributes redirectAttributes) {
			
			
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
			if(file.isEmpty()) {
				
				System.out.println("file is emptyt");
				contact.setImage("rec.png");
				
			}else {
				
				String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
	            String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
				
			contact.setImage(uniqueFilename);
			
//			File saveFile = new ClassPathResource("static/img").getFile();
			
//			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + uniqueFilename);
			
//			
//			 String uploadDirectory = "src" + File.separator + "main" + File.separator + "resources" + File.separator + 
//					 "static" + File.separator + "img" + File.separator;
			
			
			String uploadDirectory;
			try {
				
				if (System.getProperty("user.dir").contains("Intellij Projects")) {
				    uploadDirectory = "src/main/resources/static/img/";
				}
				else {
					ClassPathResource classPathResource = new ClassPathResource("static/img/");
					uploadDirectory = classPathResource.getFile().getAbsolutePath();
				}
				
				Path path = Paths.get(uploadDirectory, uniqueFilename);
				Files.createDirectories(path.getParent());
				//System.out.println(path.toAbsolutePath());
				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
//			Files.createDirectories(path.getParent());
			
			
			
//			try {
//			
//			
//			System.out.println("upoaded");
//			}catch(Exception e) {
//				
//			redirectAttributes.addFlashAttribute("successMessage","error"+e.getMessage());
//			}
//			
//			}
			
			contact.setUser(user);
			try {
				
			
			
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
			//System.out.println(pageable);
			Page<Contact> allContact = contactRepository.findContactsByUser(user, pageable);
			
			model.addAttribute("contacts" , allContact);
			model.addAttribute("currentPage" , page);
			model.addAttribute("totalPages",allContact.getTotalPages());
			
			//System.out.println(allContact.getTotalPages());
			
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
		
		
		// Add this method to your controller
		@GetMapping("/details/{cid}")
		public String showIndividualContactDetails(@PathVariable("cid") int contactId, Model model, Principal principal) {
		    // Fetch contact details by ID and add them to the model
			
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
		    Optional<Contact> contactOptional = contactRepository.findById(contactId);
		    Contact contact = contactOptional.get();
		    
		    if(user.getId() == contact.getUser().getId())
		    	model.addAttribute("contacts",contact);
		    
		    
		    return "user/details";
		}
		
		@GetMapping("/delete/{cid}")
		public String deleteContact(@PathVariable("cid")Integer contactId,Model model,Principal principal,RedirectAttributes redirectAttributes) {
			
//			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//			User user = userService.findByEmail(userDetails.getUsername());
			
//			 Optional<Contact> contactOptional = contactRepository.findById(contactId);
//			 
//			 if(contactOptional.isPresent()) {
//				 
//				 Contact contact = contactOptional.get();
//				    
//				 contact.setUser(null);
//				 
//				 contactRepository.save(contact);
//				    
//				    contactRepository.deleteById(contact.getCid());
//				    
//				    
//			    redirectAttributes.addFlashAttribute("message","Contact deleted successfully");
//				    
//				    session.setAttribute("message", "contact deleted sucess");
				    
//				    model.addAttribute("message", "deleredd");
//			 }
			
			try {
				
				contactService.deleteContact(contactId);
				redirectAttributes.addFlashAttribute("message","Contact deleted successfully");
				
			}catch(Exception e) {
				
				redirectAttributes.addFlashAttribute("message","Contact not found");
			}
			
			
			    return "redirect:/user/view-contacts/0";
		}

		
		@GetMapping("/update-contact/{cid}")
		public String updateContact(@PathVariable("cid")Integer contactId,Model model,Principal principal) {
			
			UserDetails userDetails = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
			Contact contact = contactRepository.findById(contactId).get();
			model.addAttribute("addContact", false);
			
			if(user.getId() == contact.getUser().getId())
				model.addAttribute("contact", contact);
			
			return "user/add_contacts_user";
		}
		
		
		@PostMapping("/process-update/{cid}")
		public String processContactUpdate(@PathVariable("cid")Integer contactId,@ModelAttribute Contact contact,
				@RequestParam("imageFile") MultipartFile file,Model model ,Principal principal,
				RedirectAttributes redirectAttributes) {
			
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		User user = userService.findByEmail(userDetails.getUsername());
		
			Optional<Contact> existingContactOptional = contactRepository.findById(contactId);
			Contact existingContact = existingContactOptional.get();
			
			
		try {
			
			if(!file.isEmpty()) {
				
				String newName = contactService.updateImage(existingContact, file);
				contact.setImage(newName);
			}
				
				
			else
				contact.setImage(existingContact.getImage());
			
			contact.setUser(user);
			contactRepository.save(contact);
			redirectAttributes.addFlashAttribute("successMessage", "Contact updated succcessfully");
		}catch(Exception e ) {
			
			redirectAttributes.addFlashAttribute("errorMessage", "Error saving contact: "+e.getMessage());
		}
		
//		System.out.println("Id =  	"+contact.getCid());
			
			
		return "redirect:/user/contact-status";
		}
		
		@GetMapping("/profile")
		public String profile(Model model,Principal principal) {
			
			UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
			User user = userService.findByEmail(userDetails.getUsername());
			
			model.addAttribute("user",user);
			
			return "user/profile";
		}
		
		@GetMapping("/deleteUser/{id}")
		public String deleteUser(@PathVariable("id")Integer userId,Model model,RedirectAttributes redirectAttributes) {
			
			try {
				
				userService.deleteUser(userId);
				redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
			}catch(Exception e) {
				
				redirectAttributes.addFlashAttribute("errorMessage", "contact not found");
			}
			
			return "redirect:/signup";
			
		}
		
		@GetMapping("/update-user/{id}")
		public String updateUser(@PathVariable("id")Integer userId,Model model,Principal principal) {
			
			//model.addAttribute("updateUser", false);

			User authenticatedUser = userRepository.findUserByEmail(principal.getName());
			
			if(authenticatedUser != null) {
				
				if(authenticatedUser.getId() == userId) {
					
						Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
						
						 boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
						 	 Optional<User> userOptional = userRepository.findById(userId);
						 	 
						 	 if(userOptional.isPresent()) {
						 		 
						 		User user = userOptional.get();
							 	model.addAttribute("isAdmin", isAdmin);
								 
								 model.addAttribute("updateUser", false);
								
									 model.addAttribute("user", user);
									 return "signup";
						 	 }else
						 		 return "redirect:/error";
						 		
						 
					
					
				}else
					return "redirect:/error";
			}else
				return "redirect:/error";
		}
		
		@PostMapping("/process-update-user/{id}")
	    public String processUpdateUser(@PathVariable("id") Integer userId,@ModelAttribute User updatedUser,
	    		@RequestParam("imageUrl")MultipartFile file,Principal principal,RedirectAttributes redirectAttributes) {
	        // Your logic for updating the user in the admin context
			
			
//			UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//			User user1 = userService.findByEmail(userDetails.getUsername());
//				
			Optional<User> existingUserOptional = userRepository.findById(userId);
			
			
			if(existingUserOptional.isPresent()) {
				
					User existingUser = existingUserOptional.get();
						System.out.println(updatedUser.getName()+existingUser.getName());
					if(updatedUser.getName() != null)
						existingUser.setName(updatedUser.getName());
					if(updatedUser.getEmail() != null)
						existingUser.setName(updatedUser.getName());
					if(updatedUser.getAbout() != null)
						existingUser.setAbout(updatedUser.getAbout());
				
					try {
						
						String currentImageUrl = existingUser.getImgUrl();
						//System.out.println(currentImageUrl);
						
						if(!file.isEmpty()) {
							
							String newImage = userService.updateImage(existingUser, file);
							//System.out.println(newImage);
							existingUser.setImgUrl(newImage);
							//System.out.println(existingUser.getImgUrl());
						}else {
							existingUser.setImgUrl(currentImageUrl);
						}
						userRepository.save(existingUser);
						redirectAttributes.addFlashAttribute("successMessage", "Contact updated succcessfully");
					}catch(Exception e) {
						
						redirectAttributes.addFlashAttribute("errorMessage", "Error saving contact: "+e.getMessage());
					}

			}
				
			
		//	System.out.println(existingUser);
			
	        return "redirect:/user/profile";
	    }
		
}
