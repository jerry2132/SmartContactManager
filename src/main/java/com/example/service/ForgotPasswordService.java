package com.example.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.entity.ForgotPassword;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;





@Service
public class ForgotPasswordService {

	
	@Autowired
	JavaMailSender javaMailSender;
	
	private final int MINUTES = 10;
	
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
			return "error-page";
		}
		
		else if(forgotPassword.isUsed())
		{
			model.addAttribute("error", "Already Used");
			return "error-page";
		}
		else if (isExpired(forgotPassword))
		{
			model.addAttribute("error", "Token Expired");
			return "error-page";
		}
		
		return "reset-password";
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
}
