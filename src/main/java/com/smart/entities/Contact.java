package com.smart.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="x_contact")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int c_Id;
	private String name;
	private String nickName;
	private String imageUrl;
	private String work;
	private String email;
	private String phone;
	@Column(length =10000) 
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int c_Id, String name, String nickName, String imageUrl, String work, String email, String phone,
			String description, User user) {
		super();
		this.c_Id = c_Id;
		this.name = name;
		this.nickName = nickName;
		this.imageUrl = imageUrl;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.description = description;
		this.user = user;
	}

	public int getC_Id() {
		return c_Id;
	}

	public void setC_Id(int c_Id) {
		this.c_Id = c_Id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contact [c_Id=" + c_Id + ", name=" + name + ", nickName=" + nickName + ", imageUrl=" + imageUrl
				+ ", work=" + work + ", email=" + email + ", phone=" + phone + ", description=" + description
				+ ", user=" + user + "]";
	}
	
	

}
