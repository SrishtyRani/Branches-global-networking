package com.branches.Branches.global.networking.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.branches.Branches.global.networking.Repository.AdminRepository;
import com.branches.Branches.global.networking.model.AdminUser;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/profile")
public class EditProfile {
	
	
	@Autowired
	private AdminRepository adminrepo;
	
	
    @Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@GetMapping
	public String showProfile(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String loggedInUserEmail = userDetails.getUsername();

	        Optional<AdminUser> adminUserOptional = adminrepo.findByEmail(loggedInUserEmail);

	        if (adminUserOptional.isPresent()) {
	            AdminUser adminUser = adminUserOptional.get();
	            model.addAttribute("loggedInUserName", adminUser.getName());
				  model.addAttribute("loggedInUserPhoto", adminUser.getPath());
	            model.addAttribute("adminUser", adminUser);
	        } else {
	            return "redirect:/error"; 
	        }
	    } else {
	        return "redirect:/login"; 
	    }

	    return "editprofile";
	}


	

//
//	@PostMapping
//	public String saveUser(@ModelAttribute("user") AdminUser updatedUser)
//	                   {
//
//	    Long userId = updatedUser.getId();
//
//	    if (userId != null) {
//	        Optional<AdminUser> adminUserOptional = adminrepo.findById(userId);
//
//	        if (adminUserOptional.isPresent()) {
//	            AdminUser existingUser = adminUserOptional.get();
//
//	           
//	            existingUser.setName(updatedUser.getName());
//	            existingUser.setEmail(updatedUser.getEmail());
//
//	        
//
//	            adminrepo.save(existingUser);
//
//	            return "redirect:/admin/dashboard"; 
//	        } else {
//	            return "redirect:/error";
//	        }
//	    } else {
//	        return "redirect:/error"; 
//	    }
//	}
		


	@PostMapping
	public String saveUser(@ModelAttribute("user") AdminUser updatedUser, RedirectAttributes redirectAttributes) {
	    Long userId = updatedUser.getId();

	    if (userId != null) {
	        Optional<AdminUser> adminUserOptional = adminrepo.findById(userId);

	        if (adminUserOptional.isPresent()) {
	            AdminUser existingUser = adminUserOptional.get();

	            existingUser.setName(updatedUser.getName());
	            existingUser.setEmail(updatedUser.getEmail());

	            adminrepo.save(existingUser);

	           
	            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");

	            return "redirect:/admin/profile"; 
	        } else {
	            return "redirect:/error";
	        }
	    } else {
	        return "redirect:/error"; 
	    }
	}


	@PostMapping("/password")
	public String savePassword(@ModelAttribute("user") AdminUser updatedUser,
	                           @RequestParam("oldPassword") String oldPassword,
	                           @RequestParam("newPassword") String newPassword,
	                           @RequestParam("confirmPassword") String confirmPassword,
	                           HttpSession session,
	                           RedirectAttributes redirectAttributes) {

	    Long userId = updatedUser.getId();

	    if (userId != null) {
	        Optional<AdminUser> adminUserOptional = adminrepo.findById(userId);
	        if (adminUserOptional.isPresent()) {
	            AdminUser existingUser = adminUserOptional.get();

	            if (oldPassword != null && newPassword != null && confirmPassword != null &&
	                !oldPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {

	                if (!passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
	                    redirectAttributes.addFlashAttribute("error", "Old password is incorrect.");
	                    return "redirect:/admin/profile";
	                }

	                if (!newPassword.equals(confirmPassword)) {
	                    redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match.");
	                    return "redirect:/admin/profile";
	                }

	                String encodedPassword = passwordEncoder.encode(newPassword);
	                existingUser.setPassword(encodedPassword);
	                adminrepo.save(existingUser);

	                return "redirect:/logout";
	            } else {
	                redirectAttributes.addFlashAttribute("error", "All fields are required.");
	                return "redirect:/admin/profile";
	            }
	        }
	    }
	    redirectAttributes.addFlashAttribute("error", "User not found.");
	    return "redirect:/admin/profile";
	}


	
	private static String uploadDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" +  File.separator + "profilepic";
	@PostMapping("/image")
	public String savephoto(@ModelAttribute("user") AdminUser updatedUser
			,    @RequestParam(value = "featuredImage", required = false) MultipartFile[] featuredImages) {

	    Long userId = updatedUser.getId();

	    if (userId != null) {
	        Optional<AdminUser> adminUserOptional = adminrepo.findById(userId);
	        AdminUser existingUser = adminUserOptional.get();

	        if (featuredImages != null) {
                for (MultipartFile featuredImage : featuredImages) {
                    if (!featuredImage.isEmpty()) {
                        try {
                            byte[] bytes = featuredImage.getBytes();
                            String featuredFileName = UUID.randomUUID().toString() + "_" + featuredImage.getOriginalFilename();

                            String featuredFilePath = uploadDir + File.separator + featuredFileName;

                            Path path = Paths.get(featuredFilePath);
                            Files.write(path, bytes);
                            existingUser.setPath("/profilepic/" + featuredFileName);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
	        }
	        

	            adminrepo.save(existingUser);

	            return "redirect:/admin/dashboard"; 
	        } else {
	            return "redirect:/error";
	        }
	    } 
	}
