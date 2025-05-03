package com.skillswap.Dao;



import java.util.Optional;
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

	@Override
	public boolean userLogin(User user) {
		
		Optional<User> foundUser=op.findByEmailAndPassword(user.getEmail(), user.getPassword());
		
	
		return foundUser.isPresent();
	}

}
