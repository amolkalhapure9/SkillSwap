package com.skillswap.Dao;

import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkills;
import java.util.List;

public interface UserSkillsService {
	
	public boolean saveService(UserSkills skill);
	public List<UserSkills> findUserList(User user);
	
	public boolean isSkillExists(User user,Skill skill, String category);
	public List<UserSkills> findSkillList(Skill skill, String category);
	
	public void deleteSkill(int userId,int skillId, String category) ;
		
		
	
	

}
