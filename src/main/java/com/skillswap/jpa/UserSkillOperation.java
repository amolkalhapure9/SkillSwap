package com.skillswap.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.Skill;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkills;
import java.util.List;

@Repository
public interface UserSkillOperation extends JpaRepository<UserSkills, Integer> {
	
	List<UserSkills> findByUser(User user);
	public boolean existsByUserAndSkillAndCategory(User user, Skill skill, String category); 
	
	public List<UserSkills> findBySkillAndCategory(Skill skill, String category);
	
	public UserSkills findByUserAndSkillAndCategory(User user,Skill skill, String category);
	
	

}
