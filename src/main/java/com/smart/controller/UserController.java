package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repository.ContactRepo;
import com.smart.repository.UserRepo;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	// this method is to add common data to response in all the handlers
	
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		
        String userName = principal.getName();
		
		//System.out.println(userName);
		
		User user = userRepo.getUserByEmail(userName);
		
		m.addAttribute("user", user);
	}
	
	@RequestMapping("/index")
	public String dashboard(Model m, Principal principal) {
		
		 String userName = principal.getName();
			
			//System.out.println(userName);
			
			User user = userRepo.getUserByEmail(userName);
		m.addAttribute("user", user);
		m.addAttribute("title", "DashBoard");
		return "normal/dashboard";
	}
	
	@RequestMapping("/")
	public String home(Model m, Principal principal) {
       
		User user = this.userRepo.getUserByEmail(principal.getName());
		m.addAttribute("user", user);
		m.addAttribute("title", "Home - Smart Contact Manager");
		
		return "redirect:/user/index";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		
       m.addAttribute("title", "About - Smart Contact Manager");
		
		return "normal/about";
	}
	
	@GetMapping("/add-contact")
	public String addContactForm(Model m) {
		
		m.addAttribute("title", "Add Contact");
		m.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	// processing add contact form
	
	@PostMapping("/process-contact")
	public String processAddContact(@ModelAttribute("contact") Contact contact,Model m, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {
		
		try {
			 String userName = principal.getName();
				
				System.out.println(userName);
				
				User user = userRepo.getUserByEmail(userName);
				
				// processing and uploading file
				
				if(file.isEmpty()) {
					
					System.out.println("no file selected");
					
					contact.setImageUrl("contact.png");
				}
				else {
					// upload the file to folder and update the name to contact details
					

					
					
					  // this is to make file unique
					
					 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
					   LocalDateTime now = LocalDateTime.now();
					   String pre =dtf.format(now);
					   
					 
					
					System.out.println(pre);
					//String s = Date.now().toString("yyyyMMddHHmmss");
					
					contact.setImageUrl(pre+file.getOriginalFilename());
					
					File savefile  = new ClassPathResource("static/images").getFile();
					
					Path path = Paths.get(savefile.getAbsolutePath()+File.separator+pre+file.getOriginalFilename());
					
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					System.out.println("Image is uploaded");
					
					//System.out.println(path);
					
				}
				
				session.setAttribute("message", new Message("Contact has been added successfully! You can add more :)","alert-success"));
				user.getC_list().add(contact);
				   contact.setUser(user);
		           
		           
		           
		           this.userRepo.save(user);
				  m.addAttribute("user", user);
			
				//System.out.println("data"+contact);
		
			
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			e.getStackTrace();
			session.setAttribute("message", new Message("Something went wrong! Try again","alert-danger"));
		}
		
		return "normal/add_contact_form";
	}
	
	//show 7 contacts per page
	
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		
		
           if(page<0) {
			
			return "redirect:/user/show-contacts/0";
			
		}
		String name = principal.getName();
		
		User user = this.userRepo.getUserByEmail(name);
		
		//pageable will contain two thing current page and contact(items) per page
		
		Pageable pageable =PageRequest.of(page, 4);
		Page<Contact> contacts =this.contactRepo.findContactByUser(user.getU_Id(), pageable);
		
		
		m.addAttribute("title", "User contacts");
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentpage",page);
		m.addAttribute("totalpage",contacts.getTotalPages());
		
		return "normal/show_contacts";
		
	}
	
	// showing particular contact details
	
	@GetMapping("/contact/{c_Id}")
	public String showContact(@PathVariable("c_Id") Integer cId, Model m,Principal principal) {
		
		m.addAttribute("title", "Contact Information");
		
		Contact contact = this.contactRepo.findById(cId).get();
		
         String name = principal.getName();
		User user = this.userRepo.getUserByEmail(name);
		
		if(user.getU_Id()== contact.getUser().getU_Id()) 
		m.addAttribute("contact", contact);
		
		
		
		return "normal/user_contact";
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Principal principal, HttpSession session) {
		
		Contact contact =this.contactRepo.findById(cid).get();
		
		  String name = principal.getName();
		 User user = this.userRepo.getUserByEmail(name);
		
		 if(user.getU_Id()== contact.getUser().getU_Id())
		  this.contactRepo.delete(contact);
		 
		 session.setAttribute("message", new Message("Contact deleted successfully!","alert-success"));
		return "redirect:/user/show-contacts/0";
	}
	
	// this handler is to handle the update form process of contacts
	
	@GetMapping("/update-contact/{cid}")
	public String updateContact(@PathVariable("cid") Integer cid, Principal principal, Model m) {
		
		Contact contact =this.contactRepo.findById(cid).get();
		
		  String name = principal.getName();
		 User user = this.userRepo.getUserByEmail(name);
		
		// if(user.getU_Id()== contact.getUser().getU_Id())
		 
		 m.addAttribute("title", "Update Contact");
		 
		  m.addAttribute("contact", contact);
		 
		
		 
		return "normal/update_form";
	}
	
	
	// this handler is to handle the update process of contacts from back end
	@PostMapping("/process-update")
	public String updateProcess(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Model m, HttpSession session, Principal principal) {
		
		   try {
			   
			      User user = this.userRepo.getUserByEmail(principal.getName());
			       contact.setUser(user);
			     
			     //System.out.println(contact);
			   m.addAttribute("title", "Update Contact");
			   
			   Contact oldContact = this.contactRepo.findById(contact.getC_Id()).get();
			   
			   if(file.isEmpty()) {
				   
				  String url = oldContact.getImageUrl();
				  
				  contact.setImageUrl(url);
			   }else {
				   
				   //delete old one 
				   if(oldContact.getImageUrl()!="contact.png") {
				   File deletefile  = new ClassPathResource("static/images").getFile();
				   
				   File file2 = new File(deletefile,oldContact.getImageUrl());
				   
				   file2.delete();
				   
				
				   }
				   // and update new one
					
					File savefile  = new ClassPathResource("static/images").getFile();
					
					 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
					   LocalDateTime now = LocalDateTime.now();
					   String pre =dtf.format(now);
					
					Path path = Paths.get(savefile.getAbsolutePath()+File.separator+pre+file.getOriginalFilename());
					
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					contact.setImageUrl(pre+file.getOriginalFilename());
				   
			   }
			   
			   this.contactRepo.save(contact);
			 session.setAttribute("message", new Message("Contact updated Successfully!!","alert-success"));
			 
			  m.addAttribute("contact", contact);
			 
			  
			
		} catch (Exception e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		 
		return "redirect:/user/update-contact/"+contact.getC_Id();
	}
	
	// user profile handler
	
	@GetMapping("/profile")
	public String profile(Model m, Principal principal) {
		
		m.addAttribute("title", "Profile Page");
		
		User user = this.userRepo.getUserByEmail(principal.getName());
		
		m.addAttribute("user", user);
		
		return "normal/profile";
	}
	
	
	@GetMapping("/update-user")
	public String userUpdate(Principal principal, Model m) {
		
		User user =this.userRepo.getUserByEmail(principal.getName());
		
		m.addAttribute("title", "Update User");
		m.addAttribute("user", user);
		
		System.out.println(user.getName());
		
		return"normal/update";
		
		
	}
	
	@PostMapping("/process-user-update")
	public String processUserUpdate(@ModelAttribute User user,@RequestParam("profileImage") MultipartFile file, Principal principal, Model m, HttpSession session) {
		
		User olduser =this.userRepo.getUserByEmail(principal.getName());
		
		
		try {
			
			if(file.isEmpty()) {
				
				user.setImageUrl(olduser.getImageUrl());
			}
			else {
				
			
				File fileLocation  = new ClassPathResource("static/images").getFile();
				//delete old one 
				   if(olduser.getImageUrl()!="default.png") {
				   
				   
				   File file2 = new File(fileLocation,olduser.getImageUrl());
				   
				   file2.delete();
				   
				
				   }
				   // and update new one
					
				   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
				   LocalDateTime now = LocalDateTime.now();
				   String pre =dtf.format(now);
					
					Path path = Paths.get(fileLocation.getAbsolutePath()+File.separator+pre+file.getOriginalFilename());
					
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					
					user.setImageUrl(pre+file.getOriginalFilename());
			}
			
			// now we have to manage enabled, list<Contact> and password
			
			user.setPassword(olduser.getPassword());
			user.setEnabled(olduser.isEnabled());
			user.setC_list(olduser.getC_list());
			
			
			// update user
			
			this.userRepo.save(user);
			m.addAttribute("user", user);
		 session.setAttribute("message", new Message("User updated Successfully!!","alert-success"));
		 
			
		} catch (Exception e) {
			
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went Wrong, please verify your email!!","alert-danger"));
		}
		
		
		
		
		return"redirect:/user/update-user";
		
		
	}
	
	@GetMapping("/setting")
	public String setting(Model m) {
		
		m.addAttribute("title", "Setting");
		
		return "normal/setting";
		
	}
	
	
}
