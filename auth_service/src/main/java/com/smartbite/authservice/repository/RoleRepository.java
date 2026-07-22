package com.smartbite.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartbite.authservice.entity.Role;
import com.smartbite.authservice.entity.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByRoleName(RoleType roleName);

}
