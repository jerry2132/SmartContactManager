package com.example.userdetails;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
 	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
		// TODO Auto-generated method stub
		 
		User user = userRepository.findUserByEmail(email);
		
		if(user == null) {
			
			System.out.println("not found "+email);
			
			throw new UsernameNotFoundException("notRegistered");
		}
		
//		else if (!isValidCredentials(user)) {
//			throw new BadCredentialsException("invalidCredentials");
//			
//			//return null;
//	    }
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		
		return customUserDetails;
	}

	
//	private boolean isValidCredentials(User user) {
//	    // Implement your logic to check if the credentials are valid.
//	    // For example, compare the entered password with the stored password hash.
//		
//			//User findUser = userRepository.findUserByEmail(user.getEmail());
//		
//		String storedPassword  = user.getPassword();
//			
////			if(findUser != null) {
////				
////				String checkedUser = findUser.getPassword();
////				if(passwordEncoder.matches(user.getPassword(), checkedUser)){
////					return true;
////				}
////				
////			}
////			
//		
//	    return passwordEncoder.matches(user.getPassword(), storedPassword);
//	}

}


