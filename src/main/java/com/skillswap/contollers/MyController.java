package com.skillswap.contollers;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.skillswap.Dao.UserService;
import com.skillswap.dtos.LoginDTO;
import com.skillswap.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
public class MyController {
	
	@Autowired
	 UserService userservice;
	
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
			   model.addAttribute("user",user);
			   return "profile";
		   }
		   
	   }
	   
	   @GetMapping("/openUpdateProfile")
	   public String openUpdateProfile() {
		  
		    return "updateprofile";
		   
	   }

}

