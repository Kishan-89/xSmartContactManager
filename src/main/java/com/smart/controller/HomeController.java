package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.entities.User;
import com.smart.helper.EmailContent;
import com.smart.helper.Message;
import com.smart.repository.UserRepo;
import com.smart.service.EmailService;


@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	public EmailService emailService;
	
	@GetMapping("/")
	public String home(Model m) {
		
		m.addAttribute("title", "Home - Smart Contact Manager");
		
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model m) {
		
		m.addAttribute("title", "About - Smart Contact Manager");
		
		return "about";
	}
	@GetMapping("/signup")
	public String signup(Model m) {
		
		m.addAttribute("title", "Sign Up - Smart Contact Manager");
		m.addAttribute("user", new User());
		
		return "signup";
	}
	
	
	// this is handler for registring user
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult verrors, @RequestParam(value="agreement", defaultValue="false") boolean check, Model m, HttpSession session) {
		
		
		try {
			
			if(!check) {
				
				System.out.println("terms and conditions are missing");
				throw new Exception("You can't skip terms and conditions");
			}
			
			if(verrors.hasErrors()) {
				System.out.println(verrors);
				m.addAttribute("user", user );
				
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(bcrypt.encode(user.getPassword()));
			
			User result = userRepo.save(user);
			
			//System.out.println("Adding User :"+user.toString());
			
			m.addAttribute("user", new User());
			session.setAttribute("message", new Message("Welcome " +result.getName()+", You have registered successfully!!","alert-success"));
			
			EmailContent emailContent = new EmailContent(user.getEmail(),"Welcome Aboard","Hey"+user.getName()+"! Welcome to Smart Contact Manager! Appreciate you signing up. We want you to have the best experience with managing all your contacts :)");
			 String from="smart.contact.manager89@gmail.com";
			 
			 emailService.sendEmail(emailContent.getContent(), emailContent.getSubject(), emailContent.getTo(), from);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Oops Something went wrong! Try again"+e.getMessage(),"alert-danger"));
		}
		
		return "signup";
	}
	
	@GetMapping("/signin")
	public String login(Model m) {
		
		m.addAttribute("title", "Log in - Smart Contact Manager");
		
		return "login";
	}
	


}
