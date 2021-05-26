package com.TapasCodes.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class user {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String image;
	@NotBlank(message = "Your name is required!")
	private String name;
	@NotBlank(message = "Your Email is required!")
	private String email;
	@NotBlank(message = "Set a Password!")
	private String password;
	private String role;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<userdb>  data= new ArrayList<>();
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	public List<userdb> getData() {
		return data;
	}
	public void setData(List<userdb> data) {
		this.data = data;
	}
}
