package com.branches.Branches.global.networking.ApiController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.branches.Branches.global.networking.Dto.ResponseDto;
import com.branches.Branches.global.networking.Repository.AdminRepository;
import com.branches.Branches.global.networking.Repository.InquiryRepository;
import com.branches.Branches.global.networking.model.AdminUser;
import com.branches.Branches.global.networking.model.InquiryModel;

import jakarta.validation.Valid;




@Controller
@RequestMapping("/api/v2")
public class ApiController {
	
	@Autowired
	private InquiryRepository inquaryRepo;
	
//	   @Autowired
//	    private PasswordEncoder passwordEncoder;
	   
//	   @Autowired
//	   private AdminRepository adminrepo;
	
	
//	   private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
//	
//    @PostMapping("/save")
//    public ResponseEntity<ResponseDto> saveInquiry(@RequestBody InquiryModel inquiry) {
//    	
//   
//        try {
//        	 logger.info("Received inquiry: {}", inquiry);
//        	inquaryRepo.save(inquiry);
//        	
//        	ResponseDto response = ResponseDto.builder()
//  	                .status(true)
//  	                .message("Inquiry saved successfully")
//  	                .build();
//        	 return ResponseEntity.status(HttpStatus.OK).body(response);
//        } catch (Exception e) {
//        	 logger.error("Failed to save inquiry", e);
//        	ResponseDto response = ResponseDto.builder()
//  	                .status(false)
//  	                .message("Faild to Save Inquiry ")
//  	                .build();
//        	
//        	return ResponseEntity.status(HttpStatus.OK).body(response);
//         
//        }
//    }

	
	@PostMapping("/save")
	public ResponseEntity<ResponseDto> saveInquiry(@Valid @RequestBody InquiryModel inquiry, BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	      
	        StringBuilder errorMessage = new StringBuilder("Validation error(s): ");
	        for (FieldError error : bindingResult.getFieldErrors()) {
	            errorMessage.append(error.getDefaultMessage()).append("; ");
	        }
	        ResponseDto response = ResponseDto.builder()
	                .status(false)
	                .message(errorMessage.toString())
	                .build();
	        return ResponseEntity.badRequest().body(response);
	    } else {

	        try {
	            inquaryRepo.save(inquiry);
	            ResponseDto response = ResponseDto.builder()
	                    .status(true)
	                    .message("Inquiry saved successfully")
	                    .build();
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            ResponseDto response = ResponseDto.builder()
	                    .status(false)
	                    .message("Failed to save inquiry")
	                    .build();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }
	}
	
}
	 

//	    @PostMapping("/register/user")
//	    public AdminUser createUser(@RequestBody AdminUser user) {
//	        user.setPassword(passwordEncoder.encode(user.getPassword()));
//	        return adminrepo.save(user);
//	    }
//	}

