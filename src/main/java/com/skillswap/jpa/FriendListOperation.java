package com.skillswap.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.FriendList;
import com.skillswap.entity.User;

import jakarta.transaction.Transactional;

import java.util.List;

@Repository
public interface FriendListOperation extends JpaRepository<FriendList, Integer> {
	
	List<FriendList> findByStatus(String str);
	
	@Modifying                        // marks the quesry as modifying (non-select) operation  note modifying queries can use only void int/integer as a return type .
	@Transactional                    //Ensures that query will be executed with in the transactions
	@Query("update FriendList friend set friend.status= :status where receiver= :receiverId and sender= :senderId ")
	public int updateUserStatus(User senderId, User receiverId, String status);
	

}
