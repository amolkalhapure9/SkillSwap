package com.skillswap.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillswap.entity.FriendList;
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

}
