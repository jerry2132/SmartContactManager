package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Date;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.entity.Contact;
import com.example.entity.User;
import com.example.repository.ContactRepository;
import com.example.repository.UserRepository;
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
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
//	private static String uploadDirectory = System.getProperty("user.dir") + "/src/target/uploads";
	
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
		model.addAttribute("addContact", true);
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
		
		String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
        String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
		
		try {
			UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
	
			User user = userService.findByEmail(userDetails.getUsername());
			
			if(file.isEmpty()) {
				
				System.out.println("file is empty");
				contact.setImage("rec.png");
				
			}else {
				
				
				
			contact.setImage(uniqueFilename);
				
//				 String directoryPath = "static" + File.separator + "img";
				 
				 
				
				//File saveFile = new ClassPathResource("static/img").getFile();
				
//				 File saveFile = new File(directoryPath, uniqueFilename);
				 
//				 if (!saveFile.getParentFile().exists()) {
//		                saveFile.getParentFile().mkdirs();
//		            } 
				
			//Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + uniqueFilename);
//			String uploadDirectory = "/src/main/resources/static/img/";
//				Path path = Paths.get(uploadDirectory,uniqueFilename);
				
//				Files.createDirectories(path.getParent());
				
//				Files.createDirectories(saveFile.toPath().getParent());
				
//				Files.createDirectories(path.getParent());
//				
//				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
//				
//				file.transferTo(saveFile);
				
				 //contact.setImage(uniqueFilename);
				
				//System.out.println("uploded");
			
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
				System.out.println(path.toAbsolutePath());
				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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
	
	@GetMapping("/view-contacts/{page}")
	public String viewContacts(@PathVariable("page") Integer page,Model model,Principal principal) {
		
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		User user = userService.findByEmail(userDetails.getUsername());
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = contactRepository.findContactsByUser(user, pageable);
		
		//System.out.println(page);
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage",page);
		model.addAttribute("totalPages" , contacts.getTotalPages());
		
		return "admin/view-contacts";
	}
	
	@GetMapping("/details/{cid}")
	public String showIndividualContactDetails(@PathVariable("cid")int contactId,Model model,Principal principal) {
		
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		User user = userService.findByEmail(userDetails.getUsername());
		
		Optional<Contact> contactOptional = contactRepository.findById(contactId); 
		Contact contact = contactOptional.get();
		
		if(user.getId() == contact.getUser().getId())
			model.addAttribute("contacts", contact);
		
		return "admin/details";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid")Integer contactId,Model model,RedirectAttributes redirectAttributes) {
		
		try {
			
			contactService.deleteContact(contactId);
			redirectAttributes.addFlashAttribute("message", "contact deleted successfully");
		}catch(Exception e) {
			
			redirectAttributes.addFlashAttribute("message", "contact not found");
		}
		
		return "redirect:/admin/view-contacts/0";
	}
	
	@GetMapping("/update-contact/{cid}")
	public String updateContact(@PathVariable("cid")Integer contactId,Model model) {
		
		Contact contact = contactRepository.findById(contactId).get();
		model.addAttribute("addContact", false);
		model.addAttribute("contact", contact);
		
		return "admin/add_contacts_admin";
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
	
//	System.out.println("Id =  	"+contact.getCid());
		
		
	return "redirect:/admin/contact-status";
	}
	
	@GetMapping("/profile")
	public String profile(Model model,Principal principal) {
		
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		User user = userService.findByEmail(userDetails.getUsername());
		
		model.addAttribute("user",user);
		
		return "admin/profile";
	}
	
	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id")Integer userId,Model model,RedirectAttributes redirectAttributes) {
		
		try {
			
			userService.deleteUser(userId);
			redirectAttributes.addFlashAttribute("successMessage", "user deleted successfully");
		}catch(Exception e) {
			
			redirectAttributes.addFlashAttribute("errorMessage", "user not found");
		}
		
		return "redirect:/signup";
		
	}
	
	@GetMapping("/update-user/{id}")
	public String updateUserForm(@PathVariable("id")Integer userId,Model model,Principal principal) {
		
		Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
		
		 boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		 	//authorities.stream().anyMatch(authority -> authority.getAuthority().eq)
	        // Add the isAdmin variable to the Thymeleaf context
//		 User user  = userService.findById(userId).get();
		 
//		 Optional<User> userOptional  = 
//		 UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//		 User user = userService.findByEmail(userDetails.getUsername());
				 User user = userRepository.findById(userId).get();
		 System.out.println(user);
		 System.out.println(isAdmin);
		 System.out.println(authorities);
		 model.addAttribute("isAdmin", isAdmin);
		 
		 model.addAttribute("updateUser", false);
		 model.addAttribute("user", user);
		 //System.out.println(updateUser);
		return "signup";
		
	}
	
	@PostMapping("/process-update-user/{id}")
    public String processUpdateUser(@PathVariable("id") Integer userId,@ModelAttribute User updatedUser,
    		@RequestParam("imageUrl")MultipartFile file,Principal principal,RedirectAttributes redirectAttributes) {
        // Your logic for updating the user in the admin context
		
		
//		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
//		User user1 = userService.findByEmail(userDetails.getUsername());
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
					System.out.println(currentImageUrl);
					
					if(!file.isEmpty()) {
						
						String newImage = userService.updateImage(existingUser, file);
						System.out.println(newImage);
						existingUser.setImgUrl(newImage);
						System.out.println(existingUser.getImgUrl());
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
		
        return "redirect:/admin/profile";
    }
}
