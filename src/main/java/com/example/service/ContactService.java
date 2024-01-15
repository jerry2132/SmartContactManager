package com.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Contact;

@Service
public interface ContactService {

	Contact save(Contact contact);
	List<Contact> findAllContacts();
	public void deleteContact(Integer contactId);
	
	public String updateImage(Contact existingContact, MultipartFile file) throws IOException;
	public String saveNewImage(Contact contact,MultipartFile file) throws IOException; 
}
