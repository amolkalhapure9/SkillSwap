package com.skillswap.Dao;

import org.springframework.stereotype.Repository;

import com.skillswap.entity.UpdateProfile;


public interface UpdateProfileService {
	
	public boolean updateProfile(UpdateProfile updateProfile);

}
