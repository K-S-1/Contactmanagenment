package com.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.domain.Registration;
import com.auth.repository.AuthRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	AuthRepo authRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Registration user = authRepo.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
		return new MyUserDetails(user);
	}

}
