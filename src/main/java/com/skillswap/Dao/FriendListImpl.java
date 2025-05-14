package com.skillswap.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillswap.entity.FriendList;
import com.skillswap.entity.User;
import com.skillswap.jpa.FriendListOperation;

@Service
public class FriendListImpl implements FriendListService {

	
	@Autowired
	FriendListOperation op;
	
	
	@Override
	public boolean saveUser(FriendList friendList) {
		
		 op.save(friendList);
		 return true;
		
	}


	@Override
	public List<FriendList> friendList(String str) {
		return op.findByStatus(str);
		
	}


	@Override
	public boolean isUpdated(User senderId, User receiverId, String status) {
		
		int a=op.updateUserStatus(senderId, receiverId, status);
		if(a!=0) {
			return true;
		}
		return false;
	}

}
