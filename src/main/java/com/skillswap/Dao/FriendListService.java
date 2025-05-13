package com.skillswap.Dao;

import com.skillswap.entity.FriendList;
import java.util.List;

public interface FriendListService {
	
	public boolean saveUser(FriendList friendList);
	
	public List<FriendList> friendList(String str);

}
