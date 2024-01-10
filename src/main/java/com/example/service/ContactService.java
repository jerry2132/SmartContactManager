package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Contact;

@Service
public interface ContactService {

	Contact save(Contact contact);
	List<Contact> findAllContacts();
	public void deleteContact(Integer contactId);
}
