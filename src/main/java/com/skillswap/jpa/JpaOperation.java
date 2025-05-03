package com.skillswap.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.User;

@Repository
public interface JpaOperation extends JpaRepository<User, Integer> {
	
	Optional<User> findByEmailAndPassword(String email, String Password);
	
	
	

}
