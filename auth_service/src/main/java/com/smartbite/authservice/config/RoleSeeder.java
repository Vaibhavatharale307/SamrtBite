package com.smartbite.authservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.smartbite.authservice.entity.Role;
import com.smartbite.authservice.entity.RoleType;
import com.smartbite.authservice.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
	 private final RoleRepository roleRepository;

	 @Override
	 public void run(String... args) {

	     seedRole(RoleType.ADMIN);
	     seedRole(RoleType.STUDENT);
	     seedRole(RoleType.CANTEEN_MANAGER);
	 }

	 private void seedRole(RoleType roleType) {

	     if(roleRepository.findByRoleName(roleType).isEmpty()) {

	         Role role = new Role();
	         role.setRoleName(roleType);

	         roleRepository.save(role);
	     }
	 }

}
