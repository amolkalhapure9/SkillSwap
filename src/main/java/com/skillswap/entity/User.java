package com.skillswap.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.pl.REGON;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class User {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	@NotEmpty(message="Name should not empty")
	private String fname;
	
	@Column
	@NotEmpty(message="Last Name should not empty")
	private String lname;
	
	@Column
	@NotBlank(message="Please enter email")
	@Email(message="Please enter valid email")
	private String email;
	
	 @Column
	 @NotEmpty(message = "Please enter a password")
	private String password;
	 
	 
	 @OneToOne
	 @JoinColumn(name = "details_id", referencedColumnName = "id")
	 private UpdateProfile details;
	 
	 @ManyToMany(cascade=CascadeType.ALL)
	 @JoinTable(
			 name = "user_skills",
		     joinColumns = @JoinColumn(name = "user_id"),
		     inverseJoinColumns = @JoinColumn(name = "skill_id")
			 )
	 private Set<Skill> skills= new HashSet<>();
	 


	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	public UpdateProfile getDetails() {
		return details;
	}

	public void setDetails(UpdateProfile details) {
		this.details = details;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
