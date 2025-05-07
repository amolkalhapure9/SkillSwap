package com.skillswap.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillswap.entity.UpdateProfile;
import com.skillswap.jpa.UpDateProfileOperation;

@Service
public class UpdateProfileImpl implements UpdateProfileService {
	
	@Autowired
	UpDateProfileOperation upOperation;

	@Override
	public boolean updateProfile(UpdateProfile updateProfile) {
		upOperation.save(updateProfile);
		return false;
	}

}
