package com.skillswap.contollers;


import java.security.Principal;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.skillswap.Dao.FriendListService;
import com.skillswap.Dao.UpdateProfileService;
import com.skillswap.Dao.UserService;
import com.skillswap.Dao.UserSkillsService;
import com.skillswap.dtos.LoginDTO;
import com.skillswap.dtos.PublicUserDTO;
import com.skillswap.entity.FriendList;
import com.skillswap.entity.Skill;
import com.skillswap.entity.UpdateProfile;
import com.skillswap.entity.User;
import com.skillswap.entity.UserSkills;
import com.skillswap.jpa.JpaOperation;
import com.skillswap.jpa.SkillOperation;
import com.skillswap.jpa.UpDateProfileOperation;
import com.skillswap.jpa.UserSkillOperation;

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
	
	@Autowired
	UserSkillsService userSkillService;
	
	@Autowired
	FriendListService friendListService;
	
	
	
	@GetMapping("/")
	public String openIndex(Model model, HttpSession session) {
		User user=(User)session.getAttribute("user");
		if(user!=null) {
		model.addAttribute(user);
		}
		return "index";
	}
	@GetMapping("/logout")
	public String logOut(HttpSession session, Model model) {
		session.setAttribute("user",null);
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
				   model.addAttribute("user",user);
				  
				   return "index";
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
			   List<UserSkills> userSkills=userSkillService.findUserList(managedUser);
					   
			   model.addAttribute("skill",managedUser.getSkills());
			   model.addAttribute("user",managedUser);
			   session.setAttribute("user", managedUser);
			   model.addAttribute("userSkill",userSkills);
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
		   model.addAttribute("user", user);
		   session.setAttribute("user", user);
		   
		   
		   return "redirect:/profile";
		   
		     
		   
	   }
	   

	   
	   @PostMapping("/saveSkills")
	   public String addSkills(@RequestParam("skillIds") List<Integer> skillIds,Model model, HttpSession session, @RequestParam("category") String category ) {
		   
		   User user=(User)session.getAttribute("user");
		   
		   Optional<User> managedUserOptional=op.findById(user.getId());
		   
		   if(managedUserOptional.isPresent()) {
			   User managedUser=managedUserOptional.get();
			   List<Skill> selectedSkills=skillOperation.findAllById(skillIds);
			   
//			   managedUser.getSkills().addAll(selectedSkills);
			   
			   for(Skill skill: selectedSkills) {
				   if(!userSkillService.isSkillExists(managedUser, skill, category)) {
				   
				   UserSkills userSkill=new UserSkills();
				   userSkill.setUser(managedUser);
				   userSkill.setSkill(skill);
				   userSkill.setCategory(category);
				   userSkillService.saveService(userSkill);
				   
			   }
			   }
			   
			  
			   
			   User updatedUser = op.findById(managedUser.getId()).get();
		        Hibernate.initialize(updatedUser.getSkills());
		        
			  List<UserSkills> userSkills=userSkillService.findUserList(updatedUser);
			   model.addAttribute("user",updatedUser);
			   session.setAttribute("user", updatedUser);
			   model.addAttribute("userSkill",userSkills);
			   model.addAttribute("skill", updatedUser.getSkills());
			   
		   }
		   return "profile";
		  
		   
		   
		   
		   
		   
		   
	   }
	   
	   @GetMapping("/removeskills/{skillId}/{category}")
	   public String removeSkills(@PathVariable("skillId") Integer id,@PathVariable("category") String category, Model model, HttpSession session) {
		  User user=(User)session.getAttribute("user");
		  
		  userSkillService.deleteSkill(user.getId(), id, category);
		  
 
			   
			   model.addAttribute("user", user);
               session.setAttribute("user", user);
               model.addAttribute("skill",user.getSkills());
			   
		   
		   
		   
		   return "redirect:/profile";
		   
		  
		   
	   }
	   
	   @PostMapping("/addskill")
	   public String openSkillPage(@RequestParam("category") String category, Model model, HttpSession session) {
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
		   model.addAttribute("category", category);
		   return "addskills";
		   
	   }
	   
//	   -------------------------------------
	   
	   @GetMapping("/findtutor")
	   public String findTutor(HttpSession session, Model model, Principal principale) {
		   User user=(User)session.getAttribute("user");
		   
		   if(user==null) {
			   return "redirect:/loginPage";
		   }
		   else {
			   model.addAttribute("user",user);
			   
			   List<User> users=op.findAll();
			   List<PublicUserDTO> publicUser=new ArrayList<>();
			   
			   for(User eachuser : users) {
				   
				   
				   if(!eachuser.getEmail().equals(user.getEmail())) {
					   
				   String fullname=eachuser.getFname()+" "+eachuser.getLname();
				   PublicUserDTO publicUserDTO=new PublicUserDTO();
				   publicUserDTO.setId(eachuser.getId());
				   publicUserDTO.setName(fullname);
				   
				   if(eachuser.getDetails()!=null) {
				   publicUserDTO.setBio(eachuser.getDetails().getBio());
				   publicUserDTO.setCity(eachuser.getDetails().getCity());
				   publicUserDTO.setCountry(eachuser.getDetails().getCountry());
				   
				   }
				   else {
					   publicUserDTO.setBio("");
					   publicUserDTO.setCity("");
					   publicUserDTO.setCountry("");
					  
					   
				   }
				   if(eachuser.getSkills()!=null) {
					   publicUserDTO.setSkills(eachuser.getSkills());
				   }
				  
				   
				   publicUser.add(publicUserDTO);
				   
				   }
				   
				   
			   }
			   model.addAttribute("publicusers",publicUser);
			   model.addAttribute("user",user);
			   session.setAttribute("user", user);
			   
			   System.out.println(publicUser);
			   
			   
			   
			   
			   return "findtutor";
		   }
		   
		   
	   }
	   
	   @GetMapping("/publicprofile/{publicuserid}")
	   public String openPublicProfile(@PathVariable("publicuserid") int publicuserid, Model model, HttpSession session, Principal principal) {
		   
		   System.out.println("Hello method starts ");
		   
		   Optional<User> publicuserOptional=op.findById(publicuserid);
		   
		   if(publicuserOptional.isPresent()) {
			   User publicuser=publicuserOptional.get();
			   
			   String fullname=publicuser.getFname()+" "+publicuser.getLname();
			   PublicUserDTO publicuserdto=new PublicUserDTO();
			   publicuserdto.setId(publicuser.getId());
			   publicuserdto.setName(fullname);
			   if(publicuser.getDetails()!=null) {
			   publicuserdto.setBio(publicuser.getDetails().getBio());
			   publicuserdto.setCity(publicuser.getDetails().getCity());
			   publicuserdto.setCountry(publicuser.getDetails().getCountry());
			   }
			   else {
				   publicuserdto.setBio("");
				   publicuserdto.setCity("");
				   publicuserdto.setCountry("");
				   
			   }
			   if(publicuser.getSkills()!=null) {
			   publicuserdto.setSkills(publicuser.getSkills());
			   }
			   
			  List<UserSkills> userSkills=userSkillService.findUserList(publicuser);
			   
			   
			   model.addAttribute("publicuser",publicuserdto);
			   model.addAttribute("userSkill",userSkills);
			   
			   
			   System.out.println(publicuserdto.getBio());
			   
			   
			   return "publicprofile";
			   
			   
		   }
		   else {
		   
		   return "redirect:/findtutor";
		   }
		   
		   
		  
		   
	   }
	   

//	   
	   @GetMapping("/searchUser")
	   public String SearchParticularUserList(@RequestParam("searchterm") String searchterm, HttpSession session,Model model) {
          User user=(User)session.getAttribute("user");
          
          if(searchterm.equals("")) {
        	  return "redirect:/findtutor";
          }
		   
		   if(user==null) {
			   return "redirect:/loginPage";
		   }
		   
		   List<User> allUsers=op.findAll();
		   List<PublicUserDTO> publicUser=new ArrayList<>();
		   
		   
		   for(User eachUser: allUsers) {
			   boolean match=false;
			   if(!eachUser.getEmail().equals(user.getEmail())) {
				   
				   PublicUserDTO dto=new PublicUserDTO();
				   String name=eachUser.getFname();
				   String lastname=eachUser.getLname();
				   if(searchterm.trim().equalsIgnoreCase(name) || searchterm.trim().equalsIgnoreCase(lastname) || searchterm.trim().equalsIgnoreCase(name+" "+lastname)) {
					   dto.setName(name+" "+lastname);
					   dto.setId(eachUser.getId());
					   if(eachUser.getDetails()!=null) {
						  dto.setBio(eachUser.getDetails().getBio());
						   dto.setCity(eachUser.getDetails().getCity());
						   dto.setCountry(eachUser.getDetails().getCountry());
						   
					   }
					   if(eachUser.getSkills()!=null) {
						   dto.setSkills(eachUser.getSkills());
						   }
					   match=true;
				   }
				   
				   else if(eachUser.getDetails()!=null && (searchterm.trim().equalsIgnoreCase(eachUser.getDetails().getCity()) || searchterm.trim().equalsIgnoreCase(eachUser.getDetails().getCountry()) || searchterm.trim().equalsIgnoreCase(eachUser.getDetails().getBio()))) {
					   dto.setName(name+" "+lastname);
					   dto.setId(eachUser.getId());
					   dto.setBio(eachUser.getDetails().getBio());
					   dto.setCity(eachUser.getDetails().getCity());
					   dto.setCountry(eachUser.getDetails().getCountry());
					   if(eachUser.getSkills()!=null) {
						   dto.setSkills(eachUser.getSkills());
						   }
					   match=true;
					   
				   }
				  
				   
				   else if(eachUser.getSkills()!=null && eachUser.getSkills().contains(searchterm) ) {
					   dto.setName(name+" "+lastname);
					   dto.setId(eachUser.getId());
					   if(eachUser.getDetails()!=null) {
					   dto.setBio(eachUser.getDetails().getBio());
					   dto.setCity(eachUser.getDetails().getCity());
					   dto.setCountry(eachUser.getDetails().getCountry());
					   }
					   
						   dto.setSkills(eachUser.getSkills());
						   
						   match=true;
					   
				   }
				   
				   
				   if(match==true) {
				   publicUser.add(dto);
				   }
				   
				   
			   }
			   
		   }
		   
		   model.addAttribute("publicusers", publicUser);
		   model.addAttribute("user",user);
		   session.setAttribute("user", user);
		   
		   if(publicUser.isEmpty()) {
			   model.addAttribute("noUser","No User matching is found");
		   }
		   
		   
		   
		   return "findtutor";
	   }
	   
	   
	   @PostMapping("/send-request")
	   @ResponseBody
	   public String sendRequest(@RequestBody Map<String, Object> publicuser,Model model, HttpSession session) {
		   User user=(User)session.getAttribute("user");
		   
		  String publicuserIdString=(String)publicuser.get("userid");
		  
		  int publicuserId=Integer.parseInt(publicuserIdString);
		  
		  Optional<User> publicUserOptional=op.findById(publicuserId);
		  
		  
		   
		   if(user!=null) {
			   if(publicUserOptional.isPresent()) {
				   User publiUser=publicUserOptional.get();
				   FriendList friendList=new FriendList();
				   friendList.setReceiver(publiUser);
				   friendList.setSender(user);
				   friendList.setStatus("requested");
				   
				   friendListService.saveUser(friendList);
			   }
			   
			   
			   
			  
		   }
		   model.addAttribute("user",user);
		   
		   
		   return "Request sent to user";
	   }

	   @GetMapping("/notification")
	   public String openNotification(Model model, HttpSession session) {
		   User user=(User)session.getAttribute("user");
		   if(user==null) {
			   return "redirect:/loginPage";
		   }
		   String status ="requested";
	  
		   List<FriendList> notification=friendListService.friendList(status);
		   List<FriendList> myList=new ArrayList<>();
		   for(FriendList friendList: notification) {
			   if(friendList.getReceiver().getId()==user.getId()) {
				   myList.add(friendList);
			   }
		   }
		   
		   model.addAttribute("user", user);
		   model.addAttribute("notification", myList);
		   return "notification";

		   
	   }
	   
	   @PostMapping("/acceptRequest")
	   @ResponseBody
	   public String requestAccept(@RequestBody Map<String, Object> publicuser,  HttpSession session, Model model) {
		   
		 User user=(User)session.getAttribute("user");
		 if(user==null) {
			 return "redirect:/loginPage";
		 }
		 String senderid=(String)publicuser.get("senderid");
		 int senderId=Integer.parseInt(senderid);
		 
		 Optional<User> senderOptional=op.findById(senderId);
		 if(senderOptional.isPresent()) {
			 User sender=senderOptional.get();
			 boolean isAccepted=friendListService.isUpdated(sender, user, "Accepted");
		 }
		 
		 
		 
		 
	
		 
		 
		 
		return "NOt accpeted";
		   
	   }
	   
 

}

