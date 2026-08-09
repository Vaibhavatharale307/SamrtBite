package com.smartbite.mainservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartbite.mainservice.entity.Role;
import com.smartbite.mainservice.entity.RoleType;



public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Optional<Role> findByRoleName(RoleType roleName);

}
