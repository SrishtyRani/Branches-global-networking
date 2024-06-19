package com.branches.Branches.global.networking.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import com.branches.Branches.global.networking.Repository.AdminRepository;
import com.branches.Branches.global.networking.model.AdminUser;




@Component
public class DetailService implements UserDetailsService{
	
	   private static final Logger logger = LoggerFactory.getLogger(DetailService.class);
	   @Autowired
	    private  AdminRepository repository;

	 
//	    public DetailService(AdminRepository repository) {
//	        this.repository = repository;
//	    }

	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    	System.out.println(email);
	        logger.info("Attempting to load user by email: {}", email);

	        Optional<AdminUser> user = repository.findByEmail(email);
	        if (user.isPresent()) {
	            var userObj = user.get();
	            logger.info("User found with email: {}", email);
	            logger.debug("User details - Email: {}, Password: {}, Roles: {}", userObj.getEmail(), userObj.getPassword(), userObj.getRole());
	            return User.builder()
	                    .username(userObj.getEmail())
	                    .password(userObj.getPassword())
	                    .roles(getRoles(userObj))
	                    .build();
	        } else {
	            logger.warn("User not found with email: {}", email);
	            throw new UsernameNotFoundException("User not found with email: " + email);
	        }
	    }

	    private String[] getRoles(AdminUser user) {
	        if (user.getRole() == null) {
	            return new String[]{"USER"};
	        }
	        return user.getRole().split(",");
	    }
	}
	


