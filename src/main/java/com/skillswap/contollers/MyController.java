package com.skillswap.contollers;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.skillswap.Dao.UpdateProfileService;
import com.skillswap.Dao.UserService;
import com.skillswap.dtos.LoginDTO;
import com.skillswap.entity.Skill;
import com.skillswap.entity.UpdateProfile;
import com.skillswap.entity.User;
import com.skillswap.jpa.JpaOperation;
import com.skillswap.jpa.SkillOperation;
import com.skillswap.jpa.UpDateProfileOperation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class MyController {
	
	@Autowired
	 UserService userservice;
	
	@Autowired
	UpdateProfileService profileUpdate;
	
	@Autowired
	JpaOperation op;
	
	@Autowired
	UpDateProfileOperation up;
	
	@Autowired
	SkillOperation skillOperation;
	
	@GetMapping("/")
	public String openIndex() {
		return "index";
	}
	
	   @GetMapping("/regPage")
	    public String registerUser(Model model) {
		   model.addAttribute("user",new User());
	        return "register"; // Loads templates/register.html
	    }
	   
	   @GetMapping("/loginPage")
	   public String loginUser(Model model) {
		   model.addAttribute("loginDTO", new LoginDTO());
		   return "login";
		   
		   
	   }
	   
	   @PostMapping("/userregister")
	   public String userRegister(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,HttpServletRequest req ) {
		   
		   if(result.hasErrors()) {
			   Map<String, String> errors=new HashMap<>();
			   
			   result.getFieldErrors().forEach(error -> {
		            errors.put(error.getField(), error.getDefaultMessage());
		        });
			   model.addAttribute("errors",errors);
			   
			   System.out.print(errors);
			   
			   return "register";
		   }
		   else if(!result.hasErrors()) {
			   String cpassword=req.getParameter("cpassword");
			   if(!user.getPassword().equals(cpassword)) {
				   model.addAttribute("cpassword","Please enter same password");
				   return "register";
			   }
			   else {
				  
				   if(userservice.existsByEmail(user.getEmail())) {
					   model.addAttribute("exists","Email already used");
					   return "register";
				   }
				   else {
					   
					   try {
						   boolean c=userservice.userRegister(user);
						   if(c==true) {
							   System.out.println("You have successfully registered");
							   model.addAttribute("Registered","You have successfully registered. Please Login");
							   model.addAttribute("loginDTO",new LoginDTO());
							   return "login";
						   }
						   else {
							   return "register";
						   }
						   
					   }
					   catch(Exception e) {
						   return "register";
						   
					   }
					   
				   }
			   }
			   
		   }
		  
		  
		   return "index";
		  
		   
		   
		   
	   }
	   
	   @PostMapping("/userLogin")
	   public String userLogin(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO, BindingResult result, Model model, HttpSession session) {
		   
		   if(result.hasErrors()) {
			   Map<String, String> errors=new HashMap<>();
			   
			   result.getFieldErrors().forEach(error -> {
		            errors.put(error.getField(), error.getDefaultMessage());
		        });
			   model.addAttribute("errors",errors);
			   
			   System.out.print(errors);
			   
			   return "login";
		   }
		   else {
			   model.addAttribute("loginDTO",loginDTO);
			   System.out.println(loginDTO.getEmail());
			   boolean b=userservice.userLogin(loginDTO);
			   if(b==true) {
				   System.out.println("User is present in the database with credentials: "+loginDTO.getEmail()+" "+loginDTO.getPassword());
				   User user=userservice.getUserByEmail(loginDTO.getEmail());
				   session.setAttribute("user", user);
				  
				   return "redirect:/profile";
			   }
			   else {
				   model.addAttribute("userexists","Please enter correct email or password");
				
				   return "login";
			   }
			   
		   }
		   
		   
		   
		  
	   }
	   
	   @GetMapping("/profile")
	   public String openProfile(HttpSession session, Model model) {
		   User user=(User)session.getAttribute("user");
		   if(user==null) {
			   return "redirect:/login";
		   }
		   else {
			   Optional<User> managedUserOptional=op.findById(user.getId());
			   User managedUser=managedUserOptional.get();
			   Set<Skill> skill=managedUser.getSkills();
			   
			   model.addAttribute("skill",managedUser.getSkills());
			   model.addAttribute("user",managedUser);
			   session.setAttribute("user", managedUser);
			   return "profile";
		   }
		   
	   }
	   
	   @GetMapping("/openUpdateProfile")
	   public String openUpdateProfile(HttpSession session,Model model) {
		   model.addAttribute("updateProfile",new UpdateProfile());
		   
		   User user=(User)session.getAttribute("user");
		   
		   if(user.getDetails()!=null) {
			   UpdateProfile updateProfile=user.getDetails();
			   model.addAttribute("updateProfile", updateProfile);
		   }
		   
		   
		   
		  
		    return "updateprofile";
		   
	   }
	   
	   @PostMapping("/updateProfile")
	   public String updateProfile(@Valid @ModelAttribute("updateProfile") UpdateProfile updateProfile, HttpSession session, Model model, BindingResult result) {
		   
		   
		   User user=(User)session.getAttribute("user");
		   model.addAttribute("user",user);
		   
		   boolean b=profileUpdate.updateProfile(updateProfile);
		   
		   UpdateProfile tobedelete=user.getDetails();
		   if(tobedelete!=null) {
			   user.setDetails(null);
			   op.save(user);
			   up.deleteById(tobedelete.getId());
			  
		
			   
		   }
		   
		   
		   user.setDetails(updateProfile);
		   updateProfile.setUser(user);
		   
		   
		   op.save(user);
		   
		   
		   return "profile";
		   
		     
		   
	   }
	   
	   @GetMapping("/addskills")
	   public String openAddSkills(Model model, HttpSession session) {
		   User user=(User)session.getAttribute("user");
		   
		  
		   
		   User managedUser = op.findById(user.getId()).orElse(null);
		 
		    
		    if (managedUser != null) {
		        // Initialize the lazy-loaded skills collection
		        Hibernate.initialize(managedUser.getSkills());
		    }
		   
		   
		   
		   List<Skill> allskills=skillOperation.findAll();
		   model.addAttribute("allskills", allskills);
		   
		   System.out.print(allskills);
		   
		   
		   model.addAttribute("user",managedUser);		   
		   return "addskills";
		   
	   }
	   
	   @PostMapping("/saveSkills")
	   public String addSkills(@RequestParam("skillIds") List<Integer> skillIds,Model model, HttpSession session) {
		   
		   User user=(User)session.getAttribute("user");
		   
		   Optional<User> managedUserOptional=op.findById(user.getId());
		   
		   if(managedUserOptional.isPresent()) {
			   User managedUser=managedUserOptional.get();
			   List<Skill> selectedSkills=skillOperation.findAllById(skillIds);
			   
			   managedUser.getSkills().addAll(selectedSkills);
			   
			   op.save(managedUser);
			   
			   User updatedUser = op.findById(managedUser.getId()).get();
		        Hibernate.initialize(updatedUser.getSkills());
		        
			  
			   model.addAttribute("user",updatedUser);
			   session.setAttribute("user", updatedUser);
			   model.addAttribute("skill", updatedUser.getSkills());
			   
		   }
		   return "profile";
		  
		   
		   
		   
		   
		   
		   
	   }
	   
	   @GetMapping("/removeskills/{skillId}")
	   public String removeSkills(@PathVariable("skillId") Integer id, Model model, HttpSession session) {
		   User sessionUser=(User)session.getAttribute("user");
		   
		   Optional<User> managedUserOptional=op.findById(sessionUser.getId());
		   
		   if(managedUserOptional.isPresent()) {
			   User managedUser=managedUserOptional.get();
			   
			   Optional<Skill> optionalSkill=skillOperation.findById(id);
			   if(optionalSkill!=null) {
				   Skill skill=optionalSkill.get();
				   managedUser.getSkills().remove(skill);
				   
				   op.save(managedUser);
			   }
			   
			   model.addAttribute("user", managedUser);
               session.setAttribute("user", managedUser);
               model.addAttribute("skill",managedUser.getSkills());
			   
		   }
		   
		   
		   return "redirect:/profile";
		   
	   }
	   
 

}

