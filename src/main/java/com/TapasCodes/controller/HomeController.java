package com.TapasCodes.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.TapasCodes.EmailService;
import com.TapasCodes.dao.UserRepository;
import com.TapasCodes.entities.user;
import com.TapasCodes.helper.Message;

@Controller
public class HomeController {


	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	@Autowired
	private EmailService emailService;
	@RequestMapping("/")
	public String home(Model m,Principal p,HttpSession s) {
		m.addAttribute("title", "Home | SmartIDScanner");
		return "home";
	}
	@RequestMapping("/signup")
	public String signup(Model m, HttpSession s) {
		if (s.getAttribute("message") != null)
			s.removeAttribute("message");
		m.addAttribute("title", "Register | SmartIDScanner");
		m.addAttribute("user", new user());
		return "signup";
	}
		@GetMapping("/signin")
		public String customLogin(Model m,HttpSession session,Principal p) {
			m.addAttribute("title", "Login | SmartIDScanner");
			return "login";
		}
		
		@PostMapping("/do_register")
		public String registerUser(@Valid @ModelAttribute("user") user user,BindingResult result1,Model m, HttpSession s) {
			if (result1.hasErrors()) {
				s.removeAttribute("message");
				System.out.println("Error : " + result1.toString());
				m.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setImage("contact.png");
			user.setPassword(bcrypt.encode(user.getPassword()));
			s.setAttribute("user", user);
			int otp = (int) (Math.random() * 9000) + 1000;
			System.out.println("Otp = " + otp);
			String subject = "Smart ID Scanner | OTP";
			String message=com.TapasCodes.message.before+"OTP"+com.TapasCodes.message.middle+user.getName()+com.TapasCodes.message.mid2+"YOUR OTP IS <h3 style=\"color: rgb(45, 154, 187);\">"+otp+"</h3>"+com.TapasCodes.message.end;
			String to = user.getEmail();
			boolean flag = this.emailService.sendEmail(subject, message, to);
			System.out.println("flag = "+flag);
			if (flag) {
				s.setAttribute("myotp", otp);
				s.setAttribute("email", user.getEmail());
				s.removeAttribute("message");	
				return "verify_email";
			} else {
				s.setAttribute("message", new Message("Internet Not Connected!","alert-danger"));
				return "signup";
			}
		}

		@PostMapping("/verify-email")
		public String verifyEMAIL(@RequestParam("otp")int otp,HttpSession session,Model m) {
			int myotp=(int)session.getAttribute("myotp");
			if(myotp==otp) {
				user user=(user)session.getAttribute("user");
				try {
				this.userRepository.save(user);
				m.addAttribute("user",new user());
				session.setAttribute("message", new Message("Successfully Registered !!","alert-success"));
				session.removeAttribute("email");
				String subject="Smart ID Scanner - Warm Welcome";
				String message=com.TapasCodes.message.before+"Warm_Welcome"+com.TapasCodes.message.middle+user.getName()+",</h1>\r\n"
						+ "                                                </td>\r\n"
						+ "                                            </tr>\r\n"
						+ "                                            <tr>\r\n"
						+ "                                                <td valign=\"top\" style=\"text-align: center; padding: 10px 20px 15px 20px; font-family: sans-serif; font-size: 15px; line-height: 20px; color: #fff;\">\r\n"
						+ "                                                    <p style=\"margin: 0;\">Access your data anywhere,anytime!</p>\r\n"
						+ "                                                </td>\r\n"
						+ "                                            </tr>\r\n"
						+ "                                            <tr>\r\n"
						+ "                                                <td valign=\"top\" align=\"center\" style=\"text-align: center; padding: 15px 0px 60px 0px;\">\r\n"
						+ "                                                    <center>\r\n"
						+ "                                                        <table role=\"presentation\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"center-on-narrow\" style=\"text-align: center;\">\r\n"
						+ "                                                            <tr>\r\n"
						+ "                                                                <td style=\"border-radius: 50px; background: #26a4d3; text-align: center;\" class=\"button-td\"> <a href=\"http://localhost:8080/\" style=\"background: #26a4d3; border: 15px solid #26a4d3; font-family: 'Montserrat', sans-serif; font-size: 14px; line-height: 1.1; text-align: center; text-decoration: none; display: block; border-radius: 50px; font-weight: bold;\" class=\"button-a\"> <span style=\"color:#ffffff;\" class=\"button-link\">&nbsp;&nbsp;&nbsp;&nbsp;ACCESS ACCOUNT&nbsp;&nbsp;&nbsp;&nbsp;</span> </a> </td>\r\n"
						+ "                                                            </tr>\r\n"
						+ "                                                        </table>\r\n"
						+ "                                                    </center>\r\n"
						+ "                                                </td>\r\n"
						+ "                                            </tr>\r\n"
						+ "                                        </table>\r\n"
						+ "                                    </td>\r\n"
						+ "                                </tr>\r\n"
						+ "                                <tr>\r\n"
						+ "                                    <td height=\"20\" style=\"font-size:20px; line-height:20px;\">&nbsp;</td>\r\n"
						+ "                                </tr>\r\n"
						+ "                            </table>\r\n"
						+ "                        </div>\r\n"
						+ "                    </td>\r\n"
						+ "                </tr>\r\n"
						+ "                <tr>\r\n"
						+ "                    <td bgcolor=\"#ffffff\">\r\n"
						+ "                        <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 40px 40px 20px 40px; text-align: left;\">\r\n"
						+ "                                    <h1 style=\"margin: 0; font-family: 'Montserrat', sans-serif; font-size: 20px; line-height: 26px; color: #333333; font-weight: bold;\">YOUR ACCOUNT IS NOW ACTIVE</h1>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 0px 40px 20px 40px; font-family: sans-serif; font-size: 15px; line-height: 20px; color: #555555; text-align: left; font-weight:bold;\">\r\n"
						+ "                                    <p style=\"margin: 0;\">Thanks for choosing our product.</p>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 0px 40px 20px 40px; font-family: sans-serif; font-size: 15px; line-height: 20px; color: #555555; text-align: left; font-weight:normal;\">\r\n"
						+ "                                    <p style=\"margin: 0;\">We assure you that your data is 100% secured at our end..<br><br> We have the best UI ,so that you feel convinient and much easy to use our product.</p>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                        </table>\r\n"
						+ "                    </td>\r\n"
						+ "                </tr> <!-- INTRO : END -->\r\n"
						+ "                <!-- CTA : BEGIN -->\r\n"
						+ "                <tr>\r\n"
						+ "                    <td bgcolor=\"#26a4d3\">\r\n"
						+ "                        <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\">\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 40px 40px 5px 40px; text-align: center;\">\r\n"
						+ "                                    <h1 style=\"margin: 0; font-family: 'Montserrat', sans-serif; font-size: 20px; line-height: 24px; color: #ffffff; font-weight: bold;\">YOUR FEEDBACK MOTIVATE US TO MOVE AHEAD</h1>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 0px 40px 20px 40px; font-family: sans-serif; font-size: 17px; line-height: 23px; color: #aad4ea; text-align: center; font-weight:normal;\">\r\n"
						+ "                                    <p style=\"margin: 0;\">Let us know what you think of our product</p>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td valign=\"middle\" align=\"center\" style=\"text-align: center; padding: 0px 20px 40px 20px;\">\r\n"
						+ "                                    <!-- Button : BEGIN -->\r\n"
						+ "                                    <table role=\"presentation\" align=\"center\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"center-on-narrow\">\r\n"
						+ "                                        <tr>\r\n"
						+ "                                            <td style=\"border-radius: 50px; background: #ffffff; text-align: center;\" class=\"button-td\"> <a href=\"mailto:tdkdevelopment14@gmail.com\" style=\"background: #ffffff; border: 15px solid #ffffff; font-family: 'Montserrat', sans-serif; font-size: 14px; line-height: 1.1; text-align: center; text-decoration: none; display: block; border-radius: 50px; font-weight: bold;\" class=\"button-a\"> <span style=\"color:#26a4d3;\" class=\"button-link\">&nbsp;&nbsp;&nbsp;&nbsp;GIVE FEEDBACK&nbsp;&nbsp;&nbsp;&nbsp;</span> </a> </td>\r\n"
						+ "                                        </tr>\r\n"
						+ "                                    </table>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                        </table>\r\n"
						+ "                    </td>\r\n"
						+ "                </tr>\r\n"
						+ "                <tr>\r\n"
						+ "                    <td bgcolor=\"#ffffff\">\r\n"
						+ "                        <table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\"> <br>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td align=\"center\"> <img src=\"https://img.icons8.com/dusk/64/000000/ms-share-point.png\" width=\"37\" height=\"37\" style=\"display: block; border: 0px;\" /> </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td align=\"center\" style=\"font-family: Open Sans, Helvetica, Arial, sans-serif; font-size: 14px; font-weight: 400; line-height: 24px; padding: 5px 0 10px 0;\">\r\n"
						+ "                                    <p style=\"font-size: 14px; font-weight: 800; line-height: 18px; color: #333333;\"> SmartIDScanner.com<br> Bhubaneswar,India </p>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 0px 40px 10px 40px; font-family: sans-serif; font-size: 12px; line-height: 18px; color: #666666; text-align: center; font-weight:normal;\">\r\n"
						+ "                                    <p style=\"margin: 0;\">This email was sent to you from tdkdevelopment14@gmail.com</p>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                            <tr>\r\n"
						+ "                                <td style=\"padding: 0px 40px 40px 40px; font-family: sans-serif; font-size: 12px; line-height: 18px; color: #666666; text-align: center; font-weight:normal;\">\r\n"
						+ "                                    <p style=\"margin: 0;\">Copyright &copy; 2020-2021 <b>SmartIDScanner.com</b>, All Rights Reserved.</p>\r\n"
						+ "                                </td>\r\n"
						+ "                            </tr>\r\n"
						+ "                        </table>\r\n"
						+ "                    </td>\r\n"
						+ "                </tr>\r\n"
						+ "            </table>\r\n"
						+ "        </div>\r\n"
						+ "    </center>\r\n"
						+ "</body>\r\n"
						+ "\r\n"
						+ "</html>";
				String to=user.getEmail();
				this.emailService.sendEmail(subject, message, to);
				return "signup";}
				catch (Exception e) {
					m.addAttribute("user",(user)session.getAttribute("user"));
					if(e.getMessage().equalsIgnoreCase("could not execute statement; SQL [n/a]; constraint [user.UK_ob8kqyqqgmefl0aco34akdtpe]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement")) {
						m.addAttribute("user", user);
						session.setAttribute("message", new Message("Mail Already Registered !!","alert-danger"));
					}else {
						System.out.println("Internet not connected");
						session.setAttribute("message", new Message("Internet Not Connected!","alert-danger"));	
					}
					session.removeAttribute("email");
					return "signup";
				}
			
			}else {
				session.setAttribute("message", "You Have entered Wrong OTP !");
				return "verify_email";
			}
		}
}
