	package com.branches.Branches.global.networking.Controller;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

	
@Controller
public class AdminController {
	
	@GetMapping("/login")
	public String handleLogin() {

		  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  if(authentication==null||authentication instanceof AnonymousAuthenticationToken){
			  
			  return"login";
			  
		  }
		  else {
			  if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {
	                return "redirect:/admin/dashboard"; 
	            } else if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
	                return "redirect:/user/home"; 
	            } else {
	             
	                return "redirect:/admin/dashboard";
	            }

		  }


	}
	
//	  @GetMapping("/login")
//	  public String handleLogin() {
//	    return "login";
//	  }
//	  
	  

}

