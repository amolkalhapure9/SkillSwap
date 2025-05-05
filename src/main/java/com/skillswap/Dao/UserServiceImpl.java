package com.skillswap.Dao;



import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillswap.dtos.LoginDTO;
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
	public boolean userLogin(LoginDTO loginDTO) {
		
		Optional<User> foundUser=op.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
		
	
		return foundUser.isPresent();
	}

	@Override
	public boolean existsByEmail(String email) {
		return op.existsByEmail(email);
		
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return op.findByEmail(email);
	}

}
