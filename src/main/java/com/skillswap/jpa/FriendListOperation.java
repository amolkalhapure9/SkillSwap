package com.skillswap.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.FriendList;

@Repository
public interface FriendListOperation extends JpaRepository<FriendList, Integer> {
	

}
