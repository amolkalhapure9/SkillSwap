package com.skillswap.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillswap.entity.Skill;

@Repository
public interface SkillOperation extends JpaRepository<Skill, Integer> {
	
	

}
