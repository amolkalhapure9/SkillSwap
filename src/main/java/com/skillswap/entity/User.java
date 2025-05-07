package com.skillswap.entity;

import org.hibernate.validator.constraints.pl.REGON;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
