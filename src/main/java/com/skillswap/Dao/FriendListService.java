package com.skillswap.Dao;

import com.skillswap.entity.FriendList;
import com.skillswap.entity.User;

import java.util.List;

public interface FriendListService {
	
	public boolean saveUser(FriendList friendList);
	
	public List<FriendList> friendList(String str);
	public boolean isUpdated(User senderId, User receiverId, String status );

}
