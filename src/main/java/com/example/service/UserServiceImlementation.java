package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserServiceImlementation implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    public void UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
//		user = new User(user.getAbout(),user.getEmail(),user.getName(),user.getImgUrl(),user.isEnabled(),user.getPassword(),user.getRole());
		return userRepository.save(user);
	}

}
