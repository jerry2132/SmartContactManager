package com.example.service;

import org.springframework.stereotype.Service;

import com.example.entity.Contact;

@Service
public interface ContactService {

	Contact save(Contact contact);
}
