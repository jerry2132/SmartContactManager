package com.example.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.entity.ForgotPassword;
import com.example.entity.User;
import com.example.repository.ForgotPasswordRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;





@Service
public class ForgotPasswordService {

	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	private User user;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;
	

	
	private final int MINUTES = 1;
	
	public String generateToken()
	{
		return UUID.randomUUID().toString();
	}
	 
	public LocalDateTime expireTimeRange()
	{
		return LocalDateTime.now().plusMinutes(MINUTES);
	}
	
	public String generateOtp() {
        // Generate a random 6-digit OTP (you can customize the length)
        int otp = (int) ((Math.random() * (999999 - 100000)) + 100000);
        return String.valueOf(otp);
    }
	
//	public void sendMail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException
//	{
//		
//		MimeMessage message  = javaMailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message); 
//		
//		String emailContent  = "<p>Hello user</p>"+"click the link below to reset password"+"<p><a href=\" " + emailLink + "\"> change my password</a></p>"
//				+"<br>"
//				+"ignore if not done by you";
//		
//		helper.setText(emailContent,true);
//		helper.setFrom("sumitsinghrawat34@gmail.com", "sumitSR");
//		helper.setSubject(subject);
//		helper.setTo(to);
//		javaMailSender.send(message);
//		
//		
//	}
	
	
	
	public boolean isExpired(ForgotPassword forgotPassword)
	{
		return LocalDateTime.now().isAfter(forgotPassword.getExpireTime());
	}
	
	public String checkValidity(ForgotPassword forgotPassword, Model model)
	{
		if(forgotPassword == null)
		{
			model.addAttribute("error", "Invalid Token");
			return "error";
		}
		
		else if(forgotPassword.isUsed())
		{
			model.addAttribute("error", "Already Used");
			return "error";
		}
		else if (isExpired(forgotPassword))
		{
			model.addAttribute("error", "Token Expired");
			return "error";
		}
		
		return "newPassword";
	}
	
	
	public void sendOtp(String to, String subject, String otp) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String emailContent = "<p>Hello user</p>Your OTP is: " + otp
                + "<br>Ignore if not done by you";

        helper.setText(emailContent, true);
        helper.setFrom("sumitsinghrawat34@gmail.com", "sumitSR");
        helper.setSubject(subject);
        helper.setTo(to);
        javaMailSender.send(message);
    }
	
	
	 public boolean verifyOtp(String email, String enteredOtp) {
	        User user = userService.findByEmail(email);
	        System.out.println("Otp email"+user.getEmail());
	        ForgotPassword forgotPassword = forgotPasswordRepository.findByUser(user);
	        
	        System.out.println("forgot password user  "+forgotPassword.getUser());
	        
	        ForgotPasswordService forgotPasswordService = new ForgotPasswordService();

	        if (forgotPassword != null && !forgotPassword.isUsed() &&
	                !forgotPasswordService.isExpired(forgotPassword) &&
	                forgotPassword.getOtp().equals(enteredOtp)) {
	            // Mark the token as used
//	            forgotPassword.setUsed(true);
	            forgotPasswordRepository.save(forgotPassword);
	            return true;
	        }

	        return false;
	    }
	
}
