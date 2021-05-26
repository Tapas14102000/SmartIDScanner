package com.TapasCodes.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.TapasCodes.dao.UserRepository;
import com.TapasCodes.entities.user;

@Controller
public class ForgotController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private com.TapasCodes.EmailService emailService;
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	@RequestMapping("/verify")
	public String openEmailForm(HttpSession session) {
		session.removeAttribute("message");
		session.setAttribute("title", "Smart ID Scanner");
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession s) {
		if(this.userRepository.getUserByUserName(email)==null) {
			s.setAttribute("message", "Email ID not Registered");
			return "forgot_email_form";}
		user user=this.userRepository.getUserByUserName(email);
		System.out.println("EMAIL : "+email);
		int otp=(int) (Math.random()*9000)+1000;
		System.out.println("Otp = "+otp);
		
		String subject="Smart ID Scanner - OTP";
		String message=com.TapasCodes.message.before+"OTP"+com.TapasCodes.message.middle+user.getName()+com.TapasCodes.message.mid2+"YOUR OTP IS <h3 style=\"color: rgb(45, 154, 187);\">"+otp+"</h3>"+com.TapasCodes.message.end;
		String to=email;
		boolean flag=this.emailService.sendEmail(subject, message, to);
		if(flag) {
			s.setAttribute("myotp", otp);
			s.setAttribute("email", email);
			s.removeAttribute("message");
			return "verify_otp";
		}
		else {
			s.setAttribute("message", "Internet Not Connected");
			return "forgot_email_form";
		}
	}
	
	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("otp")int otp,HttpSession session) {
		int myotp=(int)session.getAttribute("myotp");
		if(myotp==otp) {
			session.removeAttribute("message");
			return "password_change_form";
		
		}else {
			session.setAttribute("message", "You Have entered Wrong OTP !");
			return "verify_otp";
		}
	}
	
	@PostMapping("/change-password")
	public String changepassword(@RequestParam("newpassword")String newpassword,HttpSession session) {
		String email=(String)session.getAttribute("email");
		user user=this.userRepository.getUserByUserName(email);
		user.setPassword(this.bcrypt.encode(newpassword));
		this.userRepository.save(user);
		session.removeAttribute("email");
		return "redirect:/signin?change=password changed successfully..";
	}
}
