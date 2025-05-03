package com.skillswap.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.skillswap.Dao.UserService;
import com.skillswap.entity.User;



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
		   model.addAttribute("user", new User());
		   return "login";
		   
		   
	   }
	   
	   @PostMapping("/userregister")
	   public String userRegister(@ModelAttribute("user") User user) {
		   boolean res=userservice.userRegister(user);
		   
		   if(res==true) {
			   return "index";
		   }
		   else {
			   return "register";
		   }
		   
		   
		   
	   }

}

