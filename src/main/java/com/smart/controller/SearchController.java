package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repository.ContactRepo;
import com.smart.repository.UserRepo;

@RestController
public class SearchController {

	@Autowired
	private ContactRepo contactRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/user/search/{word}")
	public ResponseEntity<?> contactSearch(@PathVariable("word") String word, Principal principal){
		
		User user =this.userRepo.getUserByEmail(principal.getName());
		
		List<Contact> list = this.contactRepo.findByNameContainingAndUser(word, user);
		
		//new ResponseEntity<>(list, HttpStatus.ACCEPTED);
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
