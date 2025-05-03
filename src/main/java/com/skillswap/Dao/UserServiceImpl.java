package com.skillswap.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillswap.entity.User;
import com.skillswap.jpa.JpaOperation;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	JpaOperation op;

	@Override
	public boolean userRegister(User user) {
		
		try {
			op.save(user);
			return true;
		}
		catch(Exception e) {
			
			return false;
			
		}
	
	}

}
