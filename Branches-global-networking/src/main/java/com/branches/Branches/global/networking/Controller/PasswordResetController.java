package com.branches.Branches.global.networking.Controller;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.branches.Branches.global.networking.Repository.AdminRepository;
import com.branches.Branches.global.networking.Repository.ForgotPasswordRepo;
import com.branches.Branches.global.networking.model.AdminUser;
import com.branches.Branches.global.networking.model.ForgotPassword;

import jakarta.servlet.http.HttpSession;

@Controller
public class PasswordResetController {
	
	@Autowired
	private AdminRepository adminrepo;
	
	@Autowired
	private ForgotPasswordRepo  forgotrepo;
	

	@Autowired
	private JavaMailSender javaMailSender ;
	
    @Autowired
	private PasswordEncoder password;
	

    @GetMapping("/password-forgot")
    public String showForgotPasswordForm() {
        return "password_forget_form";
    }

    @PostMapping("/sendotp")
    public String sendOtp(@RequestParam("email") String email, Model model  ,  HttpSession session,
            RedirectAttributes redirectAttributes) {
    	
    	
        Optional<AdminUser> userOptional = adminrepo.findByEmail(email);
        if (!userOptional.isPresent()) {
        	 redirectAttributes.addFlashAttribute("error", "User not found.");
           
            return "redirect:/password-forgot";
           
        }

        AdminUser user = userOptional.get();
        Optional<ForgotPassword> existingForgotPassword = forgotrepo.findByUser(user);

        int otp = generateOTP();

        
        if (existingForgotPassword.isPresent()) {
            ForgotPassword forgotPassword = existingForgotPassword.get();
            forgotPassword.setOtp(otp);
            forgotPassword.setExpried(new Date(System.currentTimeMillis() + 60 * 60 * 1000)); 
            forgotrepo.save(forgotPassword);
        } else {
            ForgotPassword forgotPassword = new ForgotPassword();
            forgotPassword.setOtp(otp);
            forgotPassword.setUser(user);
            forgotPassword.setExpried(new Date(System.currentTimeMillis() + 60  * 1000)); 
            forgotrepo.save(forgotPassword);
        }

        sendForgotPasswordEmail(email, String.valueOf(otp));

       
        redirectAttributes.addFlashAttribute("success", "OTP sent for forgot password.");
        return "verfyotp";
    }
    
    @PostMapping("/verifyotp")
    public String verifyOtp(
                            @RequestParam("otp") int otp,
                            Model model   ,  HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Optional<ForgotPassword> existingForgotPassword = forgotrepo.findByOtp(otp);

        if (!existingForgotPassword.isPresent()) {
       	 redirectAttributes.addFlashAttribute("error", "Invalid OTP or User.");
           
            return "redirect:/sendotp";
            
        }

        ForgotPassword forgotPassword = existingForgotPassword.get();
        if (forgotPassword.getExpried().before(new Date())) {
       	 redirectAttributes.addFlashAttribute("error", "OTP has expired.");
            return "redirect:/sendotp";
           
        }

      

       
        model.addAttribute("userId", forgotPassword.getUser().getId());
        return "password_reset_form"; 
    }


    @PostMapping("/changepassword")
    public String changePassword(@RequestParam("userId") Long userId,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model , HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
       	 redirectAttributes.addFlashAttribute("error", "New password and confirm password do not match.");
            return "redirect:/changepassword";
          
        }

        Optional<AdminUser> userOptional = adminrepo.findById(userId);
        if (!userOptional.isPresent()) {
            model.addAttribute("error", "User not found");
            return "password_reset_form";
        }

       AdminUser user = userOptional.get();
        user.setPassword(password.encode(newPassword));
        adminrepo.save(user);

        return "redirect:/login"; 
    }


    private int generateOTP() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

  
    private void sendForgotPasswordEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP for Password Reset");
        String text = "Dear User,\n\n"
                + "We received a request to reset the password for your account. "
                + "To proceed, please use the following One-Time Password (OTP):\n\n"
                + "OTP: " + otp + "\n\n"
                + "This OTP is valid for the next 10 minutes. Enter it on the password reset page to complete the process.\n\n"
                + "If you didn't request this password reset, please ignore this email.\n\n"
                + "Best regards,\n"
                + "Branches  Global Network";
    message.setText(text);
        javaMailSender.send(message);
    }
}

