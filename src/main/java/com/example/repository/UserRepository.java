package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User ,Integer>{
	
	User findUserByEmail(String email);
	//public void deleteUser(Integer userId);
	User getUserById(Integer userId);
}
