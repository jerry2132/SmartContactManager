package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Contact;
import com.example.entity.User;
import com.example.repository.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

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
	public void deleteContact(Integer contactId) {
		// TODO Auto-generated method stub

		Optional<Contact> contactOptional = contactRepository.findById(contactId);

		if (contactOptional.isPresent()) {
			Contact contact = contactOptional.get();

			String imagePath = contact.getImage();
			System.out.println(imagePath);
			contact.setUser(null);

			contactRepository.save(contact);

			contactRepository.deleteById(contactId);

			if (imagePath != null && !imagePath.isEmpty()) {

				deleteImageFile(imagePath);
			}
		}
//		}else {
//			throw new ContactNotFoundException("not contact found "+contactId +"  not found");
//		}
//		

		// return null;
	}

	public void deleteImageFile(String imagePath) {

		if (imagePath == "null")
			return;

		try {

			Path imageFilePath = Paths.get("src/main/resources/static/img/", imagePath);
			Files.deleteIfExists(imageFilePath);
			System.out.println("File deleted" + imageFilePath);
		} catch (IOException e) {

			System.out.println("Error deleting file " + e.getMessage());
		}
	}

	@Override
	public String updateImage(Contact existingContact, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
//		Contact contact = existingContact.get();

//		if(file.isEmpty()) {
//			
////		contact.setImage(existingContact.);
//		return;
//		
//		}
////			
//			 String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
//		        String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
//		        String uploadDirectory = "/src/main/resources/static/img/";
//		        Path path = Paths.get(uploadDirectory, uniqueFilename);
//		        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//
//		        // Set the new image filename in the contact
//		        existingContact.setImage(uniqueFilename);
//		
//	}
//	public void updateContact()

		if (existingContact.getImage() == null || existingContact.getImage().isEmpty()) {

			return saveNewImage(existingContact, file);
		}

		deleteImageFile(existingContact.getImage());

		if (file == null || file.isEmpty()) {

			return existingContact.getImage();
		}

		return saveNewImage(existingContact, file);

	}

	@Override
	public String saveNewImage(Contact contact, MultipartFile file) throws IOException {
		// TODO Auto-generated method stub

		try {

			String formattedDateTime = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now());
			String uniqueFilename = formattedDateTime + "_" + file.getOriginalFilename();
			String uploadDirectory;
			if (System.getProperty("user.dir").contains("Intellij Projects")) {
				uploadDirectory = "src/main/resources/static/img/";
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
}
