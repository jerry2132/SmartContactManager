package com.example.userdetails;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
 	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		User user = userRepository.findUserByEmail(email);
		
		if(user == null) {
			
			throw new UsernameNotFoundException("USERNAME OR PASSWORD NOT PRESENT ,TRY REGISTERING YOURSELF");
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}

}
