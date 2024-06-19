package com.branches.Branches.global.networking.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.branches.Branches.global.networking.Repository.AdminRepository;
import com.branches.Branches.global.networking.Repository.InquiryRepository;
import com.branches.Branches.global.networking.model.AdminUser;
import com.branches.Branches.global.networking.model.InquiryModel;

@Controller
public class DashBoard {

	@Autowired
	private InquiryRepository inquaryrepo;

	@Autowired
	private AdminRepository adminrepo;

	
	@GetMapping("/admin/dashboard")
	public String getdashboard(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String loggedInUserEmail = userDetails.getUsername();
	     

	        Optional<AdminUser> adminUserOptional = adminrepo.findByEmail(loggedInUserEmail);
	        
	        if (adminUserOptional.isPresent()) {
	            AdminUser adminUser = adminUserOptional.get();
	            model.addAttribute("loggedInUserName", adminUser.getName());
	            model.addAttribute("loggedInUserPhoto", adminUser.getPath()); 
	            
	            System.out.println("Photo Path: " + adminUser.getPath());      } else {
	       
	        }
	    } else {
	       
	    }
	    return "DashBoard"; 
	}
	
//	@GetMapping("/admin/inquary")
//	public String getinquary(Model model) {
//		List<InquiryModel> inquiry = inquaryrepo.findAll();
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			String loggedInUserEmail = userDetails.getUsername();
//
//			Optional<AdminUser> adminUserOptional = adminrepo.findByEmail(loggedInUserEmail);
//			if (adminUserOptional.isPresent()) {
//				AdminUser adminUser = adminUserOptional.get();
//				model.addAttribute("loggedInUserName", adminUser.getName());
//				  model.addAttribute("loggedInUserPhoto", adminUser.getPath());
//			} else {
//				model.addAttribute("loggedInUserName", "Unknown");
//			}
//		} else {
//			model.addAttribute("loggedInUserName", "Unknown");
//		}
//		model.addAttribute("inquiry", inquiry);
//		return "inquary";
//	}
	
	

    @GetMapping("/admin/inquary")
    public String getInquiryList(Model model, @RequestParam(defaultValue = "0") int page) {
      
        int pageSize = 10; 

      
        Pageable pageable = PageRequest.of(page, pageSize);

      
        Page<InquiryModel> inquiryPage = inquaryrepo.findAll(pageable);

      
        model.addAttribute("inquiryPage", inquiryPage);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String loggedInUserEmail = userDetails.getUsername();

            adminrepo.findByEmail(loggedInUserEmail).ifPresent(adminUser -> {
                model.addAttribute("loggedInUserName", adminUser.getName());
                model.addAttribute("loggedInUserPhoto", adminUser.getPath());
            });
        } else {
            model.addAttribute("loggedInUserName", "Unknown");
        }

        return "inquary"; 
    }


    @DeleteMapping("/admin/inquary/{id}/delete")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        if (inquaryrepo.existsById(id)) {
        	inquaryrepo.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    
    }
    
    @GetMapping("/admin/inquary/deleteSelected")
    public String deleteSelectedAccessories(@RequestParam("selectedIds") List<Long> selectedIds) {
    	inquaryrepo.deleteAllById(selectedIds);
        return "redirect:/admin/inquary";
    }
}
	
	

	
//	@GetMapping("/admin/editprofile")
//	public String editprofile() {
//
//		return "editprofile";
//	}

	 
	 

	

