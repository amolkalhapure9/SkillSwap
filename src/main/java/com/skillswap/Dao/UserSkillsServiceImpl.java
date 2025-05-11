package com.skillswap.Dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkills;
import com.skillswap.jpa.JpaOperation;
import com.skillswap.jpa.SkillOperation;
import com.skillswap.jpa.UserSkillOperation;

@Service
public class UserSkillsServiceImpl implements UserSkillsService {
    
	@Autowired
	UserSkillOperation op;
	
	@Autowired
	JpaOperation userop;
	
	@Autowired 
	SkillOperation skillop;
	
	@Override
	public boolean saveService(UserSkills skill) {
		op.save(skill);
		return true;
		
	}
	@Override
	public List<UserSkills> findUserList(User user) {
		return op.findByUser(user);
		
	}
	@Override
	public boolean isSkillExists(User user, Skill skill, String category) {
		 return op.existsByUserAndSkillAndCategory(user, skill, category);
		
	}
	@Override
	public List<UserSkills> findSkillList(Skill skill, String category) {
		return op.findBySkillAndCategory(skill, category);
		
	}
	@Override
	public void deleteSkill(int userId,int skillId, String category) {
	
		Optional<User> optionalUser=userop.findById(userId);
		Optional<Skill> optionalSkill=skillop.findById(skillId);
		
		if(optionalUser.isPresent()) {
			User user=optionalUser.get();
			
			if(optionalSkill.isPresent()) {
				Skill skill=optionalSkill.get();
			
			UserSkills userSkills=op.findByUserAndSkillAndCategory(user, skill, category);
			
			if(userSkills!=null) {
				op.delete(userSkills);
			}
				
			
		}
		
		
	}
		
	

  }
}
