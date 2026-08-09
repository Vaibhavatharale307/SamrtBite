package com.smartbite.mainservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.smartbite.mainservice.entity.Role;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.repository.RoleRepository;
import com.smartbite.mainservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
	
	private final RoleRepository roleRepository;
	
	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		
		seedRole(RoleType.ADMIN);
		seedRole(RoleType.STUDENT);
		seedRole(RoleType.CANTEEN_MANAGER);
		seedDemoUser("admin@smartbite.com", "SmartBite Admin", "9999999999", RoleType.ADMIN);
		seedDemoUser("manager@smartbite.com", "SmartBite Manager", "9888888888", RoleType.CANTEEN_MANAGER);
	}

	private void seedDemoUser(String email, String name, String phone, RoleType roleType) {
		
		
		if (userRepository.findByEmail(email).isPresent())
			return;
		
		
		Role role = roleRepository.findByRoleName(roleType).orElseThrow();
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPhone(phone);
		user.setPassword(passwordEncoder.encode("123456"));
		user.setRole(role);
		userRepository.save(user);
	}
	

	private void seedRole(RoleType roleType) {
		
		if (roleRepository.findByRoleName(roleType).isEmpty()) {
			Role role = new Role();
			role.setRoleName(roleType);
			roleRepository.save(role);
			
		}
	}
}
