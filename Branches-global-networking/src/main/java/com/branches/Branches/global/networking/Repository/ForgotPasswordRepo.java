package com.branches.Branches.global.networking.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.branches.Branches.global.networking.model.AdminUser;
import com.branches.Branches.global.networking.model.ForgotPassword;


public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, Long> {

	Optional<ForgotPassword> findByOtp(int otp);

	Optional<ForgotPassword> findByUser(AdminUser user);

}
