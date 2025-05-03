package com.skillswap.Dao;

import org.springframework.stereotype.Service;

import com.skillswap.entity.User;


public interface UserService {
	
	public boolean userRegister(User user);

}
