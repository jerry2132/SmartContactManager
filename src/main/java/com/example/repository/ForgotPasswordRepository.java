package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ForgotPassword;
import com.example.entity.User;


@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long>{
	
	ForgotPassword findByToken(String token);
	
	ForgotPassword findByUser(User user);
	

}
