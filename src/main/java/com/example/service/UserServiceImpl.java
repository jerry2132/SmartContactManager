package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private User user;
	
	@Autowired
    public void UserServiceImplementation(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
//		user = new User(user.getAbout(),user.getEmail(),user.getName(),user.getImgUrl(),user.isEnabled(),user.getPassword(),user.getRole());
		String encoder = passwordEncoder.encode(user.getPassword());
		user.setPassword(encoder);
		user.setRole("ROLE_USER");
		return userRepository.save(user);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findUserByEmail(email);
	}

	@Override
	public void deleteUser(Integer userId) {
		// TODO Auto-generated method stub
		
		Optional <User> userOptional = userRepository.findById(userId);
		
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			String imagePath = user.getImgUrl();
			System.out.println(imagePath);
			
			
			
			userRepository.deleteById(userId);
			
			if (imagePath != null && !imagePath.isEmpty()) {

				deleteImageFile(imagePath);
			}
		}
		
		
		
	}

	@Override
	public String updateImage(User existingUser, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		
		if(existingUser.getImgUrl() == null || existingUser.getImgUrl().isEmpty()) {
			
			System.out.println("image url not null");
			return saveNewImage(existingUser , file);
			
		}
			
		if(!existingUser.getImgUrl().equals("contact.png")) {
			System.out.println(existingUser.getImgUrl());
			deleteImageFile(existingUser.getImgUrl());
			
		}
			
		
		if(file == null || file.isEmpty()) {
			
			System.out.println("fie is empty");
			return existingUser.getImgUrl();
		}
		
		
		System.out.println("outer savenew w");
		return saveNewImage(existingUser , file);
		
		//return null;
	}

	@Override
	public String saveNewImage(User user,MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		
		try {

			String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
			String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
			String uploadDirectory;
			if (System.getProperty("user.dir").contains("Intellij Projects")) {
				uploadDirectory = "src/main/resources/static/registeredImage/";
			} else {
				ClassPathResource classPathResource = new ClassPathResource("static/img/");
				uploadDirectory = classPathResource.getFile().getAbsolutePath();
			}

			Path path = Paths.get(uploadDirectory, uniqueFilename);
			Files.createDirectories(path.getParent());
			System.out.println(path.toAbsolutePath());
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//			existingContact.setImage(uniqueFilename);
			return uniqueFilename;
		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}


	}

	@Override
	public void deleteImageFile(String imagePath) {
		// TODO Auto-generated method stub
		if (imagePath == "null")
			return;

		try {

			Path imageFilePath = Paths.get("src/main/resources/static/registeredImage/", imagePath);
			Files.deleteIfExists(imageFilePath);
			System.out.println("File deleted" + imageFilePath);
		} catch (IOException e) {

			System.out.println("Error deleting file " + e.getMessage());
		}
	}

//	@Override
//	public User findById(Integer userId) {
//		// TODO Auto-generated method stub
//		return userRepository.getUserById(userId);
//	}

}
