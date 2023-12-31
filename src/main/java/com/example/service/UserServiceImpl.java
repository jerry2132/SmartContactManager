package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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

}
