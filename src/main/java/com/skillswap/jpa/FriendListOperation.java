package com.skillswap.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.FriendList;
import java.util.List;

@Repository
public interface FriendListOperation extends JpaRepository<FriendList, Integer> {
	
	List<FriendList> findByStatus(String str);
	

}
