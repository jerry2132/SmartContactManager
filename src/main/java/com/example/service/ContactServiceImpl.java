package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Contact;
import com.example.entity.User;
import com.example.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService{
	
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private User user;
	
	
	@Autowired
	public ContactServiceImpl(ContactRepository contactRepository, User user) {
		
		this.contactRepository = contactRepository;
		this.user = user;
	}



	@Override
	public Contact save(Contact contact) {
		// TODO Auto-generated method stub
		
//		contact.setUser(user);
		
		return contactRepository.save(contact);
	}



	@Override
	public List<Contact> findAllContacts() {
		// TODO Auto-generated method stub
		return contactRepository.findAll();
	}



	@Override
	public void  deleteContact(Integer contactId) {
		// TODO Auto-generated method stub
		
		Optional<Contact> contactOptional = contactRepository.findById(contactId);
		
		if(contactOptional.isPresent()) {
			Contact contact = contactOptional.get();
			
			String imagePath = contact.getImage();
			System.out.println(imagePath);
			contact.setUser(null);
			
			contactRepository.save(contact);
			
			contactRepository.deleteById(contactId);
			
			if(imagePath != null && !imagePath.isEmpty()) {
				
				deleteImageFile(imagePath);
			}
		}	
//		}else {
//			throw new ContactNotFoundException("not contact found "+contactId +"  not found");
//		}
//		
		
		//return null;
	}
		
	public void deleteImageFile(String imagePath) {
		
		try {
			
			Path imageFilePath = Paths.get("src/main/resources/static/img/",imagePath);
			Files.deleteIfExists(imageFilePath);
			System.out.println("File deleted" + imageFilePath);
		}catch(IOException e) {
			
			System.out.println("Error deleting file " + e.getMessage());
		}
	}
	
	
	

}
