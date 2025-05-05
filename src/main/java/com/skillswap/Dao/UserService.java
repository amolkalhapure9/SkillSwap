package com.skillswap.Dao;

import org.springframework.stereotype.Service;

import com.skillswap.dtos.LoginDTO;
import com.skillswap.entity.User;


public interface UserService {
	
	public boolean userRegister(User user);
	
	public boolean userLogin(LoginDTO loginDTO);
	
	public boolean existsByEmail(String email);
	
	public User getUserByEmail(String email);

}
