package com.smartbite.authservice.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartbite.authservice.entity.User;
import com.smartbite.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;


/*
 * this class loads user details from db for
 *  spring security auth
 */
@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
	
	//Repository used to fetch user details
	private final UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		User user = userRepo.findByEmail(email)
				.orElseThrow(()-> new UsernameNotFoundException("User NOT FOUND"));
		
		////////////////////////////////////////////////////////////////
		// Convert User entity into Spring Security UserDetails object
		return new org.springframework.security.core.userdetails.User(user.getEmail(),
				user.getPassword(),
				
				Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().name())));
				
				
	}
	
	

}
