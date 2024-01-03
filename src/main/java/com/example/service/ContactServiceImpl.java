package com.example.service;

import java.util.List;

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

	
	
	

}
