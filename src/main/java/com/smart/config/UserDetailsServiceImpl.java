package com.smart.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.entities.User;
import com.smart.repository.UserRepo;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//fetching user from database
		
		User user = userRepo.getUserByEmail(username);
		
		if(user==null)
			throw new UsernameNotFoundException("Username doesn't exists ! please verify");
		
		CustomUserDetails customUserdetail = new CustomUserDetails(user);
		
		
		return customUserdetail;
	}

}
