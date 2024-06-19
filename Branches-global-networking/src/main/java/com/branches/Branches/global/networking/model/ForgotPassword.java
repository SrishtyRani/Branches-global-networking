package com.branches.Branches.global.networking.model;

import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class ForgotPassword {
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer otp;
	private Date expried;
	
	@OneToOne
	private AdminUser user;
	
	
    public ForgotPassword() {}

    // Builder class
    public static class Builder {
        private Integer id;
        private Integer otp;
        private Date expired;
        private AdminUser user;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder otp(Integer otp) {
            this.otp = otp;
            return this;
        }

        public Builder expired(Date expired) {
            this.expired = expired;
            return this;
        }

        public Builder user(AdminUser user) {
            this.user = user;
            return this;
        }

        public ForgotPassword build() {
            ForgotPassword forgotPassword = new ForgotPassword();
            forgotPassword.setId(this.id);
            forgotPassword.setOtp(this.otp);
            forgotPassword.setExpried(this.expired);
            forgotPassword.setUser(this.user);
            return forgotPassword;
        }
    }


	public ForgotPassword(Integer id, Integer otp, Date expried, AdminUser user) {
		super();
		this.id = id;
		this.otp = otp;
		this.expried = expried;
		this.user = user;
	}

	public Integer  getId() {
		return id;
	}

	public void setId(Integer  id2) {
		this.id = id2;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public Date getExpried() {
		return expried;
	}

	public void setExpried(Date expried) {
		this.expried = expried;
	}

	public AdminUser getUser() {
		return user;
	}

	public void setUser(AdminUser user) {
		this.user = user;
	}


}


