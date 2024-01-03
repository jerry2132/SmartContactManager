package com.example.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Contact;
import com.example.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact , Integer>{
	
	//@Query("from Contact as c where c.user.id =:userId")
//	public Page<Contact> findContactsByUser(@Param("userId")int userId, Pageable pageable);
	
	public Page<Contact> findContactsByUser(User user, Pageable pageable);

}
