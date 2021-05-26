package com.TapasCodes.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.TapasCodes.ScannedImage;
import com.TapasCodes.dao.UserRepository;
import com.TapasCodes.dao.dbRepository;
import com.TapasCodes.entities.user;
import com.TapasCodes.entities.userdb;
import com.TapasCodes.helper.Message;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private dbRepository dbrepo;
	
	@GetMapping("/profile")
	public String yourProfile(Model m, HttpSession session, Principal p) {
		user user = this.userRepository.getUserByUserName(p.getName());
		m.addAttribute("title", user.getName());
		int count = this.dbrepo.countbyid(user);
		m.addAttribute("total", count);
		m.addAttribute("user", user);
		return "profile";
	}
	
	@GetMapping("/update-contact-user")
	public String updateFormUser(Model m, HttpSession session, Principal p) {
		user user = this.userRepository.getUserByUserName(p.getName());
		m.addAttribute("title", "Update " + user.getName());
		m.addAttribute("user", user);
		return "update_form_user";
	}
	
	@PostMapping("/process-update-user")
	public String updateHandlerUser(@ModelAttribute user user, Model m,
			@RequestParam("profileImages") MultipartFile file, HttpSession session, Principal p) {
		try {
			user user1 = this.userRepository.getUserByUserName(p.getName());
			if (!file.isEmpty()) {
				// delete old photo and update new one
				String x = user1.getId() + user1.getImage();
				File deleteFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(deleteFile.getAbsolutePath() + File.separator + x);
				Files.deleteIfExists(path);
				user.setImage(file.getOriginalFilename());
				File savefFile = new ClassPathResource("static/img").getFile();
				Path path1 = Paths
						.get(savefFile.getAbsolutePath() + File.separator + user1.getId() + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("path=" + path);
			} else {
				user.setImage(user1.getImage());
			}
			user.setPassword(user1.getPassword());
						user.setId(user1.getId());
						user.setEmail(user1.getEmail());
			user.setRole(user1.getRole());
			this.userRepository.save(user);

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		return "redirect:/user/profile";
	}
	
	@GetMapping("/show-history/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, HttpSession session, Principal p) {
		user user = this.userRepository.getUserByUserName(p.getName());
		m.addAttribute("user", user);
		m.addAttribute("title", "Contacts");
		Pageable pageable = PageRequest.of(page, 6);
		Page<userdb> users = this.dbrepo.findHistoryByUser(this.userRepository.getUserByUserName(p.getName()).getId(), pageable);
		m.addAttribute("userdb", users);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", users.getTotalPages());
		System.out.println("contacts.getTotalPages() = " + users.getTotalPages() + "\npage = " + page);
		return "show_History";
	}


	@GetMapping("/scan")
	public String openAddContactForm(Model m, Principal p) {
		m.addAttribute("title", "Scanner");
		user user = this.userRepository.getUserByUserName(p.getName());
		m.addAttribute("user", user);
		m.addAttribute("userdb", new userdb());
		return "scanner";
	}
	
	@PostMapping("/process-scan")
	public String processContact(@ModelAttribute userdb data,@RequestParam("profileImage") MultipartFile file, Model m, HttpSession session, Principal p) throws IOException {
		user user1= this.userRepository.getUserByUserName(p.getName());
		if(file.getOriginalFilename()=="") {
			m.addAttribute("title", "Scanner");
			m.addAttribute("user", user1);
			m.addAttribute("userdb", new userdb());
			session.setAttribute("message", new Message("No Photo Choosen !","alert-danger"));
			return "scanner";
		}
			data.setUser(user1);
			data.setImage("NA");
			data.setText("NA");
			data.setUpload(new Date().toString());
			user1.getData().add(data);
			this.userRepository.save(user1);
			userdb userdata=null;
			try {
			userdata=this.dbrepo.findimg(data.getUpload(),data.getUser());
			System.out.println(userdata);
			String x = userdata.getDbid() + file.getOriginalFilename();
			userdata.setImage(x);
			File savefFile = new ClassPathResource("static/img").getFile();
			Path path1 = Paths
					.get(savefFile.getAbsolutePath() + File.separator + x);
			Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File : "+savefFile.getAbsolutePath() + File.separator + x);
			ITesseract it = new Tesseract();
			it.setDatapath("tessdata");
			it.setLanguage("eng");
			String str = it.doOCR(new File(savefFile.getAbsolutePath() + File.separator + x));
			//String str=ScannedImage.extract(savefFile.getAbsolutePath() + File.separator + x);
			userdata.setText(str);
			this.dbrepo.save(userdata);
			m.addAttribute("title", "Scanner");
			m.addAttribute("user", user1);
			m.addAttribute("userdb", userdata);
			}
			catch (Exception e) {
				System.out.println("Excepetion found"+e.getMessage());
				Optional<userdb> user = this.dbrepo.findById(userdata.getDbid());
				if(user!=null) {
				File deleteFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(deleteFile.getAbsolutePath() + File.separator + user.get().getImage());
				Files.deleteIfExists(path);
				System.out.println(user.get().getImage()+ " deleted");
				this.dbrepo.deleteById(userdata.getDbid());}
				m.addAttribute("user", user1);
				m.addAttribute("title", "Scanner");
				m.addAttribute("userdb", new userdb());
				session.setAttribute("message", new Message("The Image has bad graphics/texture ,Extraction Stopped !","alert-danger"));
				return "scanner";
			}
		return "scanned_Result";
	}
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) throws IOException {
		Optional<userdb> user = this.dbrepo.findById(id);
		File deleteFile = new ClassPathResource("static/img").getFile();
		Path path = Paths.get(deleteFile.getAbsolutePath() + File.separator + user.get().getImage());
		Files.deleteIfExists(path);
		System.out.println(user.get().getImage()+ " deleted");
		this.dbrepo.deleteById(id);
		return "redirect:/user/show-history/0";
	}

}
