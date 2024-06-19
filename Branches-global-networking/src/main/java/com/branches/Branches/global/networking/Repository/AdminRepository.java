package com.branches.Branches.global.networking.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
	

import com.branches.Branches.global.networking.model.AdminUser;


public interface AdminRepository extends JpaRepository<AdminUser, Long>  {

	   Optional<AdminUser> findByEmail(String email);


	   
	
	  
}
