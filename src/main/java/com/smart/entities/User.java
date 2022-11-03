package com.smart.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="x_user")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int u_Id;
	
	@NotBlank(message="Name should not be empty")
	@Size(min=3, max=20, message="Must contain between 3-20 characters only")
	private String name;
	
	@Column(unique = true)
	@Pattern(regexp="^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message="Please provide a valid email")
	private String email;
	
	@Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message="Password should contain Minimum eight characters, at least one letter, one number and one special character")
	private String password;
	
	private String role;
	private boolean enabled;
	private String imageUrl;
	
	@Column(length=500)
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Contact> c_list = new ArrayList<>();

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int u_Id, String name, String email, String password, String role, boolean enabled, String imageUrl,
			String about, List<Contact> c_list) {
		super();
		this.u_Id = u_Id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.enabled = enabled;
		this.imageUrl = imageUrl;
		this.about = about;
		this.c_list = c_list;
	}

	public int getU_Id() {
		return u_Id;
	}

	public void setU_Id(int u_Id) {
		this.u_Id = u_Id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getC_list() {
		return c_list;
	}

	public void setC_list(List<Contact> c_list) {
		this.c_list = c_list;
	}

	@Override
	public String toString() {
		return "User [u_Id=" + u_Id + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
				+ role + ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", c_list=" + c_list
				+ "]";
	}
	
	
}
