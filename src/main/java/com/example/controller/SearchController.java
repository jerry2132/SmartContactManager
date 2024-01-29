package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Contact;
import com.example.entity.User;
import com.example.repository.ContactRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.userdetails.CustomUserDetailsServiceImpl;

@RestController
public class SearchController {
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal){
		
		System.out.println(query);
		
		UserDetails userDetails  = customUserDetailsServiceImpl.loadUserByUsername(principal.getName());
		User user = userService.findByEmail(userDetails.getUsername());
		
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
	}
}