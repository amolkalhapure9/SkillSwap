package com.skillswap.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.UpdateProfile;

@Repository
public interface UpDateProfileOperation extends JpaRepository<UpdateProfile, Integer> {
	

}
