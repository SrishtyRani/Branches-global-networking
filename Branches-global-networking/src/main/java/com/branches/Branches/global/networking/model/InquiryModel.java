package com.branches.Branches.global.networking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Entity
public class InquiryModel {
	  @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   @NotBlank(message = "Service name is mandatory")
	  private String serviceName;
	   @NotBlank(message = "name is mandatory")
	  private String name;
	   @Email(message = "Please provide a valid email address")
	    @NotBlank(message = "Email is mandatory")
	  private String email;
	   @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
	    private String phone; 
	   @NotBlank(message = "message is mandatory")
	  private String message;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	  
}
