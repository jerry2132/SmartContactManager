package com.example.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Contact;
import com.example.entity.User;

public interface UserService {

	User save(User user);
	User findByEmail(String email);
	public void deleteUser(Integer userId);
//	User findById(Integer usreId);
	public void deleteImageFile(String imagePath);
	public String updateImage(User existingUser, MultipartFile file) throws IOException;
	public String saveNewImage(User user,MultipartFile file) throws IOException; 
}
